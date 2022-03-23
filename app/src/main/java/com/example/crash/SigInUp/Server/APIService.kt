package com.example.crash.SigInUp.Server

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface APIService {
    @POST(".")
    suspend fun createEmployee(@Body requestBody: RequestBody): Response<ResponseBody>

}