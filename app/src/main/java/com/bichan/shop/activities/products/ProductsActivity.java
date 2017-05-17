package com.bichan.shop.activities.products;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.bichan.shop.BaseApp;
import com.bichan.shop.R;
import com.bichan.shop.adapters.home.ProductsAdapter;
import com.bichan.shop.models.ProductMiniResponse;
import com.bichan.shop.models.ProductsFilter;
import com.bichan.shop.networking.NetworkError;
import com.bichan.shop.networking.Service;
import com.github.ybq.endless.Endless;
import com.nex3z.notificationbadge.NotificationBadge;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class ProductsActivity extends BaseApp implements View.OnClickListener{
    public static final String EXTRA_CATEGORY_ID = "EXTRA_CATEGORY_ID";
    public static final String EXTRA_CATEGORY_NAME = "EXTRA_CATEGORY_NAME";
    public static final String EXTRA_NAME_SEARCH = "EXTRA_NAME_SEARCH";

    @Inject
    public Service service;

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.btnBack)
    AppCompatImageButton btnBack;
    @BindView(R.id.btnFavorite)
    AppCompatImageButton btnFavorite;
    @BindView(R.id.favoriteBadge)
    NotificationBadge favoriteBadge;
    @BindView(R.id.btnCart)
    AppCompatImageButton btnCart;
    @BindView(R.id.cartBadge)
    NotificationBadge cartBadge;

    @BindView(R.id.rvProducts)
    RecyclerView rvProducts;

    private ProductsAdapter productsAdapter;
    StaggeredGridLayoutManager manager;
    private Endless endless;
    private ProductsFilter productsFilter;
    private String title;

    private CompositeSubscription subscriptions;

    private void init(){
        productsFilter = new ProductsFilter(10);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            productsFilter.setCategoryId(bundle.getString(EXTRA_CATEGORY_ID));
            productsFilter.setName(bundle.getString(EXTRA_NAME_SEARCH));
            title = bundle.getString(EXTRA_CATEGORY_NAME) + productsFilter.getName();
        }
        subscriptions = new CompositeSubscription();
    }

    private void initView(){
        tvTitle.setText(title);
        favoriteBadge.setNumber(3);
        cartBadge.setNumber(5);
        btnBack.setOnClickListener(this);

        rvProducts.setHasFixedSize(true);
        productsAdapter = new ProductsAdapter(this);
        manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        manager.setSpanCount(2);
        rvProducts.setLayoutManager(manager);
        rvProducts.setAdapter(productsAdapter);
        View loadingView = View.inflate(this, R.layout.layout_loading, null);
        endless = Endless.applyTo(rvProducts, loadingView);
        endless.setLoadMoreListener(new Endless.LoadMoreListener() {
            @Override
            public void onLoadMore(int page) {
                productsFilter.next();
                getMoreProduct();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDeps().inject(this);
        setContentView(R.layout.activity_products);
        ButterKnife.bind(this);
        init();
        initView();
        getMoreProduct();
    }

    private void getMoreProduct(){
        productsAdapter.startLoading();
        Subscription subscription = service.getProducts(productsFilter, new Service.GetProductsCallback() {
            @Override
            public void onSuccess(ProductMiniResponse productMiniResponse) {
                productsAdapter.stopLoading();
                if(productMiniResponse.getProductMinis().size() != 0){
                    productsAdapter.addProducts(productMiniResponse.getProductMinis());
                    endless.loadMoreComplete();
                }else{
                    endless.setLoadMoreAvailable(false);
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnBack:
                onBackPressed();
                break;
        }
    }
}
