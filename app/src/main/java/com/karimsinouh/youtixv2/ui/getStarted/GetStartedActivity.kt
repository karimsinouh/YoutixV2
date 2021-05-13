package com.karimsinouh.youtixv2.ui.getStarted

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.karimsinouh.youtixv2.R
import com.karimsinouh.youtixv2.databinding.ActivityGetStartedBinding
import com.karimsinouh.youtixv2.ui.main.MainActivity
import com.karimsinouh.youtixv2.utils.CHANNEL_ID
import com.karimsinouh.youtixv2.utils.FirstTime

class GetStartedActivity : AppCompatActivity() {

    private lateinit var binding:ActivityGetStartedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_started)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        binding= ActivityGetStartedBinding.inflate(layoutInflater)


        binding.getStarted.setOnClickListener {
            FirstTime.isFirstTime(this,false)
            Intent(this,MainActivity::class.java).let {
                startActivity(it)
                finish()
            }
        }

        binding.visitChannel.setOnClickListener {
            val url="https://www.youtube.com/channel/$CHANNEL_ID"
            openBrowser(url)
        }

    }

    private fun openBrowser(url:String){
        val intent=Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
}