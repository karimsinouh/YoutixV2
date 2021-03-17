package com.karimsinouh.youtixv2.ui.menu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.karimsinouh.youtixv2.R
import com.karimsinouh.youtixv2.databinding.FragmentMenuBinding

class MenuFragment:Fragment(R.layout.fragment_menu) {

    private lateinit var binding:FragmentMenuBinding
    private lateinit var nav:NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentMenuBinding.bind(view)
        nav=findNavController()


        binding.history.setOnClickListener {

        }

        binding.watchLater.setOnClickListener {

        }


    }

}