package com.karimsinouh.youtixv2.ui.menu

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.karimsinouh.youtixv2.R
import com.karimsinouh.youtixv2.databinding.FragmentMenuBinding
import com.karimsinouh.youtixv2.utils.*

class MenuFragment:Fragment(R.layout.fragment_menu) {

    private lateinit var binding:FragmentMenuBinding
    private lateinit var nav:NavController

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentMenuBinding.bind(view)
        nav=findNavController()


        binding.history.setOnClickListener {
            navigateToList(ACTION_HISTORY)
        }

        binding.watchLater.setOnClickListener {
            navigateToList(ACTION_WATCH_LATER)
        }

        binding.viewOnYoutube.setOnClickListener {
            val url="https://www.youtube.com/channel/$CHANNEL_ID"
            openBrowser(url)
        }

        binding.feedback.setOnClickListener {
            openBrowser(FEEDBACK_URL)
        }


    }

    private fun openBrowser(url:String){
        val intent=Intent(Intent.ACTION_VIEW, Uri.parse(url))
        requireActivity().startActivity(intent)
    }

    private fun navigateToList(action:String){
        nav.navigate(R.id.menu_to_list, bundleOf(ACTION to action))
    }

}