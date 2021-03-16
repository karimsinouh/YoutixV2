package com.karimsinouh.youtixv2.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.karimsinouh.youtixv2.data.items.VideoItem
import com.karimsinouh.youtixv2.databinding.ItemPlaylistVideoBinding
import javax.inject.Inject

class PlaylistVideosAdapter @Inject constructor(
        private val glide:RequestManager
):RecyclerView.Adapter<PlaylistVideosAdapter.PlaylistVideoHolder>() {

    inner class PlaylistVideoHolder(private val binding:ItemPlaylistVideoBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(item: VideoItem)=binding.apply{
            glide.load(item.snippet.thumbnails.medium.url).into(thumbnail)
            title.text=item.snippet.title

            root.setOnClickListener { _->
                onClick?.let {
                    it(item)
                }
            }

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=
            PlaylistVideoHolder( ItemPlaylistVideoBinding.inflate(LayoutInflater.from(parent.context),parent,false) )

    override fun onBindViewHolder(holder: PlaylistVideoHolder, position: Int) {
        val item=differ.currentList[position]
        holder.bind(item)
    }

    override fun getItemCount()=differ.currentList.size

    //diff utils
    private val diffCallback=object: DiffUtil.ItemCallback<VideoItem>(){
        override fun areItemsTheSame(oldItem: VideoItem, newItem: VideoItem)=
                oldItem.snippet.resourceId?.videoId==oldItem.snippet.resourceId?.videoId

        override fun areContentsTheSame(oldItem: VideoItem, newItem: VideoItem)=
                oldItem.snippet.hashCode()==oldItem.snippet.hashCode()
    }

    private val differ=AsyncListDiffer(this,diffCallback)

    fun submitList(list:List<VideoItem>)=differ.submitList(list)

    //callbacks

    private var onClick: ( (VideoItem) ->Unit)?=null
    fun setOnClickListener(listener:(VideoItem) ->Unit){
        onClick=listener
    }

}