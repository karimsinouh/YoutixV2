package com.karimsinouh.youtixv2.ui.descriptionBottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.karimsinouh.youtixv2.R
import com.karimsinouh.youtixv2.data.Snippet
import com.karimsinouh.youtixv2.databinding.FragmentDescriptionBinding
import com.karimsinouh.youtixv2.utils.SNIPPET
import org.ocpsoft.prettytime.PrettyTime
import java.text.SimpleDateFormat

class DescriptionBottomSheet:BottomSheetDialogFragment() {

    private lateinit var binding:FragmentDescriptionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ):View =inflater.inflate(R.layout.fragment_description,container,false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentDescriptionBinding.bind(view)

        val snippet=arguments?.getSerializable(SNIPPET) as Snippet

        val prettyTime=PrettyTime()
        val dateFormatter= SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val time=dateFormatter.parse(snippet.publishedAt)

        binding.apply {
            title.text=snippet.title
            description.text=snippet.description
            date.text=prettyTime.format(time)
        }

    }

}