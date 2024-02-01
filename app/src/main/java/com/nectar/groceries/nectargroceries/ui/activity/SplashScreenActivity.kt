package com.nectar.groceries.nectargroceries.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.nectar.groceries.nectargroceries.R
import com.nectar.groceries.nectargroceries.data.preference.AppPersistence
import com.nectar.groceries.nectargroceries.data.preference.AppPreference
import com.nectar.groceries.nectargroceries.databinding.ActivitySplashScreenBinding
import com.nectar.groceries.nectargroceries.ui.fragment.ParentFragment

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : ParentActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var activity: Activity
    private lateinit var appPreference: AppPreference

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
        binding = DataBindingUtil.setContentView(this,R.layout.activity_splash_screen)
        activity = this@SplashScreenActivity
        initViews()
    }

    private fun initViews() {
        appPreference = AppPreference(activity)
        checkNull()
        val isIntroShow = appPreference.getPreference(AppPersistence.keys.IS_INTRO_SHOW) as? Boolean ?: false
        val targetActivity = if(isIntroShow) MainActivity::class.java else StartActivity::class.java
        Handler(Looper.getMainLooper()).postDelayed({
            ParentFragment.listRefresh = true
            startActivity(Intent(activity,targetActivity))
            finish()
        },2000)
    }

    private fun checkNull() {
        if (appPreference.getPreference(AppPersistence.keys.IS_INTRO_SHOW) == null){
            appPreference.setPreference(AppPersistence.keys.IS_INTRO_SHOW,false)
        }
        if (appPreference.getPreference(AppPersistence.keys.IS_LOGIN) == null){
            appPreference.setPreference(AppPersistence.keys.IS_LOGIN,false)
        }
        if (appPreference.getPreference(AppPersistence.keys.USERNAME) == null){
            appPreference.setPreference(AppPersistence.keys.USERNAME,"")
        }
        if (appPreference.getPreference(AppPersistence.keys.EMAIL) == null){
            appPreference.setPreference(AppPersistence.keys.EMAIL,"")
        }
        if (appPreference.getPreference(AppPersistence.keys.PASSWORD) == null){
            appPreference.setPreference(AppPersistence.keys.PASSWORD,"")
        }
        if (appPreference.getPreference(AppPersistence.keys.CONFIRM_PASSWORD) == null){
            appPreference.setPreference(AppPersistence.keys.CONFIRM_PASSWORD,"")
        }
        if (appPreference.getPreference(AppPersistence.keys.USER_INFO_DATA) == null){
            appPreference.setPreference(AppPersistence.keys.USER_INFO_DATA,"")
        }
        if (appPreference.getPreference(AppPersistence.keys.IS_FILE_ADDRESS_INFO) == null){
            appPreference.setPreference(AppPersistence.keys.IS_FILE_ADDRESS_INFO,false)
        }
        if (appPreference.getPreference(AppPersistence.keys.LAST_ORDER_ID_NUMBER) == null){
            appPreference.setPreference(AppPersistence.keys.LAST_ORDER_ID_NUMBER,0)
        }
    }
}