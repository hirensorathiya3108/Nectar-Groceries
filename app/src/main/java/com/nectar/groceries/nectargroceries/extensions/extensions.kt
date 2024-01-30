package com.nectar.groceries.nectargroceries.extensions

import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat

const val SHORT_ANIMATION_DURATION = 150L


fun View.beInvisibleIf(beInvisible: Boolean) = if (beInvisible) beInvisible() else beVisible()

fun View.beVisibleIf(beVisible: Boolean) = if (beVisible) beVisible() else beGone()

fun View.beGoneIf(beGone: Boolean) = beVisibleIf(!beGone)

fun View.beInvisible() {
    visibility = View.INVISIBLE
}

fun View.beVisible() {
    visibility = View.VISIBLE
}

fun View.beGone() {
    visibility = View.GONE
}

fun View.isVisible() = visibility == View.VISIBLE

fun View.isInvisible() = visibility == View.INVISIBLE

fun View.isGone() = visibility == View.GONE

fun View.fadeIn() {
    animate().alpha(1f).setDuration(SHORT_ANIMATION_DURATION).withStartAction { beVisible() }.start()
}

fun View.fadeOut() {
    animate().alpha(0f).setDuration(SHORT_ANIMATION_DURATION).withEndAction { beGone() }.start()
}

fun View.loadAnimation(animation:Int) {
    AnimationUtils.loadAnimation(context,animation)
}

fun View.getColor(color:Int) = ContextCompat.getColor(context,color)

fun Float.toPx(context: Context) = (this * context.resources.displayMetrics.scaledDensity + 0.5F)