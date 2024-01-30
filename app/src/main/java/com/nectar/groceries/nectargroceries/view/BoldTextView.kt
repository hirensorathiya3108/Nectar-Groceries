package com.nectar.groceries.nectargroceries.view

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView


class BoldTextView : AppCompatTextView {
    constructor(mContext: Context?) : super(mContext!!) {
        setTextFont()
    }

    constructor(mContext: Context?, set: AttributeSet?) : super(
        mContext!!, set
    ) {
        setTextFont()
    }

    constructor(mContext: Context?, set: AttributeSet?, defaultStyle: Int) : super(
        mContext!!, set, defaultStyle
    ) {
        setTextFont()
    }

    fun setColors(textColor: Int, accentColor: Int, backgroundColor: Int) {
        setTextColor(textColor)
        setLinkTextColor(accentColor)
    }

    private fun setTextFont() {
        val typeface = Typeface.createFromAsset(context.assets, "Pangram-Bold.otf")
        setTypeface(typeface)
    }
}