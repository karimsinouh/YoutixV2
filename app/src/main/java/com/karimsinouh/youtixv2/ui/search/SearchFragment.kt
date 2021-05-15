package com.karimsinouh.youtixv2.ui.search

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.karimsinouh.youtixv2.R
import com.karimsinouh.youtixv2.adapters.SearchHistoryAdapter
import com.karimsinouh.youtixv2.adapters.SearchItemsAdapter
import com.karimsinouh.youtixv2.databinding.FragmentSearchBinding
import com.karimsinouh.youtixv2.utils.KIND_VIDEO
import com.karimsinouh.youtixv2.utils.PLAYLIST_ID
import com.karimsinouh.youtixv2.utils.PLAYLIST_NAME
import com.karimsinouh.youtixv2.utils.VIDEO_ID
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment:Fragment(R.layout.fragment_search) {

    private lateinit var binding:FragmentSearchBinding
    private lateinit var builder:MaterialAlertDialogBuilder
    private lateinit var nav:NavController

    private var isLoading=true

    @Inject lateinit var adapter:SearchItemsAdapter
    @Inject lateinit var historyAdapter:SearchHistoryAdapter

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


        binding.input.editText?.doOnTextChanged { text, _, _, _ ->
            if (!vm.searched){

                binding.rcv.adapter=historyAdapter

                vm.searchInHistory(text.toString()){
                    historyAdapter.submitList(it)
                }

                if (text.isNullOrEmpty()){
                    historyAdapter.submitList(vm.searchHistory.value?: emptyList())
                }
            }
        }

        binding.rcv.adapter=historyAdapter
        historyAdapter.submitList(vm.searchHistory.value?: emptyList())


        historyAdapter.setOnClickListener {
            binding.input.editText?.setText(it.query)
            search(it.query)
        }

        historyAdapter.onRemoveListener {
            vm.removeSearchHistory(it)
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

        addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val canLoadMore=
                        vm.result.value?.isNotEmpty()!!
                                && vm.nextPageToken!="" || vm.result.value?.isEmpty()!!
                                && vm.nextPageToken==""
                if(!canScrollVertically(1) && !isLoading && canLoadMore && vm.result.value?.isNotEmpty()!! )
                    loadMore()
            }
        })
    }

    private fun subscribeToObservers(){

        vm.result.observe(viewLifecycleOwner){
            if (it.isEmpty()){
                if(vm.query.isNotEmpty())
                    Toast.makeText(requireContext(),"No results for: ${vm.query}",Toast.LENGTH_SHORT).show()

                binding.bar.visibility=View.GONE
            }else{
                adapter.submitList(it)
                binding.bar.visibility=View.GONE
                binding.rcv.visibility=View.VISIBLE
            }
            isLoading=false
            binding.loadMoreBar.visibility=View.GONE
        }

        vm.error.observe(viewLifecycleOwner){
            builder.setMessage(it).create().show()
        }

        vm.searchHistory.observe(viewLifecycleOwner){
            if (!vm.searched){
                historyAdapter.submitList(it)
            }
        }

    }

    private fun search(q: String) {
        binding.rcv.adapter=adapter

        binding.bar.visibility=View.VISIBLE
        binding.rcv.visibility=View.GONE

        vm.search(q)
    }

    private fun loadMore(){
        if (vm.nextPageToken.isNotEmpty()){
            vm.search(vm.query,loadMore = true)
            binding.loadMoreBar.visibility=View.VISIBLE
            isLoading=true
        }

    }

}