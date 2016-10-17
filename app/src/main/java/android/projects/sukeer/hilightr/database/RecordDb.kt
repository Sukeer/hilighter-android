package android.projects.sukeer.hilightr.database

import android.content.Context
import android.projects.sukeer.hilightr.utility.App
import org.jetbrains.anko.db.*

/**
 *
 * Author: Sukeerthi Khadri
 * Created: 10/16/16
 */
class RecordDb(context: Context = App.instance) {

    private val dbHelper: DbHelper = DbHelper(context)

    // parse row of Cursor into model object
    private val parser = rowParser { _id: Long, person: String, place: String, highlight: Long ->
        RecordModel(person, place, highlight)
    }

    fun clearTable() = dbHelper.use {
        clear(RecordConstant.TABLE_NAME)
    }

    fun addRecord(record: RecordModel) = dbHelper.use {
        with(record) {
            insert(RecordConstant.TABLE_NAME, *map.toVarArgArray())
        }
    }

    fun updateRecord(record: RecordModel) = dbHelper.use {
        with(record) {
            update(RecordConstant.TABLE_NAME, *map.toVarArgArray())
                    .where("_id = {recordId}", "recordId" to _id)
                    .exec()
        }
    }

    fun removeRecord(id: Long) = dbHelper.use {
        delete(RecordConstant.TABLE_NAME, "_id = {recordId}", "recordId" to id)
    }

    fun getRecord(id: Long) = dbHelper.use {
        select(RecordConstant.TABLE_NAME)
                .where("_id = {recordId}", "recordId" to id)
                .parseOpt(parser)
    }

    fun getAllRecords() = dbHelper.use {
        select(RecordConstant.TABLE_NAME)
                .exec { parseList(parser) }
    }
}