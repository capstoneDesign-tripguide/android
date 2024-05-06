package com.example.secondcapstone

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface retroTestInterface {
    //어노테이션과 엔드포인트 정의
    //POST 어노테이션은 서버에 데이터 등록할 때 사용
    @POST("/users")

    //Body 어노테이션은 요청 본문에 데이터를 포함시키기 위해 사용

    fun sendDataToServer(@Body data: LoginRequest): Call<LoginSuccessResponse>
}