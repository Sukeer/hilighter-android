package android.projects.sukeer.hilightr.database

import android.database.sqlite.SQLiteDatabase

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
        map({ Pair(it.key, it.value!!)}).toTypedArray()


