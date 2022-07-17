package com.rmiragaya.deptnasachallenge.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.rmiragaya.deptnasachallenge.databinding.ActivityMainBinding
import com.rmiragaya.deptnasachallenge.repo.NasaRepo
import com.rmiragaya.deptnasachallenge.ui.viewmodel.MainViewmodel
import com.rmiragaya.deptnasachallenge.ui.viewmodel.MainViewmodelFactory
import com.rmiragaya.deptnasachallenge.utils.Resource

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var viewmodel: MainViewmodel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewmodel = ViewModelProvider(this,
            MainViewmodelFactory(NasaRepo)
        )[MainViewmodel::class.java]

        installSplashScreen().apply {
            setKeepVisibleCondition{
                viewmodel.dateListResponse.value?.data == null
            }
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}