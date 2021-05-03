package com.karimsinouh.youtixv2.ui.videos

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.karimsinouh.youtixv2.R
import com.karimsinouh.youtixv2.adapters.PagerAdapter
import com.karimsinouh.youtixv2.adapters.VideosAdapter
import com.karimsinouh.youtixv2.data.entities.WatchLater
import com.karimsinouh.youtixv2.database.Database
import com.karimsinouh.youtixv2.databinding.FragmentVideosBinding
import com.karimsinouh.youtixv2.ui.main.MainViewModel
import com.karimsinouh.youtixv2.utils.VIDEO_ID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class VideosFragment: Fragment(R.layout.fragment_videos) {

    private val vm by activityViewModels<MainViewModel>()
    private lateinit var binding: FragmentVideosBinding
    private lateinit var nav:NavController
    private lateinit var layoutManager:GridLayoutManager

    private var isLoading=true

    @Inject lateinit var pagerAdapter: PagerAdapter
    @Inject lateinit var adapter:VideosAdapter
    @Inject lateinit var db:Database

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding= FragmentVideosBinding.bind(view)
        nav=findNavController()
        layoutManager= GridLayoutManager(requireContext(),3)

        setupRcv()

        setupPager()

        subscribeToObservers()

        adapter.setOnClickListener {
            navigateToViewVideo(it.snippet.resourceId?.videoId!!)
        }

        pagerAdapter.onPlayClicked {
            navigateToViewVideo(it)
        }

        pagerAdapter.onShareClicked {
            share(it)
        }

        pagerAdapter.onAddToListChanges { id, isChecked ->
            lifecycleScope.launch {
                if (isChecked){
                    val item=WatchLater(id)
                    db.watchLater().add(item)
                }else{
                    db.watchLater().delete(id)
                }
            }
        }

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


    private fun navigateToViewVideo(id:String){
        nav.navigate(R.id.videos_to_videoInfo, bundleOf(VIDEO_ID to id))
    }

    private fun subscribeToObservers(){

        vm.videos.observe(viewLifecycleOwner){
            if(it.isNotEmpty()){
                binding.bar.alpha=0f
                adapter.submitItems(it)
                pagerAdapter.submitList(listOf(it[0],it[1],it[it.size-1]))
            }
            isLoading=false
        }

    }

    private fun setupRcv()=binding.rcv.apply{
        setHasFixedSize(true)
        layoutManager=this@VideosFragment.layoutManager
        adapter=this@VideosFragment.adapter

        addOnScrollListener(object:RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if(!canScrollVertically(1) && !isLoading)
                    loadMore()

            }
        })
    }

    private fun setupPager()=binding.pager.apply {
        binding.pager.adapter=pagerAdapter
    }

    private fun loadMore()=lifecycleScope.launch {
        vm.loadVideos()
    }


}