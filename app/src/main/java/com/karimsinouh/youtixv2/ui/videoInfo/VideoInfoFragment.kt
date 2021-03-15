package com.karimsinouh.youtixv2.ui.videoInfo

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.RequestManager
import com.karimsinouh.youtixv2.R
import com.karimsinouh.youtixv2.data.items.VideoItem
import com.karimsinouh.youtixv2.databinding.FragmentVideoInfoBinding
import com.karimsinouh.youtixv2.utils.VIDEO_ID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class VideoInfoFragment: Fragment(R.layout.fragment_video_info) {

    private lateinit var binding:FragmentVideoInfoBinding
    private lateinit var videoId:String
    private val vm by viewModels<VideoInfoViewModel>()

    @Inject lateinit var glide:RequestManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentVideoInfoBinding.bind(view)
        videoId=arguments?.getString(VIDEO_ID)!!

        lifecycleScope.launch {
            vm.loadVideo(videoId)
        }

        subscribeToObservers()

    }


    private fun subscribeToObservers(){
        vm.video.observe(viewLifecycleOwner){
            bindVideo(it)
            showViews()
        }

        vm.error.observe(viewLifecycleOwner){

        }
    }

    private fun bindVideo(video:VideoItem)=binding.apply{
        glide.load(video.snippet.thumbnails.medium.url).into(thumbnail)

        title.text=video.snippet.title
        description.text=video.snippet.description
    }

    private fun showViews()=binding.apply{
        bar.alpha=0f
        nestedScrollView.alpha=1f
    }

    private fun hideViews()=binding.apply{
        bar.alpha=1f
        nestedScrollView.alpha=0f
    }

}