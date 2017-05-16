package com.bichan.shop.networking;

import com.bichan.shop.models.CategoryResponse;
import com.bichan.shop.models.HomeCategoryResponse;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by cuong on 5/16/2017.
 */

public interface NetworkService {

    @GET("api/category?act=getDesktop")
    Observable<HomeCategoryResponse> getHomeCategory();

    @GET("api/category?act=getCategories")
    Observable<CategoryResponse> getCategory();
}