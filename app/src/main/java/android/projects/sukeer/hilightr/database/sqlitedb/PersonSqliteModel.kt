package android.projects.sukeer.hilightr.database.sqlitedb

import android.projects.sukeer.hilightr.database.sqlitedb.DbTask
import android.projects.sukeer.hilightr.database.sqlitedb.Model
import android.projects.sukeer.hilightr.utility.log

/**
 *
 * Author: Sukeerthi Khadri
 * Created: 11/5/16
 */
class PersonSqliteModel(val listener: Model.Listener) : Model<PersonModel> {

    private val db = PersonDb()
    private lateinit var dbAsync: DbTask

    override fun insert(item: PersonModel) {
        dbAsync = DbTask(db, object : DbTask.Listener {
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
            }

        })
        dbAsync.execute({ db.addItem(item) })
    }

    override fun delete(pName: String, pValue: Any) {
        listener.onItemDeleted(db.removeItem(pName, pValue))
    }

    override fun get(pName: String, pValue: Any) {
        listener.onItemRetrieved(db.getItems(pName, pValue))
    }

    override fun update(updatedItem: PersonModel) {
        listener.onItemUpdated(db.updateItem(updatedItem))
    }
}