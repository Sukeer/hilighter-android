package android.projects.sukeer.hilightr.utility

import android.database.sqlite.SQLiteDatabase
import android.util.Log

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
fun <K, V : Any> MutableMap<K, V?>.toVarArgArray(): Array<out Pair<K, V>> =
        map({ Pair(it.key, it.value!!) }).toTypedArray()

// AppCompatActivity extensions
fun Any.log(message: String) {
    Log.d(javaClass.simpleName, message)
}
