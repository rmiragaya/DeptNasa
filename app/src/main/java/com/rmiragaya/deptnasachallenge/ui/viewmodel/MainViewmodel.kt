package com.rmiragaya.deptnasachallenge.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rmiragaya.deptnasachallenge.models.DatePhotosItem
import com.rmiragaya.deptnasachallenge.models.DateResponseItem
import com.rmiragaya.deptnasachallenge.models.DownloadState
import com.rmiragaya.deptnasachallenge.repo.NasaRepo
import com.rmiragaya.deptnasachallenge.utils.Resource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewmodel(
    private val repo: NasaRepo
) : ViewModel() {

    private val _dateListResponse = MutableLiveData<Resource<MutableList<DateResponseItem>>>()
    val dateListResponse: LiveData<Resource<MutableList<DateResponseItem>>>
        get() = _dateListResponse

    private val getListExceptionHandler = CoroutineExceptionHandler { _, w ->
        _dateListResponse.postValue(Resource.Error(w.message))
    }

    private val getPhotosExceptionHandler = CoroutineExceptionHandler { _, w ->
        _dateLoading.postValue( dateLoading.value.apply { this?.downloadState = DownloadState.ERROR }
        )
    }

    private val _dateLoading = MutableLiveData<DateResponseItem>()
    val dateLoading: LiveData<DateResponseItem>
        get() = _dateLoading

    init {
        if (_dateListResponse.value?.data == null) getDateList()
    }

    private fun getDateList() = viewModelScope.launch(Dispatchers.IO + getListExceptionHandler) {
        _dateListResponse.postValue(Resource.Loading())
        val response = repo.getDateList()
        _dateListResponse.postValue(handleListReponse(response))
    }

    private fun handleListReponse(response: Response<MutableList<DateResponseItem>>): Resource<MutableList<DateResponseItem>> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if (resultResponse.isNotEmpty()) {
                    return Resource.Success(resultResponse)
                } else {
                    return Resource.Error(response.message())
                }
            }
        }
        return Resource.Error(response.message())
    }

    fun getDatePhotos(date: String?){
        viewModelScope.launch(Dispatchers.IO + getPhotosExceptionHandler) {

            if (date == null) return@launch

                _dateLoading.postValue(
                    DateResponseItem(
                        date = date,
                        downloadState = DownloadState.LOADING
                    )
                )
                val response = repo.getPhotosOfTheDate(date)
                _dateLoading.postValue(handlePhotoListResponse(date, response))
        }
    }

    private fun handlePhotoListResponse(
        date: String,
        response: Response<ArrayList<DatePhotosItem>>
    ): DateResponseItem {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if (resultResponse.isNotEmpty()) {
                    return DateResponseItem(date, response.body(), DownloadState.SUCCES)
                } else {
                    return DateResponseItem(date, downloadState = DownloadState.ERROR)
                }
            }
        }
        return DateResponseItem(date, downloadState = DownloadState.ERROR)
    }
}