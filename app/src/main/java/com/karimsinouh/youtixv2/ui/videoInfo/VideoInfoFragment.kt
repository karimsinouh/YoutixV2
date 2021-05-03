package com.karimsinouh.youtixv2.ui.videoInfo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.RequestManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.karimsinouh.youtixv2.R
import com.karimsinouh.youtixv2.data.items.VideoItem
import com.karimsinouh.youtixv2.databinding.FragmentVideoInfoBinding
import com.karimsinouh.youtixv2.ui.videoPlayer.PlayerActivity
import com.karimsinouh.youtixv2.utils.VIDEO_ID
import com.karimsinouh.youtixv2.utils.ViewsFormatter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.ocpsoft.prettytime.PrettyTime
import java.text.SimpleDateFormat
import javax.inject.Inject

@AndroidEntryPoint
class VideoInfoFragment: Fragment(R.layout.fragment_video_info) {

    private lateinit var binding:FragmentVideoInfoBinding
    private lateinit var videoId:String
    private val vm by viewModels<VideoInfoViewModel>()

    private val request=AdRequest.Builder().build()
    private var interstitialAd: InterstitialAd?=null

    @Inject lateinit var glide:RequestManager
    @Inject lateinit var prettyTime: PrettyTime

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentVideoInfoBinding.bind(view)
        videoId=arguments?.getString(VIDEO_ID)!!

        InterstitialAd.load(requireContext(),getString(R.string.interstitial),request,object : InterstitialAdLoadCallback(){
            override fun onAdLoaded(p0: InterstitialAd) {
                super.onAdLoaded(p0)
                interstitialAd=p0
            }
        })

        lifecycleScope.launch {
            vm.loadVideo(videoId)
        }

        subscribeToObservers()

    }

    private fun showAd(){
        interstitialAd?.show(requireActivity())
    }

    private fun share(id:String){
        val url="https://youtube.com/watch?v=$id"
        val intent=Intent().also {
            it.action=Intent.ACTION_SEND
            it.type="text/plain"
            it.putExtra(Intent.EXTRA_TEXT,url)
        }
        requireActivity().startActivity(Intent.createChooser(intent,getString(R.string.share_video)))
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

        val dateFormatter= SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val time=dateFormatter.parse(video.snippet.publishedAt)
        date.text=prettyTime.format(time)

        views.text=ViewsFormatter.format(video.statistics?.viewCount ?: 0)
        likes.text=ViewsFormatter.format(video.statistics?.likeCount ?: 0)
        dislikes.text=ViewsFormatter.format(video.statistics?.dislikeCount ?: 0)

        playButton.setOnClickListener {
            showAd()
            navigateToPlayer(videoId)
        }

        vm.exists{
            later.isChecked=it
        }

        vm.existsInHistory{exists, historyItem ->
            if(exists){
                watchBar.visibility=View.VISIBLE
                val progress=historyItem?.currentMillis?.toFloat()!! /historyItem.duration.toFloat()* 100
                watchBar.progress=progress.toInt()
            }else{
                binding.watchBar.visibility=View.GONE
            }
        }

        later.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
                vm.addToWatchLater()
            else
                vm.deleteFromWatchLater()
        }

        share.setOnClickListener {
            share(video.id!!)
        }

    }

    private fun showViews()=binding.apply{
        bar.alpha=0f
        nestedScrollView.alpha=1f
    }

    private fun hideViews()=binding.apply{
        bar.alpha=1f
        nestedScrollView.alpha=0f
    }

    private fun navigateToPlayer(videoId: String) {
        val intent= Intent(requireContext(), PlayerActivity::class.java).also {
            it.putExtra(VIDEO_ID,videoId)
            activity?.startActivity(it)
        }
    }
}