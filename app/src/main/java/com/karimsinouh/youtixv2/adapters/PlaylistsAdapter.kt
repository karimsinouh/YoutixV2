package com.karimsinouh.youtixv2.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.karimsinouh.youtixv2.data.items.PlaylistItem
import com.karimsinouh.youtixv2.databinding.ItemPlaylistBinding
import javax.inject.Inject


class PlaylistsAdapter @Inject constructor(
        private val glide:RequestManager
):RecyclerView.Adapter<PlaylistsAdapter.PlaylistHolder>() {

    inner class PlaylistHolder(private val binding:ItemPlaylistBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(item:PlaylistItem)=binding.apply{
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
            PlaylistHolder( ItemPlaylistBinding.inflate(LayoutInflater.from(parent.context),parent,false) )

    override fun onBindViewHolder(holder: PlaylistHolder, position: Int) {
        val item=differ.currentList[position]
        holder.bind(item)
    }

    override fun getItemCount()=differ.currentList.size

    //diff utils
    private val diffCallback=object: DiffUtil.ItemCallback<PlaylistItem>(){
        override fun areItemsTheSame(oldItem: PlaylistItem, newItem: PlaylistItem)=
                oldItem.id==newItem.id

        override fun areContentsTheSame(oldItem: PlaylistItem, newItem: PlaylistItem)
        =oldItem.snippet.hashCode()==newItem.snippet.hashCode()
    }

    private val differ=AsyncListDiffer(this,diffCallback)

    fun submitList(list:List<PlaylistItem>)=differ.submitList(list)

    //callbacks
    private var onClick:( (PlaylistItem)->Unit )?=null

    fun setOnClickListener( listener:(PlaylistItem)->Unit){
        onClick=listener
    }
}