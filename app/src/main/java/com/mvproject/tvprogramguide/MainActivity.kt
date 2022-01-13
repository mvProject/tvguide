package com.mvproject.tvprogramguide

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.mvproject.tvprogramguide.databinding.ActivityMainBinding
import com.mvproject.tvprogramguide.helpers.LocaleHelper
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val navHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
    }
    private val navController get() = navHostFragment.navController

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //navController.addOnDestinationChangedListener { controller, destination, arguments ->
        //    controller.backStack.forEach {
        //        Timber.d(
        //            "currentStack ${it.destination.label}"
        //        )
        //    }
        //}
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.checkAvailableChannelsUpdate()
        mainViewModel.checkFullProgramsUpdate()
        Timber.d("testing MainActivity onResume")
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleHelper.wrapContext(base))
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        LocaleHelper.overrideLocale(this)
    }
}