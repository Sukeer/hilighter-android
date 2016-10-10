package android.projects.sukeer.hilightr.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.projects.sukeer.hilightr.utility.App
import org.jetbrains.anko.db.*

/**
 *
 * Author: Sukeerthi Khadri
 * Created: 10/9/16
 */
class DbHelper(context: Context = App.instance) : ManagedSQLiteOpenHelper(context, DbHelper.DB_NAME, null, DbHelper.DB_VERSION) {
    companion object {
        val DB_NAME = "hilightr.db"
        val DB_VERSION = 1
        val instance by lazy { DbHelper() }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(Place.TABLE_NAME, true,
                Place.COL_ID to TEXT + PRIMARY_KEY + NOT_NULL,
                Place.COL_NAME to TEXT + NOT_NULL,
                Place.COL_ADDR to TEXT + NOT_NULL,
                Place.COL_PHONE to TEXT + NOT_NULL,
                Place.COL_WEB to TEXT + NOT_NULL,
                Place.COL_LAT to REAL + NOT_NULL,
                Place.COL_LON to REAL + NOT_NULL,
                Place.COL_TYPE to INTEGER + NOT_NULL,
                Place.COL_PRICE to INTEGER + NOT_NULL,
                Place.COL_RATING to INTEGER + NOT_NULL)
        db.createTable(Person.TABLE_NAME, true,
                Person.COL_ID to TEXT + PRIMARY_KEY + NOT_NULL,
                Person.COL_NAME to TEXT + NOT_NULL,
                Person.COL_EMAIL to TEXT + NOT_NULL,
                Person.COL_PHOTO to TEXT + NOT_NULL,
                Person.COL_TOKEN to TEXT + NOT_NULL)
        db.createTable(Highlight.TABLE_NAME, true,
                Highlight.COL_ID to INTEGER + PRIMARY_KEY + NOT_NULL,
                Highlight.COL_MESSAGE to TEXT + NOT_NULL,
                Highlight.COL_PERSON to TEXT + NOT_NULL,
                Highlight.COL_PLACE to TEXT + NOT_NULL,
                Highlight.COL_DATE to INTEGER + NOT_NULL,
                "" to FOREIGN_KEY(Highlight.COL_PERSON, Person.TABLE_NAME, Person.COL_ID),
                "" to FOREIGN_KEY(Highlight.COL_PLACE, Place.TABLE_NAME, Place.COL_ID))
        db.createTable(Record.TABLE_NAME, true,
                Record.COL_ID to INTEGER + PRIMARY_KEY + NOT_NULL,
                Record.COL_PER_ID to TEXT + NOT_NULL,
                Record.COL_PLACE_ID to TEXT + NOT_NULL,
                Record.COL_HIGHLIGHT_ID to INTEGER + NOT_NULL,
                "" to FOREIGN_KEY(Record.COL_PER_ID, Person.TABLE_NAME, Person.COL_ID),
                "" to FOREIGN_KEY(Record.COL_PLACE_ID, Place.TABLE_NAME, Place.COL_ID),
                "" to FOREIGN_KEY(Record.COL_HIGHLIGHT_ID, Highlight.TABLE_NAME, Highlight.COL_ID))
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(Place.TABLE_NAME, true)
        db.dropTable(Person.TABLE_NAME, true)
        db.dropTable(Highlight.TABLE_NAME, true)
        db.dropTable(Record.TABLE_NAME, true)
        onCreate(db)
    }
}