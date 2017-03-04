package android.projects.sukeer.hilightr.main

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.projects.sukeer.hilightr.R
import android.projects.sukeer.hilightr.database.sqlitedb.PersonSqlite
import android.projects.sukeer.hilightr.database.sqlitedb.PlaceSqlite
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.PopupMenu
import android.view.Menu
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView

/**
 *
 * Author: Sukeerthi Khadri
 * Created: 2/20/17
 */
class OptionsDialogFragment : DialogFragment() {

    companion object {
        val OPTION_FILTER_TYPE = "${OptionsDialogFragment::class.java.simpleName}.intent.extra.FILTER_TYPE"
        val OPTION_FILTER_VALUE = "${OptionsDialogFragment::class.java.simpleName}.intent.extra.FILTER_VALUE"
        val OPTION_SORT_VALUE = "${OptionsDialogFragment::class.java.simpleName}.intent.extra.SORT_VALUE"

        const val FILTER_TYPE_PLACE = "Place"
        const val FILTER_TYPE_PERSON = "Person"

        const val SORT_ASCENDING = "Ascending"
        const val SORT_DESCENDING = "Descending"
    }

    private val placeDb by lazy { PlaceSqlite() }
    private val personDb: PersonSqlite by lazy { PersonSqlite() }

    private lateinit var places: List<String>
    private lateinit var people: List<String>

    private val layoutView: View by lazy { activity.layoutInflater.inflate(R.layout.dialog_filter, null) }

    lateinit var filterTypeContainer: LinearLayout
    lateinit var filterValueContainer: LinearLayout
    lateinit var sortContainer: LinearLayout

    private val filterTypeText by lazy { layoutView.findViewById(R.id.tv_filter_by) as TextView }
    private val filterValueText by lazy { layoutView.findViewById(R.id.tv_filter_value) as TextView }
    private val sortText by lazy { layoutView.findViewById(R.id.tv_sort) as TextView }

    private val filterByMenu: PopupMenu by lazy {
        PopupMenu(activity, filterTypeContainer).apply {
            menuInflater.inflate(R.menu.menu_dialog_filter, menu)
            setOnMenuItemClickListener {
                filterTypeText.text = it.title
                refreshMenu(filterValueMenu.menu, when (it.itemId) {
                    R.id.option_person -> people
                    R.id.option_place -> places
                    else -> listOf()
                })
                true
            }
        }
    }

    private val filterValueMenu: PopupMenu by lazy {
        PopupMenu(activity, filterValueContainer).apply {
            setOnMenuItemClickListener {
                filterValueText.text = it.title
                true
            }
        }
    }

    private val sortByMenu: PopupMenu by lazy {
        PopupMenu(activity, sortContainer).apply {
            menuInflater.inflate(R.menu.menu_dialog_sort, menu)
            setOnMenuItemClickListener {
                sortText.text = it.title
                true
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        filterTypeContainer = (layoutView.findViewById(R.id.container_filter_type) as LinearLayout).apply {
            setOnClickListener {
                filterByMenu.show()
            }
        }
        filterValueContainer = (layoutView.findViewById(R.id.container_filter_value) as LinearLayout).apply {
            setOnClickListener {
                filterValueMenu.show()
            }
        }
        sortContainer = (layoutView.findViewById(R.id.container_sort) as LinearLayout).apply {
            setOnClickListener {
                sortByMenu.show()
            }
        }

        placeDb.getAllItems {
            places = it.map { it.name }
        }
        personDb.getAllItems {
            people = it.map { it.name }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
                .setTitle("Choose filter options")
                .setView(layoutView)
                .setPositiveButton(R.string.positive_text) { dialog, id ->
                    targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_OK, Intent().apply {
                        val extra = Bundle()
                        extra.putString(OPTION_FILTER_TYPE, filterTypeText.text.toString())
                        extra.putString(OPTION_FILTER_VALUE, filterValueText.text.toString())
                        extra.putString(OPTION_SORT_VALUE, sortText.text.toString())
                        putExtras(extra)
                    })
                }
                .setNegativeButton(R.string.negative_text) { dialog, id ->
                    targetFragment?.onActivityResult(targetRequestCode, Activity.RESULT_CANCELED, null)
                }

        return builder.create()
    }

    private fun refreshMenu(menu: Menu, items: List<String>) {
        menu.clear()
        items.forEach { menu.add(Menu.NONE, Menu.NONE, Menu.NONE, it) }
    }
}