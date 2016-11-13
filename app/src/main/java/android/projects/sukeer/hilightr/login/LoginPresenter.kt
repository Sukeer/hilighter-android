package android.projects.sukeer.hilightr.login

import android.content.Intent
import android.projects.sukeer.hilightr.database.sqlitedb.PersonModel
import android.projects.sukeer.hilightr.database.sqlitedb.PersonSqliteModel
import android.projects.sukeer.hilightr.utility.App
import android.projects.sukeer.hilightr.utility.log
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_login.*

/**
 *
 * Author: Sukeerthi Khadri
 * Created: 11/5/16
 */
class LoginPresenter(val view: LoginFragment) : LoginContract.Presenter {
    init {
        view.setPresenter(this)
    }

    private val model = PersonSqliteModel(this)

    override fun addUser(user: FirebaseUser) {
        val newUser = PersonModel(user.uid, user.displayName!!, user.email!!, user.photoUrl.toString())
        model.insert(newUser)
    }

    override fun onItemInserted(id: Long) {
        log("Inserted: $id")
        view.startMain()
    }

    override fun onItemRetrieved(items: List<Any>) {
        log("Retrieved ${items.size}")
    }

    override fun onItemDeleted(numOfDeletes: Int) {
        log("Deleted $numOfDeletes")
    }

    override fun onItemUpdated(numOfUpdates: Int) {
        log("Updated $numOfUpdates")
    }

    override fun setup() {
        log("Setup time!!")

        view.loginBtn.fragment = view
        view.loginBtn.setReadPermissions("email", "public_profile", "user_friends")
        view.loginBtn.registerCallback(App.instance.callbackManager, App.instance.facebookCallback)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        App.instance.callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    override fun onStart() {
        App.instance.onStart()
    }

    override fun onStop() {
        App.instance.onStop()
    }

    override fun onDetach() {
        log("onDetach: ${view.activity != null}")
    }

    override fun onDestroy() {
        App.instance.accessTokenTracker.stopTracking()
    }
}