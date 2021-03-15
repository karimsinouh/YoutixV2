package com.karimsinouh.youtixv2.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.karimsinouh.youtixv2.data.items.PlaylistIem
import com.karimsinouh.youtixv2.databinding.ItemPlaylistBinding
import javax.inject.Inject
import javax.inject.Singleton


class PlaylistsAdapter @Inject constructor(
        private val glide:RequestManager
):RecyclerView.Adapter<PlaylistsAdapter.PlaylistHolder>() {

    inner class PlaylistHolder(private val binding:ItemPlaylistBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(item:PlaylistIem)=binding.apply{
            glide.load(item.snippet.thumbnails.medium.url).into(thumbnail)
            title.text=item.snippet.title
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
    private val diffCallback=object: DiffUtil.ItemCallback<PlaylistIem>(){
        override fun areItemsTheSame(oldItem: PlaylistIem, newItem: PlaylistIem)=
                oldItem.id==newItem.id

        override fun areContentsTheSame(oldItem: PlaylistIem, newItem: PlaylistIem)
        =oldItem.snippet.hashCode()==newItem.snippet.hashCode()
    }

    private val differ=AsyncListDiffer(this,diffCallback)

    fun submitList(list:List<PlaylistIem>)=differ.submitList(list)

}