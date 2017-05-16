package com.bichan.shop.networking;


import com.bichan.shop.models.CategoryResponse;
import com.bichan.shop.models.HomeCategoryResponse;

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

}
