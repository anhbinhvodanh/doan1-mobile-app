package com.bichan.shop.activities.order;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bichan.shop.BaseApp;
import com.bichan.shop.MyApplication;
import com.bichan.shop.R;
import com.bichan.shop.activities.product.ProductDetailActivity;
import com.bichan.shop.adapters.order.OrderProductAdapter;
import com.bichan.shop.models.OrderDetal;
import com.bichan.shop.models.OrderDetalResponse;
import com.bichan.shop.models.OrderProduct;
import com.bichan.shop.networking.NetworkError;
import com.bichan.shop.networking.Service;

import org.fabiomsr.moneytextview.MoneyTextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class OrderDetalActivity extends BaseApp {
    public static final String EXTRA_ORDER_ID = "EXTRA_ORDER_ID";

    @BindView(R.id.btnBack)
    AppCompatImageButton btnBack;
    @BindView(R.id.tvOrderId)
    TextView tvOrderId;
    @BindView(R.id.tvDateAdded)
    TextView tvDateAdded;
    @BindView(R.id.tvShippingStatus)
    TextView tvShippingStatus;
    @BindView(R.id.tvCustomer)
    TextView tvCustomer;
    @BindView(R.id.tvCity)
    TextView tvCity;
    @BindView(R.id.tvZone)
    TextView tvZone;
    @BindView(R.id.tvPayStatus)
    TextView tvPayStatus;
    @BindView(R.id.tvTotalDiscount)
    MoneyTextView tvTotalDiscount;
    @BindView(R.id.rvOrderProduct)
    RecyclerView rvOrderProduct;

    OrderProductAdapter orderProductAdapter;
    StaggeredGridLayoutManager manager;

    MyApplication mApp;
    private String token;
    private String orderId;

    private CompositeSubscription subscriptions;

    private MaterialDialog dialogLoading;

    @Inject
    public Service service;

    private void init(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            orderId = bundle.getString(EXTRA_ORDER_ID);
        }else{
            finish();
        }

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
        rvOrderProduct.setHasFixedSize(true);
        rvOrderProduct.setNestedScrollingEnabled(false);
        orderProductAdapter = new OrderProductAdapter();
        manager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        manager.setSpanCount(1);
        rvOrderProduct.setLayoutManager(manager);
        rvOrderProduct.setAdapter(orderProductAdapter);

        orderProductAdapter.setOnItemClickListener(new OrderProductAdapter.OnItemClickListener() {
            @Override
            public void onClick(OrderProduct orderProduct) {
                Intent intent = new Intent(OrderDetalActivity.this, ProductDetailActivity.class);
                intent.putExtra(ProductDetailActivity.EXTRA_PRODUCT_ID, orderProduct.getOrderProductId());
                intent.putExtra(ProductDetailActivity.EXTRA_OPTION_ID, orderProduct.getProductOptionId());
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
        setContentView(R.layout.activity_order_detal);
        ButterKnife.bind(this);
        init();
        initView();

        getOrder();
    }


    private void getOrder(){
        dialogLoading.show();
        Subscription subscription = service.getOrder(token, orderId, new Service.GetOrderCallback() {
            @Override
            public void onSuccess(OrderDetalResponse orderDetalResponse) {
                dialogLoading.dismiss();
                if(orderDetalResponse.isStatus()){
                    setDataOrder(orderDetalResponse.getData());
                }else {
                    finish();
                }
            }

            @Override
            public void onError(NetworkError networkError) {

            }
        });
        subscriptions.add(subscription);
    }

    private void setDataOrder(OrderDetal dataOrder){
        tvCity.setText(dataOrder.getCity());
        tvCustomer.setText(dataOrder.getCustomer());
        tvZone.setText(dataOrder.getZone());
        tvDateAdded.setText(dataOrder.getDateAdded());
        tvShippingStatus.setText(dataOrder.getShippingStatus());
        tvOrderId.setText(dataOrder.getOrderId());
        tvPayStatus.setText(dataOrder.getStatus());
        float total = 0;

        try{
            total = Float.parseFloat(dataOrder.getTotal());
        }catch (Exception e){
            total = 0;
        }

        tvTotalDiscount.setAmount(total);

        orderProductAdapter.clear();
        orderProductAdapter.setOrderProducts(dataOrder.getProducts());

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
