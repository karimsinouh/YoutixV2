package com.karimsinouh.youtixv2.ui.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.karimsinouh.youtixv2.R
import com.karimsinouh.youtixv2.adapters.MessagesAdapter
import com.karimsinouh.youtixv2.databinding.ActivityChatBinding
import com.karimsinouh.youtixv2.utils.IdentityDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {

    private val identityDialog by lazy {
        IdentityDialog(this)
    }

    private val adapter by lazy {
        MessagesAdapter(identityDialog.id()){

        }
    }

    private lateinit var binding:ActivityChatBinding
    private val vm by viewModels<ChatViewModel>()

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_YoutixV2)
        binding= ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!identityDialog.exists()) {
            identityDialog.show()
            binding.root.visibility=View.GONE
        }else
            vm.loadMessages()

        identityDialog.onDismissListener {
            if (identityDialog.exists() && !binding.root.isShown) {
                binding.root.visibility = View.VISIBLE
                vm.loadMessages()
            }
        }

        binding.editButton.setOnClickListener {
            identityDialog.show()
        }

        binding.input.setEndIconOnClickListener {
            val text=binding.input.editText?.text.toString()

            if (text.isNotEmpty()) {
                vm.sendMessage(text)
                binding.input.editText?.setText("")
            }

        }

        setupRcv()

        subscribeToObservers()
    }

    private fun subscribeToObservers(){
        vm.messages.observe(this){
            it?.let {
                adapter.submitList(it)
                binding.progressBar.visibility=View.GONE
            }
        }
    }

    private fun setupRcv(){
        binding.rcv.layoutManager=LinearLayoutManager(this).apply {
            reverseLayout=true
        }
        binding.rcv.adapter=adapter
    }


}