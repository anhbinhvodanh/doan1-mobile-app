package com.bichan.shop.networking;

import com.bichan.shop.models.CategoryResponse;
import com.bichan.shop.models.CustomerRespone;
import com.bichan.shop.models.HomeCategoryResponse;
import com.bichan.shop.models.HomeSliderResponse;
import com.bichan.shop.models.LoginResponse;
import com.bichan.shop.models.ProductMiniCartResponse;
import com.bichan.shop.models.ProductMiniResponse;
import com.bichan.shop.models.ProductOptionResponse;
import com.bichan.shop.models.ProductResponse;
import com.bichan.shop.models.RegisterResponse;
import com.bichan.shop.models.ReviewAddResponse;
import com.bichan.shop.models.ReviewResponse;
import com.bichan.shop.models.SubmitResponse;
import com.bichan.shop.models.TotalResponse;

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

    @GET("api/review?act=getReviews")
    Observable<ReviewResponse> getProductReview(
            @Query("product_id") String productId);

    @GET("api/review?act=addReview")
    Observable<ReviewAddResponse> addProductReview(
            @Query("product_id") String productId,
            @Query("text") String text,
            @Query("rating") String rating,
            @Query("token") String token);

    @GET("api/customer?act=register")
    Observable<RegisterResponse> register(
            @Query("email") String email,
            @Query("firstname") String firstname,
            @Query("lastname") String lastname,
            @Query("password") String password,
            @Query("social_id") String social_id,
            @Query("network") String network);

    @GET("api/customer?act=login")
    Observable<LoginResponse> login(
            @Query("email") String email,
            @Query("password") String password);

    @GET("api/customer?act=login")
    Observable<LoginResponse> loginSocial(
            @Query("social_id") String social_id,
            @Query("network") String network);

    @GET("api/customer?act=getWish")
    Observable<ProductMiniResponse> getWish(
            @Query("token") String token);

    @GET("api/customer?act=clearWish")
    Observable<SubmitResponse> clearWish(
            @Query("token") String token);

    @GET("api/customer?act=deleteWish")
    Observable<SubmitResponse> deleteWish(
            @Query("token") String token,
            @Query("product_id") String product_id);

    @GET("api/customer?act=addWish")
    Observable<SubmitResponse> addWish(
            @Query("token") String token,
            @Query("product_id") String product_id);


    @GET("api/customer?act=getCart")
    Observable<ProductMiniCartResponse> getCart(
            @Query("token") String token);

    @GET("api/customer?act=addCart")
    Observable<SubmitResponse> addCart(
            @Query("token") String token,
            @Query("product_option_id") String product_option_id,
            @Query("quantity") String quantity);

    @GET("api/customer?act=updateCart")
    Observable<SubmitResponse> updateCart(
            @Query("token") String token,
            @Query("product_option_id") String product_id,
            @Query("quantity") String quantity);

    @GET("api/customer?act=deleteCart")
    Observable<SubmitResponse> deleteCart(
            @Query("token") String token,
            @Query("product_option_id") String product_id);

    @GET("api/customer?act=clearCart")
    Observable<SubmitResponse> clearCart(
            @Query("token") String token);

    @GET("api/customer?act=getCustomer")
    Observable<CustomerRespone> getCustomer(
            @Query("token") String token);


    @GET("api/customer?act=getWishTotal")
    Observable<TotalResponse> getWishTotal(
            @Query("token") String token);

    @GET("api/customer?act=getCartTotal")
    Observable<TotalResponse> getCartTotal(
            @Query("token") String token);

    @GET("api/customer?act=checkWished")
    Observable<SubmitResponse> checkWished(
            @Query("token") String token,
            @Query("product_id") String product_id);

}