package android.projects.sukeer.hilightr.database.sqlitedb

import android.projects.sukeer.hilightr.adapters.HighlightListAdapter
import android.projects.sukeer.hilightr.main.Query

/**
 *
 * Author: Sukeerthi Khadri
 * Created: 2/20/17
 */
abstract class SqliteModel<T : DbModel>(val db: DbDao<T>) {

    fun insertItem(item: T, fn: (Long) -> Unit) {
        val task = DbTask(db, object : BaseDbTaskListener() {
            override fun onPostExecute(result: List<Any>) {
                if (result.isNotEmpty() && result[0] is Long) {
                    fn(result[0] as Long)
                }
            }
        })
        task.execute({ db.addItem(item) })
    }

    fun updateItem(item: T, fn: (Int) -> Unit) {
        val task = DbTask(db, object : BaseDbTaskListener() {
            override fun onPostExecute(result: List<Any>) {
                if (result.isNotEmpty() && result[0] is Int) {
                    fn(result[0] as Int)
                }
            }
        })
        task.execute({ db.updateItem(item) })
    }

    fun removeItem(item: T, fn: (Int) -> Unit) {
        val task = DbTask(db, object : BaseDbTaskListener() {
            override fun onPostExecute(result: List<Any>) {
                if (result.isNotEmpty() && result[0] is Int) {
                    fn(result[0] as Int)
                }
            }
        })
        task.execute({ db.removeItem("_id", item._id) })
    }

    fun removeItem(id: Long, fn: (Int) -> Unit) {
        val task = DbTask(db, object : BaseDbTaskListener() {
            override fun onPostExecute(result: List<Any>) {
                if (result.isNotEmpty() && result[0] is Int) {
                    fn(result[0] as Int)
                }
            }
        })
        task.execute({ db.removeItem("_id", id) })
    }

    fun getItem(id: Any, fn: (T) -> Unit) {
        val task = DbTask(db, object : BaseDbTaskListener() {
            override fun onPostExecute(result: List<Any>) {
                if (result.isNotEmpty()) {
                    fn(result[0] as T)
                }
            }
        })
        task.execute({ db.getItem("_id", id) })
    }

    fun getItems(key: String, value: Any, fn: (List<T>) -> Unit) {
        val task = DbTask(db, object : BaseDbTaskListener() {
            override fun onPostExecute(result: List<Any>) {
                if (result.isNotEmpty()) {
                    fn(result[0] as List<T>)
                }
            }
        })
        task.execute({ db.getItems(key, value) })
    }

    fun getAllItems(fn: (List<T>) -> Unit) {
        val task = DbTask(db, object : BaseDbTaskListener() {
            override fun onPostExecute(result: List<Any>) {
                if (result.isNotEmpty()) {
                    fn(result[0] as List<T>)
                }
            }
        })
        task.execute({ db.getAllItems() })
    }
}

class HighlightSqlite : SqliteModel<HighlightModel>(HighlightDb())
class PersonSqlite : SqliteModel<PersonModel>(PersonDb())
class PlaceSqlite : SqliteModel<PlaceModel>(PlaceDb())
class RecordSqlite : SqliteModel<RecordModel>(RecordDb())
class ListItemSqlite : SqliteModel<ListItemModel>(ListItemDb()) {

    fun getListItems(query: Query, fn: (List<HighlightListAdapter.ListItem>) -> Unit) {
        val task = DbTask(db, object : BaseDbTaskListener() {
            override fun onPostExecute(result: List<Any>) {
                if (result.isNotEmpty()) {
                    fn(result[0] as List<HighlightListAdapter.ListItem>)
                }
            }
        })
        with(query) {
            task.execute({
                (db as ListItemDb).getHighlightListItems(filterKey, filterValue,
                        sort, sortValue, searchKey, searchValue)
            })
        }
    }
}