package com.karimsinouh.youtixv2.ui.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.karimsinouh.youtixv2.R
import com.karimsinouh.youtixv2.databinding.ActivityChatBinding
import com.karimsinouh.youtixv2.utils.IdentityDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {

    private val identityDialog by lazy {
        IdentityDialog(this)
    }

    private lateinit var binding:ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_YoutixV2)
        binding= ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!identityDialog.exists()) {
            identityDialog.show()
            binding.root.visibility=View.GONE
        }

        identityDialog.onDismissListener {
            if (identityDialog.exists() && !binding.root.isShown)
                binding.root.visibility=View.VISIBLE
        }

        binding.editButton.setOnClickListener {
            identityDialog.show()
        }

    }


}