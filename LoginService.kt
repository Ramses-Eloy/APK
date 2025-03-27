package com.dbsnetwork.iptv

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("login") // Replace with your actual login endpoint
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>
}

