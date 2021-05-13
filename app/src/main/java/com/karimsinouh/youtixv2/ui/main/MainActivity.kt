package com.karimsinouh.youtixv2.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.karimsinouh.youtixv2.R
import com.karimsinouh.youtixv2.databinding.ActivityMainBinding
import com.karimsinouh.youtixv2.ui.getStarted.GetStartedActivity
import com.karimsinouh.youtixv2.utils.Connectivity
import com.karimsinouh.youtixv2.utils.FirstTime
import com.karimsinouh.youtixv2.utils.VIDEO_ID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    
    private lateinit var binding:ActivityMainBinding
    private lateinit var vm:MainViewModel
    private lateinit var builder:MaterialAlertDialogBuilder
    private lateinit var nav:NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_YoutixV2)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (FirstTime.isFirstTime(this))
            openGetStarted()

        nav= findNavController(R.id.navHost)

        binding.bottomNav.setupWithNavController(nav)

        nav.addOnDestinationChangedListener { _, destination, _ ->
            binding.bottomNav.visibility=when(destination.id){
                R.id.videos->View.VISIBLE
                R.id.search->View.VISIBLE
                R.id.playlists->View.VISIBLE
                R.id.menu->View.VISIBLE
                else->View.GONE
            }
        }

        vm=ViewModelProvider(this).get(MainViewModel::class.java)

        builder=MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.alert))
                .setPositiveButton(getString(R.string.ok),null)

        load()

        subscribeToObservers()

        Firebase.messaging.subscribeToTopic("videos").addOnCompleteListener {
            if(it.isSuccessful)
                Log.d("topic","Successfully")
            else
                Log.d("topic",it.exception?.message!!)
        }

        if(intent.action=="notification"){
            val videoId=intent.getStringExtra(VIDEO_ID)
            openVideo(videoId!!)
        }

        loadAd()

    }

    private fun loadAd(){
        val request=AdRequest.Builder().build()
        binding.adView.loadAd(request)
    }

    private fun subscribeToObservers(){
        vm.error.observe(this){
            builder.setMessage(it).create().show()
        }
    }


    private fun load(){
        if (Connectivity.hasInternet(this)){

            lifecycleScope.launch {
                if(vm.videos.value?.isEmpty()!!)
                    vm.loadVideos()
            }

            lifecycleScope.launch {
                if(vm.playlists.value?.isEmpty()!!)
                    vm.loadPlaylists()
            }

        }else{

            binding.connectivityError.visibility=View.VISIBLE

        }
    }


    //this functions gets triggered when the user click on a notification
    private fun openVideo(videoId:String){
        val args= bundleOf(VIDEO_ID to videoId)
        nav.navigate(R.id.global_to_videoInfo,args)
    }

    private fun openGetStarted(){
        Intent(this,GetStartedActivity::class.java).let {
            startActivity(it)
            finish()
        }
    }

}