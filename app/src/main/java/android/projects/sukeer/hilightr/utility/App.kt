package android.projects.sukeer.hilightr.utility

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.projects.sukeer.hilightr.database.sqlitedb.PersonDb
import android.projects.sukeer.hilightr.database.sqlitedb.PersonModel
import com.facebook.*
import com.facebook.appevents.AppEventsLogger
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth

/**
 *
 * Author: Sukeerthi Khadri
 * Created: 10/9/16
 */
class App : Application() {

    // Facebook
    lateinit var callbackManager: CallbackManager
    val facebookCallback = object : FacebookCallback<LoginResult> {
        override fun onError(error: FacebookException?) {
            log(error.toString())
        }

        override fun onCancel() {
            log("onCancel")
        }

        override fun onSuccess(result: LoginResult) {
            log("onSuccess: ${result.accessToken.toString()}")
            handleFacebookAccessToken(result.accessToken)
        }
    }
    lateinit var accessTokenTracker: AccessTokenTracker

    // Firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var authListener: FirebaseAuth.AuthStateListener

    companion object {
        lateinit var instance: App
            private set
        val globalSharedPrefFile = "global_shared_pref"
    }

    private lateinit var sharedPref: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        instance = this
        sharedPref = getSharedPreferences(globalSharedPrefFile, Context.MODE_PRIVATE)
        setupFacebook()
        setupFirebase()
    }

    private fun setupFirebase() {
        auth = FirebaseAuth.getInstance()
        authListener = FirebaseAuth.AuthStateListener {
            val user = it.currentUser
            if (user != null) {
                log("authstatechange: signed_in ${user.uid}\n" +
                        "${user.displayName}\n${user.photoUrl}\n${user.email}")

                if (sharedPref.getBoolean("firstTimeLogin", true)) {
                    log("Add person")
                    val db = PersonDb()
                    db.addItem(PersonModel(user.uid, user.displayName!!, user.email!!, user.photoUrl.toString())) != -1L
                    sharedPref.edit().putBoolean("firstTimeLogin", false).apply()
                    // take user to main activity

                } else {
                    log("Don't do anything, user is already added to db and not needing to be thrown to mainactivity")
                    // take user to main activity..maybe
                }
            } else {
                log("authstatechange: signed_out")
                // throw user into loginactivity
            }
        }
    }

    private fun setupFacebook() {
        FacebookSdk.sdkInitialize(this)
        AppEventsLogger.activateApp(instance)
        callbackManager = CallbackManager.Factory.create()
        accessTokenTracker = object : AccessTokenTracker() {
            override fun onCurrentAccessTokenChanged(oldAccessToken: AccessToken?, currentAccessToken: AccessToken?) {
                log("Old: ${oldAccessToken?.userId}\nCurr: ${currentAccessToken?.userId}")
                if (currentAccessToken == null) {
                    auth.signOut()
                }
            }
        }
        accessTokenTracker.startTracking()
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        log("handleFacebookAccessToken $token")

        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
                .addOnCompleteListener() {
                    log("signInWithCredential: onComplete ${it.isSuccessful}")
                    if (!it.isSuccessful) {
                        log("signInWithCredential: ${it.exception}")
                    }
                }
    }

    fun onStart() {
        auth.addAuthStateListener(authListener)
        if (!accessTokenTracker.isTracking) {
            accessTokenTracker.startTracking()
        }
    }

    fun onStop() {
        auth.removeAuthStateListener(authListener)
        if (accessTokenTracker.isTracking) {
            accessTokenTracker.stopTracking()
        }
    }
}