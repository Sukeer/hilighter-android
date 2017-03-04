package android.projects.sukeer.hilightr.edit

import android.projects.sukeer.hilightr.BasePresenter
import android.projects.sukeer.hilightr.BaseView

/**
 *
 * Author: Sukeerthi Khadri
 * Created: 1/8/17
 */
object EditContract {
    val KEY_EDIT_MODE = "${EditContract.javaClass.simpleName}.action.mode"
    val EDIT_MODE_CREATE = 0
    val EDIT_MODE_EDIT = 1

    val REQUEST_PLACE_PICKER = 0
}
interface EditView : BaseView<EditPresenter> {
    fun show(title: String? = null, place: String? = null, description: String? = null)
}

interface EditPresenter : BasePresenter {
    fun delete(id: Long)
    fun save(id: Long? = null, title: String, message: String)
}