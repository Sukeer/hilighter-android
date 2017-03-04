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
        val DB_VERSION = 4
        val instance by lazy { DbHelper() }
    }

    override fun onConfigure(db: SQLiteDatabase) {
        db.setForeignKeyConstraintsEnabled(true)
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(PlaceModel.constants.TABLE_NAME, true,
                PlaceModel.constants.COL_ID to TEXT + PRIMARY_KEY + NOT_NULL,
                PlaceModel.constants.COL_NAME to TEXT + NOT_NULL,
                PlaceModel.constants.COL_ADDR to TEXT + NOT_NULL,
                PlaceModel.constants.COL_PHONE to TEXT + NOT_NULL,
                PlaceModel.constants.COL_WEB to TEXT + NOT_NULL,
                PlaceModel.constants.COL_LAT to REAL + NOT_NULL,
                PlaceModel.constants.COL_LON to REAL + NOT_NULL,
                PlaceModel.constants.COL_TYPE to INTEGER + NOT_NULL,
                PlaceModel.constants.COL_PRICE to INTEGER + NOT_NULL,
                PlaceModel.constants.COL_RATING to REAL + NOT_NULL,
                PlaceModel.constants.COL_DATE_CREATED to INTEGER + NOT_NULL)
        db.createTable(PersonModel.constants.TABLE_NAME, true,
                PersonModel.constants.COL_ID to TEXT + PRIMARY_KEY + NOT_NULL,
                PersonModel.constants.COL_NAME to TEXT + NOT_NULL,
                PersonModel.constants.COL_EMAIL to TEXT + NOT_NULL,
                PersonModel.constants.COL_PHOTO to TEXT + NOT_NULL,
                PersonModel.constants.COL_DATE_CREATED to INTEGER + NOT_NULL)
        db.execSQL("CREATE TABLE ${HighlightModel.constants.TABLE_NAME} (" +
                "${HighlightModel.constants.COL_ID} INTEGER PRIMARY KEY," +
                "${HighlightModel.constants.COL_TITLE} TEXT NOT NULL," +
                "${HighlightModel.constants.COL_MESSAGE} TEXT NOT NULL," +
                "${HighlightModel.constants.COL_PERSON} TEXT NOT NULL," +
                "${HighlightModel.constants.COL_PLACE} TEXT NOT NULL," +
                "${HighlightModel.constants.COL_DATE_CREATED} INTEGER NOT NULL," +
                "${HighlightModel.constants.COL_DATE_UPDATED} INTEGER NOT NULL," +
                "FOREIGN KEY(${HighlightModel.constants.COL_PERSON}) REFERENCES ${PersonModel.constants.TABLE_NAME}(${PersonModel.constants.COL_ID}) ON DELETE CASCADE," +
                "FOREIGN KEY(${HighlightModel.constants.COL_PLACE}) REFERENCES ${PlaceModel.constants.TABLE_NAME}(${PlaceModel.constants.COL_ID}) ON DELETE CASCADE" +
                ");")
        db.execSQL("CREATE TABLE ${RecordModel.constants.TABLE_NAME} (" +
                "${RecordModel.constants.COL_ID} INTEGER PRIMARY KEY," +
                "${RecordModel.constants.COL_PER_ID} TEXT NOT NULL," +
                "${RecordModel.constants.COL_PLACE_ID} TEXT NOT NULL," +
                "${RecordModel.constants.COL_HIGHLIGHT_ID} INTEGER NOT NULL UNIQUE," +
                "FOREIGN KEY(${RecordModel.constants.COL_PER_ID}) REFERENCES ${PersonModel.constants.TABLE_NAME}(${PersonModel.constants.COL_ID}), " +
                "FOREIGN KEY(${RecordModel.constants.COL_PLACE_ID}) REFERENCES ${PlaceModel.constants.TABLE_NAME}(${PlaceModel.constants.COL_ID}), " +
                "FOREIGN KEY(${RecordModel.constants.COL_HIGHLIGHT_ID}) REFERENCES ${HighlightModel.constants.TABLE_NAME}(${HighlightModel.constants.COL_ID}) ON DELETE CASCADE" +
                ");")

        // views
        db.execSQL("CREATE VIEW ${ListItemModel.TABLE_NAME} AS " +
                "SELECT highlight._id, highlight.title, highlight.message, place.name AS ${ListItemModel.COL_PLACE_NAME}, person.name AS ${ListItemModel.COL_PERSON_NAME}, highlight.date_created AS ${ListItemModel.COL_DATE} " +
                "FROM highlight LEFT JOIN place ON highlight.place = place._id " +
                "LEFT JOIN person ON highlight.person = person._id")

        // triggers

        // when highlight has been added, add record of highlight to record table
        db.execSQL("CREATE TRIGGER insert_record AFTER INSERT ON ${HighlightModel.constants.TABLE_NAME}" +
                " BEGIN" +
                " INSERT INTO ${RecordModel.constants.TABLE_NAME}" +
                " (${RecordModel.constants.COL_PER_ID}, ${RecordModel.constants.COL_PLACE_ID}, ${RecordModel.constants.COL_HIGHLIGHT_ID})" +
                " VALUES (new.${HighlightModel.constants.COL_PERSON}, new.${HighlightModel.constants.COL_PLACE}, new.${HighlightModel.constants.COL_ID});" +
                " END;")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(RecordModel.constants.TABLE_NAME, true)
        db.dropTable(HighlightModel.constants.TABLE_NAME, true)
        db.dropTable(PlaceModel.constants.TABLE_NAME, true)
        db.dropTable(PersonModel.constants.TABLE_NAME, true)
        db.execSQL("DROP TRIGGER IF EXISTS insert_record")
        db.execSQL("DROP VIEW IF EXISTS list_items")
        onCreate(db)
    }
}