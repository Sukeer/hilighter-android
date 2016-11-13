package android.projects.sukeer.hilightr.login

import android.content.Intent
import android.projects.sukeer.hilightr.database.sqlitedb.SqliteModel
import com.google.firebase.auth.FirebaseUser

/**
 *
 * Author: Sukeerthi Khadri
 * Created: 11/6/16
 */
interface LoginContract {

    interface View {
        fun setPresenter(presenter: Presenter)
        fun startMain()
    }

    interface Presenter : SqliteModel.Listener {
        fun addUser(user: FirebaseUser)
        fun setup()
        fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
        fun onStart()
        fun onStop()
        fun onDetach()
        fun onDestroy()
    }
}