package com.bichan.shop.deps;

import com.bichan.shop.MainActivity;
import com.bichan.shop.activities.cart.CheckoutActivity;
import com.bichan.shop.activities.home.HomeActivity;
import com.bichan.shop.activities.login.LoginActivity;
import com.bichan.shop.activities.order.OrderActivity;
import com.bichan.shop.activities.order.OrderDetalActivity;
import com.bichan.shop.activities.product.ProductAddReviewActivity;
import com.bichan.shop.activities.product.ProductDetailActivity;
import com.bichan.shop.activities.product.ProductReviewActivity;
import com.bichan.shop.activities.products.ProductsActivity;
import com.bichan.shop.activities.wish.WishActivity;
import com.bichan.shop.fragments.home.CartFragment;
import com.bichan.shop.fragments.home.CategoryFragment;
import com.bichan.shop.fragments.home.HomeCategoryListFragment;
import com.bichan.shop.networking.NetworkModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by cuong on 5/16/2017.
 */


@Singleton
@Component(modules = {NetworkModule.class,})
public interface Deps {
    void inject(HomeActivity homeActivity);
    void inject(HomeCategoryListFragment homeCategoryListFragment);
    void inject(CategoryFragment categoryFragment);
    void inject(ProductsActivity productsActivity);
    void inject(ProductDetailActivity productDetailActivity);
    void inject(ProductReviewActivity productReviewActivity);
    void inject(ProductAddReviewActivity productAddReviewActivity);
    void inject(LoginActivity loginActivity);
    void inject(WishActivity wishActivity);
    void inject(CartFragment cartFragment);
    void inject(MainActivity mainActivity);
    void inject(OrderActivity orderActivity);
    void inject(OrderDetalActivity orderDetalActivity);
    void inject(CheckoutActivity checkoutActivity);
}