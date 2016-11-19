package android.projects.sukeer.hilightr.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.projects.sukeer.hilightr.R
import android.projects.sukeer.hilightr.utility.log
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 *
 * Author: Sukeerthi Khadri
 * Created: 11/6/16
 */
class LoginFragment() : Fragment(), LoginContract.View {

    companion object {
        val tag = "login"
    }

    private lateinit var pres: LoginContract.Presenter

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater?.inflate(R.layout.fragment_login, container, false)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        pres.setup()
    }

    override fun onStart() {
        super.onStart()
        pres.onStart()
    }

    override fun onStop() {
        super.onStop()
        pres.onStop()
    }

    override fun onDetach() {
        super.onDetach()
    }

    override fun onDestroy() {
        super.onDestroy()
        pres.onDestroy()
    }

    override fun startMain() {
        log("startMain")
    }

    override fun setPresenter(presenter: LoginContract.Presenter) {
        pres = presenter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        log("onActivityResult")
        pres.onActivityResult(requestCode, resultCode, data)
    }

}