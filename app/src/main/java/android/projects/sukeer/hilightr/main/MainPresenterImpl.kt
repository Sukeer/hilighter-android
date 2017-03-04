package android.projects.sukeer.hilightr.main

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.projects.sukeer.hilightr.adapters.HighlightListAdapter
import android.projects.sukeer.hilightr.database.sqlitedb.ListItemModel
import android.projects.sukeer.hilightr.database.sqlitedb.ListItemSqlite
import android.projects.sukeer.hilightr.main.MainContract.INTENT_KEY_POSITION
import android.projects.sukeer.hilightr.utility.App
import android.projects.sukeer.hilightr.utility.logDebug

/**
 *
 * Author: Sukeerthi Khadri
 * Created: 2/19/17
 */
class MainPresenterImpl(override val view: MainView) : MainPresenter {
    init {
        view.setPresenter(this)
    }

    private val listItemDb by lazy { ListItemSqlite() }

    override val currQuery: Query = Query()

    override val intentFilter: IntentFilter by lazy {
        val filter = IntentFilter(App.ACTION_LOGIN)
        filter.addAction(App.ACTION_LOGOUT)
        filter
    }

    override val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                App.ACTION_LOGOUT -> {
                    view.finish()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                MainContract.REQUEST_EDIT -> {
                    data?.extras?.let {
                        logDebug("Manyo")
                        val editedItem = HighlightListAdapter.ListItem(it.getLong(ListItemModel.COL_ID),
                                it.getString(ListItemModel.COL_TITLE), it.getString(ListItemModel.COL_MESSAGE),
                                it.getString(ListItemModel.COL_PLACE_NAME), it.getString(ListItemModel.COL_PERSON_NAME),
                                it.getString(ListItemModel.COL_DATE))
                        val position = it.getInt(INTENT_KEY_POSITION)
                        view.refreshItem(position, editedItem)
                    }
                }
                MainContract.REQUEST_OPTIONS -> {
                    data?.extras?.let {
                        when (it.getString(OptionsDialogFragment.OPTION_FILTER_TYPE)) {
                            OptionsDialogFragment.FILTER_TYPE_PLACE -> {
                                currQuery.filterKey = MainContract.FILTER_PLACE
                                currQuery.sort = MainContract.FILTER_PLACE
                            }
                            OptionsDialogFragment.FILTER_TYPE_PERSON -> {
                                currQuery.filterKey = MainContract.FILTER_PERSON
                                currQuery.sort = MainContract.FILTER_PERSON
                            }
                        }
                        currQuery.filterValue = it.getString(OptionsDialogFragment.OPTION_FILTER_VALUE)
                        when (it.getString(OptionsDialogFragment.OPTION_SORT_VALUE)) {
                            OptionsDialogFragment.SORT_ASCENDING -> currQuery.sortValue = MainContract.SORT_ASC
                            OptionsDialogFragment.SORT_DESCENDING -> currQuery.sortValue = MainContract.SORT_DES
                        }
                        getListItems()
                    }
                }
                MainContract.REQUEST_SEARCH -> {
                    data?.extras?.let {
                        val searchKey = it.getString(SearchDialogFragment.KEY_SEARCH_CATEGORY)
                        val searchValue = it.getString(SearchDialogFragment.KEY_SEARCH_QUERY)
                        getListItems(Query(searchKey = searchKey, searchValue = searchValue))
                    }
                }
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            when (requestCode) {
                MainContract.REQUEST_EDIT -> {
                    logDebug("cancelled edit")
                }
                MainContract.REQUEST_OPTIONS -> {
                    logDebug("cancelled options")
                }
                MainContract.REQUEST_SEARCH -> {
                    view.showToast("No results, search again")
                }
            }
        }
    }

    override fun getListItems(query: Query) {
        listItemDb.getListItems(query) {
            view.refreshHeader("Filter by : ${query.filterKey}", "${it.size} items")
            view.refreshList(it)
        }
    }

}