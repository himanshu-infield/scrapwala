package com.scrapwala.utils.network

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.scrapwala.utils.ApiError
import com.scrapwala.utils.ApiResult
import com.scrapwala.utils.ApiSuccess
import com.scrapwala.utils.Constant
import com.scrapwala.utils.ErrorResponse
import com.scrapwala.utils.FailureException

import okhttp3.Request
import okio.Timeout
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class  NetworkResultCall<T : Any> constructor(
    val mcontext: Context,
    private val proxy: Call<T>

) : Call<ApiResult<T>> {


    override fun enqueue(callback: Callback<ApiResult<T>>) {



        proxy.enqueue(object : Callback<T> {
            val emptyError = ErrorResponse(200, "", null)
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val networkResult = try {
                      val body = response.body()
//                    if (response.code() == 401) {
////                        Log.d("APIException", "onResponse: 401 from api")
//                        val app = mcontext.applicationContext as SquareyardsApplication
//
//                        Toast.makeText(mcontext.applicationContext.)
//
////                        if (app.globalDialog == null) {
////
////                            val mHandler = Handler(Looper.getMainLooper())
////                            mHandler.post(Runnable {
////                                val builder = showAlertDialog(
////                                    "401",
////                                    "401 exception from api",
////                                    mcontext,
////                                    object : DialogClicked {
////                                        override fun onClick() {
////
////                                        }
////                                    })
////                                app.globalDialog = builder.show()
////
////
////                            })
////
////
//////
////
////                        }
//                    }

                    if (response.isSuccessful && body != null) {
                        ApiSuccess(body, response.headers())
                    } else {

                            val error = Gson().fromJson(
                                response.errorBody()!!.charStream(),
                                ErrorResponse::class.java
                            )
                            ApiError(error)



                    }
                } catch (e: Throwable) {
                    FailureException(e.message?:"")
                }
                callback.onResponse(this@NetworkResultCall, Response.success(networkResult))
            }

            override fun onFailure(call: Call<T>, t: Throwable) {

                var errorMsg: String?=""
                if (t is SocketTimeoutException) {
                    errorMsg = Constant.CONNECTION_TIMEOUT
                } else if (t is UnknownHostException) {
                    errorMsg = Constant.NO_INTERNET_AVAILABLE
                } else if (t is ConnectException) {
                    errorMsg = Constant.SERVER_NOT_RESPONDING
                } else if (t is JSONException || t is JsonSyntaxException) {
                    errorMsg = Constant.UNEXPECTED_RESULT
                } else {
                    errorMsg = Constant.SOMETHING_WENT_WRONG
                }


                callback.onResponse(this@NetworkResultCall, Response.success( FailureException<T>(errorMsg)))



            }
        })
    }

    override fun execute(): Response<ApiResult<T>> = throw NotImplementedError()
    override fun clone(): Call<ApiResult<T>> = NetworkResultCall(mcontext, proxy.clone())
    override fun request(): Request = proxy.request()
    override fun timeout(): Timeout = proxy.timeout()
    override fun isExecuted(): Boolean = proxy.isExecuted
    override fun isCanceled(): Boolean = proxy.isCanceled
    override fun cancel() {
        proxy.cancel()
    }
}