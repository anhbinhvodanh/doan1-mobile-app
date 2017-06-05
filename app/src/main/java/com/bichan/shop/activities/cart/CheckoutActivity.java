package com.bichan.shop.activities.cart;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bichan.shop.BuildConfig;
import com.bichan.shop.MyApplication;
import com.bichan.shop.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CheckoutActivity extends AppCompatActivity {

    @BindView(R.id.btnBack)
    AppCompatImageButton btnBack;
    @BindView(R.id.wvCheckout)
    WebView wvCheckout;


    MyApplication mApp;
    String token;
    String url;

    private MaterialDialog dialogLoading;

    private void init(){
        mApp = ((MyApplication)getApplicationContext());
        if(!mApp.hasToken()){
            finish();
        }
        token = mApp.getUserToken();
        url = BuildConfig.BASEURL + "account/checkout?token=" + token;

        dialogLoading = new MaterialDialog.Builder(this)
                .customView(R.layout.layout_dialog_loading, false)
                .canceledOnTouchOutside(true)
                .build();
        dialogLoading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    private void initView(){

        WebSettings settings = wvCheckout.getSettings();
        settings.setJavaScriptEnabled(true);
        wvCheckout.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        wvCheckout.setWebViewClient(new WebViewClient(){
            public void onPageFinished(WebView view, String url) {
                dialogLoading.dismiss();
            }
        });


        dialogLoading.show();
        wvCheckout.loadUrl(url);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
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
