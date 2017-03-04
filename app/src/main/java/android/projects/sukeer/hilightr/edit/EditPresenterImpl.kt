package android.projects.sukeer.hilightr.edit

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.projects.sukeer.hilightr.database.sqlitedb.HighlightDb
import android.projects.sukeer.hilightr.database.sqlitedb.HighlightModel
import android.projects.sukeer.hilightr.database.sqlitedb.PlaceDb
import android.projects.sukeer.hilightr.database.sqlitedb.PlaceModel
import android.projects.sukeer.hilightr.utility.App
import android.projects.sukeer.hilightr.utility.logDebug
import android.support.v4.app.Fragment
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlacePicker

/**
 *
 * Author: Sukeerthi Khadri
 * Created: 1/8/17
 */
class EditPresenterImpl(override val view: EditView) : EditPresenter {
    init {
        view.setPresenter(this)
    }

    // database
    private val highlightDb by lazy { HighlightDb() }
    private val placeDb by lazy { PlaceDb() }

    // current place chosen by place picker. If null, then place for existing highlight is not being updated
    // this must be non-null for new highlights
    private var chosenPlace: Place? = null

    override val intentFilter: IntentFilter by lazy {
        val filter = IntentFilter(App.ACTION_LOGIN)
        filter.addAction(App.ACTION_LOGOUT)
        filter
    }

    override val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            logDebug(intent.action)
        }
    }

    override fun delete(id: Long) {
        val deletions = highlightDb.removeItem("_id", id)
        when (deletions) {
            0 -> view.showToast("Failed to delete highlight")
            1 -> {
                // successful deletion
                view.showToast("Deleted highlight")
                view.finish()
            }
            else -> view.showToast("Deleted $deletions highlights")
        }
    }

    override fun save(id: Long?, title: String, message: String) {
        if (savePlace()) {
            if (id == null) {
                // new highlight
                if (chosenPlace != null) {
                    val nHighlight = HighlightModel(title, message, App.currPerson?._id!!, chosenPlace?.id!!, System.currentTimeMillis() / 1000, System.currentTimeMillis() / 1000)
                    if (highlightDb.addItem(nHighlight) != -1L) {
                        view.showToast("Added highlight")
                        view.finish()
                    } else {
                        view.showToast("Error adding highlight")
                    }
                } else {
                    view.showToast("Place not chosen for new highlight")
                }
            } else {
                // existing highlight
                val existing = highlightDb.getItem("_id", id)
                if (existing != null) {
                    val map = existing.map
                    chosenPlace?.let { map["place"] = it.id }
                    // save highlight
                    if (highlightDb.updateItem(HighlightModel(map)) > 0) {
                        view.showToast("Updated highlight")
                        view.finish()
                    } else {
                        view.showToast("Could not update highlight")
                    }
                } else {
                    view.showToast("Highlight does not exist. Create a new one.")
                }
            }
        } else {
            view.showToast("Error saving place")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            EditContract.REQUEST_PLACE_PICKER -> {
                if (resultCode == Activity.RESULT_OK) {
                    chosenPlace = PlacePicker.getPlace((view as Fragment).activity, data)
                    view.show(place = "${chosenPlace?.name}")
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    view.showToast("Place was not chosen")
                }
            }
        }
    }

    private fun savePlace(): Boolean {
        chosenPlace?.let {
            if (placeDb.getItem("_id", it.id) == null) {
                // return whether place was added successfully
                return (placeDb.addItem(PlaceModel(it.id, "${it.name}", "${it.address}", "${it.phoneNumber}", it.websiteUri?.toString() ?: "",
                        it.latLng.latitude, it.latLng.longitude, it.placeTypes[0], it.priceLevel, it.rating, System.currentTimeMillis() / 1000)) != -1L)
            }
        }
        return true
    }

}
