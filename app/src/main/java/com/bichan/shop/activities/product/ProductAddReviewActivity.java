package com.bichan.shop.activities.product;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

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
    @Inject
    public Service service;

    @BindView(R.id.ratingBar)
    RatingBar ratingBar;
    @BindView(R.id.edtText)
    EditText edtText;
    @BindView(R.id.btnAddReview)
    Button btnAddReview;

    private String userToken;
    private String productId;

    Observable<String> editTextObservable;
    Observable<Float> ratingBarObservable;

    private CompositeSubscription subscriptions;

    private void init(){
        MyApplication mApp = ((MyApplication)getApplicationContext());
        userToken = mApp.getUserToken();
        productId = "113";

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
    }

    private void initView(){
        btnAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addReview(edtText.getText().toString().trim(), Float.toString(ratingBar.getNumStars()));
            }
        });
    }

    private void addReview(String text, String rating){
        Subscription addReviewSubscription = service.addProductReview(
                productId, text, rating, userToken,
                new Service.AddProductReviewCallback() {
                    @Override
                    public void onSuccess(ReviewAddResponse reviewAddResponse) {
                        if(reviewAddResponse.isStatus()){

                        }else {

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
}
