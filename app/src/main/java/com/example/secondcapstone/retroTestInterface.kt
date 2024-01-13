package com.example.secondcapstone

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface retroTestInterface {
    @POST("/users") //엔드포인트
    fun sendDataToServer(@Body data: retroTestRequest): Call<retroTestResponse>
}