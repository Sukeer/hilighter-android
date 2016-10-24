package android.projects.sukeer.hilightr.core

import android.content.Intent
import android.os.Bundle
import android.projects.sukeer.hilightr.R
import android.projects.sukeer.hilightr.utility.App
import android.projects.sukeer.hilightr.utility.log
import android.support.v7.app.AppCompatActivity
import com.facebook.*
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

/**
 *
 * Author: Sukeerthi Khadri
 * Created: 10/22/16
 */
class MainActivity : AppCompatActivity() {

    private lateinit var callbackManager: CallbackManager
    private lateinit var auth: FirebaseAuth
    private lateinit var authListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FacebookSdk.sdkInitialize(App.instance.applicationContext)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        authListener = FirebaseAuth.AuthStateListener {
            val user = it.currentUser
            if (user != null) {
                log("authstatechange: signed_in ${user.uid}\n" +
                        "${user.displayName}\n${user.photoUrl}\n${user.email}")
            } else {
                log("authstatechange: signed_out")
            }
        }
        callbackManager = CallbackManager.Factory.create()
        loginBtn.setReadPermissions("email", "public_profile", "user_friends")
        loginBtn.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                log("onSuccess : $result")
                handleFacebookAccessToken(result.accessToken)
            }

            override fun onCancel() {
                log("onCancel")
            }

            override fun onError(error: FacebookException) {
                log("onError: $error")
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener(authListener)
    }

    override fun onStop() {
        super.onStop()
        auth.removeAuthStateListener(authListener)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        log("handleFacebookAccessToken $token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this) {
                    log("signInWithCredential: onComplete ${it.isSuccessful}")

                    if (!it.isSuccessful) {
                        log("signInWithCredential: ${it.exception}")
                        toast("authentication failed")
                    }
                }
    }
}
