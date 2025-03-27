package com.dbsnetwork.iptv

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ChannelService {
    @POST("login") // Replace with your actual login endpoint
    suspend fun login(
        @Query("username") username: String,
        @Query("password") password: String
    ): Response<LoginResponse>

    @GET("channels") // Replace with your actual get channels endpoint
    suspend fun getChannels(): Response<List<Channel>>

    @GET("channels/featured")
    suspend fun getFeaturedChannels(): Response<List<Channel>>
}



