package com.scrapwala.utils.network

import android.content.Context
import com.scrapwala.utils.ApiResult
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type


class NetworkResultCallAdapter(
    private val resultType: Type,
    val mcontext: Context,
    ) : CallAdapter<Type, Call<ApiResult<Type>>> {



    override fun responseType(): Type = resultType

    override fun adapt(call: Call<Type>): NetworkResultCall<Type> {
        return NetworkResultCall(mcontext,call)
    }
}