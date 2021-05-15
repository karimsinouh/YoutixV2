package com.karimsinouh.youtixv2.ui.videoPlayer

import android.app.PictureInPictureParams
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.util.Rational
import android.view.WindowManager
import androidx.room.Room
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import com.karimsinouh.youtixv2.R
import com.karimsinouh.youtixv2.data.entities.HistoryItem
import com.karimsinouh.youtixv2.database.Database
import com.karimsinouh.youtixv2.databinding.ActivityPlayerBinding
import com.karimsinouh.youtixv2.utils.API_KEY
import com.karimsinouh.youtixv2.utils.VIDEO_ID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlayerActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {

    private lateinit var binding:ActivityPlayerBinding
    private lateinit var videoId:String
    private lateinit var db:Database
    private var player:YouTubePlayer?=null

    private var seeked=false
    private var isPictureInPicture=false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_YoutixV2)

        binding= ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        videoId=intent.getStringExtra(VIDEO_ID)!!

        binding.player.initialize(API_KEY,this)

        db=Room.databaseBuilder(this,Database::class.java,"database").fallbackToDestructiveMigration().build()
    }

    override fun onInitializationSuccess(p0: YouTubePlayer.Provider?, player: YouTubePlayer?, p2: Boolean) {

        this.player=player

        player?.loadVideo(videoId)
        player?.setShowFullscreenButton(false)

        player?.setPlaybackEventListener(object :YouTubePlayer.PlaybackEventListener{
            override fun onPlaying() {
                existsInHistory{exists, historyItem ->
                    if (exists){
                        if (!seeked)
                            historyItem?.currentMillis?.let {
                                player.seekToMillis(it)
                                seeked=true
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
        val exists=db.history().exists(videoId)
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
        if(millis>6)
            CoroutineScope(Dispatchers.IO).launch {
                db.history().updateMillis(videoId,millis)
            }
    }


    //picture in picture stuff

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val pipBuilder=PictureInPictureParams.Builder().setAspectRatio(Rational(2,1)).build()
            enterPictureInPictureMode(pipBuilder)
        }
    }

    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean, newConfig: Configuration?) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        isPictureInPicture=isInPictureInPictureMode
    }

    override fun onPause() {
        super.onPause()
        if (isPictureInPicture)
            player?.play()
    }

    override fun onDestroy() {
        super.onDestroy()
        updateMillis(player?.currentTimeMillis!!)
    }

}