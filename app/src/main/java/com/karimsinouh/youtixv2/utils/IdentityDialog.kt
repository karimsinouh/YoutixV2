package com.karimsinouh.youtixv2.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.edit
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.karimsinouh.youtixv2.R
import com.karimsinouh.youtixv2.databinding.LayoutCreateProfileBinding
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject
import kotlin.random.Random


class IdentityDialog constructor(
    private val context:Context
) {

    private val prefs=context.getSharedPreferences("identity_prefs",Context.MODE_PRIVATE)

    fun exists()=prefs.getString("id","0")!="0"

    fun name()=prefs.getString("name","")

    fun show(){

        val currentId=prefs.getString("id","0")
        var name=name()
        val isFirstTime= currentId=="0"

        val view=LayoutInflater.from(context).inflate(R.layout.layout_create_profile,null,false)

        val binding=LayoutCreateProfileBinding.bind(view)
        val dialog=MaterialAlertDialogBuilder(context)

        dialog.setOnDismissListener { _->
            onDismiss?.let {
                it()
            }
        }

        dialog.setView(binding.root)

        binding.input.setText(name)

        if (!isFirstTime) {
            binding.textView.visibility = View.GONE
            binding.input.setText(name)
        }

        binding.done.setOnClickListener {

            name=binding.input.text.toString()

            if (name!!.isEmpty()){
                binding.input.error="Please enter a name"
                return@setOnClickListener
            }else if (name!!.length>=15){
                binding.input.error="Please enter a proper name"
                return@setOnClickListener
            }

            prefs.edit {
                if (isFirstTime){
                    val newId=name?.trim().toString()+System.currentTimeMillis()+ Random.nextInt(0,10000)
                    putString("id",newId)
                }
                putString("name",name)
                commit()
            }

            binding.done()

        }

        dialog.show()

    }

    private fun LayoutCreateProfileBinding.done(){
        textView.visibility=View.GONE
        inputLayout.visibility=View.GONE
        done.visibility=View.GONE

        doneIcon.visibility=View.VISIBLE
        doneText.visibility=View.VISIBLE

    }

    private var onDismiss:(()->Unit)?=null

    fun onDismissListener(listener:()->Unit){
        onDismiss=listener
    }

}