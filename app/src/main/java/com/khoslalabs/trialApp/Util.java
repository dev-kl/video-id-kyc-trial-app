package com.khoslalabs.trialApp;


import com.khoslalabs.vikycapi.api.ApiException;
import com.khoslalabs.vikycapi.api.ApiResponse;
import com.khoslalabs.vikycapi.api.ApiService;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Amanshu Raikwar
 */
public class Util {

    static ApiService apiService() {

        // set logging level to none if build type is not debug
        HttpLoggingInterceptor.Level httpLoggingLevel =
                BuildConfig.DEBUG
                        ? HttpLoggingInterceptor.Level.BODY
                        : HttpLoggingInterceptor.Level.NONE;

        OkHttpClient.Builder okHttpClientBuilder =
                new OkHttpClient
                        .Builder()
                        .addInterceptor(new HttpLoggingInterceptor().setLevel(httpLoggingLevel))
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .writeTimeout(60, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS);

        // adding environment header param if build flavor is dev
        // noinspection ConstantConditions
        if (BuildConfig.FLAVOR.equalsIgnoreCase("dev")) {
            okHttpClientBuilder.addInterceptor(
                    new Interceptor() {
                        @Override
                        public Response intercept(@NonNull Chain chain) throws IOException {
                            return chain.proceed(
                                    chain
                                            .request()
                                            .newBuilder()
                                            .addHeader(
                                                    "Environment",
                                                    "PREPROD"
                                            )
                                            .build()
                            );
                        }
                    }
            );
        }

        Retrofit retrofit =
                new Retrofit.Builder()
                        .baseUrl("https://sandbox.veri5digital.com/video-id-kyc/api/1.0/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .client(okHttpClientBuilder.build())
                        .build();

        return retrofit.create(ApiService.class);
    }

    static <T> Observable<ApiResponse<T>> apiResponseInterceptor(final ApiResponse<T> apiResponse) {

        return Observable.fromCallable(new Callable<ApiResponse<T>>() {
            @Override
            public ApiResponse<T> call() throws ApiException {
                if (apiResponse.getResponseStatus().getStatus() ==
                        ApiResponse.ResponseStatus.Status.FAIL) {
                    throw new ApiException(
                            apiResponse.getResponseStatus().getCode(),
                            apiResponse.getResponseStatus().getMessage()
                    );
                }
                return apiResponse;
            }
        });
    }
}
