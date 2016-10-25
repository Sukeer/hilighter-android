package android.projects.sukeer.hilightr.login

import android.content.Intent
import android.os.Bundle
import android.projects.sukeer.hilightr.R
import android.projects.sukeer.hilightr.database.PersonConstant
import android.projects.sukeer.hilightr.database.PersonDb
import android.projects.sukeer.hilightr.database.PersonModel
import android.projects.sukeer.hilightr.utility.App
import android.projects.sukeer.hilightr.utility.log
import android.support.v7.app.AppCompatActivity
import com.facebook.*
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.toast

/**
 *
 * Author: Sukeerthi Khadri
 * Created: 10/22/16
 */
class LoginActivity : AppCompatActivity() {

    // Facebook
    private lateinit var callbackManager: CallbackManager
    // Firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var authListener: FirebaseAuth.AuthStateListener
    // Database
    private lateinit var personDb: PersonDb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FacebookSdk.sdkInitialize(App.instance.applicationContext)
        personDb = PersonDb()

        setContentView(R.layout.activity_login)

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
        callbackManager = com.facebook.CallbackManager.Factory.create()
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

    private fun initializeFirebase() {
        auth = FirebaseAuth.getInstance()
        authListener = FirebaseAuth.AuthStateListener {
            val currUser = it.currentUser
            // user signed in
            if (currUser != null) {
                if (personDb.getPersonByColumn(PersonConstant.COL_ID, currUser.uid) == null) {
                    personDb.addPerson(PersonModel(currUser.uid, currUser.displayName!!,
                            currUser.email!!, currUser.photoUrl!!.toString()))

                    log("signed_in: ${currUser.uid}\n" +
                            "${currUser.displayName}\n${currUser.photoUrl}\n${currUser.email}")
                }
            }
        }
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
