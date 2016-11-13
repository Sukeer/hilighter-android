package android.projects.sukeer.hilightr.database.sqlitedb

import android.os.AsyncTask

/**
 *
 * Author: Sukeerthi Khadri
 * Created: 11/5/16
 */
class DbTask(val dbDao: DbDao<out DbModel>, var listener: Listener) : AsyncTask<DbDao<out DbModel>.() -> Any?, Unit, List<Any>>() {

    interface Listener {
        fun onPreExecute()
        fun onProgressUpdate()
        fun onPostExecute(result: List<Any>)
        fun onCancelled(result: List<Any>?)
    }

    override fun onPreExecute() {
        listener.onPreExecute()
    }

    override fun doInBackground(vararg operations: DbDao<out DbModel>.() -> Any?): List<Any> {
        val results: MutableList<Any> = mutableListOf()
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

