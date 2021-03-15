package com.karimsinouh.youtixv2.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.karimsinouh.youtixv2.R
import com.karimsinouh.youtixv2.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    
    private lateinit var binding:ActivityMainBinding
    private lateinit var vm:MainViewModel
    private lateinit var builder:MaterialAlertDialogBuilder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNav.setupWithNavController( findNavController(R.id.navHost) )

        vm=ViewModelProvider(this).get(MainViewModel::class.java)

        lifecycleScope.launch {
            if(vm.videos.value?.isEmpty()!!)
                vm.loadVideos()
        }

        lifecycleScope.launch {
            if(vm.playlists.value?.isEmpty()!!)
                vm.loadPlaylists()
        }


        builder=MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.alert))
                .setPositiveButton(getString(R.string.ok),null)

        subscribeToObservers()

    }

    private fun subscribeToObservers(){
        vm.error.observe(this){
            builder.setMessage(it).create().show()
        }
    }



}