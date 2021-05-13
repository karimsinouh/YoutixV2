package com.karimsinouh.youtixv2.ui.getStarted

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class SquareImage(context:Context,attributeSet: AttributeSet):
    AppCompatImageView(context,attributeSet) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }

}