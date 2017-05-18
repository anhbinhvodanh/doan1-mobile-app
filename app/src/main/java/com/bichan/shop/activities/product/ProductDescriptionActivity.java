package com.bichan.shop.activities.product;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.bichan.shop.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductDescriptionActivity extends AppCompatActivity {
    public static final String EXTRA_DATA = "EXTRA_DATA";
    private String data;

    @BindView(R.id.btnBack)
    AppCompatImageButton btnBack;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.webview)
    WebView webview;

    private void init(){
        tvTitle.setText("Mô tả sản phẩm");
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            data = bundle.getString(EXTRA_DATA);
        }else {
            data = "";
        }
    }

    private void initView(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        final String mimeType = "text/html";
        final String encoding = "UTF-8";

        webview.loadDataWithBaseURL("", data, mimeType, encoding, "");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_description);
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
