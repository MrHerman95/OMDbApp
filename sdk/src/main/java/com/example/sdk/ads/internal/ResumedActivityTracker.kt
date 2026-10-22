package com.example.sdk.ads.internal

import android.app.Activity
import android.app.Application
import android.os.Bundle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first

/**
 * Holds the activity in the foreground, which full-screen ads need.
 *
 * A plain reference is safe because only resumed activities are stored, and while the process is
 * alive a resumed activity is always paused before it's stopped or destroyed. An activity that
 * finishes before it's resumed skips `onPause`, but it was never stored. When the process is
 * killed, no callback runs, but the tracker dies with it.
 */
internal class ResumedActivityTracker : Application.ActivityLifecycleCallbacks {

    private val resumedActivity = MutableStateFlow<Activity?>(null)

    fun register(application: Application) {
        application.registerActivityLifecycleCallbacks(this)
    }

    suspend fun awaitResumedActivity(): Activity {
        return resumedActivity.filterNotNull().first()
    }

    override fun onActivityResumed(activity: Activity) {
        resumedActivity.value = activity
    }

    override fun onActivityPaused(activity: Activity) {
        // Another activity may already be resumed (switching activities, multi-window)
        if (resumedActivity.value === activity) {
            resumedActivity.value = null
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = Unit
    override fun onActivityStarted(activity: Activity) = Unit
    override fun onActivityStopped(activity: Activity) = Unit
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit
    override fun onActivityDestroyed(activity: Activity) = Unit
}
