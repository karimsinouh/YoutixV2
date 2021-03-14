package com.karimsinouh.youtixv2.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.karimsinouh.youtixv2.data.Snippet
import com.karimsinouh.youtixv2.data.items.VideoItem
import com.karimsinouh.youtixv2.databinding.ItemPagerHeaderBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PagerAdapter @Inject constructor(
        val glide:RequestManager
):RecyclerView.Adapter<PagerAdapter.PagerHolder>() {

    inner class PagerHolder(private val binding:ItemPagerHeaderBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(snippet: Snippet){
            glide.load(snippet.thumbnails.medium.url).into(binding.thumbnail)
            binding.title.text=snippet.title
            binding.description.text=snippet.description

            //list toggle button
            binding.listButton.setOnCheckedChangeListener { _, isChecked ->

            }

            //play button
            binding.playButton.setOnClickListener {

            }

            //share button
            binding.shareButton.setOnClickListener {

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=
            PagerHolder( ItemPagerHeaderBinding.inflate(LayoutInflater.from(parent.context),parent,false) )

    override fun onBindViewHolder(holder: PagerHolder, position: Int) {
        val item=differ.currentList[position]
        holder.bind(item.snippet)
    }

    override fun getItemCount()=differ.currentList.size

    private val diffCallback=object :DiffUtil.ItemCallback<VideoItem>(){
        override fun areItemsTheSame(oldItem: VideoItem, newItem: VideoItem)=
                oldItem.snippet.resourceId?.videoId==newItem.snippet.resourceId?.videoId

        override fun areContentsTheSame(oldItem: VideoItem, newItem: VideoItem)=
                oldItem.snippet.hashCode()==newItem.snippet.hashCode()
    }

    private val differ=AsyncListDiffer(this,diffCallback)

    fun submitList(list:List<VideoItem>)=differ.submitList(list)

}