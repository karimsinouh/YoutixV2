package com.karimsinouh.youtixv2.ui.viewPlaylist

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.karimsinouh.youtixv2.R
import com.karimsinouh.youtixv2.adapters.PlaylistVideosAdapter
import com.karimsinouh.youtixv2.data.Snippet
import com.karimsinouh.youtixv2.data.items.VideoItem
import com.karimsinouh.youtixv2.databinding.FragmentViewPlaylistBinding
import com.karimsinouh.youtixv2.ui.videoPlayer.PlayerActivity
import com.karimsinouh.youtixv2.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.ocpsoft.prettytime.PrettyTime
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ViewPlaylistFragment:Fragment(R.layout.fragment_view_playlist) {

    private lateinit var binding:FragmentViewPlaylistBinding
    private lateinit var playlistId:String
    private lateinit var dialog:MaterialAlertDialogBuilder
    private lateinit var nav:NavController
    private lateinit var playlistName:String
    private lateinit var lManager:LinearLayoutManager
    private var isLoading=true

    @Inject lateinit var glide:RequestManager
    @Inject lateinit var adapter:PlaylistVideosAdapter
    @Inject lateinit var prettyTime: PrettyTime
    private val vm by viewModels<ViewPlaylistViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentViewPlaylistBinding.bind(view)
        nav=findNavController()

        playlistId=arguments?.getString(PLAYLIST_ID)!!
        playlistName=arguments?.getString(PLAYLIST_NAME)!!

        dialog=MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.alert))
                .setPositiveButton(getString(R.string.ok)){_,_->
                    findNavController().popBackStack()
                }

        binding.playlistTitle.text=playlistName

        setupRcv()

        adapter.setOnClickListener {
            vm.loadVideo(it.snippet.resourceId?.videoId!!)
            bind(it)
            binding.root.fullScroll(View.FOCUS_UP)
        }

        subscribeToObservers()

        loadVideos()

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
            isLoading=false
        }

        vm.error.observe(viewLifecycleOwner){
            dialog.setMessage(it).show()
            binding.bar.visibility=View.GONE
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun bind(video:VideoItem, withStatistics:Boolean?=false)=binding.apply{
        videoTitle.text=video.snippet.title
        glide.load(video.snippet.thumbnails.medium.url).into(thumbnail)

        val dateFormatter=SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val time=dateFormatter.parse(video.snippet.publishedAt)
        date.text=prettyTime.format(time)

        if (withStatistics!!){
            likes.text=ViewsFormatter.format(video.statistics?.likeCount!!)
            views.text=ViewsFormatter.format(video.statistics.viewCount)
            dislikes.text=ViewsFormatter.format(video.statistics.dislikeCount)

            //in watch later
            vm.exists {
                binding.later.isChecked=it
            }

            binding.later.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked)
                    vm.addToWatchLater()
                else
                    vm.deleteFromWatchLater()
            }

            vm.existsInHistory { exists, item ->
                if(exists){
                    binding.watchBar.visibility=View.VISIBLE
                    val progress=item?.currentMillis?.toFloat()!! / item.duration.toFloat() *100
                    binding.watchBar.progress=progress.toInt()
                }else{
                    binding.watchBar.visibility=View.GONE
                }
            }
        }

        playButton.setOnClickListener {
            if(video.snippet.resourceId?.videoId!=null){
                navigateToPlayer(video.snippet.resourceId.videoId)
            }else{
                navigateToPlayer(video.id!!)
            }
        }

        videoTitle.setOnClickListener {
            showDescriptionDialog(video.snippet)
        }

    }

    private fun showDescriptionDialog(snippet:Snippet){
        val args= bundleOf(SNIPPET to snippet)
        nav.navigate(R.id.view_descriptionDialog,args)
    }

    private fun navigateToPlayer(videoId: String) {
        val intent=Intent(requireContext(),PlayerActivity::class.java).also {
            it.putExtra(VIDEO_ID,videoId)
        }
        activity?.startActivity(intent)
    }

    private fun setupRcv()=binding.rcv.apply {
        lManager= LinearLayoutManager(requireContext())
        layoutManager=lManager
        setHasFixedSize(true)
        adapter=this@ViewPlaylistFragment.adapter

        addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val canLoadeMore=vm.videos.value?.isNotEmpty()!! && vm.nextPageToken!="" || vm.videos.value?.isEmpty()!! && vm.nextPageToken==""
                if(!canScrollVertically(1) && !isLoading && canLoadeMore)
                    loadVideos()
            }
        })

    }

    private fun loadVideos(){
        isLoading=true
        binding.bar.visibility=View.VISIBLE
        lifecycleScope.launch {
            vm.loadVideos(playlistId)
        }
    }

}