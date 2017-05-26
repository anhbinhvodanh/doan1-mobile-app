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
import com.bichan.shop.models.ProductsFilter;
import com.bichan.shop.models.RegisterResponse;
import com.bichan.shop.models.ReviewAddResponse;
import com.bichan.shop.models.ReviewResponse;
import com.bichan.shop.models.SubmitResponse;
import com.bichan.shop.models.TotalResponse;

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

    public interface RegisterCallback {
        void onSuccess(RegisterResponse registerResponse);
        void onError(NetworkError networkError);
    }

    public Subscription register(String email,
                                 String firstname,
                                 String lastname,
                                 String password,
                                 String social_id,
                                 String network,
                                 final RegisterCallback callback) {
        return networkService.register(email, firstname, lastname, password, social_id, network)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends RegisterResponse>>() {
                    @Override
                    public Observable<? extends RegisterResponse> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<RegisterResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));

                    }

                    @Override
                    public void onNext(RegisterResponse registerResponse) {
                        callback.onSuccess(registerResponse);
                    }
                });
    }

    public interface LoginCallback {
        void onSuccess(LoginResponse loginResponse);
        void onError(NetworkError networkError);
    }

    public Subscription login(String email,
                                 String password,
                                 final LoginCallback callback) {
        return networkService.login(email, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends LoginResponse>>() {
                    @Override
                    public Observable<? extends LoginResponse> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<LoginResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));

                    }

                    @Override
                    public void onNext(LoginResponse loginResponse) {
                        callback.onSuccess(loginResponse);
                    }
                });
    }

    public interface LoginSocialCallback {
        void onSuccess(LoginResponse loginResponse);
        void onError(NetworkError networkError);
    }

    public Subscription loginSocial(String social_id,
                                    String network,
                                    final LoginSocialCallback callback) {
        return networkService.loginSocial(social_id, network)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends LoginResponse>>() {
                    @Override
                    public Observable<? extends LoginResponse> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<LoginResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));

                    }

                    @Override
                    public void onNext(LoginResponse loginResponse) {
                        callback.onSuccess(loginResponse);
                    }
                });
    }


    public interface GetWishCallback {
        void onSuccess(ProductMiniResponse productMiniResponse);
        void onError(NetworkError networkError);
    }

    public Subscription getWish(String token, final GetWishCallback callback) {
        return networkService.getWish(token)
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

    public interface ClearWishCallback {
        void onSuccess(SubmitResponse submitResponse);
        void onError(NetworkError networkError);
    }

    public Subscription clearWish(String token, final ClearWishCallback callback) {
        return networkService.clearWish(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends SubmitResponse>>() {
                    @Override
                    public Observable<? extends SubmitResponse> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<SubmitResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));

                    }

                    @Override
                    public void onNext(SubmitResponse submitResponse) {
                        callback.onSuccess(submitResponse);
                    }
                });
    }


    public interface DeleteWishCallback {
        void onSuccess(SubmitResponse submitResponse);
        void onError(NetworkError networkError);
    }

    public Subscription deleteWish(String token, String product_id, final DeleteWishCallback callback) {
        return networkService.deleteWish(token, product_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends SubmitResponse>>() {
                    @Override
                    public Observable<? extends SubmitResponse> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<SubmitResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));

                    }

                    @Override
                    public void onNext(SubmitResponse submitResponse) {
                        callback.onSuccess(submitResponse);
                    }
                });
    }

    public interface AddWishCallback {
        void onSuccess(SubmitResponse submitResponse);
        void onError(NetworkError networkError);
    }

    public Subscription addWish(String token, String product_id, final AddWishCallback callback) {
        return networkService.addWish(token, product_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends SubmitResponse>>() {
                    @Override
                    public Observable<? extends SubmitResponse> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<SubmitResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));

                    }

                    @Override
                    public void onNext(SubmitResponse submitResponse) {
                        callback.onSuccess(submitResponse);
                    }
                });
    }

    /////////////////// API CART ////////////////////

    public interface GetCartCallback {
        void onSuccess(ProductMiniCartResponse productMiniCartResponse);
        void onError(NetworkError networkError);
    }

    public Subscription getCart(String token, final GetCartCallback callback) {
        return networkService.getCart(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends ProductMiniCartResponse>>() {
                    @Override
                    public Observable<? extends ProductMiniCartResponse> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<ProductMiniCartResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));

                    }

                    @Override
                    public void onNext(ProductMiniCartResponse productMiniCartResponse) {
                        callback.onSuccess(productMiniCartResponse);
                    }
                });
    }


    public interface AddCartCallback {
        void onSuccess(SubmitResponse submitResponse);
        void onError(NetworkError networkError);
    }

    public Subscription addCart(String token, String product_option_id, String quantity, final AddCartCallback callback) {
        return networkService.addCart(token, product_option_id, quantity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends SubmitResponse>>() {
                    @Override
                    public Observable<? extends SubmitResponse> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<SubmitResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));

                    }

                    @Override
                    public void onNext(SubmitResponse submitResponse) {
                        callback.onSuccess(submitResponse);
                    }
                });
    }

    public interface UpdateCartCallback {
        void onSuccess(SubmitResponse submitResponse);
        void onError(NetworkError networkError);
    }

    public Subscription updateCart(String token, String product_id, String quantity, final UpdateCartCallback callback) {
        return networkService.updateCart(token, product_id, quantity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends SubmitResponse>>() {
                    @Override
                    public Observable<? extends SubmitResponse> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<SubmitResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));

                    }

                    @Override
                    public void onNext(SubmitResponse submitResponse) {
                        callback.onSuccess(submitResponse);
                    }
                });
    }

    public interface DeleteCartCallback {
        void onSuccess(SubmitResponse submitResponse);
        void onError(NetworkError networkError);
    }

    public Subscription deleteCart(String token, String product_id, final DeleteCartCallback callback) {
        return networkService.deleteCart(token, product_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends SubmitResponse>>() {
                    @Override
                    public Observable<? extends SubmitResponse> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<SubmitResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));

                    }

                    @Override
                    public void onNext(SubmitResponse submitResponse) {
                        callback.onSuccess(submitResponse);
                    }
                });
    }

    public interface ClearCartCallback {
        void onSuccess(SubmitResponse submitResponse);
        void onError(NetworkError networkError);
    }

    public Subscription clearCart(String token, final ClearCartCallback callback) {
        return networkService.clearCart(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends SubmitResponse>>() {
                    @Override
                    public Observable<? extends SubmitResponse> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<SubmitResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));

                    }

                    @Override
                    public void onNext(SubmitResponse submitResponse) {
                        callback.onSuccess(submitResponse);
                    }
                });
    }

    ////////////////// API CUSTOMER /////////////////

    public interface GetCustomerCallback {
        void onSuccess(CustomerRespone customerRespone);
        void onError(NetworkError networkError);
    }

    public Subscription getCustomer(String token, final GetCustomerCallback callback) {
        return networkService.getCustomer(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends CustomerRespone>>() {
                    @Override
                    public Observable<? extends CustomerRespone> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<CustomerRespone>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));

                    }

                    @Override
                    public void onNext(CustomerRespone customerRespone) {
                        callback.onSuccess(customerRespone);
                    }
                });
    }



    public interface GetCartTotalCallback {
        void onSuccess(TotalResponse totalResponse);
        void onError(NetworkError networkError);
    }

    public Subscription getCartTotal(String token, final GetCartTotalCallback callback) {
        return networkService.getCartTotal(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends TotalResponse>>() {
                    @Override
                    public Observable<? extends TotalResponse> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<TotalResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));

                    }

                    @Override
                    public void onNext(TotalResponse totalResponse) {
                        callback.onSuccess(totalResponse);
                    }
                });
    }

    public interface GetWishTotalCallback {
        void onSuccess(TotalResponse totalResponse);
        void onError(NetworkError networkError);
    }

    public Subscription getWishTotal(String token, final GetWishTotalCallback callback) {
        return networkService.getWishTotal(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends TotalResponse>>() {
                    @Override
                    public Observable<? extends TotalResponse> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<TotalResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));

                    }

                    @Override
                    public void onNext(TotalResponse totalResponse) {
                        callback.onSuccess(totalResponse);
                    }
                });
    }


    public interface CheckWishedCallback {
        void onSuccess(SubmitResponse submitResponse);
        void onError(NetworkError networkError);
    }

    public Subscription checkWished(String token, String product_id,final CheckWishedCallback callback) {
        return networkService.checkWished(token, product_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends SubmitResponse>>() {
                    @Override
                    public Observable<? extends SubmitResponse> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<SubmitResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));

                    }

                    @Override
                    public void onNext(SubmitResponse submitResponse) {
                        callback.onSuccess(submitResponse);
                    }
                });
    }
}
