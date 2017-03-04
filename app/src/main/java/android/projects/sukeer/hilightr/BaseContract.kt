package android.projects.sukeer.hilightr

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter

/**
 *
 * Author: Sukeerthi Khadri
 * Created: 11/17/16
 */
interface BaseView<T : BasePresenter> {
    var pres: T

    fun setup()
    fun showToast(message: String)
    fun finish(intent : Intent? = null)
    fun setPresenter(presenter: T) {
        pres = presenter
    }
}

interface BasePresenter {
    val view: BaseView<out BasePresenter>
    val intentFilter: IntentFilter
    val receiver: BroadcastReceiver

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
}