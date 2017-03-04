package android.projects.sukeer.hilightr.login

import android.os.Bundle
import android.projects.sukeer.hilightr.R
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

        var loginFrag = supportFragmentManager.findFragmentById(R.id.container) as LoginFragment?
        if (loginFrag == null) {
            loginFrag = LoginFragment()
            LoginPresenterImpl(loginFrag)
            supportFragmentManager.beginTransaction().add(R.id.container, loginFrag).commit()
        }
    }

}
