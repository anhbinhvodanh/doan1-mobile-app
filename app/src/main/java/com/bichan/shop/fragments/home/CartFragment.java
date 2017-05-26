package com.bichan.shop.fragments.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bichan.shop.BaseFragment;
import com.bichan.shop.MyApplication;
import com.bichan.shop.Prefs.PrefsUser;
import com.bichan.shop.R;
import com.bichan.shop.activities.product.ProductDetailActivity;
import com.bichan.shop.adapters.cart.ProductCartAdapter;
import com.bichan.shop.models.ProductMiniCart;
import com.bichan.shop.models.ProductMiniCartResponse;
import com.bichan.shop.models.SubmitResponse;
import com.bichan.shop.networking.NetworkError;
import com.bichan.shop.networking.Service;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.facebook.FacebookSdk.getApplicationContext;

public class CartFragment extends BaseFragment {
    @Inject
    public Service service;

    @BindView(R.id.rvProducts)
    RecyclerView rvProducts;
    @BindView(R.id.btnClearCart)
    Button btnClearCart;

    private ProductCartAdapter productCartAdapter;
    StaggeredGridLayoutManager manager;

    private MaterialDialog dialogLoading;

    private String token;
    private CompositeSubscription subscriptions;

    public CartFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDeps().inject(this);
        MyApplication mApp = ((MyApplication)getApplicationContext());
        if(!mApp.hasToken()){
            // login
            getActivity().finish();
        }
        token = mApp.getUserToken();


        subscriptions = new CompositeSubscription();

        dialogLoading = new MaterialDialog.Builder(getActivity())
                .customView(R.layout.layout_dialog_loading, false)
                .canceledOnTouchOutside(true)
                .build();
        dialogLoading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvProducts.setHasFixedSize(true);
        productCartAdapter = new ProductCartAdapter(getActivity());
        productCartAdapter.changeView();
        manager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        manager.setSpanCount(1);
        rvProducts.setLayoutManager(manager);
        rvProducts.setAdapter(productCartAdapter);

        productCartAdapter.setOnItemProductClickListener(new ProductCartAdapter.OnItemProductClickListener() {
            @Override
            public void onClick(ProductMiniCart productMini) {
                Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                intent.putExtra(ProductDetailActivity.EXTRA_PRODUCT_ID, productMini.getProductId());
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        productCartAdapter.setOnRemoveClickListener(new ProductCartAdapter.OnRemoveClickListener() {
            @Override
            public void onClick(ProductMiniCart productMini, int position) {
                removeCart(productMini, position);
            }
        });

        productCartAdapter.setOnCartQuantityChangeListener(new ProductCartAdapter.OnCartQuantityChangeListener() {

            @Override
            public void onClick(ProductMiniCart productMini, int value) {
                updateCart(productMini, value);
            }
        });

        btnClearCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearCart();
            }
        });
    }

    private void getCart(){
        productCartAdapter.clearAll();
        productCartAdapter.startLoading();
        Subscription subscription = service.getCart(token, new Service.GetCartCallback() {
            @Override
            public void onSuccess(ProductMiniCartResponse productMiniResponse) {
                productCartAdapter.stopLoading();
                productCartAdapter.addProducts(productMiniResponse.getProductMinis());
                int num = 0;
                for(ProductMiniCart productMiniCart: productMiniResponse.getProductMinis()){
                    try{
                        num += Integer.parseInt(productMiniCart.getQuantityCart());
                    }catch (Exception e){
                        num += 0;
                    }
                }
                PrefsUser.setCartNum(num);
            }

            @Override
            public void onError(NetworkError networkError) {
            }
        });
        subscriptions.add(subscription);
    }


    private void clearCart(){
        dialogLoading.show();
        Subscription subscription = service.clearCart(token, new Service.ClearCartCallback() {
            @Override
            public void onSuccess(SubmitResponse submitResponse) {
                dialogLoading.dismiss();
                if(submitResponse.isStatus()){
                    productCartAdapter.clearAll();
                    PrefsUser.setCartNum(0);
                }else{

                }
            }

            @Override
            public void onError(NetworkError networkError) {

            }
        });
        subscriptions.add(subscription);
    }

    private void removeCart(final ProductMiniCart productMini, final int position){
        dialogLoading.show();
        Subscription subscription = service.deleteCart(token, productMini.getProductOptionId(), new Service.DeleteCartCallback() {
            @Override
            public void onSuccess(SubmitResponse submitResponse) {
                dialogLoading.dismiss();
                if(submitResponse.isStatus()){
                    productCartAdapter.removeProduct(position);
                    int num = PrefsUser.getCartNum();
                    PrefsUser.setCartNum(num - Integer.parseInt(productMini.getQuantityCart()));
                }else{

                }
            }

            @Override
            public void onError(NetworkError networkError) {

            }
        });
        subscriptions.add(subscription);
    }


    private void updateCart(ProductMiniCart productMini, final int value){
        dialogLoading.show();
        Subscription subscription = service.updateCart(token, productMini.getProductOptionId(), Integer.toString(value), new Service.UpdateCartCallback() {
            @Override
            public void onSuccess(SubmitResponse submitResponse) {
                dialogLoading.dismiss();
                PrefsUser.setCartNum(value);
            }

            @Override
            public void onError(NetworkError networkError) {

            }
        });
        subscriptions.add(subscription);
    }

    @Override
    public void onResume() {
        super.onResume();
        getCart();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
