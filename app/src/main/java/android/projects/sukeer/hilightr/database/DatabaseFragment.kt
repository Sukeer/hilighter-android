package android.projects.sukeer.hilightr.database

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment

/**
 *
 * Author: Sukeerthi Khadri
 * Created: 10/25/16
 */
class DatabaseFragment : Fragment() {

    companion object {
        interface BackgroundTaskListener {
            fun onPreExecute()
            fun onProgressUpdate()
            fun onPostExecute(result: List<Any>)
            fun onCancelled(result: List<Any>?)
        }
    }

    private var listener: BackgroundTaskListener? = null
    private lateinit var task: DatabaseBackgroundTask
    private var isExecuting: Boolean = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BackgroundTaskListener) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    fun startTask(dbDao: DbDao<out DbModel>, vararg operations: DbDao<out DbModel>.() -> Any?) {
        if (!isExecuting) {
            task = DatabaseBackgroundTask(dbDao)
            task.execute(*operations)
            isExecuting = true
        }
    }

    fun cancelTask(): Boolean {
        task.cancel(false)
        isExecuting = !task.isCancelled
        return isExecuting
    }

    fun updateTaskExecutingStatus(status: Boolean) {
        isExecuting = status
    }

    private inner class DatabaseBackgroundTask(val dbDao: DbDao<out DbModel>) : AsyncTask<DbDao<out DbModel>.() -> Any?, Unit, List<Any>>() {

        override fun onPreExecute() {
            listener?.onPreExecute()
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
            listener?.onPostExecute(result)
        }

        override fun onCancelled(result: List<Any>) {
            listener?.onCancelled(result)
        }

        override fun onProgressUpdate(vararg values: Unit) {
            listener?.onProgressUpdate()
        }

    }


}
