package com.rmiragaya.deptnasachallenge.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rmiragaya.deptnasachallenge.repo.NasaRepo

class MainViewmodelFactory(
    private val repo: NasaRepo
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewmodel(repo) as T
    }
}