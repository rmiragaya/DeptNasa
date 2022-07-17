package com.rmiragaya.deptnasachallenge.api

import com.rmiragaya.deptnasachallenge.models.DatePhotosItem
import com.rmiragaya.deptnasachallenge.models.DateResponseItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface NasaApi {

    @GET("api/enhanced/all")
    suspend fun getAvailableDates(
    ): Response<MutableList<DateResponseItem>>

    @GET("api/enhanced/date/{date}")
    suspend fun getPhotosOfDate(
        @Path("date") date: String
    ): Response<ArrayList<DatePhotosItem>>
}