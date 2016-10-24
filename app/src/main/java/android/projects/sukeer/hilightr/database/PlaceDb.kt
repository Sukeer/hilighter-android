package android.projects.sukeer.hilightr.database

import android.content.Context
import android.projects.sukeer.hilightr.utility.App
import android.projects.sukeer.hilightr.utility.clear
import android.projects.sukeer.hilightr.utility.toVarArgArray
import org.jetbrains.anko.db.*

/**
 *
 * Author: Sukeerthi Khadri
 * Created: 10/10/16
 */
class PlaceDb(val context: Context = App.instance) {

    private val dbHelper: DbHelper
        get() =
        if (context == App.instance) {
            DbHelper.instance
        } else {
            DbHelper(context)
        }

    // parse row of Cursor into model object
    private val parser = rowParser { _id: String, name: String, address: String, phone: String,
                                     website: String, latitude: Double, longitude: Double, type: Int, price: Int, rating: Int ->
        PlaceModel(_id, name, address, phone, website, latitude, longitude, type, price, rating)
    }

    fun clearTable() = dbHelper.use {
        clear(PlaceConstant.TABLE_NAME)
    }

    fun addPlace(place: PlaceModel) = dbHelper.use {
        with(place) {
            insert(PlaceConstant.TABLE_NAME, *map.toVarArgArray())
        }
    }

    fun updatePlace(place: PlaceModel) = dbHelper.use {
        with(place) {
            update(PlaceConstant.TABLE_NAME, *map.toVarArgArray())
                    .where("_id = {placeId}", "placeId" to _id)
                    .exec()
        }
    }

    fun removePlace(id: String) = dbHelper.use {
        delete(PlaceConstant.TABLE_NAME, "_id = {placeId}", "placeId" to id)
    }

    fun getPlace(id: String) = dbHelper.use {
        select(PlaceConstant.TABLE_NAME).where("_id = {placeId}", "placeId" to id).parseOpt(parser)
    }

    fun getAllPlaces() = dbHelper.use {
        select(PlaceConstant.TABLE_NAME).exec { parseList(parser) }
    }
}