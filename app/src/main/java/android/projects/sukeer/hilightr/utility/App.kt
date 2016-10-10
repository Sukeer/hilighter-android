package android.projects.sukeer.hilightr.utility

import android.app.Application

/**
 *
 * Author: Sukeerthi Khadri
 * Created: 10/9/16
 */
class App : Application() {
    companion object {
        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}