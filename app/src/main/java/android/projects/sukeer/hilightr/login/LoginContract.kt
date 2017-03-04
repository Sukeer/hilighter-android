package android.projects.sukeer.hilightr.login

import android.projects.sukeer.hilightr.BasePresenter
import android.projects.sukeer.hilightr.BaseView

/**
 *
 * Author: Sukeerthi Khadri
 * Created: 2/19/17
 */
interface LoginView : BaseView<LoginPresenter> {
    fun startMain()
    fun startBrowser()
}

interface LoginPresenter : BasePresenter