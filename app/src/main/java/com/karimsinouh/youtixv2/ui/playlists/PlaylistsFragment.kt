package com.karimsinouh.youtixv2.ui.playlists

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.karimsinouh.youtixv2.R
import com.karimsinouh.youtixv2.adapters.PlaylistsAdapter
import com.karimsinouh.youtixv2.api.Repository
import com.karimsinouh.youtixv2.data.items.PlaylistItem
import com.karimsinouh.youtixv2.databinding.FragmentPlaylistsBinding
import com.karimsinouh.youtixv2.ui.main.MainViewModel
import com.karimsinouh.youtixv2.utils.PLAYLIST_ITEM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PlaylistsFragment:Fragment(R.layout.fragment_playlists) {

    private lateinit var binding:FragmentPlaylistsBinding
    private lateinit var nav:NavController
    private val vm by activityViewModels<MainViewModel>()

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
        nav.navigate(R.id.playlists_to_viewPlaylist, bundleOf(PLAYLIST_ITEM to playlist))
    }

    private fun setupRcv()=binding.rcv.apply{
        layoutManager=LinearLayoutManager(requireContext())
        setHasFixedSize(true)
        adapter=this@PlaylistsFragment.adapter
    }

    private fun subscribeToObservers(){

        vm.playlists.observe(viewLifecycleOwner){
            if(it.isNotEmpty()){
                binding.bar.visibility=View.GONE
                adapter.submitList(it)
            }
            Log.d("wtf",it.size.toString())
        }

    }


}