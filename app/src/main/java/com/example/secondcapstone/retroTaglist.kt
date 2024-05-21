package com.example.secondcapstone

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface retroTaglist {
    @POST("/plan") //endpoint
    fun sendPlan(@Body plan: planDto): Call<retroTaglistResponse>
}