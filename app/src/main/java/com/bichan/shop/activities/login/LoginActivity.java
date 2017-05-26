package com.bichan.shop.activities.login;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.afollestad.materialdialogs.MaterialDialog;
import com.astuetz.PagerSlidingTabStrip;
import com.bichan.shop.BaseApp;
import com.bichan.shop.MyApplication;
import com.bichan.shop.Prefs.PrefsUser;
import com.bichan.shop.R;
import com.bichan.shop.adapters.FragmentAdapter;
import com.bichan.shop.fragments.login.LoginFragment;
import com.bichan.shop.fragments.login.RegisterFragment;
import com.bichan.shop.models.Customer;
import com.bichan.shop.models.CustomerRespone;
import com.bichan.shop.models.DataLogin;
import com.bichan.shop.models.DataLoginSocial;
import com.bichan.shop.models.DataRegister;
import com.bichan.shop.models.LoginResponse;
import com.bichan.shop.models.RegisterResponse;
import com.bichan.shop.models.Social;
import com.bichan.shop.networking.NetworkError;
import com.bichan.shop.networking.Service;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Arrays;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class LoginActivity extends BaseApp implements
        GoogleApiClient.OnConnectionFailedListener,
        RegisterFragment.OnSocialClickListener,
        RegisterFragment.OnRegisterClickListener,
        LoginFragment.OnSocialClickListener,
        LoginFragment.OnLoginClickListener{
    public static final int LOGIN_SUCCESSFULL = 1234;

    @Inject
    public Service service;

    @BindView(R.id.viewpaper)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    PagerSlidingTabStrip tabs;

    MyApplication mApp;

    private FragmentAdapter fragmentAdapter;
    private ArrayList<Fragment> fragmentArrayList;

    private static final int RC_SIGN_IN = 9001;
    private static final int RC_FB_LOGIN = 1234;
    private static final int RC_FB_REGISTER = 1235;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions gso;

    private CallbackManager callbackManager;


    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;

    private Fragment fragmentCurrent;

    private CompositeSubscription subscriptions;

    private boolean login;

    MaterialDialog dialogLoading;

    private void init(){
        mApp = ((MyApplication)getApplicationContext());
        dialogLoading = new MaterialDialog.Builder(this)
                .customView(R.layout.layout_dialog_loading, false)
                .cancelable(false)
                .canceledOnTouchOutside(true)
                .build();
        dialogLoading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        subscriptions = new CompositeSubscription();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // facebook
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            private ProfileTracker mProfileTracker;
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                if(Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                            if(login){
                                DataLoginSocial dataLoginSocial = new DataLoginSocial();
                                dataLoginSocial.setSocial(Social.FACEBOOK);
                                dataLoginSocial.setSocial_id(profile2.getId());
                                loginSocial(dataLoginSocial);
                            }else{
                                sendData("", profile2.getName(), profile2.getId(), Social.FACEBOOK);
                            }
                            mProfileTracker.stopTracking();
                        }
                    };
                }
                else {
                    Profile profile = Profile.getCurrentProfile();
                    if(login){
                        DataLoginSocial dataLoginSocial = new DataLoginSocial();
                        dataLoginSocial.setSocial(Social.FACEBOOK);
                        dataLoginSocial.setSocial_id(profile.getId());
                        loginSocial(dataLoginSocial);
                    }else{
                        sendData("", profile.getName(), profile.getId(), Social.FACEBOOK);
                    }
                }
            }
            @Override
            public void onCancel() { Log.e("TAG", "onCancel"); sendData(); }
            @Override
            public void onError(FacebookException exception) { Log.e("TAG", exception.toString()); sendData();}
        });


        fragmentArrayList = new ArrayList<Fragment>();
        fragmentAdapter = new FragmentAdapter(getApplicationContext(), getSupportFragmentManager(), fragmentArrayList);
        viewPager.setAdapter(fragmentAdapter);
        tabs.setViewPager(viewPager);

        loginFragment = new LoginFragment();
        fragmentArrayList.add(loginFragment);
        fragmentAdapter.addTitle("Đăng nhập");

        registerFragment = new RegisterFragment();
        fragmentArrayList.add(registerFragment);
        fragmentAdapter.addTitle("Đăng ký");

        fragmentAdapter.notifyDataSetChanged();
    }

    private void initView(){

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDeps().inject(this);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        init();
        initView();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
            return;
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            if(login){
                DataLoginSocial dataLoginSocial = new DataLoginSocial();
                dataLoginSocial.setSocial(Social.GOOGLE);
                dataLoginSocial.setSocial_id(acct.getId());
                loginSocial(dataLoginSocial);
            }else {
                sendData(acct.getEmail(), acct.getDisplayName(), acct.getId(), Social.GOOGLE);
            }
        } else {
            if(login){

            }else{
                sendData();
            }
        }
    }

    @Override
    public void onClick(Social social, Fragment fragment) {
        login = false;
        fragmentCurrent = fragment;
        switch (social){
            case FACEBOOK:
                linkToFacebook();
                break;
            case GOOGLE:
                linkToGoogle();
                break;
        }
    }

    private void linkToFacebook(){
        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email", "public_profile"));
    }

    private void linkToGoogle(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void sendData(){
        if(fragmentCurrent instanceof RegisterFragment){
            ((RegisterFragment) fragmentCurrent).receiveDataRegister(null);
        }
    }

    private void sendData(String email, String name, String id, Social social){
        if(fragmentCurrent instanceof RegisterFragment){
            DataRegister dataRegister = new DataRegister();
            dataRegister.setEmail(email);
            dataRegister.setFirstname(name);
            dataRegister.setSocial_id(id);
            dataRegister.setSocial(social);
            ((RegisterFragment) fragmentCurrent).receiveDataRegister(dataRegister);
        }
    }

    @Override
    public void onClick(DataRegister dataRegister) {
        register(dataRegister);
    }

    private void register(DataRegister dataRegister){
        if(dataRegister == null)
            return;
        dialogLoading.show();
        Subscription subscription = service.register(
                dataRegister.getEmail(),
                dataRegister.getFirstname(),
                dataRegister.getLastname(),
                dataRegister.getPassword(),
                dataRegister.getSocial_id(),
                dataRegister.getNetwork(),
                new Service.RegisterCallback() {
                    @Override
                    public void onSuccess(RegisterResponse registerResponse) {
                        dialogLoading.dismiss();
                        if(registerResponse.isStatus()){
                            Log.d("TAG", "Dang ky thanh cong");
                            return;
                        }
                        Log.d("TAG", "Dang ky that bai");
                    }

                    @Override
                    public void onError(NetworkError networkError) {
                        dialogLoading.dismiss();
                    }
                });
        subscriptions.add(subscription);
    }

    @Override
    public void onClick(Social social) {
        login = true;
        switch (social){
            case FACEBOOK:
                linkToFacebook();
                break;
            case GOOGLE:
                linkToGoogle();
                break;
        }
    }

    @Override
    public void onClick(DataLogin dataLogin) {
        if(dataLogin != null)
            login(dataLogin);
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
                            getCustomer(loginResponse.getToken());
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
                            getCustomer(loginResponse.getToken());
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


    private void getCustomer(String token){
        dialogLoading.show();
        Subscription subscription = service.getCustomer(token, new Service.GetCustomerCallback() {
            @Override
            public void onSuccess(CustomerRespone customerRespone) {
                dialogLoading.dismiss();
                if(customerRespone.isStatus()){
                    Customer customer = customerRespone.getCustomer();
                    PrefsUser.setEmail(customer.getEmail());
                    PrefsUser.setCustomerId(customer.getCustomerId());
                    PrefsUser.setFirstname(customer.getFirstname());
                    PrefsUser.setLastname(customer.getLastname());
                    PrefsUser.setTelephone(customer.getTelephone());
                    PrefsUser.setDate_added(customer.getDateAdded());
                    // back
                    Intent returnIntent = new Intent();
                    setResult(LOGIN_SUCCESSFULL,returnIntent);
                    finish();
                }else{

                }
            }

            @Override
            public void onError(NetworkError networkError) {

            }
        });
        subscriptions.add(subscription);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(subscriptions != null)
            subscriptions.unsubscribe();
    }
}
