package android.projects.sukeer.hilightr.login

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.projects.sukeer.hilightr.utility.App

/**
 *
 * Author: Sukeerthi Khadri
 * Created: 2/19/17
 */
class LoginPresenterImpl(override val view: LoginView) : LoginPresenter {
    init {
        view.setPresenter(this)
    }

    override val intentFilter: IntentFilter = IntentFilter().apply {
        addAction(App.ACTION_LOGIN)
        addAction(App.ACTION_LOGOUT)
    }

    override val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                App.ACTION_LOGIN -> view.startMain()
                App.ACTION_LOGOUT -> view.showToast("Logged out")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // forward result to Facebook callbackManager
        App.instance.callbackManager.onActivityResult(requestCode, resultCode, data)
    }

}
