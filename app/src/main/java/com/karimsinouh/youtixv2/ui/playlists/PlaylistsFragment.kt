package com.karimsinouh.youtixv2.ui.playlists

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.karimsinouh.youtixv2.R
import com.karimsinouh.youtixv2.adapters.PlaylistsAdapter
import com.karimsinouh.youtixv2.data.items.PlaylistItem
import com.karimsinouh.youtixv2.databinding.FragmentPlaylistsBinding
import com.karimsinouh.youtixv2.ui.main.MainViewModel
import com.karimsinouh.youtixv2.utils.PLAYLIST_ID
import com.karimsinouh.youtixv2.utils.PLAYLIST_NAME
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PlaylistsFragment:Fragment(R.layout.fragment_playlists) {

    private lateinit var binding:FragmentPlaylistsBinding
    private lateinit var nav:NavController
    private val vm by activityViewModels<MainViewModel>()
    private var isLoading=true

    private lateinit var lManager:LinearLayoutManager
    @Inject lateinit var adapter:PlaylistsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentPlaylistsBinding.bind(view)
        nav=findNavController()

        setupRcv()

        subscribeToObservers()

        adapter.setOnClickListener {
            navigateToViewPlaylist(it)
        }

    }

    private fun navigateToViewPlaylist(playlist:PlaylistItem){
        nav.navigate(R.id.playlists_to_viewPlaylist, bundleOf(PLAYLIST_ID to playlist.id, PLAYLIST_NAME to playlist.snippet.title))
    }

    private fun setupRcv()=binding.rcv.apply{
        lManager= LinearLayoutManager(requireContext())
        layoutManager=lManager
        setHasFixedSize(true)
        adapter=this@PlaylistsFragment.adapter

        addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val canLoad=vm.playlists.value?.isNotEmpty()!! && vm.playlistsNextPageToken!="" || vm.playlists.value?.isEmpty()!! && vm.playlistsNextPageToken==""
                if (!canScrollVertically(1) && !isLoading && canLoad)
                    loadMore()
            }
        })

    }

    private fun loadMore(){
        lifecycleScope.launch {
            vm.loadPlaylists()
        }
        binding.loadMoreBar.visibility=View.VISIBLE
    }

    private fun subscribeToObservers(){

        vm.playlists.observe(viewLifecycleOwner){
            if(it.isNotEmpty()){
                binding.bar.visibility=View.GONE
                adapter.submitList(it)
            }
            isLoading=false
            binding.loadMoreBar.visibility=View.GONE
        }

    }


}