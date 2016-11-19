package android.projects.sukeer.hilightr.database.sqlitedb

import android.content.Context
import android.projects.sukeer.hilightr.utility.App
import org.jetbrains.anko.db.rowParser

/**
 *
 * Author: Sukeerthi Khadri
 * Created: 10/16/16
 */
class RecordDb(override val context: Context = App.instance) : DbDao<RecordModel>() {

    // parse row of Cursor into model object
    override val parser = rowParser { _id: Long?, person: String?, place: String?, highlight: Long? ->
        val params = mutableMapOf("_id" to _id, "person" to person, "place" to place, "highlight" to highlight)
        RecordModel(params)
    }

    override val constants = RecordModel.constants
}