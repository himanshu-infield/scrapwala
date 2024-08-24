package com.scrapwala.api

import android.content.Context
import com.scrapwala.utils.Constant
import com.scrapwala.utils.network.NetworkResultCallAdapterFactory
import com.sq.yrd.squareyards.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

interface ApiService {
//
//    @POST("/auth/otp/send")
//    suspend fun getOtp(@HeaderMap headers: Map<String, String>,
//        @Body request:OtpRequest):ApiResult<GetOtpResponse>





//    @POST("/auth/otp/verify")
//    suspend fun verifyOtp(@HeaderMap headers: Map<String, String>,
//                       @Body request:VerifyOtpRequest):ApiResult<VerifyOtpResponse>
//
//    @GET("/master/user/countrycodes")
//    suspend fun getCountryCode():ApiResult<CountryCodeResponse>








    companion object{
        private var BASE_URL = Constant.BASE_URL
        fun create(context: Context): ApiService {
            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

            val okHttpClient = OkHttpClient.Builder().connectTimeout(50, TimeUnit.SECONDS).writeTimeout(50, TimeUnit.SECONDS)
            .readTimeout(50, TimeUnit.SECONDS).apply {
                addInterceptor(
                    Interceptor { chain ->
                        val builder = chain.request().newBuilder()
                        builder.header("appSource","android")

                        builder.header("appVersion",BuildConfig.VERSION_NAME)

                        builder.header("appVersionCode",BuildConfig.VERSION_CODE.toString())

                        return@Interceptor chain.proceed(builder.build())
                    }
                )
                addInterceptor(logger)
            }.build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(NetworkResultCallAdapterFactory.create(context))
                .build()
                .create(ApiService::class.java)
        }

    }
}