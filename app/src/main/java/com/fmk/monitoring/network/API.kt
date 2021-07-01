package com.fmk.monitoring.network

import com.fmk.monitoring.data.ResultGetSearchNews
import com.fmk.monitoring.data.ResultTransferPapago
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

class API {
    interface NaverAPI {
        @GET("v1/search/news.json")
        fun getSearchNews(
            @Query("query") query: String,
            @Query("display") display: Int? = null,
            @Query("start") start: Int? = null
        ): Call<ResultGetSearchNews>

        @FormUrlEncoded
        @POST("v1/papago/n2mt")
        fun transferPapago(
            @Field("source") source: String,
            @Field("target") target: String,
            @Field("text") text: String
        ): Call<ResultTransferPapago>

        companion object {
            private const val BASE_URL_NAVER_API = "https://openapi.naver.com/"
            private const val CLIENT_ID = "rSPLI4drbubkoLUv24Lp"
            private const val CLIENT_SECRET = "NwuNHYFOic"

            fun create(): NaverAPI {
                val httpLoggingInterceptor = HttpLoggingInterceptor()
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

                val headerInterceptor = Interceptor {
                    val request = it.request()
                        .newBuilder()
                        .addHeader("X-Naver-Client-Id", CLIENT_ID)
                        .addHeader("X-Naver-Client-Secret", CLIENT_SECRET)
                        .build()
                    return@Interceptor it.proceed(request)
                }

                val client = OkHttpClient.Builder()
                    .addInterceptor(headerInterceptor)
                    .addInterceptor(httpLoggingInterceptor)
                    .build()

                return Retrofit.Builder()
                    .baseUrl(BASE_URL_NAVER_API)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(NaverAPI::class.java)
            }
        }
    }
}