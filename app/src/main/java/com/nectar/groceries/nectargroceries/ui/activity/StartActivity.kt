package com.nectar.groceries.nectargroceries.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.nectar.groceries.nectargroceries.R
import com.nectar.groceries.nectargroceries.data.preference.AppPersistence
import com.nectar.groceries.nectargroceries.data.preference.AppPreference
import com.nectar.groceries.nectargroceries.databinding.ActivityStartBinding

class StartActivity : ParentActivity(),View.OnClickListener {
    private lateinit var binding: ActivityStartBinding
    private lateinit var activity: Activity
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        try {
            if (Build.VERSION.SDK_INT >= 28) {
                val attributes = window.attributes
                attributes.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window.attributes = attributes
            } else {
                window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        binding = DataBindingUtil.setContentView(this,R.layout.activity_start)
        activity = this@StartActivity
        initViews()
    }

    private fun initViews() {
        binding.continueBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.continueBtn ->{
                AppPreference(activity).setPreference(AppPersistence.keys.IS_INTRO_SHOW,true)
                startActivity(Intent(activity, MainActivity::class.java))
                finish()
            }
        }
    }
}