package com.karimsinouh.youtixv2.ui.videos

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.karimsinouh.youtixv2.R
import com.karimsinouh.youtixv2.adapters.PagerAdapter
import com.karimsinouh.youtixv2.adapters.VideosAdapter
import com.karimsinouh.youtixv2.databinding.FragmentVideosBinding
import com.karimsinouh.youtixv2.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class VideosFragment: Fragment(R.layout.fragment_videos) {

    private val vm by activityViewModels<MainViewModel>()
    private lateinit var binding: FragmentVideosBinding

    @Inject lateinit var pagerAdapter: PagerAdapter
    @Inject lateinit var adapter:VideosAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentVideosBinding.bind(view)

        setupRcv()

        setupPager()

        subscribeToObservers()

        lifecycleScope.launch {
            if (vm.videos.value?.isEmpty()!! )
                vm.loadVideos()
        }

    }


    private fun subscribeToObservers(){

        vm.videos.observe(viewLifecycleOwner){
            if(it.isNotEmpty()){
                binding.bar.alpha=0f
                adapter.submitItems(it)
                pagerAdapter.submitList(listOf(it[1],it[2],it[3]))
            }
        }

    }

    private fun setupRcv()=binding.rcv.apply{
        setHasFixedSize(true)
        layoutManager=GridLayoutManager(requireContext(),3)
        adapter=this@VideosFragment.adapter
    }

    private fun setupPager()=binding.pager.apply {
        binding.pager.adapter=pagerAdapter
    }

}