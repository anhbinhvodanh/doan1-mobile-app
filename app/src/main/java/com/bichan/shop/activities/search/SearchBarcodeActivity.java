package com.bichan.shop.activities.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.bichan.shop.R;
import com.bichan.shop.activities.product.ProductDetailActivity;
import com.blankj.utilcode.util.BarUtils;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class SearchBarcodeActivity extends AppCompatActivity implements ZBarScannerView.ResultHandler {

    private ZBarScannerView mScannerView;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_search_barcode);
        BarUtils.setTransparentStatusBar(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
            ab.setTitle("Quét mã vạch");
        }

        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZBarScannerView(this);
        contentFrame.addView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }
    @Override
    public void handleResult(Result result) {
        /*Intent productsIntent = new Intent(this, ProductsActivity.class);
        productsIntent.putExtra(ProductsActivity.EXTRA_CATEGORY_ID, "");
        productsIntent.putExtra(ProductsActivity.EXTRA_CATEGORY_NAME, "");
        productsIntent.putExtra(ProductsActivity.EXTRA_NAME_SEARCH, result.getContents());
        startActivity(productsIntent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        mScannerView.resumeCameraPreview(this);
        finish();*/

        openProductDetailActivity("112");
    }

    private void openProductDetailActivity(String productId){
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra(ProductDetailActivity.EXTRA_PRODUCT_ID, productId);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
