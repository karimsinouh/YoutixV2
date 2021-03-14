package com.karimsinouh.youtixv2.ui.videos

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.karimsinouh.youtixv2.R
import com.karimsinouh.youtixv2.databinding.FragmentVideosBinding

class VideosFragment: Fragment(R.layout.fragment_videos) {

    private lateinit var binding: FragmentVideosBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentVideosBinding.bind(view)


    }

}