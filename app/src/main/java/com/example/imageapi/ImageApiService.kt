package com.example.imageapi

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

// http://glyffix.com/api/Image?word=dog

interface ImageApiService {

    @GET("api/Image")
    fun getImages(@Query("word") word : String) : Call<SearchResponse>

}