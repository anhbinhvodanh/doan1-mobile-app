package com.bichan.shop;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bichan.shop.Prefs.PrefsUser;
import com.bichan.shop.activities.home.HomeActivity;
import com.bichan.shop.models.DataLogin;
import com.bichan.shop.models.DataLoginSocial;
import com.bichan.shop.models.LoginResponse;
import com.bichan.shop.models.TotalResponse;
import com.bichan.shop.networking.NetworkError;
import com.bichan.shop.networking.Service;

import javax.inject.Inject;

import butterknife.ButterKnife;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends BaseApp {
    @Inject
    public Service service;
    MyApplication mApp;

    private CompositeSubscription subscriptions;
    MaterialDialog dialogLoading;

    private void init(){
        mApp = ((MyApplication)getApplicationContext());
        subscriptions = new CompositeSubscription();
        dialogLoading = new MaterialDialog.Builder(this)
                .customView(R.layout.layout_dialog_loading, false)
                .cancelable(false)
                .canceledOnTouchOutside(true)
                .build();
        dialogLoading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    private void initView(){

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDeps().inject(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        init();
        initView();

        checkLogin();



    }

    private void open(){
        // test
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();

        /*Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra(ProductDetailActivity.EXTRA_PRODUCT_ID, "113");
        startActivity(intent);
        finish();*/
    }

    private void checkLogin(){
        if(PrefsUser.isLogin()){
            DataLogin dataLogin = PrefsUser.getDataLogin();
            login(dataLogin);
            return;
        }

        if(PrefsUser.isLoginSocial()){
            DataLoginSocial dataLoginSocial = PrefsUser.getSocial();
            loginSocial(dataLoginSocial);
            return;
        }
        mApp.removeToken();
        open();

    }

    private void loginSocial(final DataLoginSocial dataLoginSocial){
        dialogLoading.show();
        Subscription subscription = service.loginSocial(
                dataLoginSocial.getSocial_id(),
                dataLoginSocial.getNetwork(),
                new Service.LoginSocialCallback() {
                    @Override
                    public void onSuccess(LoginResponse loginResponse) {
                        dialogLoading.dismiss();
                        if(loginResponse.isStatus()){
                            PrefsUser.setSocial(dataLoginSocial.getSocial(), dataLoginSocial.getSocial_id());
                            mApp.setUserToken(loginResponse.getToken());
                            getCartTotal(loginResponse.getToken());
                            Log.d("ahihi", loginResponse.getToken());
                        }else{
                            mApp.removeToken();
                        }
                    }

                    @Override
                    public void onError(NetworkError networkError) {
                        dialogLoading.dismiss();
                    }
                }
        );
        subscriptions.add(subscription);
    }

    private void login(final DataLogin dataLogin){
        dialogLoading.show();
        Subscription subscription = service.login(
                dataLogin.getEmail(),
                dataLogin.getPassword(),
                new Service.LoginCallback() {
                    @Override
                    public void onSuccess(LoginResponse loginResponse) {
                        dialogLoading.dismiss();
                        if(loginResponse.isStatus()){
                            PrefsUser.setEmail(dataLogin.getEmail());
                            PrefsUser.setPassword(dataLogin.getPassword());
                            mApp.setUserToken(loginResponse.getToken());
                            getCartTotal(loginResponse.getToken());
                        }else{
                            mApp.removeToken();
                        }
                    }

                    @Override
                    public void onError(NetworkError networkError) {
                        dialogLoading.dismiss();
                    }
                }

        );
        subscriptions.add(subscription);
    }


    private void getCartTotal(final String token){
        dialogLoading.show();
        Subscription subscription = service.getCartTotal(token, new Service.GetCartTotalCallback() {
            @Override
            public void onSuccess(TotalResponse totalResponse) {
                if(totalResponse.isStatus()){
                    PrefsUser.setCartNum(totalResponse.getData());
                }
                getWishTotal(token);
            }

            @Override
            public void onError(NetworkError networkError) {
                open();
            }
        });
        subscriptions.add(subscription);
    }


    private void getWishTotal(String token){
        dialogLoading.show();
        Subscription subscription = service.getWishTotal(token, new Service.GetWishTotalCallback() {
            @Override
            public void onSuccess(TotalResponse totalResponse) {
                if(totalResponse.isStatus()){
                    PrefsUser.setWishNum(totalResponse.getData());
                }
                open();
            }

            @Override
            public void onError(NetworkError networkError) {
                open();
            }
        });
        subscriptions.add(subscription);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(subscriptions != null){
            subscriptions.unsubscribe();
        }
    }


}
