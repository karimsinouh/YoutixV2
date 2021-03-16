package com.karimsinouh.youtixv2.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.karimsinouh.youtixv2.data.ResourceId
import com.karimsinouh.youtixv2.data.items.SearchItem
import com.karimsinouh.youtixv2.databinding.ItemSearchItemBinding
import com.karimsinouh.youtixv2.utils.KIND_VIDEO
import javax.inject.Inject

class SearchItemsAdapter @Inject constructor(
        private val glide:RequestManager
) :RecyclerView.Adapter<SearchItemsAdapter.SearchItemHolder>(){

    inner class SearchItemHolder(private val binding:ItemSearchItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(item:SearchItem)=binding.apply{
            glide.load(item.snippet.thumbnails.medium.url).into(thumbnail)
            title.text=item.snippet.title
            kind.text=if (item.id.kind== KIND_VIDEO) "Video" else "Playlist"

            root.setOnClickListener { _->
                onClick?.let {
                    it(item.id)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=
            SearchItemHolder( ItemSearchItemBinding.inflate(LayoutInflater.from(parent.context),parent,false) )

    override fun onBindViewHolder(holder: SearchItemHolder, position: Int) {
        val item=differ.currentList[position]
        holder.bind(item)
    }

    override fun getItemCount()=differ.currentList.size

    //diff utils
    private val diffCallback=object:DiffUtil.ItemCallback<SearchItem>(){
        override fun areItemsTheSame(oldItem: SearchItem, newItem: SearchItem):Boolean{
            return if (oldItem.id.kind== KIND_VIDEO)
                oldItem.id.videoId==newItem.id.videoId
            else
                oldItem.id.playlistId==newItem.id.playlistId
        }

        override fun areContentsTheSame(oldItem: SearchItem, newItem: SearchItem)=
                oldItem.snippet.hashCode()==newItem.snippet.hashCode()
    }

    private val differ=AsyncListDiffer(this,diffCallback)

    fun submitList(list:List<SearchItem>)=differ.submitList(list)

    //callbacks
    private var onClick:( (ResourceId)->Unit )?=null

    fun setOnClickListener(listener:(ResourceId)->Unit){
        onClick=listener
    }

}