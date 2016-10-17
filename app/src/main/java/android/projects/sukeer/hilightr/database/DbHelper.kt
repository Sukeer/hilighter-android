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

    override fun onConfigure(db: SQLiteDatabase) {
        db.setForeignKeyConstraintsEnabled(true)
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(PlaceConstant.TABLE_NAME, true,
                PlaceConstant.COL_ID to TEXT + PRIMARY_KEY,
                PlaceConstant.COL_NAME to TEXT + NOT_NULL,
                PlaceConstant.COL_ADDR to TEXT + NOT_NULL,
                PlaceConstant.COL_PHONE to TEXT + NOT_NULL,
                PlaceConstant.COL_WEB to TEXT + NOT_NULL,
                PlaceConstant.COL_LAT to REAL + NOT_NULL,
                PlaceConstant.COL_LON to REAL + NOT_NULL,
                PlaceConstant.COL_TYPE to INTEGER + NOT_NULL,
                PlaceConstant.COL_PRICE to INTEGER + NOT_NULL,
                PlaceConstant.COL_RATING to INTEGER + NOT_NULL)
        db.createTable(PersonConstant.TABLE_NAME, true,
                PersonConstant.COL_ID to TEXT + PRIMARY_KEY,
                PersonConstant.COL_NAME to TEXT + NOT_NULL,
                PersonConstant.COL_EMAIL to TEXT + NOT_NULL,
                PersonConstant.COL_PHOTO to TEXT + NOT_NULL,
                PersonConstant.COL_TOKEN to TEXT + NOT_NULL + UNIQUE)
        db.createTable(HighlightConstant.TABLE_NAME, true,
                HighlightConstant.COL_ID to INTEGER + PRIMARY_KEY,
                HighlightConstant.COL_MESSAGE to TEXT + NOT_NULL,
                HighlightConstant.COL_PERSON to TEXT + NOT_NULL,
                HighlightConstant.COL_PLACE to TEXT + NOT_NULL,
                HighlightConstant.COL_DATE to INTEGER + NOT_NULL,
                "" to FOREIGN_KEY(HighlightConstant.COL_PERSON, PersonConstant.TABLE_NAME, PersonConstant.COL_ID),
                "" to FOREIGN_KEY(HighlightConstant.COL_PLACE, PlaceConstant.TABLE_NAME, PlaceConstant.COL_ID))
        db.createTable(RecordConstant.TABLE_NAME, true,
                RecordConstant.COL_ID to INTEGER + PRIMARY_KEY,
                RecordConstant.COL_PER_ID to TEXT + NOT_NULL,
                RecordConstant.COL_PLACE_ID to TEXT + NOT_NULL,
                RecordConstant.COL_HIGHLIGHT_ID to INTEGER + NOT_NULL,
                "" to FOREIGN_KEY(RecordConstant.COL_PER_ID, PersonConstant.TABLE_NAME, PersonConstant.COL_ID),
                "" to FOREIGN_KEY(RecordConstant.COL_PLACE_ID, PlaceConstant.TABLE_NAME, PlaceConstant.COL_ID),
                "" to FOREIGN_KEY(RecordConstant.COL_HIGHLIGHT_ID, HighlightConstant.TABLE_NAME, HighlightConstant.COL_ID))
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(PlaceConstant.TABLE_NAME, true)
        db.dropTable(PersonConstant.TABLE_NAME, true)
        db.dropTable(HighlightConstant.TABLE_NAME, true)
        db.dropTable(RecordConstant.TABLE_NAME, true)
        onCreate(db)
    }
}