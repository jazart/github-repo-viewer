package com.aerofs.takehometest;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jazart on 1/9/2018.
 */

public class GitHubServiceGenerator implements GitHubConstants {

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = builder.build();

    public static <S> S createService(Class<S> serviceClass) {
        retrofit = builder.build();
        return retrofit.create(serviceClass);
    }
}
