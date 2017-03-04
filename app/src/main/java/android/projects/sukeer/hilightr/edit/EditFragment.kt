package android.projects.sukeer.hilightr.edit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.projects.sukeer.hilightr.R
import android.projects.sukeer.hilightr.database.sqlitedb.ListItemModel
import android.projects.sukeer.hilightr.edit.EditContract.EDIT_MODE_CREATE
import android.projects.sukeer.hilightr.edit.EditContract.EDIT_MODE_EDIT
import android.projects.sukeer.hilightr.edit.EditContract.REQUEST_PLACE_PICKER
import android.projects.sukeer.hilightr.utility.App
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.support.v4.app.NavUtils
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.SpannableStringBuilder
import android.view.*
import com.google.android.gms.location.places.ui.PlacePicker
import kotlinx.android.synthetic.main.fragment_edit.*
import org.jetbrains.anko.toast

/**
 *
 * Author: Sukeerthi Khadri
 * Created: 1/8/17
 */
class EditFragment : Fragment(), EditView {

    override lateinit var pres: EditPresenter

    private val mode by lazy {
        when (arguments.getString(EditContract.KEY_EDIT_MODE)) {
            Intent.ACTION_INSERT -> EditContract.EDIT_MODE_CREATE
            Intent.ACTION_EDIT -> EditContract.EDIT_MODE_EDIT
            else -> throw RuntimeException("Not a valid intent action edit mode.")
        }
    }

    // views
    private val title: TextInputEditText by lazy { activity.findViewById(R.id.title) as TextInputEditText }
    private val titleField: TextInputLayout by lazy { activity.findViewById(R.id.title_field) as TextInputLayout }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val root = inflater.inflate(R.layout.fragment_edit, container, false)
        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_edit, menu)
        when (mode) {
            EditContract.EDIT_MODE_CREATE -> menu.removeItem(R.id.action_delete)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                startActivity(NavUtils.getParentActivityIntent(activity).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                })
                return true
            }
            R.id.action_done -> {
                if (validInput()) {
                    when (mode) {
                        EDIT_MODE_CREATE -> pres.save(title = title.text.toString(), message = message.text.toString())
                        EDIT_MODE_EDIT -> pres.save(arguments.getLong(ListItemModel.COL_ID), title.text.toString(), message.text.toString())
                        else -> showToast("Unknown mode")
                    }
                    return true
                }
            }
            R.id.action_delete -> {
                if (mode == EDIT_MODE_EDIT) {
                    pres.delete(arguments.getLong(ListItemModel.COL_ID))
                    return true
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setup()
    }

    override fun onResume() {
        super.onResume()
        App.broadcastManager.registerReceiver(pres.receiver, pres.intentFilter)
    }

    override fun onPause() {
        super.onPause()
        App.broadcastManager.unregisterReceiver(pres.receiver)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //  pass on result to presenter here, b.c. we need to store state (Place selected) in the presenter. View should only hold view states
        pres.onActivityResult(requestCode, resultCode, data)
    }

    private fun validInput(): Boolean {
        titleField.error = if (title.text.isNullOrBlank()) getString(R.string.error_empty_field) else null
        message_field.error = if (message.text.isNullOrBlank()) getString(R.string.error_empty_field) else null

        return !place.text.isNullOrBlank() && message_field.error == null && titleField.error == null
    }

    override fun show(title: String?, place: String?, description: String?) {
        this.title.text = SpannableStringBuilder(title ?: this.title.text)
        this.place.text = SpannableStringBuilder(place ?: this.place.text)
        this.message.text = SpannableStringBuilder(description ?: this.message.text)
    }

    override fun setup() {
        // setup toolbar
        val toolbar: Toolbar = activity.findViewById(R.id.toolbar) as Toolbar
        val parentActivity = activity as AppCompatActivity
        parentActivity.setSupportActionBar(toolbar)
        parentActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)

        when (mode) {
            EDIT_MODE_CREATE -> {
                parentActivity.supportActionBar?.title = getString(R.string.toolbar_create)
            }
            EDIT_MODE_EDIT -> {
                parentActivity.supportActionBar?.title = getString(R.string.toolbar_edit)
                show(arguments.getString(ListItemModel.COL_TITLE),
                        arguments.getString(ListItemModel.COL_PLACE_NAME),
                        arguments.getString(ListItemModel.COL_MESSAGE))
            }
        }
        place_container.setOnClickListener {
            val builder = PlacePicker.IntentBuilder()
            startActivityForResult(builder.build(parentActivity), REQUEST_PLACE_PICKER)
        }

    }

    override fun showToast(message: String) {
        activity.toast(message)
    }

    override fun finish(intent: Intent?) {
        activity.setResult(Activity.RESULT_OK, Intent("Done"))
        activity.finish()
    }
}