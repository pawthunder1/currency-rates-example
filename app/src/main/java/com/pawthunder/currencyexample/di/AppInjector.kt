package com.pawthunder.currencyexample.di

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.pawthunder.currencyexample.CurrencyApp
import dagger.android.AndroidInjection
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection

/**
 * Injector inject activities and fragments upon their creation if they are marked for injection.
 * [Activity] is marked by [HasAndroidInjector] and [Fragment] by [Injectable].
 */
object AppInjector {

    /**
     * Register injection of application's activities and fragments upon their creation.
     * @param application Application containing activities and fragments which can be injected
     * @see AndroidInjection.inject
     * @see AndroidSupportInjection.inject
     */
    fun init(application: CurrencyApp) {
        DaggerAppComponent.builder().application(application).build().inject(application)

        application.registerActivityLifecycleCallbacks(object :
            Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                handleActivity(activity)
            }

            override fun onActivityStarted(activity: Activity) {}

            override fun onActivityResumed(activity: Activity) {}

            override fun onActivityPaused(activity: Activity) {}

            override fun onActivityStopped(activity: Activity) {}

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {}

            override fun onActivityDestroyed(activity: Activity) {}
        })
    }

    private fun handleActivity(activity: Activity) {
        if (activity is HasAndroidInjector)
            AndroidInjection.inject(activity)

        if (activity is FragmentActivity) {
            activity.supportFragmentManager.registerFragmentLifecycleCallbacks(
                object : FragmentManager.FragmentLifecycleCallbacks() {
                    override fun onFragmentCreated(
                        manager: FragmentManager,
                        fragment: Fragment,
                        savedInstanceState: Bundle?
                    ) {
                        if (fragment is Injectable) {
                            AndroidSupportInjection.inject(fragment)
                        }
                    }
                }, true
            )
        }
    }
}
