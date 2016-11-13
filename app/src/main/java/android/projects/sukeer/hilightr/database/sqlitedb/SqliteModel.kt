package android.projects.sukeer.hilightr.database.sqlitedb

import android.projects.sukeer.hilightr.utility.log

/**
 *
 * Author: Sukeerthi Khadri
 * Created: 11/13/16
 */
abstract class SqliteModel<T : DbModel> {

    interface Listener {
        fun onItemInserted(id: Long)
        fun onItemsRetrieved(items: List<Any>)
        fun onItemDeleted(numOfDeletes: Int)
        fun onItemUpdated(numOfUpdates: Int)
        fun onCancelled(items: List<Any>?)
    }

    abstract val db: DbDao<T>
    abstract val listener: Listener

    fun insert(item: T) {
        val dbTask = DbTask(db, object : DbTask.Listener {
            override fun onPreExecute() {
                log("onPreExec")
            }

            override fun onProgressUpdate() {
                log("onProgressUp")
            }

            override fun onPostExecute(result: List<Any>) {
                listener.onItemInserted(result[0] as Long)
            }

            override fun onCancelled(result: List<Any>?) {
                log("onCancelled")
                listener.onCancelled(result)
            }
        })
        dbTask.execute({ db.addItem(item) })
    }

    fun get(key: String, value: Any) {
        val dbTask = DbTask(db, object : DbTask.Listener {
            override fun onPreExecute() {
                log("onPreExec")
            }

            override fun onProgressUpdate() {
                log("onProgressUp")
            }

            override fun onPostExecute(result: List<Any>) {
                listener.onItemsRetrieved(result)
            }

            override fun onCancelled(result: List<Any>?) {
                log("onCancelled")
                listener.onCancelled(result)
            }
        })
        dbTask.execute({ db.getItems(key, value) })
    }

    fun delete(key: String, value: Any) {
        val dbTask = DbTask(db, object : DbTask.Listener {
            override fun onPreExecute() {
                log("onPreExec")
            }

            override fun onProgressUpdate() {
                log("onProgressUp")
            }

            override fun onPostExecute(result: List<Any>) {
                listener.onItemDeleted(result[0] as Int)
            }

            override fun onCancelled(result: List<Any>?) {
                log("onCancelled")
                listener.onCancelled(result)
            }
        })
        dbTask.execute({ db.removeItem(key, value) })
    }

    fun update(updatedItem: T) {
        val dbTask = DbTask(db, object : DbTask.Listener {
            override fun onPreExecute() {
                log("onPreExec")
            }

            override fun onProgressUpdate() {
                log("onProgressUp")
            }

            override fun onPostExecute(result: List<Any>) {
                listener.onItemUpdated(result[0] as Int)
            }

            override fun onCancelled(result: List<Any>?) {
                log("onCancelled")
                listener.onCancelled(result)
            }
        })
        dbTask.execute({ db.updateItem(updatedItem) })
    }

}

class PersonSqliteModel(override val listener: Listener) : SqliteModel<PersonModel>() {
    override val db: DbDao<PersonModel> = PersonDb()
}

class PlaceSqliteModel(override val listener: Listener) : SqliteModel<PlaceModel>() {
    override val db: DbDao<PlaceModel> = PlaceDb()
}

class HighlightSqliteModel(override val listener: Listener) : SqliteModel<HighlightModel>() {
    override val db: DbDao<HighlightModel> = HighlightDb()
}

class RecordSqliteModel(override val listener: Listener) : SqliteModel<RecordModel>() {
    override val db: DbDao<RecordModel> = RecordDb()
}