package com.bichan.shop.activities.product;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bichan.shop.BaseApp;
import com.bichan.shop.R;
import com.bichan.shop.adapters.product.ProductReviewAdapter;
import com.bichan.shop.models.Review;
import com.bichan.shop.models.ReviewResponse;
import com.bichan.shop.networking.NetworkError;
import com.bichan.shop.networking.Service;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class ProductReviewActivity extends BaseApp {
    public static final String EXTRA_PRODUCT_ID = "EXTRA_PRODUCT_ID";

    @Inject
    public Service service;

    @BindView(R.id.rvProductReview)
    RecyclerView rvProductReview;
    @BindView(R.id.btnNewReview)
    Button btnNewReview;
    @BindView(R.id.btnReviewMore)
    Button btnReviewMore;
    @BindView(R.id.btnBack)
    AppCompatImageButton btnBack;


    private ProductReviewAdapter productReviewAdapter;
    StaggeredGridLayoutManager manager2;

    private MaterialDialog dialogLoading;
    private String productId;

    private CompositeSubscription subscriptions;

    private void init(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            productId = bundle.getString(EXTRA_PRODUCT_ID);
        }else{
            finish();
        }

        subscriptions = new CompositeSubscription();

        dialogLoading = new MaterialDialog.Builder(this)
                .title(R.string.dialog_loading_title)
                .content(R.string.dialog_loading_mess)
                .progress(true, 0).build();
    }

    private void initView(){
        btnReviewMore.setVisibility(View.GONE);
        btnNewReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProductAddReviewActivity(productId);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        rvProductReview.setHasFixedSize(true);
        rvProductReview.setNestedScrollingEnabled(false);
        productReviewAdapter = new ProductReviewAdapter(this);
        manager2 = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        manager2.setSpanCount(1);
        rvProductReview.setLayoutManager(manager2);
        rvProductReview.setAdapter(productReviewAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDeps().inject(this);
        setContentView(R.layout.activity_product_review);
        ButterKnife.bind(this);
        init();
        initView();

        getProductReview();
    }

    private void openProductAddReviewActivity(String productId){
        Intent productAddReviewIntent = new Intent(this, ProductAddReviewActivity.class);
        productAddReviewIntent.putExtra(ProductAddReviewActivity.EXTRA_PRODUCT_ID, productId);
        startActivityForResult(productAddReviewIntent, 1);
    }

    private void getProductReview(){
        dialogLoading.show();
        Subscription productReviewSubscription = service.getProductReview(productId, new Service.GetProductReviewCallback() {
            @Override
            public void onSuccess(ReviewResponse reviewResponse) {
                dialogLoading.dismiss();
                setDataProductReview(reviewResponse.getReviews());
            }

            @Override
            public void onError(NetworkError networkError) {

            }
        });
        subscriptions.add(productReviewSubscription);
    }

    private void setDataProductReview(ArrayList<Review> reviews){
        productReviewAdapter.clear();
        for(int i = 0 ; i < reviews.size(); i++){
            productReviewAdapter.addItem(reviews.get(i));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                boolean added = data.getBooleanExtra(ProductAddReviewActivity.EXTRA_ADDED_REVIEW, false);
                if(added){
                    getProductReview();
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscriptions != null) {
            subscriptions.unsubscribe();
        }
    }
}
