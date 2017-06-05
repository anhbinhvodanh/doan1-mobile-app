package com.bichan.shop.activities.order;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bichan.shop.BaseApp;
import com.bichan.shop.MyApplication;
import com.bichan.shop.R;
import com.bichan.shop.adapters.order.OrderAdapter;
import com.bichan.shop.models.Order;
import com.bichan.shop.models.OrderResponse;
import com.bichan.shop.networking.NetworkError;
import com.bichan.shop.networking.Service;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class OrderActivity extends BaseApp {
    @Inject
    public Service service;

    @BindView(R.id.btnBack)
    AppCompatImageButton btnBack;
    @BindView(R.id.rvOrder)
    RecyclerView rvOrder;

    MyApplication mApp;
    private String token;

    private OrderAdapter orderAdapter;
    StaggeredGridLayoutManager manager;

    private CompositeSubscription subscriptions;

    private MaterialDialog dialogLoading;

    private void init(){
        mApp = ((MyApplication)getApplicationContext());
        if(!mApp.hasToken()){
            finish();
        }
        token = mApp.getUserToken();
        subscriptions = new CompositeSubscription();

        dialogLoading = new MaterialDialog.Builder(this)
                .customView(R.layout.layout_dialog_loading, false)
                .canceledOnTouchOutside(true)
                .build();
        dialogLoading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    private void initView(){
        rvOrder.setHasFixedSize(true);
        orderAdapter = new OrderAdapter();
        manager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        manager.setSpanCount(1);
        rvOrder.setLayoutManager(manager);
        rvOrder.setAdapter(orderAdapter);

        orderAdapter.setOnItemClickListener(new OrderAdapter.OnItemClickListener() {
            @Override
            public void onClick(Order order) {
                Intent intent = new Intent(OrderActivity.this, OrderDetalActivity.class);
                intent.putExtra(OrderDetalActivity.EXTRA_ORDER_ID, order.getOrderId());
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDeps().inject(this);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);

        init();
        initView();
        getOrders();
    }

    private void getOrders(){
        dialogLoading.show();
        Subscription subscription = service.getOrders(token, new Service.GetOrdersCallback() {
            @Override
            public void onSuccess(OrderResponse orderResponse) {
                dialogLoading.dismiss();
                if(orderResponse.isStatus()){
                    orderAdapter.clear();
                    orderAdapter.setOrders(orderResponse.getData());
                }
            }

            @Override
            public void onError(NetworkError networkError) {

            }
        });
        subscriptions.add(subscription);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscriptions != null) {
            subscriptions.unsubscribe();
        }
    }
}
