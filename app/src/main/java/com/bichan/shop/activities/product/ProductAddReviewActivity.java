package com.bichan.shop.activities.product;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bichan.shop.BaseApp;
import com.bichan.shop.MyApplication;
import com.bichan.shop.R;
import com.bichan.shop.RxHelper;
import com.bichan.shop.models.ReviewAddResponse;
import com.bichan.shop.networking.NetworkError;
import com.bichan.shop.networking.Service;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func2;
import rx.subscriptions.CompositeSubscription;

public class ProductAddReviewActivity extends BaseApp {
    public static final String EXTRA_PRODUCT_ID = "EXTRA_PRODUCT_ID";
    public static final String EXTRA_ADDED_REVIEW = "EXTRA_ADDED_REVIEW";

    @Inject
    public Service service;

    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    @BindView(R.id.edtText)
    EditText edtText;
    @BindView(R.id.btnAddReview)
    Button btnAddReview;
    @BindView(R.id.btnBack)
    AppCompatImageButton btnBack;


    private String userToken;
    private String productId;

    Observable<String> editTextObservable;
    Observable<Float> ratingBarObservable;

    private CompositeSubscription subscriptions;

    private MaterialDialog dialogLoading;
    private MaterialDialog dialogError;

    private void init(){

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            productId = bundle.getString(EXTRA_PRODUCT_ID);
        }else{
            onBackPressed();
        }

        MyApplication mApp = ((MyApplication)getApplicationContext());
        userToken = mApp.getUserToken();

        editTextObservable = RxHelper.getTextWatcherObservable(edtText);
        ratingBarObservable = RxHelper.getTextWatcherObservable(ratingBar);

        subscriptions = new CompositeSubscription();

        Observable.combineLatest(
                editTextObservable,
                ratingBarObservable,
                new Func2<String, Float, Boolean>() {
                    @Override
                    public Boolean call(String s, Float aFloat) {
                        if(s.equals("") || aFloat <= 0)
                            return false;
                        return true;
                    }
                }).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                btnAddReview.setEnabled(aBoolean);
            }
        });

        dialogLoading = new MaterialDialog.Builder(this)
                .title(R.string.dialog_loading_title)
                .content(R.string.dialog_loading_mess)
                .progress(true, 0).build();

        dialogError = new MaterialDialog.Builder(this)
                .title(R.string.dialog_error_title)
                .content(R.string.dialog_error_mess_add_review)
                .positiveText(R.string.dialog_agree)
                .iconRes(R.drawable.ic_error_outline_black_24dp)
                .build();
    }

    private void initView(){
        btnAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addReview(edtText.getText().toString().trim(), Float.toString(ratingBar.getNumStars()));
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void addReview(String text, String rating){
        Log.d("ahihi", text);
        Log.d("ahihi", rating);
        Log.d("ahihi", productId);
        Log.d("ahihi", userToken);
        dialogLoading.show();
        Subscription addReviewSubscription = service.addProductReview(
                productId, text, rating, userToken,
                new Service.AddProductReviewCallback() {
                    @Override
                    public void onSuccess(ReviewAddResponse reviewAddResponse) {
                        dialogLoading.dismiss();
                        if(reviewAddResponse.isStatus()){
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra(EXTRA_ADDED_REVIEW, true);
                            setResult(Activity.RESULT_OK,returnIntent);
                            finish();
                        }else {
                            dialogError.show();
                        }
                    }

                    @Override
                    public void onError(NetworkError networkError) {

                    }
                });

        subscriptions.add(addReviewSubscription);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDeps().inject(this);
        setContentView(R.layout.activity_product_add_review);
        ButterKnife.bind(this);
        init();
        initView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
