package android.projects.sukeer.hilightr.database.sqlitedb

/**
 *
 * Author: Sukeerthi Khadri
 * Created: 11/5/16
 */
interface Model<T> {
    fun insert(item: T)
    fun delete(pName: String, pValue: Any)
    fun get(pName: String, pValue: Any)
    fun update(updatedItem: T)

    interface Listener {
        fun onItemInserted(id: Long)
        fun onItemRetrieved(items: List<Any>)
        fun onItemDeleted(numOfDeletes: Int)
        fun onItemUpdated(numOfUpdates: Int)
    }
}
