package com.example.elena.shopeasy.dagger.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Absolute on 1/10/2018.
 */

@Module
public class NetModule {

    public static final String BASE_URL =
            "https://d17h27t6h515a5.cloudfront.net/";//may try with local json in assets

    @Provides
    @Singleton
    Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
