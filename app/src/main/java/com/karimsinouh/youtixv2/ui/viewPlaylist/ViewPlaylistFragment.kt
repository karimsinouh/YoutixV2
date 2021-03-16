package com.karimsinouh.youtixv2.ui.viewPlaylist

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import com.karimsinouh.youtixv2.R
import com.karimsinouh.youtixv2.adapters.PlaylistVideosAdapter
import com.karimsinouh.youtixv2.data.items.VideoItem
import com.karimsinouh.youtixv2.databinding.FragmentViewPlaylistBinding
import com.karimsinouh.youtixv2.utils.PLAYLIST_ID
import com.karimsinouh.youtixv2.utils.PLAYLIST_NAME
import com.karimsinouh.youtixv2.utils.ViewsFormatter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ViewPlaylistFragment:Fragment(R.layout.fragment_view_playlist) {

    private lateinit var binding:FragmentViewPlaylistBinding
    private lateinit var playlistId:String
    private lateinit var playlistName:String

    @Inject lateinit var glide:RequestManager
    @Inject lateinit var adapter:PlaylistVideosAdapter

    private val vm by viewModels<ViewPlaylistViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentViewPlaylistBinding.bind(view)

        playlistId=arguments?.getString(PLAYLIST_ID)!!
        playlistName=arguments?.getString(PLAYLIST_NAME)!!

        binding.playlistTitle.text=playlistName

        setupRcv()

        adapter.setOnClickListener {
            vm.loadVideo(it.snippet.resourceId?.videoId!!)
            bind(it)
            binding.root.fullScroll(View.FOCUS_UP)
        }

        vm.loadVideos(playlistId)

        subscribeToObservers()
    }

    private fun subscribeToObservers(){
        vm.video.observe(viewLifecycleOwner){
            bind(it,true)
            if (!binding.thumbnailContainer.isShown){
                binding.thumbnailContainer.visibility=View.VISIBLE
                binding.videoInfo.visibility=View.VISIBLE
                binding.playlistTitle.visibility=View.GONE
            }
        }

        vm.videos.observe(viewLifecycleOwner){
            adapter.submitList(it)
            binding.bar.visibility=View.GONE
            binding.rcv.visibility=View.VISIBLE
        }

        vm.error.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(),it,Toast.LENGTH_SHORT).show()
        }
    }

    private fun bind(video:VideoItem,withStatistics:Boolean?=false)=binding.apply{
        videoTitle.text=video.snippet.title
        glide.load(video.snippet.thumbnails.medium.url).into(thumbnail)

        if (withStatistics!!){
            likes.text=ViewsFormatter.format(video.statistics?.likeCount!!)
            views.text=ViewsFormatter.format(video.statistics.viewCount)
            dislikes.text=ViewsFormatter.format(video.statistics.dislikeCount)
        }
    }

    private fun setupRcv()=binding.rcv.apply {
        layoutManager=LinearLayoutManager(requireContext())
        setHasFixedSize(true)
        adapter=this@ViewPlaylistFragment.adapter
    }

}