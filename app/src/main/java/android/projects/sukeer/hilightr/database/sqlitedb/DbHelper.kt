package android.projects.sukeer.hilightr.database.sqlitedb

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
        db.createTable(PlaceModel.constants.TABLE_NAME, true,
                PlaceModel.constants.COL_ID to TEXT + PRIMARY_KEY,
                PlaceModel.constants.COL_NAME to TEXT + NOT_NULL,
                PlaceModel.constants.COL_ADDR to TEXT + NOT_NULL,
                PlaceModel.constants.COL_PHONE to TEXT + NOT_NULL,
                PlaceModel.constants.COL_WEB to TEXT + NOT_NULL,
                PlaceModel.constants.COL_LAT to REAL + NOT_NULL,
                PlaceModel.constants.COL_LON to REAL + NOT_NULL,
                PlaceModel.constants.COL_TYPE to INTEGER + NOT_NULL,
                PlaceModel.constants.COL_PRICE to INTEGER + NOT_NULL,
                PlaceModel.constants.COL_RATING to INTEGER + NOT_NULL)
        db.createTable(PersonModel.constants.TABLE_NAME, true,
                PersonModel.constants.COL_ID to TEXT + PRIMARY_KEY,
                PersonModel.constants.COL_NAME to TEXT + NOT_NULL,
                PersonModel.constants.COL_EMAIL to TEXT + NOT_NULL,
                PersonModel.constants.COL_PHOTO to TEXT + NOT_NULL)
        db.createTable(HighlightModel.constants.TABLE_NAME, true,
                HighlightModel.constants.COL_ID to INTEGER + PRIMARY_KEY,
                HighlightModel.constants.COL_TITLE to TEXT + NOT_NULL,
                HighlightModel.constants.COL_MESSAGE to TEXT + NOT_NULL,
                HighlightModel.constants.COL_PERSON to TEXT + NOT_NULL,
                HighlightModel.constants.COL_PLACE to TEXT + NOT_NULL,
                HighlightModel.constants.COL_DATE to INTEGER + NOT_NULL,
                "" to FOREIGN_KEY(HighlightModel.constants.COL_PERSON, PersonModel.constants.TABLE_NAME, PersonModel.constants.COL_ID),
                "" to FOREIGN_KEY(HighlightModel.constants.COL_PLACE, PlaceModel.constants.TABLE_NAME, PlaceModel.constants.COL_ID))
        /*db.execSQL("CREATE TABLE ${RecordModel.constants.TABLE_NAME} (" +
                "${RecordModel.constants.COL_ID} INTEGER PRIMARY KEY NOT NULL," +
                "${RecordModel.constants.COL_PER_ID} TEXT NOT NULL," +
                "${RecordModel.constants.COL_PLACE_ID} TEXT NOT NULL," +
                "${RecordModel.constants.COL_HIGHLIGHT_ID} INTEGER NOT NULL UNIQUE," +
                "FOREIGN KEY(${RecordModel.constants.COL_PER_ID}) REFERENCES ${PersonModel.constants.TABLE_NAME}(${PersonModel.constants.COL_ID})," +
                "FOREIGN KEY(${RecordModel.constants.COL_PLACE_ID}) REFERENCES ${PlaceModel.constants.TABLE_NAME}(${PlaceModel.constants.COL_ID})," +
                "FOREIGN KEY(${RecordModel.constants.COL_HIGHLIGHT_ID}) REFERENCES ${HighlightModel.constants.TABLE_NAME}(${HighlightModel.constants.COL_ID}));")
        */
        db.createTable(RecordModel.constants.TABLE_NAME, true,
                RecordModel.constants.COL_ID to INTEGER + PRIMARY_KEY,
                RecordModel.constants.COL_PER_ID to TEXT + NOT_NULL,
                RecordModel.constants.COL_PLACE_ID to TEXT + NOT_NULL,
                RecordModel.constants.COL_HIGHLIGHT_ID to INTEGER + NOT_NULL + UNIQUE,
                "" to FOREIGN_KEY(RecordModel.constants.COL_PER_ID, PersonModel.constants.TABLE_NAME, PersonModel.constants.COL_ID),
                "" to FOREIGN_KEY(RecordModel.constants.COL_PLACE_ID, PlaceModel.constants.TABLE_NAME, PlaceModel.constants.COL_ID),
                "" to FOREIGN_KEY(RecordModel.constants.COL_HIGHLIGHT_ID, HighlightModel.constants.TABLE_NAME, HighlightModel.constants.COL_ID))
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(PlaceModel.constants.TABLE_NAME, true)
        db.dropTable(PersonModel.constants.TABLE_NAME, true)
        db.dropTable(HighlightModel.constants.TABLE_NAME, true)
        db.dropTable(RecordModel.constants.TABLE_NAME, true)
        onCreate(db)
    }
}