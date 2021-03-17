package com.karimsinouh.youtixv2.ui.list

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.karimsinouh.youtixv2.R
import com.karimsinouh.youtixv2.adapters.PlaylistVideosAdapter
import com.karimsinouh.youtixv2.data.entities.HistoryItem
import com.karimsinouh.youtixv2.data.entities.SearchHistory
import com.karimsinouh.youtixv2.data.entities.WatchLater
import com.karimsinouh.youtixv2.data.items.VideoItem
import com.karimsinouh.youtixv2.databinding.FragmentListBinding
import com.karimsinouh.youtixv2.utils.ACTION
import com.karimsinouh.youtixv2.utils.ACTION_HISTORY
import dagger.hilt.android.AndroidEntryPoint
import java.lang.StringBuilder
import javax.inject.Inject

@AndroidEntryPoint
class ListFragment:Fragment(R.layout.fragment_list) {

    private lateinit var binding:FragmentListBinding
    private lateinit var nav:NavController
    private lateinit var action:String

    @Inject lateinit var adapter:PlaylistVideosAdapter

    private val vm by viewModels<ListViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentListBinding.bind(view)
        nav=findNavController()
        action=arguments?.getString(ACTION)!!

        if (action == ACTION_HISTORY)
            historyAction()
        else
            watchLaterAction()

        setupRcv()

        subscribeToObservers()

        binding.backButton.setOnClickListener {
            nav.popBackStack()
        }


    }

    private fun setupRcv()=binding.rcv.apply{
        layoutManager=LinearLayoutManager(requireContext())
        setHasFixedSize(true)
        adapter=this@ListFragment.adapter
    }

    private fun subscribeToObservers(){

        vm.list.observe(viewLifecycleOwner){

            adapter.submitList(it)

            if (it.isEmpty())
                binding.text.visibility=View.VISIBLE
            else
                binding.text.visibility=View.GONE

            if (binding.bar.isShown)
                binding.bar.visibility=View.GONE
        }


        vm.error.observe(viewLifecycleOwner){
            if(binding.bar.isShown)
                binding.bar.visibility=View.GONE

            binding.text.text=it
            binding.text.visibility=View.VISIBLE
        }
    }

    private fun historyAction(){
        binding.title.text = getString(R.string.history)

        vm.history.observe(viewLifecycleOwner){
            val ids=it.extractHistoryIds()
            vm.loadList(ids)
        }
    }

    private fun watchLaterAction(){
        binding.title.text = getString(R.string.watch_later)

        vm.watchLater.observe(viewLifecycleOwner){
            val ids=it.extractWatchLaterIds()
            vm.loadList(ids)
        }

    }

    private fun List<HistoryItem>.extractHistoryIds():String{
        val ids=StringBuilder()
        for (id in this){
            ids.append("${id.videoId},")
        }
        return ids.toString()
    }

    private fun List<WatchLater>.extractWatchLaterIds():String{
        val ids=StringBuilder()
        for (id in this){
            ids.append("${id.videoId},")
        }
        return ids.toString()
    }

}