package com.scrapwala.api

import android.content.Context
import com.scrapwala.BuildConfig
import com.scrapwala.screens.pickups.category.model.CategoryResponse
import com.scrapwala.screens.pickups.model.AddAddressData
import com.scrapwala.screens.pickups.model.AddressListResponse
import com.scrapwala.screens.pickups.model.CityListResponse
import com.scrapwala.screens.pickups.model.SuccessResponse
import com.scrapwala.utils.ApiResult
import com.scrapwala.utils.Constant
import com.scrapwala.utils.network.NetworkResultCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface ApiService {

    @GET("/api/category")
    suspend fun userGetCategory(
    ): ApiResult<CategoryResponse>

    @GET("/api/city")
    suspend fun getCityList():ApiResult<CityListResponse>

    @GET("/api/address/user/{id}")
    suspend fun getAddressList(@Path("id") id: Int):ApiResult<AddressListResponse>

    @GET("/api/address/delete/{id}")
    suspend fun deleteAddress(@Path("id") id: Int):ApiResult<SuccessResponse>


    @POST("/api/address/create")
    suspend fun saveAddress(@Body request:AddAddressData):ApiResult<SuccessResponse>



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

                        builder.header("appVersion", BuildConfig.VERSION_NAME)

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