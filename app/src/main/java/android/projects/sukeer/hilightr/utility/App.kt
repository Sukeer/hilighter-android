package android.projects.sukeer.hilightr.utility

import android.app.Application
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger

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
        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(instance)
    }
}