package com.bichan.shop.activities.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Button;

import com.astuetz.PagerSlidingTabStrip;
import com.bichan.shop.BaseApp;
import com.bichan.shop.R;
import com.bichan.shop.adapters.FragmentAdapter;
import com.bichan.shop.fragments.login.LoginFragment;
import com.bichan.shop.fragments.login.RegisterFragment;
import com.bichan.shop.models.DataRegister;
import com.bichan.shop.models.RegisterResponse;
import com.bichan.shop.models.Social;
import com.bichan.shop.networking.NetworkError;
import com.bichan.shop.networking.Service;
import com.facebook.AccessToken;
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
        RegisterFragment.OnRegisterClickListener{
    @Inject
    public Service service;

    @BindView(R.id.btnLogin)
    Button btnLogin;

    @BindView(R.id.btnLogin2)
    Button btnLogin2;

    @BindView(R.id.viewpaper)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    PagerSlidingTabStrip tabs;

    private FragmentAdapter fragmentAdapter;
    private ArrayList<Fragment> fragmentArrayList;

    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions gso;

    private CallbackManager callbackManager;


    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;

    private Fragment fragmentCurrent;

    private CompositeSubscription subscriptions;

    private void init(){
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
                Log.d("TAG", "success");
                AccessToken accessToken = loginResult.getAccessToken();
                Log.d("TAG", accessToken.getUserId());
                if(Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                            Log.v("TAG", profile2.getName());
                            sendData("", profile2.getName(), profile2.getId(), Social.FACEBOOK);
                            mProfileTracker.stopTracking();
                        }
                    };
                }
                else {
                    Profile profile = Profile.getCurrentProfile();
                    Log.v("TAG", profile.getName());
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
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
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
            Log.d("ahihi", acct.getDisplayName());
            Log.d("ahihi", acct.getEmail());
            Log.d("ahihi", acct.getPhotoUrl().toString());
            Log.d("ahihi", acct.getId());
            sendData(acct.getEmail(), acct.getDisplayName(), acct.getId(), Social.GOOGLE);
        } else {
            sendData();
        }
    }

    @Override
    public void onClick(Social social, Fragment fragment) {
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
                        if(registerResponse.isStatus()){
                            Log.d("TAG", "Dang ky thanh cong");
                            return;
                        }
                        Log.d("TAG", "Dang ky that bai");
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
