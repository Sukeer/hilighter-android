package android.projects.sukeer.hilightr.database

import android.content.Context
import android.projects.sukeer.hilightr.utility.App
import android.projects.sukeer.hilightr.utility.clear
import android.projects.sukeer.hilightr.utility.toVarArgArray
import org.jetbrains.anko.db.*

/**
 *
 * Author: Sukeerthi Khadri
 * Created: 10/25/16
 */
abstract class DbDao<Model : DbModel> {

    abstract val parser: RowParser<Model>
    abstract val context: Context
    abstract val constants: TableConstant

    private val dbHelper: DbHelper
        get() = if (context == App.instance) DbHelper.instance else DbHelper(context)

    fun clearTable() = dbHelper.use {
        clear(constants.TABLE_NAME)
    }

    fun addItem(item: Model) = dbHelper.use {
        with(item) {
            insert(constants.TABLE_NAME, *map.toVarArgArray())
        }
    }

    fun updateItem(updatedItem: Model) = dbHelper.use {
        with(updatedItem) {
            update(constants.TABLE_NAME, *map.toVarArgArray())
                    .where("_id = {id}", "id" to _id)
                    .exec()
        }
    }

    fun removeItem(column: String, value: Any) = dbHelper.use {
        delete(constants.TABLE_NAME, "$column = {value}", "value" to value)
    }

    fun getItem(column: String, value: Any) = dbHelper.use {
        select(constants.TABLE_NAME)
                .where("$column = {value}", "value" to value)
                .parseOpt(parser)
    }

    fun getAllItems() = dbHelper.use {
        select(constants.TABLE_NAME)
                .exec { parseList(parser) }
    }

}