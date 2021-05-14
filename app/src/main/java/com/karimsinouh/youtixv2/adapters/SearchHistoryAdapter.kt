package com.karimsinouh.youtixv2.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.karimsinouh.youtixv2.data.entities.SearchHistory
import com.karimsinouh.youtixv2.databinding.ItemSearchHistoryBinding
import com.karimsinouh.youtixv2.databinding.ItemSearchItemBinding
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class SearchHistoryAdapter @Inject constructor() :RecyclerView.Adapter<SearchHistoryAdapter.SearchHistoryViewHolder>(){

    inner class SearchHistoryViewHolder(private val binding:ItemSearchHistoryBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(item:SearchHistory){
            binding.query.text=item.query
            binding.remove.setOnClickListener {

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
    =SearchHistoryViewHolder(ItemSearchHistoryBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: SearchHistoryViewHolder, position: Int) {
        val item=differ.currentList[position]
        holder.bind(item)
    }

    override fun getItemCount()=differ.currentList.size

    //diff utils
    private val diffCallback=object : DiffUtil.ItemCallback<SearchHistory>(){
        override fun areItemsTheSame(oldItem: SearchHistory, newItem: SearchHistory)=oldItem.id==newItem.id
        override fun areContentsTheSame(oldItem: SearchHistory, newItem: SearchHistory)=oldItem.query==newItem.query
    }

    private val differ=AsyncListDiffer(this,diffCallback)

    fun submitList(list:List<SearchHistory>)=differ.submitList(list)

}