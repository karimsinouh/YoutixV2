package com.karimsinouh.youtixv2.ui.playlists

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.karimsinouh.youtixv2.R
import com.karimsinouh.youtixv2.adapters.PlaylistsAdapter
import com.karimsinouh.youtixv2.api.Repository
import com.karimsinouh.youtixv2.databinding.FragmentPlaylistsBinding
import com.karimsinouh.youtixv2.ui.main.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PlaylistsFragment:Fragment(R.layout.fragment_playlists) {

    private lateinit var binding:FragmentPlaylistsBinding
    private val vm by activityViewModels<MainViewModel>()

    @Inject lateinit var adapter:PlaylistsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentPlaylistsBinding.bind(view)

        setupRcv()

        subscribeToObservers()

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