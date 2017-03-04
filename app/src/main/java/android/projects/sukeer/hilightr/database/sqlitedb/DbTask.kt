package android.projects.sukeer.hilightr.database.sqlitedb

import android.os.AsyncTask

/**
 *
 * Author: Sukeerthi Khadri
 * Created: 11/5/16
 */
class DbTask<T : DbModel>(val dbDao: DbDao<T>, var listener: Listener) : AsyncTask<DbDao<T>.() -> Any?, Unit, List<Any>>() {

    interface Listener {
        fun onPreExecute()
        fun onProgressUpdate()
        fun onPostExecute(result: List<Any>)
        fun onCancelled(result: List<Any>?)
    }

    override fun onPreExecute() {
        listener.onPreExecute()
    }

    override fun doInBackground(vararg operations: DbDao<T>.() -> Any?): List<Any> {
        val results = mutableListOf<Any>()
        for (operation in operations) {
            if (!isCancelled) {
                val result = operation(dbDao)
                if (result != null) {
                    results.add(result)
                }
            } else {
                break
            }
        }
        return results
    }

    override fun onPostExecute(result: List<Any>) {
        listener.onPostExecute(result)
    }

    override fun onCancelled(result: List<Any>) {
        listener.onCancelled(result)
    }

    override fun onProgressUpdate(vararg values: Unit) {
        listener.onProgressUpdate()
    }
}

// Base implementation of DbTask.Listener that allows for subclasses of this class to only worry about implementing onPostExecute, the callback we usually care about in an AsyncTask.
abstract class BaseDbTaskListener : DbTask.Listener {
    override fun onPreExecute() {}
    override fun onProgressUpdate() {}
    override fun onCancelled(result: List<Any>?) {}
}