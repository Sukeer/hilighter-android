package android.projects.sukeer.hilightr.main

import android.projects.sukeer.hilightr.BasePresenter
import android.projects.sukeer.hilightr.BaseView
import android.projects.sukeer.hilightr.adapters.HighlightListAdapter
import android.projects.sukeer.hilightr.database.sqlitedb.ListItemModel

/**
 *
 * Author: Sukeerthi Khadri
 * Created: 1/8/17
 */
object MainContract {
    // query option constants
    val FILTER_PLACE = ListItemModel.COL_PLACE_NAME
    val FILTER_PERSON = ListItemModel.COL_PERSON_NAME
    val FILTER_DATE = ListItemModel.COL_DATE
    val SORT_ASC = "ASC"
    val SORT_DES = "DESC"
    val SEARCH_TITLE = ListItemModel.COL_TITLE
    val SEARCH_MESSAGE = ListItemModel.COL_MESSAGE

    val REQUEST_EDIT = 0
    val REQUEST_OPTIONS = 1
    val REQUEST_SEARCH = 2

    val INTENT_KEY_POSITION = "position"
}

data class Query(var filterKey: String? = null, var filterValue: String? = null,
                 var sort: String? = null, var sortValue: String? = null,
                 var searchKey: String? = null, var searchValue: String? = null)

interface MainPresenter : BasePresenter {
    val currQuery: Query

    fun getListItems(query: Query = currQuery)
}

interface MainView : BaseView<MainPresenter> {
    fun refreshItem(position: Int, item: HighlightListAdapter.ListItem)
    fun startEdit(item: HighlightListAdapter.ListItem? = null)
    fun refreshList(items: List<HighlightListAdapter.ListItem>)
    fun refreshHeader(primaryText: String, secondaryText: String)
}