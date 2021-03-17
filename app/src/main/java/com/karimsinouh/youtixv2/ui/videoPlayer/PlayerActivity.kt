package com.karimsinouh.youtixv2.ui.videoPlayer

import android.os.Bundle
import android.view.WindowManager
import androidx.room.Room
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.karimsinouh.youtixv2.data.entities.HistoryItem
import com.karimsinouh.youtixv2.database.Database
import com.karimsinouh.youtixv2.databinding.ActivityPlayerBinding
import com.karimsinouh.youtixv2.utils.API_KEY
import com.karimsinouh.youtixv2.utils.VIDEO_ID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlayerActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {


    private lateinit var binding:ActivityPlayerBinding
    private lateinit var videoId:String
    private lateinit var db: Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        videoId=intent.getStringExtra(VIDEO_ID)!!

        binding.player.initialize(API_KEY,this)

        db=Room.databaseBuilder(this,Database::class.java,"database").build()

    }

    override fun onInitializationSuccess(p0: YouTubePlayer.Provider?, player: YouTubePlayer?, p2: Boolean) {



        player?.loadVideo(videoId)
        player?.setShowFullscreenButton(false)

        player?.setPlaybackEventListener(object :YouTubePlayer.PlaybackEventListener{
            override fun onPlaying() {
                existsInHistory{exists, historyItem ->
                    if (exists){
                        historyItem?.currentMillis?.let {
                            player.seekToMillis(it)
                        }
                    }else{
                        addToHistory(player.durationMillis)
                    }
                }
            }

            override fun onPaused() {
                updateMillis(player.currentTimeMillis)
            }

            override fun onStopped() {
                updateMillis(player.currentTimeMillis)
            }

            override fun onBuffering(p0: Boolean) {
                updateMillis(player.currentTimeMillis)
            }

            override fun onSeekTo(p0: Int) {

            }

        })

    }

    override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {

    }


    private fun existsInHistory( listener:(exists:Boolean,historyItem:HistoryItem?)->Unit )= CoroutineScope(Dispatchers.IO).launch{
        val exists=db.watchLater().exists(videoId)
        if(exists){
            val item=db.history().get(videoId)
            listener(true,item)
        }else
            listener(false,null)
    }

    private fun addToHistory(duration: Int){
        CoroutineScope(Dispatchers.IO).launch {
            val item=HistoryItem(videoId,duration,0)
            db.history().add(item)
        }
    }


    private fun updateMillis(millis:Int){
        CoroutineScope(Dispatchers.IO).launch {
            db.history().updateMillis(videoId,millis)
        }
    }

}