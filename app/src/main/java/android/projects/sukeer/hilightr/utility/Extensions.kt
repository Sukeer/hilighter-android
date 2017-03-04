package android.projects.sukeer.hilightr.utility

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.support.design.widget.Snackbar
import android.util.Log
import android.view.View

/**
 *
 * Author: Sukeerthi Khadri
 * Created: 10/10/16
 */

// SQLiteDatabase extensions
fun SQLiteDatabase.clear(tableName: String) {
    execSQL("delete from $tableName")
}

// MutableMap extensions
fun <K, V : Any> Map<K, V>.toVarArgArray(): Array<out Pair<K, V>> =
        map({ Pair(it.key, it.value) }).toTypedArray()

fun <V> Map<String, V>.toContentValues(): ContentValues {
    val ret = ContentValues()
    forEach {
        val key = it.key
        val value = it.value
        when (value) {
            is Short -> ret.put(key, value)
            is Long -> ret.put(key, value)
            is Double -> ret.put(key, value)
            is Int -> ret.put(key, value)
            is String -> ret.put(key, value)
            is Boolean -> ret.put(key, value)
            is Float -> ret.put(key, value)
            else -> throw ClassCastException(value.toString())
        }
    }
    return ret
}

fun <V> Array<Pair<String, V>>.toContentValues(): ContentValues {
    val ret = ContentValues()
    forEach {
        val key = it.first
        val value = it.second
        when (value) {
            is Short -> ret.put(key, value)
            is Long -> ret.put(key, value)
            is Double -> ret.put(key, value)
            is Int -> ret.put(key, value)
            is String -> ret.put(key, value)
            is Boolean -> ret.put(key, value)
            is Float -> ret.put(key, value)
            else -> throw ClassCastException(value.toString())
        }
    }
    return ret
}

// Global extensions
fun Any.logDebug(message: String, tag: String = javaClass.simpleName) {
    Log.d(tag, message)
}

fun View.snackbar(message: String, length: Int, posAction: String?, posListener: View.OnClickListener?) {
    Snackbar.make(this, message, length)
            .setAction(posAction, posListener)
            .show()
}