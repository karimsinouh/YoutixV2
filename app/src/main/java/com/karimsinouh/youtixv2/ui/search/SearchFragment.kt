package com.karimsinouh.youtixv2.ui.search

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.karimsinouh.youtixv2.R
import com.karimsinouh.youtixv2.adapters.SearchItemsAdapter
import com.karimsinouh.youtixv2.databinding.FragmentSearchBinding
import com.karimsinouh.youtixv2.utils.KIND_VIDEO
import com.karimsinouh.youtixv2.utils.PLAYLIST_ID
import com.karimsinouh.youtixv2.utils.PLAYLIST_NAME
import com.karimsinouh.youtixv2.utils.VIDEO_ID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment:Fragment(R.layout.fragment_search) {

    private lateinit var binding:FragmentSearchBinding
    private lateinit var builder:MaterialAlertDialogBuilder
    private lateinit var nav:NavController

    @Inject lateinit var adapter:SearchItemsAdapter
    private lateinit var lManager:LinearLayoutManager
    private val vm by viewModels<SearchViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentSearchBinding.bind(view)
        nav=findNavController()

        setupRcv()

        builder=MaterialAlertDialogBuilder(requireContext()).setTitle("Error")

        subscribeToObservers()

        binding.input.setEndIconOnClickListener {
            val q=binding.input.editText?.text.toString()
            if (q.isNotEmpty())
                search(q)
        }

        adapter.setOnClickListener {id,name->
            if (id.kind== KIND_VIDEO)
                navigateToVideoInfo(id.videoId!!)
            else
                navigateToViewPlaylist(id.playlistId!!,name)
        }
    }

    private fun navigateToVideoInfo(id:String){
        nav.navigate(R.id.search_to_videoInfo, bundleOf(VIDEO_ID to id))
    }

    private fun navigateToViewPlaylist(id:String,playlistName:String){
        nav.navigate(R.id.search_to_viewPlaylist, bundleOf(PLAYLIST_ID to id, PLAYLIST_NAME to playlistName))
    }

    private fun setupRcv()=binding.rcv.apply{
        lManager= LinearLayoutManager(requireContext())
        layoutManager=lManager
        setHasFixedSize(true)
        adapter=this@SearchFragment.adapter
    }

    private fun subscribeToObservers(){

        vm.result.observe(viewLifecycleOwner){
            if (it.isEmpty()){
                Toast.makeText(requireContext(),"No results",Toast.LENGTH_SHORT).show()
                binding.bar.visibility=View.GONE
            }else{
                adapter.submitList(it)
                binding.bar.visibility=View.GONE
                binding.rcv.visibility=View.VISIBLE
            }
        }

        vm.error.observe(viewLifecycleOwner){
            builder.setMessage(it).create().show()
        }

    }

    private fun search(q: String) {
        binding.bar.visibility=View.VISIBLE
        binding.rcv.visibility=View.GONE

        lifecycleScope.launch {
            vm.search(q)
        }
    }

}