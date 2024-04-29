package com.example.secondcapstone

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface retroTaglist {
    @POST("") //endpoint
    fun sendTagList(@Body tagList: List<String>): Call<retroTaglistResponse>
}