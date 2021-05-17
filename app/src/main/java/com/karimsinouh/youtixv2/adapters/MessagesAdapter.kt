package com.karimsinouh.youtixv2.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.karimsinouh.youtixv2.data.Message
import com.karimsinouh.youtixv2.databinding.ItemMessageLeftBinding
import com.karimsinouh.youtixv2.databinding.ItemMessageRightBinding
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

class MessagesAdapter(
        private val uid:String,
        val onClick:()->Unit
):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object{
        private const val TYPE_LEFT=1
        private const val TYPE_RIGHT=2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){

            TYPE_LEFT->MessageLeftHolder(ItemMessageLeftBinding.inflate(LayoutInflater.from(parent.context),parent,false))


            TYPE_RIGHT->MessageRightHolder(ItemMessageRightBinding.inflate(LayoutInflater.from(parent.context),parent,false))

            else -> null!!

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder){
            is MessageRightHolder->holder.bind(position)
            is MessageLeftHolder->holder.bind(position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item=differ.currentList[position]
        return  if (item.id==uid) TYPE_RIGHT else TYPE_LEFT
    }

    override fun getItemCount()=differ.currentList.size

    //diff utils
    private val diffCallback=object : DiffUtil.ItemCallback<Message>(){
        override fun areItemsTheSame(oldItem: Message, newItem: Message)=oldItem.id==newItem.id
        override fun areContentsTheSame(oldItem: Message, newItem: Message)=oldItem.hashCode()==newItem.hashCode()
    }

    private val differ=AsyncListDiffer(this,diffCallback)

    fun submitList(newList:List<Message>)=differ.submitList(newList)

    inner class MessageLeftHolder(private val binding: ItemMessageLeftBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            val msg=differ.currentList[position]
            binding.message.text=msg.message
            binding.userName.text=msg.userName
        }
    }

    inner class MessageRightHolder(private val binding:ItemMessageRightBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            val msg=differ.currentList[position]
            binding.message.text=msg.message
        }
    }

}