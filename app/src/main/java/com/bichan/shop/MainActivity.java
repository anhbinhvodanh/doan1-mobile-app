package com.bichan.shop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bichan.shop.activities.product.ProductDetailActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // test
        /*Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();*/

        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra(ProductDetailActivity.EXTRA_PRODUCT_ID, "113");
        startActivity(intent);
        finish();
    }
}
