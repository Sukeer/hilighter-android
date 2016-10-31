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
abstract class DbDao<T : DbModel> {

    abstract val parser: RowParser<T>
    abstract val context: Context
    abstract val constants: TableConstant

    private val dbHelper: DbHelper
        get() = if (context == App.instance) DbHelper.instance else DbHelper(context)

    fun clearTable() = dbHelper.use {
        clear(constants.TABLE_NAME)
    }

    fun addItem(item: T) = dbHelper.use {
        with(item) {
            insert(constants.TABLE_NAME, *map.toVarArgArray())
        }
    }

    fun updateItem(updatedItem: T) = dbHelper.use {
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

/*
This class is generic because if we didn't use generics and simply specified DbModel as the type for the row parser and
the other pieces of code where an instance of DbModel (including its subclasses) is expected, then we could pass two or
more different instances of DbModel to the row parser and methods of this class. For example,

parser: RowParser<RecordModel>
val item: PersonModel
addItem(item)

Here we can insert items that are not compatible with the parser which will cause error upon retrieval due to the wrong
item being inserted and parsed.
 */