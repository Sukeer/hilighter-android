package android.projects.sukeer.hilightr.database.sqlitedb

import android.content.Context
import android.projects.sukeer.hilightr.adapters.HighlightListAdapter
import android.projects.sukeer.hilightr.utility.App
import org.jetbrains.anko.db.RowParser
import org.jetbrains.anko.db.parseList
import org.jetbrains.anko.db.rowParser
import java.text.SimpleDateFormat
import java.util.*

/**
 *
 * Author: Sukeerthi Khadri
 * Created: 2/25/17
 */
class ListItemDb(override val context: Context = App.instance) : DbDao<ListItemModel>() {
    override val parser: RowParser<ListItemModel> = rowParser { _id: Long, title: String, message: String,
                                                                place: String, person: String, date_created: Long ->
        val dateFormat = SimpleDateFormat("MM'/'dd'/'yyyy", Locale.US)
        ListItemModel(_id, title, message, place, person, date_created)
    }

    override val constants: TableConstant = ListItemModel.constants

    override fun addItem(item: ListItemModel): Long {
        throw UnsupportedOperationException()
    }

    override fun removeItem(column: String, value: Any): Int {
        throw UnsupportedOperationException()
    }

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
        query(constants.TABLE_NAME, null, if (where == "") null else where, if (where == "") null else whereVals.toTypedArray(), null, null, sort?.let { "$it $sortValue" }, null)
                .parseList(rowParser { _id: Long, title: String, message: String, place: String, person: String, date_created: Long ->
                    val dateFormat = SimpleDateFormat("MM'/'dd'/'yyyy", Locale.US)
                    HighlightListAdapter.ListItem(_id, title, message, place, person, dateFormat.format(Date(date_created * 1000)))
                })
    }
}