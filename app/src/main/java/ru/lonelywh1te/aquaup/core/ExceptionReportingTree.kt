package ru.lonelywh1te.aquaup.core

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.crashlytics
import timber.log.Timber

class ExceptionReportingTree: Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == Log.ERROR || priority == Log.WARN) {
            if (t != null) {
                Firebase.crashlytics.recordException(t)
            } else {
                Firebase.crashlytics.log(message)
            }
        }
    }

}