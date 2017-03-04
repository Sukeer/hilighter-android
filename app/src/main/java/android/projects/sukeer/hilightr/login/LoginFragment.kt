package android.projects.sukeer.hilightr.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.projects.sukeer.hilightr.R
import android.projects.sukeer.hilightr.main.MainActivity
import android.projects.sukeer.hilightr.utility.App
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.*
import com.facebook.AccessToken
import kotlinx.android.synthetic.main.fragment_login.*
import org.jetbrains.anko.toast

/**
 *
 * Author: Sukeerthi Khadri
 * Created: 11/6/16
 */
class LoginFragment : Fragment(), LoginView {

    override lateinit var pres: LoginPresenter

    private val isLoggedIn: Boolean
        get() = AccessToken.getCurrentAccessToken() != null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater?.inflate(R.layout.fragment_login, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setup()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_login, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_about -> startBrowser()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        if (isLoggedIn) {
            startMain()
        }
        App.broadcastManager.registerReceiver(pres.receiver, pres.intentFilter)
    }

    override fun onPause() {
        super.onPause()
        App.broadcastManager.unregisterReceiver(pres.receiver)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        pres.onActivityResult(requestCode, resultCode, data)
    }

    override fun setup() {
        btn_login.fragment = this
        btn_login.setReadPermissions("email", "public_profile", "user_friends")
        btn_login.registerCallback(App.instance.callbackManager, App.instance.facebookCallback)

        // setup toolbar
        val toolbar = activity.findViewById(R.id.toolbar) as Toolbar
        toolbar.title = ""
        val parentActivity = activity as AppCompatActivity
        parentActivity.setSupportActionBar(toolbar)
    }

    override fun startBrowser() = startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Sukeer/hilighter-android")))

    override fun startMain() {
        startActivity(Intent(activity, MainActivity::class.java).apply {
            // ensure that only a single instance of the main activity is shown (in case it is started more than once)
            // and clear any activities above it in the task
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        })
        finish()
    }

    override fun showToast(message: String) {
        activity.toast(message)
    }

    override fun finish(intent: Intent?) {
        activity.finish()
    }

}