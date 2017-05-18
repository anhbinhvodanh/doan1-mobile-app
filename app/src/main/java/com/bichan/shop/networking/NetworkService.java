package com.bichan.shop.networking;

import com.bichan.shop.models.CategoryResponse;
import com.bichan.shop.models.HomeCategoryResponse;
import com.bichan.shop.models.HomeSliderResponse;
import com.bichan.shop.models.ProductMiniResponse;
import com.bichan.shop.models.ProductOptionResponse;
import com.bichan.shop.models.ProductResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by cuong on 5/16/2017.
 */

public interface NetworkService {

    @GET("api/category?act=getDesktop")
    Observable<HomeCategoryResponse> getHomeCategory();

    @GET("api/category?act=getCategories")
    Observable<CategoryResponse> getCategory();

    @GET("api/setting?act=getSlider")
    Observable<HomeSliderResponse> getHomeSlider();

    @GET("api/product?act=getProducts&filter_sub_category=true")
    Observable<ProductMiniResponse> getProducts(
            @Query("filter_category_id") String categoryId,
            @Query("filter_name") String name,
            @Query("start") int start,
            @Query("limit") int limit,
            @Query("sort") String sort,
            @Query("order") String order);

    @GET("api/product?act=getProduct")
    Observable<ProductResponse> getProduct(
            @Query("product_id") String productId);

    @GET("api/product?act=getOptionsByProductId")
    Observable<ProductOptionResponse> getProductOption(
            @Query("product_id") String productId);
}