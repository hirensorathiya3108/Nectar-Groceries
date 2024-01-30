package com.nectar.groceries.nectargroceries

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.Process
import android.os.StrictMode
import androidx.lifecycle.*
import com.google.firebase.FirebaseApp
import com.nectar.groceries.nectargroceries.ui.activity.SplashScreenActivity

//import com.squareup.leakcanary.LeakCanary
//import com.squareup.leakcanary.RefWatcher
//import com.google.firebase.analytics.FirebaseAnalytics

//import org.opencv.android.OpenCVLoader

class MyApplication : Application(), Application.ActivityLifecycleCallbacks, LifecycleObserver {

    private var currentActivity: Activity? = null
    private val disWidth = 720
//    private lateinit var refWatcher: RefWatcher

    //    var appOpenAdManager: AppOpenAdManager? = null

    private var context1: Context? = null
    companion object {
        private var appInstance: MyApplication? = null

        @JvmField
        var isDebug = false
        var isAdShowing = true

//        @JvmStatic
//        fun getRefWatcher(context: Context? = null): RefWatcher {
//            val myApplication = context?.applicationContext as MyApplication
//            return myApplication.refWatcher
//        }

        @JvmStatic
        val context: Context
            get() = appInstance!!.applicationContext

    }

    fun findContext(): Context? {
        if (context1 == null) {
            val readerPdfApplication: MyApplication = appInstance!!
            if (readerPdfApplication == null) {
                Process.killProcess(Process.myPid())
            } else {
                context1 = readerPdfApplication.applicationContext
            }
        }
        return context1
    }

    override fun onCreate() {
        super.onCreate()
        appInstance = this
        isDebug = BuildConfig.DEBUG
        context1 = applicationContext

        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return
//        }

//        refWatcher = LeakCanary.install(this)
        FirebaseApp.initializeApp(this)
//        FirebaseAnalytics.getInstance(this)

        try {
//            MobileAds.initialize(this) { }
        } catch (e: Exception) {
            e.printStackTrace()
        }

//        appOpenAds = AppOpenAds()
        ProcessLifecycleOwner.get().lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                lifecycleEventObserver(event)
            }
        })
//        appOpenAdManager = AppOpenAdManager()
//        appOpenAds = AppOpenAds()

//        disWidth = DisplaySizeHelper.getDisplayWidth();
//        if (disWidth > 720)
//            disWidth = 720;
//        Constants.Companion.VideoWidth = disWidth;
//        Constants.Companion.VideoHeight = disWidth;
    }

    fun lifecycleEventObserver(
        event: Lifecycle.Event
    ) {
        when (WhenMappings.EnumSwitchMapping[event.ordinal]) {
            Lifecycle.Event.ON_START.ordinal -> {
                if (currentActivity != null && currentActivity !is SplashScreenActivity) {
                    try {
//                        val checkAppOpenAdTimer: Boolean =
//                            appOpenAds!!.checkAppOpenAdTimer(currentActivity)

//                        if (currentActivity !is SplashScreenActivity && checkAppOpenAdTimer && appOpenAds != null) {
//                            appOpenAds!!.showAdIfAvailable(currentActivity!!)
//                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            Lifecycle.Event.ON_RESUME.ordinal -> {

            }
            Lifecycle.Event.ON_PAUSE.ordinal -> {
            }
            Lifecycle.Event.ON_DESTROY.ordinal -> {
            }
            else -> {
            }
        }
    }

    object WhenMappings {
        val EnumSwitchMapping: IntArray
        init {
            val iArr = IntArray(Lifecycle.Event.values().size)
            iArr[Lifecycle.Event.ON_START.ordinal] = 1
            iArr[Lifecycle.Event.ON_RESUME.ordinal] = 2
            iArr[Lifecycle.Event.ON_PAUSE.ordinal] = 3
            iArr[Lifecycle.Event.ON_DESTROY.ordinal] = 4
            EnumSwitchMapping = iArr
        }
    }

    /**
     * LifecycleObserver method that shows the app open ad when the app moves to foreground.
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected fun onMoveToForeground() {
        // Show the ad (if available) when the app moves to foreground.
//        appOpenAdManager.showAdIfAvailable(currentActivity);
    }

    /**
     * ActivityLifecycleCallback methods.
     */
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityStarted(activity: Activity) {
        // An ad activity is started when an ad is showing, which could be AdActivity class from Google
        // SDK or another activity class implemented by a third party mediation partner. Updating the
        // currentActivity only when an ad is not showing will ensure it is not an ad activity, but the
        // one that shows the ad.
//        if (!AppOpenAds.isShowingAd) {
//            currentActivity = activity
//        }
    }

    override fun onActivityResumed(activity: Activity) {}
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}

    /**
     * Interface definition for a callback to be invoked when an app open ad is complete
     * (i.e. dismissed or fails to show).
     */
    interface OnShowAdCompleteListener {
        fun onShowAdComplete()
    }
}