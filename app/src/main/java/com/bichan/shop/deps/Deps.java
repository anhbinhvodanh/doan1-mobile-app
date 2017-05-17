package com.bichan.shop.deps;

import com.bichan.shop.activities.products.ProductsActivity;
import com.bichan.shop.fragments.home.CategoryFragment;
import com.bichan.shop.fragments.home.HomeCategoryListFragment;
import com.bichan.shop.networking.NetworkModule;
import com.bichan.shop.activities.home.HomeActivity;

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
}