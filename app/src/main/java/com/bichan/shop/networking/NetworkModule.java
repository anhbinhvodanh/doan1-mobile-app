package com.bichan.shop.networking;

import com.bichan.shop.BuildConfig;
import com.bichan.shop.models.CategoryResponse;
import com.bichan.shop.models.HomeCategoryResponse;
import com.bichan.shop.models.HomeSliderResponse;
import com.bichan.shop.models.ProductMiniResponse;
import com.bichan.shop.models.ProductOptionResponse;
import com.bichan.shop.models.ProductResponse;
import com.bichan.shop.models.ReviewAddResponse;
import com.bichan.shop.models.ReviewResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by cuong on 5/16/2017.
 */

@Module
public class NetworkModule {
    File cacheFile;

    public NetworkModule(File cacheFile) {
        this.cacheFile = cacheFile;
    }

    @Provides
    @Singleton
    Retrofit provideCall() {
        Cache cache = null;
        try {
            cache = new Cache(cacheFile, 10 * 1024 * 1024);
        } catch (Exception e) {
            e.printStackTrace();
        }

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();

                        // Customize the request
                        Request request = original.newBuilder()
                                .header("Content-Type", "application/json")
                                .removeHeader("Pragma")
                                .header("Cache-Control", String.format("max-age=%d", BuildConfig.CACHETIME))
                                .build();

                        okhttp3.Response response = chain.proceed(request);
                        response.cacheResponse();
                        // Customize or return the response
                        return response;
                    }
                })
                .cache(cache)

                .build();

        return new Retrofit.Builder()
                .baseUrl(BuildConfig.BASEURL)
                .client(okHttpClient)
                .addConverterFactory(createGsonConverter())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    private Converter.Factory createGsonConverter() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(HomeCategoryResponse.class, new JsonResponseDeserializer());
        gsonBuilder.registerTypeAdapter(CategoryResponse.class, new JsonResponseDeserializer());
        gsonBuilder.registerTypeAdapter(HomeSliderResponse.class, new JsonResponseDeserializer());
        gsonBuilder.registerTypeAdapter(ProductMiniResponse.class, new JsonResponseDeserializer());
        gsonBuilder.registerTypeAdapter(ProductResponse.class, new JsonResponseDeserializer());
        gsonBuilder.registerTypeAdapter(ProductOptionResponse.class, new JsonResponseDeserializer());
        gsonBuilder.registerTypeAdapter(ReviewResponse.class, new JsonResponseDeserializer());
        gsonBuilder.registerTypeAdapter(ReviewAddResponse.class, new JsonResponseDeserializer());
        Gson gson = gsonBuilder.create();
        return GsonConverterFactory.create(gson);
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    public NetworkService providesNetworkService(
            Retrofit retrofit) {
        return retrofit.create(NetworkService.class);
    }
    @Provides
    @Singleton
    @SuppressWarnings("unused")
    public Service providesService(
            NetworkService networkService) {
        return new Service(networkService);
    }

}