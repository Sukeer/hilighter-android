package android.projects.sukeer.hilightr.database.sqlitedb

import android.content.Context
import android.projects.sukeer.hilightr.utility.App
import org.jetbrains.anko.db.rowParser

/**
 *
 * Author: Sukeerthi Khadri
 * Created: 10/16/16
 */
class HighlightDb(override val context: Context = App.instance) : DbDao<HighlightModel>() {

    // parse row of Cursor into model object
    override val parser = rowParser { _id: Long?, title: String?, message: String?, person: String?, place: String?, date: Long? ->
        val params = mutableMapOf("_id" to _id, "title" to title, "message" to message, "person" to person, "place" to place, "date" to date)
        HighlightModel(params)
    }

    override val constants = HighlightModel.constants
}