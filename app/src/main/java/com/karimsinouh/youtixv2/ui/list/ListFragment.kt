package com.karimsinouh.youtixv2.ui.list

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.karimsinouh.youtixv2.R
import com.karimsinouh.youtixv2.adapters.PlaylistVideosAdapter
import com.karimsinouh.youtixv2.data.entities.HistoryItem
import com.karimsinouh.youtixv2.data.entities.WatchLater
import com.karimsinouh.youtixv2.databinding.FragmentListBinding
import com.karimsinouh.youtixv2.utils.ACTION
import com.karimsinouh.youtixv2.utils.ACTION_HISTORY
import com.karimsinouh.youtixv2.utils.VIDEO_ID
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ListFragment:Fragment(R.layout.fragment_list) {

    private lateinit var binding:FragmentListBinding
    private lateinit var nav:NavController
    private lateinit var action:String
    private lateinit var lManager:LinearLayoutManager

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

        adapter.setOnClickListener {
            navigateToVideoInfo(it.id!!)
        }


    }

    private fun navigateToVideoInfo(videoId:String){
        nav.navigate(R.id.list_to_viewVideo, bundleOf(VIDEO_ID to videoId))
    }

    private fun setupRcv()=binding.rcv.apply{
        lManager= LinearLayoutManager(requireContext())
        layoutManager=lManager
        setHasFixedSize(true)
        adapter=this@ListFragment.adapter


        val itemTouchHelperCallback=object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT){
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
               return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position=viewHolder.adapterPosition

                if(action== ACTION_HISTORY)
                    vm.deleteFromHistory(position)
                else
                    vm.deleteFromWatchLater(position)

            }

        }

    }

    private fun subscribeToObservers(){

        vm.list.observe(viewLifecycleOwner){

            adapter.submitList(it)

            if (it.isEmpty())
                binding.emptyLayout.visibility=View.VISIBLE
            else
                binding.emptyLayout.visibility=View.GONE

            if (binding.bar.isShown)
                binding.bar.visibility=View.GONE
        }


        vm.error.observe(viewLifecycleOwner){
            if(binding.bar.isShown)
                binding.bar.visibility=View.GONE

            binding.emptyLayout.visibility=View.VISIBLE
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