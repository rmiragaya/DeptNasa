package com.rmiragaya.deptnasachallenge.repo

import com.rmiragaya.deptnasachallenge.api.RetrofitInstance

object NasaRepo {
    suspend fun getDateList() =
        RetrofitInstance.nasaApi.getAvailableDates()

    suspend fun getPhotosOfTheDate(date : String) =
        RetrofitInstance.nasaApi.getPhotosOfDate(date)
}