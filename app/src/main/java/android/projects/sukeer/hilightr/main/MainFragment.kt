package android.projects.sukeer.hilightr.main

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.projects.sukeer.hilightr.R
import android.projects.sukeer.hilightr.adapters.HighlightListAdapter
import android.projects.sukeer.hilightr.database.sqlitedb.ListItemModel
import android.projects.sukeer.hilightr.edit.EditActivity
import android.projects.sukeer.hilightr.login.LoginActivity
import android.projects.sukeer.hilightr.utility.App
import android.projects.sukeer.hilightr.utility.logDebug
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_main.*
import org.jetbrains.anko.toast

/**
 *
 * Author: Sukeerthi Khadri
 * Created: 1/8/17
 */
class MainFragment : Fragment(), MainView {
    override lateinit var pres: MainPresenter

    private val BUNDLE_RECYCLER_STATE = "${javaClass.simpleName}.recycler.layout"

    private val adapter: HighlightListAdapter by lazy { HighlightListAdapter(mutableListOf(), { startEdit(it) }) }

    private val primaryText by lazy { activity.findViewById(R.id.tv_primary_header) as TextView }
    private val secondaryText by lazy { activity.findViewById(R.id.tv_secondary_header) as TextView }
    private val fab by lazy { activity.findViewById(R.id.fab_create) as FloatingActionButton }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_main, container, false)
        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_filter -> {
                val optionsDialog = OptionsDialogFragment()
                optionsDialog.setTargetFragment(this, MainContract.REQUEST_OPTIONS)
                optionsDialog.show(activity.supportFragmentManager, "filter")
            }
            R.id.action_search -> {
                val searchDialog = SearchDialogFragment()
                searchDialog.setTargetFragment(this, MainContract.REQUEST_SEARCH)
                searchDialog.show(activity.supportFragmentManager, "search")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setup()
        // restore state
        savedInstanceState?.let {
            val recyclerViewState = it.getParcelable<Parcelable>(BUNDLE_RECYCLER_STATE)
            recycler.layoutManager.onRestoreInstanceState(recyclerViewState)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(BUNDLE_RECYCLER_STATE, recycler.layoutManager.onSaveInstanceState())
    }

    override fun onResume() {
        super.onResume()
        App.broadcastManager.registerReceiver(pres.receiver, pres.intentFilter)
        pres.getListItems()
    }

    override fun onPause() {
        super.onPause()
        App.broadcastManager.unregisterReceiver(pres.receiver)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        pres.onActivityResult(requestCode, resultCode, data)
    }

    override fun setup() {
        // setup toolbar
        val toolbar = activity.findViewById(R.id.toolbar) as Toolbar
        val parentActivity = activity as AppCompatActivity
        parentActivity.setSupportActionBar(toolbar)

        val fab = activity.findViewById(R.id.fab_create) as FloatingActionButton
        fab.setOnClickListener {
            startEdit()
        }

        if (recycler.adapter == null) {
            recycler.adapter = adapter
        }
        if (recycler.layoutManager == null) {
            recycler.layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun startEdit(item: HighlightListAdapter.ListItem?) {
        if (item == null) {
            startActivityForResult(Intent(Intent.ACTION_INSERT, null, activity, EditActivity::class.java), MainContract.REQUEST_EDIT)
        } else {
            startActivityForResult(Intent(Intent.ACTION_EDIT, null, activity, EditActivity::class.java).apply {
                putExtras(Bundle().apply {
                    putLong(ListItemModel.COL_ID, item.id)
                    putString(ListItemModel.COL_TITLE, item.title)
                    putString(ListItemModel.COL_MESSAGE, item.message)
                    putString(ListItemModel.COL_PLACE_NAME, item.place)
                    putString(ListItemModel.COL_DATE, item.date)
                    putString(ListItemModel.COL_PERSON_NAME, item.person)
                })
            }, MainContract.REQUEST_EDIT)
        }
    }

    override fun showToast(message: String) {
        activity.toast(message)
    }

    override fun refreshList(items: List<HighlightListAdapter.ListItem>) {
        if (items.isEmpty()) {
            logDebug("Bruh...")
            // show empty layout
        } else {
            logDebug("not bruh...")
            items.let {
                adapter.items.clear()
                adapter.items.addAll(it)
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun refreshHeader(primaryText: String, secondaryText: String) {
        this.primaryText.text = primaryText
        this.secondaryText.text = secondaryText
    }

    override fun finish(intent: Intent?) {
        // start login activity in new task
        startActivity(Intent(activity, LoginActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        })
        activity.finish()
    }

    override fun refreshItem(position: Int, item: HighlightListAdapter.ListItem) {
        if (position in 0..adapter.itemCount) {
            adapter.items[position] = item
            adapter.notifyItemChanged(position)
        }
    }
}