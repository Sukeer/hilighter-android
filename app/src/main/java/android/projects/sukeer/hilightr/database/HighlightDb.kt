package android.projects.sukeer.hilightr.database

import android.content.Context
import android.projects.sukeer.hilightr.utility.App
import android.projects.sukeer.hilightr.utility.clear
import android.projects.sukeer.hilightr.utility.toVarArgArray
import org.jetbrains.anko.db.*

/**
 *
 * Author: Sukeerthi Khadri
 * Created: 10/16/16
 */
class HighlightDb(val context: Context = App.instance) {

    private val dbHelper: DbHelper
        get() =
        if (context == App.instance) {
            DbHelper.instance
        } else {
            DbHelper(context)
        }

    // parse row of Cursor into model object
    private val parser = rowParser { _id: Long?, message: String?, person: String?, place: String?, date: Long? ->
        val params = mutableMapOf("_id" to _id, "message" to message, "person" to person, "place" to place, "date" to date)
        HighlightModel(params)
    }

    fun clearTable() = dbHelper.use {
        clear(HighlightConstant.TABLE_NAME)
    }

    fun addHighlight(highlight: HighlightModel) = dbHelper.use {
        with(highlight) {
            insert(HighlightConstant.TABLE_NAME, *map.toVarArgArray())
        }
    }

    fun updateHighlight(highlight: HighlightModel) = dbHelper.use {
        with(highlight) {
            update(HighlightConstant.TABLE_NAME, *map.toVarArgArray())
                    .where("_id = {highlightId}", "highlightId" to _id)
                    .exec()
        }
    }

    fun removeHighlight(id: Long) = dbHelper.use {
        delete(HighlightConstant.TABLE_NAME, "_id = {highlightId}", "highlightId" to id)
    }

    fun getHighlight(id: Long) = dbHelper.use {
        select(HighlightConstant.TABLE_NAME)
                .where("_id = {highlightId}", "highlightId" to id)
                .parseOpt(parser)
    }

    fun getAllHighlights() = dbHelper.use {
        select(HighlightConstant.TABLE_NAME)
                .exec { parseList(parser) }
    }
}