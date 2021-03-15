package com.karimsinouh.youtixv2.ui.search

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.karimsinouh.youtixv2.R
import com.karimsinouh.youtixv2.adapters.SearchItemsAdapter
import com.karimsinouh.youtixv2.databinding.FragmentSearchBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment:Fragment(R.layout.fragment_search) {

    private lateinit var binding:FragmentSearchBinding
    private lateinit var builder:MaterialAlertDialogBuilder

    @Inject lateinit var adapter:SearchItemsAdapter
    private val vm by viewModels<SearchViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentSearchBinding.bind(view)


        setupRcv()

        builder=MaterialAlertDialogBuilder(requireContext()).setTitle("Error")

        subscribeToObservers()

        binding.input.setEndIconOnClickListener {
            val q=binding.input.editText?.text.toString()
            if (q.isNotEmpty())
                search(q)
        }
    }

    private fun setupRcv()=binding.rcv.apply{
        layoutManager=LinearLayoutManager(requireContext())
        setHasFixedSize(true)
        adapter=this@SearchFragment.adapter
    }

    private fun subscribeToObservers(){

        vm.result.observe(viewLifecycleOwner){
            if (it.isEmpty()){
                Toast.makeText(requireContext(),"No results",Toast.LENGTH_SHORT).show()
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