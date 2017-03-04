package android.projects.sukeer.hilightr.main

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.projects.sukeer.hilightr.R
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.Menu
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import org.jetbrains.anko.find

/**
 *
 * Author: Sukeerthi Khadri
 * Created: 3/2/17
 */
class SearchDialogFragment : DialogFragment() {

    companion object {
        const val KEY_SEARCH_QUERY = "key_search_query"
        const val KEY_SEARCH_CATEGORY = "key_search_category"
    }

    private val layoutView by lazy { activity.layoutInflater.inflate(R.layout.dialog_search, null) }
    private val searchContainer by lazy { layoutView.find<LinearLayout>(R.id.container_search_by) }
    private val searchType by lazy { layoutView.find<TextView>(R.id.tv_search_by) }
    private val searchField by lazy { layoutView.find<TextInputLayout>(R.id.search_field) }
    private val searchQuery by lazy { layoutView.find<TextInputEditText>(R.id.et_search_query) }

    private val searchPopup: PopupMenu by lazy {
        PopupMenu(activity, searchContainer).apply {
            menu.add(Menu.NONE, R.id.option_title, Menu.NONE, getString(R.string.title_hint))
            menu.add(Menu.NONE, R.id.option_message, Menu.NONE, getString(R.string.message_hint))
            setOnMenuItemClickListener {
                searchType.text = it.title
                true
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        searchContainer.setOnClickListener {
            searchPopup.show()
        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
                .setTitle("Search...")
                .setView(layoutView)
                .setPositiveButton(R.string.positive_text) { dialog, id ->
                    if (isValid()) {
                        targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, Intent().apply {
                            putExtra(KEY_SEARCH_CATEGORY, searchType.text.toString().toLowerCase())
                            putExtra(KEY_SEARCH_QUERY, searchType.text.toString().toLowerCase())
                        })
                    } else {
                        targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_CANCELED, null)
                    }
                }
                .setNegativeButton(R.string.negative_text) { dialog, id ->
                    targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_CANCELED, null)
                }

        return builder.create()
    }

    private fun isValid(): Boolean {
        searchField.error = if (searchQuery.text.isNullOrBlank()) getString(R.string.error_empty_field) else null
        return searchField.error == null && searchType.text != getString(R.string.subtext_placeholder)
    }

}