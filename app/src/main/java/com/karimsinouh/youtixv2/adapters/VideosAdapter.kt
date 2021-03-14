package com.karimsinouh.youtixv2.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.karimsinouh.youtixv2.data.items.VideoItem
import com.karimsinouh.youtixv2.databinding.ItemVideoBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideosAdapter @Inject constructor(
    val glide:RequestManager
):RecyclerView.Adapter<VideosAdapter.VideoHolder>() {


    inner class VideoHolder(private val binding:ItemVideoBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(video:VideoItem){
            binding.title.text=video.snippet.title
            glide.load(video.snippet.thumbnails.medium.url).into(binding.thumbnail)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
    =VideoHolder( ItemVideoBinding.inflate(LayoutInflater.from(parent.context),parent,false) )

    override fun onBindViewHolder(holder: VideoHolder, position: Int) {
        val item=differ.currentList[position]
        holder.bind(item)

        //onClick
        holder.itemView.setOnClickListener { _->
            onClick?.let {
                it(item)
            }
        }

        //onLongClick
        holder.itemView.setOnLongClickListener { view->
            onLongClick?.let {
                it(view,item)
            }
            true
        }

    }

    override fun getItemCount()=differ.currentList.size


    //diff Utils
    private val diffCallback=object: DiffUtil.ItemCallback<VideoItem>(){

        override fun areItemsTheSame(oldItem: VideoItem, newItem: VideoItem): Boolean {
            return if (oldItem.kind=="youtube#playlistItemListResponse"){
                oldItem.snippet.resourceId?.videoId==newItem.snippet.resourceId?.videoId
            }else{
                oldItem.id==newItem.id
            }
        }

        override fun areContentsTheSame(oldItem: VideoItem, newItem: VideoItem)
        =oldItem.hashCode()==newItem.hashCode()

    }

    private val differ=AsyncListDiffer(this,diffCallback)

    fun submitItems(items:List<VideoItem>){
        differ.submitList(items)
    }

    //callbacks
    private var onClick:((VideoItem)->Unit)?=null
    fun setOnClickListener(listener:(VideoItem)->Unit){
        onClick=listener
    }

    private var onLongClick:( (View,VideoItem)->Unit )?=null
    fun setOnLongClickListener(listener:(View,VideoItem)->Unit){
        onLongClick=listener
    }
}