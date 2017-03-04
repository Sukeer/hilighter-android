package android.projects.sukeer.hilightr.database.sqlitedb

import android.content.Context
import android.projects.sukeer.hilightr.adapters.HighlightListAdapter
import android.projects.sukeer.hilightr.utility.App
import org.jetbrains.anko.db.parseList
import org.jetbrains.anko.db.rowParser
import java.text.SimpleDateFormat
import java.util.*

/**
 *
 * Author: Sukeerthi Khadri
 * Created: 10/16/16
 */
class HighlightDb(override val context: Context = App.instance) : DbDao<HighlightModel>() {

    // parse row of Cursor into model object
    override val parser = rowParser { _id: Long, title: String, message: String, person: String, place: String, date_created: Long, date_updated: Long ->
        val params = mutableMapOf("_id" to _id, "title" to title, "message" to message, "person" to person,
                "place" to place, "date_created" to date_created, "date_updated" to date_updated)
        HighlightModel(params)
    }

    override val constants = HighlightModel.constants

    fun getHighlightListItems(filterKey: String? = null, filterValue: String? = null,
                              sort: String? = null, sortValue: String? = null,
                              searchKey: String? = null, searchValue: String? = null) = dbHelper.use {

        val whereVals = mutableListOf<String>()
        var where = ""
        if (filterKey != null) {
            where += filterKey.let {
                filterValue?.let { whereVals.add(it) }
                "$it = ?"
            }
        }
        if (searchKey != null) {
            where += searchKey.let {
                searchValue?.let { whereVals.add("%$it%") }
                " ${if (filterKey != null) "AND " else ""}$it LIKE ?"
            }
        }
        query("list_items", null, if (where == "") null else where, if (where == "") null else whereVals.toTypedArray(), null, null, sort?.let { "$it $sortValue" }, null)
                .parseList(rowParser { _id: Long, title: String, message: String, place: String, person: String, date_created: Long ->
                    val dateFormat = SimpleDateFormat("MM'/'dd'/'yyyy", Locale.US)
                    HighlightListAdapter.ListItem(_id, title, message, place, person, dateFormat.format(Date(date_created * 1000)))
                })
    }

}