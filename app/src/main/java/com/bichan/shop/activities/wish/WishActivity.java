package com.bichan.shop.activities.wish;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bichan.shop.BaseApp;
import com.bichan.shop.MyApplication;
import com.bichan.shop.Prefs.PrefsUser;
import com.bichan.shop.R;
import com.bichan.shop.activities.login.LoginActivity;
import com.bichan.shop.activities.product.ProductDetailActivity;
import com.bichan.shop.adapters.wish.ProductsWishAdapter;
import com.bichan.shop.models.ProductMini;
import com.bichan.shop.models.ProductMiniResponse;
import com.bichan.shop.models.SubmitResponse;
import com.bichan.shop.networking.NetworkError;
import com.bichan.shop.networking.Service;
import com.kennyc.view.MultiStateView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class WishActivity extends BaseApp {
    @Inject
    public Service service;

    @BindView(R.id.rvProducts)
    RecyclerView rvProducts;
    @BindView(R.id.btnClearWish)
    Button btnClearWish;
    @BindView(R.id.btnBack)
    AppCompatImageButton btnBack;

    @BindView(R.id.layoutStateLogin)
    MultiStateView layoutStateLogin;

    private ProductsWishAdapter productsWishAdapter;
    StaggeredGridLayoutManager manager;

    private MaterialDialog dialogLoading;

    private String token;
    private CompositeSubscription subscriptions;
    MyApplication mApp;

    private void init(){
        mApp = ((MyApplication)getApplicationContext());

        token = mApp.getUserToken();
        subscriptions = new CompositeSubscription();

        dialogLoading = new MaterialDialog.Builder(this)
                .customView(R.layout.layout_dialog_loading, false)
                .canceledOnTouchOutside(true)
                .build();
        dialogLoading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    private void initView(){
        rvProducts.setHasFixedSize(true);
        productsWishAdapter = new ProductsWishAdapter(this);
        productsWishAdapter.changeView();
        manager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        manager.setSpanCount(1);
        rvProducts.setLayoutManager(manager);
        rvProducts.setAdapter(productsWishAdapter);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        layoutStateLogin.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.retry)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(WishActivity.this, LoginActivity.class);
                        startActivity(i);
                    }
                });

        productsWishAdapter.setOnItemProductClickListener(new ProductsWishAdapter.OnItemProductClickListener() {
            @Override
            public void onClick(ProductMini productMini) {
                Intent intent = new Intent(WishActivity.this, ProductDetailActivity.class);
                intent.putExtra(ProductDetailActivity.EXTRA_PRODUCT_ID, productMini.getProductId());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        productsWishAdapter.setOnRemoveClickListener(new ProductsWishAdapter.OnRemoveClickListener() {
            @Override
            public void onClick(ProductMini productMini, int position) {
                removeWish(productMini, position);
            }
        });

        productsWishAdapter.setOnAddToCartClickListener(new ProductsWishAdapter.OnAddToCartClickListener() {
            @Override
            public void onClick(ProductMini productMini) {
                addToCart(productMini.getProductOptionId());
            }
        });

        btnClearWish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearWish();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDeps().inject(this);
        setContentView(R.layout.activity_wish);
        ButterKnife.bind(this);
        init();
        initView();

    }


    private void addToCart(String id){
        dialogLoading.show();
        String token = mApp.getUserToken();
        Subscription subscription = service.addCart(
                token,
                id,
                Integer.toString(1),
                new Service.AddCartCallback() {
                    @Override
                    public void onSuccess(SubmitResponse submitResponse) {
                        dialogLoading.dismiss();
                        if(submitResponse.isStatus()){
                            PrefsUser.updateCartNum(true);
                        }else{
                            Toast.makeText(getApplicationContext(), "Sản phẩm vượt quá số lượng cho phép.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(NetworkError networkError) {

                    }
                });
        subscriptions.add(subscription);
    }

    private void getWish(){
        productsWishAdapter.clearAll();
        productsWishAdapter.startLoading();
        Subscription subscription = service.getWish(token, new Service.GetWishCallback() {
            @Override
            public void onSuccess(ProductMiniResponse productMiniResponse) {
                productsWishAdapter.stopLoading();
                productsWishAdapter.addProducts(productMiniResponse.getProductMinis());
                PrefsUser.setWishNum(productMiniResponse.getProductMinis().size());
            }

            @Override
            public void onError(NetworkError networkError) {

            }
        });
        subscriptions.add(subscription);
    }


    private void clearWish(){
        dialogLoading.show();
        Subscription subscription = service.clearWish(token, new Service.ClearWishCallback() {
            @Override
            public void onSuccess(SubmitResponse submitResponse) {
                dialogLoading.dismiss();
                if(submitResponse.isStatus()){
                    productsWishAdapter.clearAll();
                    PrefsUser.setWishNum(0);
                }else{

                }
            }

            @Override
            public void onError(NetworkError networkError) {

            }
        });
        subscriptions.add(subscription);
    }

    private void removeWish(ProductMini productMini, final int position){
        dialogLoading.show();
        Subscription subscription = service.deleteWish(token, productMini.getProductId(), new Service.DeleteWishCallback() {
            @Override
            public void onSuccess(SubmitResponse submitResponse) {
                dialogLoading.dismiss();
                if(submitResponse.isStatus()){
                    productsWishAdapter.removeProduct(position);
                    PrefsUser.updateWishNum(false);
                }else{

                }
            }

            @Override
            public void onError(NetworkError networkError) {

            }
        });
        subscriptions.add(subscription);
    }

    public void onResume() {
        super.onResume();
        if(!mApp.hasToken()){
            layoutStateLogin.setViewState(MultiStateView.VIEW_STATE_ERROR);
        }else{
            layoutStateLogin.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            token = mApp.getUserToken();
            getWish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(subscriptions != null){
            subscriptions.unsubscribe();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
