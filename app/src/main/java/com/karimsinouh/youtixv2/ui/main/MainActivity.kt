package com.karimsinouh.youtixv2.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.JsonObject
import com.karimsinouh.youtixv2.R
import com.karimsinouh.youtixv2.adapters.VideosAdapter
import com.karimsinouh.youtixv2.api.RetrofitAPI
import com.karimsinouh.youtixv2.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    
    private lateinit var binding:ActivityMainBinding
    private lateinit var vm:MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vm=ViewModelProvider(this).get(MainViewModel::class.java)

        binding.bottomNav.setupWithNavController( findNavController(R.id.navHost) )

    }



}