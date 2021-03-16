package com.karimsinouh.youtixv2.ui.videoPlayer

import android.os.Bundle
import android.view.WindowManager
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.karimsinouh.youtixv2.databinding.ActivityPlayerBinding
import com.karimsinouh.youtixv2.utils.API_KEY
import com.karimsinouh.youtixv2.utils.VIDEO_ID

class PlayerActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {


    private lateinit var binding:ActivityPlayerBinding
    private lateinit var videoId:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        videoId=intent.getStringExtra(VIDEO_ID)!!

        binding.player.initialize(API_KEY,this)

    }

    override fun onInitializationSuccess(p0: YouTubePlayer.Provider?, player: YouTubePlayer?, p2: Boolean) {
        player?.loadVideo(videoId)
    }

    override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {

    }


}