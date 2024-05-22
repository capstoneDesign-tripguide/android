package com.example.secondcapstone

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface retroTaglist {
    @POST("/plan") //endpoint
    fun sendPlan(@Body plan: planDto): Call<retroTaglistResponse>

    @POST("/auth/signup")
    fun sendSignUP(@Body signUp: informationOf_signup): Call<ResponseBody>

    @POST("/auth/login")
    fun sendLogin(@Body login: informationOf_login): Call<ResponseBody>
}