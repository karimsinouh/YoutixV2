package com.karimsinouh.youtixv2.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.karimsinouh.youtixv2.data.items.VideoItem
import com.karimsinouh.youtixv2.databinding.ItemPlaylistVideoBinding
import dagger.hilt.android.scopes.FragmentScoped
import java.lang.NullPointerException
import javax.inject.Inject

@FragmentScoped
class PlaylistVideosAdapter @Inject constructor(
        private val glide:RequestManager
):RecyclerView.Adapter<PlaylistVideosAdapter.PlaylistVideoHolder>() {

    inner class PlaylistVideoHolder(private val binding:ItemPlaylistVideoBinding):RecyclerView.ViewHolder(binding.root){

        fun bind(item: VideoItem)=binding.apply{

            try {
                glide.load(item.snippet.thumbnails.medium.url).into(thumbnail)
            }catch (e:NullPointerException){
                glide.load("https://i.ytimg.com/img/no_thumbnail.jpg").into(thumbnail)
            }

            title.text=item.snippet.title

            root.setOnClickListener { _->
                onClick?.let {
                    it(item)
                }
            }

            root.setOnLongClickListener {
                onLongClick?.let {
                    it(item.id!!)
                }
                true
            }

            val selectionMode=selectedItems.isNotEmpty()

            if(selectionMode) {
                checkBox.visibility = View.VISIBLE
                checkBox.isChecked=selectedItems.contains(item.id)
            }else{
                checkBox.visibility = View.GONE
            }

        }

    }

    private val selectedItems=ArrayList<String>()

    fun setSelectedList(list:List<String>){
        selectedItems.clear()
        selectedItems.addAll(list)
        this.notifyDataSetChanged()
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
            if(oldItem.snippet.resourceId?.videoId!=null)
                oldItem.snippet.resourceId.videoId==newItem.snippet.resourceId?.videoId
            else
                oldItem.id==newItem.id

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

    private var onLongClick: ((videoId:String)->Unit)?=null
    fun setOnLongClickListener( listener: (videoId:String) -> Unit ){
        onLongClick=listener
    }

}