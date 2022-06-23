package com.example.finalexam.api

import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface API {

    companion object {
        const val API_KEY = "563492ad6f91700001000001bc501978586d43c49ded2bfe9eb69261"

        operator fun invoke(): API = Retrofit.Builder()
            .baseUrl("https://api.pexels.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        val original: Request = chain.request()

                        val requestBuilder: Request.Builder = original.newBuilder()
                        requestBuilder.addHeader("Authorization", API_KEY)

                        val request: Request = requestBuilder.build()
                        chain.proceed(request)
                    }.build()
            )
            .build()
            .create(API::class.java)
    }

    @GET("curated?page=1&per_page=100")
    fun getPhotos(): Call<Photos>
}