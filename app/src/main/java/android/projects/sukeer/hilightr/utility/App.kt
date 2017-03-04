package android.projects.sukeer.hilightr.utility

import android.app.Application
import android.content.Intent
import android.projects.sukeer.hilightr.database.sqlitedb.BaseDbTaskListener
import android.projects.sukeer.hilightr.database.sqlitedb.DbTask
import android.projects.sukeer.hilightr.database.sqlitedb.PersonDb
import android.projects.sukeer.hilightr.database.sqlitedb.PersonModel
import android.support.v4.content.LocalBroadcastManager
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

    companion object {
        lateinit var instance: App
            private set
        val broadcastManager: LocalBroadcastManager by lazy { LocalBroadcastManager.getInstance(instance) }
        val ACTION_LOGIN = "android.projects.sukeer.hilightr.action.ACTION_LOGIN"
        val ACTION_LOGOUT = "android.projects.sukeer.hilightr.action.ACTION_LOGOUT"
        var currPerson: PersonModel? = null
            private set
    }

    // Firebase
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val authListener: FirebaseAuth.AuthStateListener = FirebaseAuth.AuthStateListener {
        val user = it.currentUser
        if (user != null) {
            logDebug("authstatechange: signed_in ${user.uid}\n${user.displayName}\n${user.photoUrl}\n${user.email}")
            // set new current user
            if (currPerson == null || currPerson?._id != user.uid) {
                currPerson = PersonModel(user.uid, user.displayName!!, user.email!!, user.photoUrl.toString(), System.currentTimeMillis() / 1000)
            }
            // add user to DB
            val personDb = PersonDb(this)
            val insertDbTask = DbTask(personDb, object : BaseDbTaskListener() {
                override fun onPostExecute(result: List<Any>) {
                    val insertionId = result[0] as Long
                    if (insertionId == -1L) {
                        logDebug("User probably already exists")
                    }
                }
            })
            currPerson?.let { insertDbTask.execute({ personDb.addItem(it) }) }

            val loginIntent = Intent(ACTION_LOGIN)
            broadcastManager.sendBroadcast(loginIntent)
        } else {
            val logoutIntent = Intent(ACTION_LOGOUT)
            broadcastManager.sendBroadcast(logoutIntent)
        }
    }

    // Facebook
    val callbackManager: CallbackManager by lazy { CallbackManager.Factory.create() }
    val facebookCallback = object : FacebookCallback<LoginResult> {
        override fun onError(error: FacebookException?) {
            logDebug("Facebook callback: onError ${error.toString()}")
        }

        override fun onCancel() {
            logDebug("Facebook callback: onCancel")
        }

        override fun onSuccess(result: LoginResult) {
            logDebug("Facebook callback : onSuccess: ${result.accessToken}")
            handleFacebookAccessToken(result.accessToken)
        }
    }
    private val accessTokenTracker: AccessTokenTracker by lazy {
        object : AccessTokenTracker() {
            override fun onCurrentAccessTokenChanged(oldAccessToken: AccessToken?, currentAccessToken: AccessToken?) {
                logDebug("Old: ${oldAccessToken?.userId}\nCurr: ${currentAccessToken?.userId}")
                if (currentAccessToken == null) {
                    // Already signed out of FB, so sign out Firebase user
                    auth.signOut()
                }
            }
        }
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        // login to Firebase using FB credential
        auth.signInWithCredential(credential)
                .addOnCompleteListener() {
                    if (!it.isSuccessful) {
                        logDebug("signInWithCredential: ${it.exception}")
                    } else {
                        logDebug("signInWithCredential: success ${it.isSuccessful}")
                    }
                }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        deleteDatabase("highlight.db")
        setupFirebase()
        setupFacebook()
    }

    private fun setupFirebase() {
        auth.addAuthStateListener(authListener)
    }

    private fun setupFacebook() {
        FacebookSdk.sdkInitialize(this)
        AppEventsLogger.activateApp(instance)
        accessTokenTracker.startTracking()
    }
//    fun onStop() {
//        auth.removeAuthStateListener(authListener)
//        if (accessTokenTracker.isTracking) {
//            accessTokenTracker.stopTracking()
//        }
//    }
}