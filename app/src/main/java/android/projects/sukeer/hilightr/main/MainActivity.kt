package android.projects.sukeer.hilightr.main

import android.os.Bundle
import android.projects.sukeer.hilightr.R
import android.support.v7.app.AppCompatActivity

/**
 *
 * Author: Sukeerthi Khadri
 * Created: 1/8/17
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var mainFrag = supportFragmentManager.findFragmentById(R.id.container) as MainFragment?
        if (mainFrag == null) {
            mainFrag = MainFragment()

            MainPresenterImpl(mainFrag)
            supportFragmentManager.beginTransaction().add(R.id.container, mainFrag).commit()
        }
    }
}
