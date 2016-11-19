package android.projects.sukeer.hilightr.login

import android.content.Intent
import android.os.Bundle
import android.projects.sukeer.hilightr.R
import android.projects.sukeer.hilightr.utility.log
import android.support.v7.app.AppCompatActivity

/**
 *
 * Author: Sukeerthi Khadri
 * Created: 10/22/16
 */
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var viewFrag = supportFragmentManager.findFragmentByTag(LoginFragment.tag) as LoginFragment?
        if (viewFrag == null) {
            log("Null")
            viewFrag = LoginFragment()
            val presenter = LoginPresenter(viewFrag)
            supportFragmentManager.beginTransaction().add(R.id.container, viewFrag, LoginFragment.tag).commit()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }

}
