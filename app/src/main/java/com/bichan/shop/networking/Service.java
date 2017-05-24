package com.bichan.shop.networking;


import com.bichan.shop.models.CategoryResponse;
import com.bichan.shop.models.HomeCategoryResponse;
import com.bichan.shop.models.HomeSliderResponse;
import com.bichan.shop.models.ProductMiniResponse;
import com.bichan.shop.models.ProductOptionResponse;
import com.bichan.shop.models.ProductResponse;
import com.bichan.shop.models.ProductsFilter;
import com.bichan.shop.models.ReviewAddResponse;
import com.bichan.shop.models.ReviewResponse;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;



/**
 * Created by cuong on 5/16/2017.
 */

public class Service {
    private final NetworkService networkService;

    public Service(NetworkService networkService) {
        this.networkService = networkService;
    }

    public Subscription getHomeCategory(final GetHomeCategoryCallback callback) {

        return networkService.getHomeCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends HomeCategoryResponse>>() {
                    @Override
                    public Observable<? extends HomeCategoryResponse> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<HomeCategoryResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));

                    }

                    @Override
                    public void onNext(HomeCategoryResponse homeCategoryResponse) {
                        callback.onSuccess(homeCategoryResponse);
                    }
                });
    }

    public interface GetHomeCategoryCallback {
        void onSuccess(HomeCategoryResponse homeCategoryResponse);
        void onError(NetworkError networkError);
    }



    public interface GetCategoryCallback {
        void onSuccess(CategoryResponse categoryResponse);
        void onError(NetworkError networkError);
    }

    /**
     * Api get list of category
     * @param callback
     * @return
     */
    public Subscription getCategory(final GetCategoryCallback callback) {
        return networkService.getCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends CategoryResponse>>() {
                    @Override
                    public Observable<? extends CategoryResponse> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<CategoryResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));

                    }

                    @Override
                    public void onNext(CategoryResponse categoryResponse) {
                        callback.onSuccess(categoryResponse);
                    }
                });
    }


    public interface GetHomeSliderCallback {
        void onSuccess(HomeSliderResponse homeSliderResponse);
        void onError(NetworkError networkError);
    }

    public Subscription getHomeSlider(final GetHomeSliderCallback callback) {
        return networkService.getHomeSlider()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends HomeSliderResponse>>() {
                    @Override
                    public Observable<? extends HomeSliderResponse> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<HomeSliderResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));

                    }

                    @Override
                    public void onNext(HomeSliderResponse homeSliderResponse) {
                        callback.onSuccess(homeSliderResponse);
                    }
                });
    }

    public interface GetProductsCallback {
        void onSuccess(ProductMiniResponse productMiniResponse);
        void onError(NetworkError networkError);
    }

    public Subscription getProducts(ProductsFilter productsFilter, final GetProductsCallback callback) {
        return networkService.getProducts(
                productsFilter.getCategoryId(),
                productsFilter.getName(),
                productsFilter.getStart(),
                productsFilter.getLimit(),
                productsFilter.getSortType(),
                productsFilter.getSortOrder())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends ProductMiniResponse>>() {
                    @Override
                    public Observable<? extends ProductMiniResponse> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<ProductMiniResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));

                    }

                    @Override
                    public void onNext(ProductMiniResponse productMiniResponse) {
                        callback.onSuccess(productMiniResponse);
                    }
                });
    }


    public interface GetProductCallback {
        void onSuccess(ProductResponse productResponse);
        void onError(NetworkError networkError);
    }

    public Subscription getProduct(String productId, final GetProductCallback callback) {
        return networkService.getProduct(productId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends ProductResponse>>() {
                    @Override
                    public Observable<? extends ProductResponse> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<ProductResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));

                    }

                    @Override
                    public void onNext(ProductResponse productMiniResponse) {
                        callback.onSuccess(productMiniResponse);
                    }
                });
    }


    public interface GetProductOptionCallback {
        void onSuccess(ProductOptionResponse productOptionResponse);
        void onError(NetworkError networkError);
    }

    public Subscription getProductOption(String productId, final GetProductOptionCallback callback) {
        return networkService.getProductOption(productId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends ProductOptionResponse>>() {
                    @Override
                    public Observable<? extends ProductOptionResponse> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<ProductOptionResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));

                    }

                    @Override
                    public void onNext(ProductOptionResponse productOptionResponse) {
                        callback.onSuccess(productOptionResponse);
                    }
                });
    }


    public interface GetProductReviewCallback {
        void onSuccess(ReviewResponse reviewResponse);
        void onError(NetworkError networkError);
    }

    public Subscription getProductReview(String productId, final GetProductReviewCallback callback) {
        return networkService.getProductReview(productId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends ReviewResponse>>() {
                    @Override
                    public Observable<? extends ReviewResponse> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<ReviewResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));

                    }

                    @Override
                    public void onNext(ReviewResponse reviewResponse) {
                        callback.onSuccess(reviewResponse);
                    }
                });
    }

    public interface AddProductReviewCallback {
        void onSuccess(ReviewAddResponse reviewAddResponse);
        void onError(NetworkError networkError);
    }

    public Subscription addProductReview(String productId, String text, String rating, String token, final AddProductReviewCallback callback) {
        return networkService.addProductReview(productId, text, rating, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends ReviewAddResponse>>() {
                    @Override
                    public Observable<? extends ReviewAddResponse> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<ReviewAddResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));

                    }

                    @Override
                    public void onNext(ReviewAddResponse reviewAddResponse) {
                        callback.onSuccess(reviewAddResponse);
                    }
                });
    }

}
