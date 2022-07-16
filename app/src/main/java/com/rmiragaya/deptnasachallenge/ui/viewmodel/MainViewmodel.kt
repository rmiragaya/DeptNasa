package com.rmiragaya.deptnasachallenge.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rmiragaya.deptnasachallenge.models.DateResponse
import com.rmiragaya.deptnasachallenge.models.DateResponseItem
import com.rmiragaya.deptnasachallenge.repo.NasaRepo
import com.rmiragaya.deptnasachallenge.utils.Resource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewmodel(
    val repo : NasaRepo
    ) : ViewModel() {

    private val _dateListResponse = MutableLiveData<Resource<ArrayList<DateResponseItem?>>>()
    val dateListResponse: LiveData<Resource<ArrayList<DateResponseItem?>>>
        get() = _dateListResponse

    private val coroutineExceptionHandler = CoroutineExceptionHandler { r, w ->
        _dateListResponse.postValue(Resource.Error(w.message))
    }

    init {
        getDateList()
    }

    private fun getDateList() = viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {

        _dateListResponse.postValue(Resource.Loading())
        val response = repo.getDateList()
        _dateListResponse.postValue(handleListReponse(response))
    }

    private fun handleListReponse(response: Response<ArrayList<DateResponseItem?>>): Resource<ArrayList<DateResponseItem?>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if (resultResponse.isNotEmpty()){
                    return Resource.Success(resultResponse)
                } else {
                    return Resource.Error(response.message())
                }
            }
        }
        return Resource.Error(response.message())
    }

    fun getDatePhotos() = viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
        
    }
















}