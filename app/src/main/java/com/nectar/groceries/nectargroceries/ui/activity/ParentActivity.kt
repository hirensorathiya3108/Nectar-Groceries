package com.nectar.groceries.nectargroceries.ui.activity

import android.app.Activity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.nectar.groceries.nectargroceries.data.preference.AppPreference

open class ParentActivity:AppCompatActivity() {
    fun hideNavigate(activity: Activity,navigation:Boolean = false) {
        if (navigation) {
            val uiOptions =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION /*or View.SYSTEM_UI_FLAG_IMMERSIVE*/ or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            activity.window.decorView.systemUiVisibility = uiOptions
        }
    }

}