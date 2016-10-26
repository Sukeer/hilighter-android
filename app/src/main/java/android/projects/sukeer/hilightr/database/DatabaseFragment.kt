package android.projects.sukeer.hilightr.database

import android.content.Context
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
            fun onPostExecute()
            fun onCancelled()
        }
    }

    private var listener: BackgroundTaskListener? = null
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



}
