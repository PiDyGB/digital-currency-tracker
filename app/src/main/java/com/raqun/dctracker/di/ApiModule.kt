package com.raqun.dctracker.di

import com.raqun.dctracker.BuildConfig
import com.raqun.dctracker.api.ApiConstants
import com.raqun.dctracker.api.DefaultRequestInterceptor
import com.raqun.dctracker.api.TrackerServices
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by tyln on 12/09/2017.
 */
@Module
internal class ApiModule {

    @Provides @Singleton @Named("url") fun provideBaseUrl(): String = "www.example.com"

    @Provides @Singleton fun provideRequestInterceptor(): Interceptor
            = DefaultRequestInterceptor()

    @Provides @Singleton fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    }

    @Provides @Singleton fun provideOkHttpClient(requestInterceptor: DefaultRequestInterceptor,
                                                 loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return with(OkHttpClient.Builder()) {
            addInterceptor(requestInterceptor)
            if (BuildConfig.DEBUG) addInterceptor(loggingInterceptor)
            connectTimeout(ApiConstants.TIMEOUT_INMILIS, TimeUnit.MILLISECONDS)
            build()
        }
    }

    @Provides @Singleton fun provideRetrofit(@Named("url") baseUrl: String, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    @Provides @Singleton fun provideApiServices(retrofit: Retrofit): TrackerServices
            = retrofit.create(TrackerServices::class.java)
}