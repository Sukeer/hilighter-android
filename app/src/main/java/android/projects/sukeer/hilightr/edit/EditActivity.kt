package android.projects.sukeer.hilightr.edit

import android.os.Bundle
import android.projects.sukeer.hilightr.R
import android.support.v7.app.AppCompatActivity

class EditActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        var editFrag = supportFragmentManager.findFragmentById(R.id.container) as EditFragment?
        if (editFrag == null) {
            editFrag = EditFragment()

            // pass intent data to fragment args
            val args = intent.extras ?: Bundle()
            args.putString(EditContract.KEY_EDIT_MODE, intent.action)
            editFrag.arguments = args

            EditPresenterImpl(editFrag)
            supportFragmentManager.beginTransaction().add(R.id.container, editFrag).commit()
        }
    }
}
