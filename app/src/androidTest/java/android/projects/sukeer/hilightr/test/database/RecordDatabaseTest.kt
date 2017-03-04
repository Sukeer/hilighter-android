package android.projects.sukeer.hilightr.test.database

import android.database.sqlite.SQLiteConstraintException
import android.projects.sukeer.hilightr.database.sqlitedb.*
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

/**
 *
 * Author: Sukeerthi Khadri
 * Created: 10/16/16
 */
@RunWith(AndroidJUnit4::class)
class RecordDatabaseTest {

    companion object {
        lateinit private var highlightDb: HighlightDb
        lateinit private var placeDb: PlaceDb
        lateinit private var personDb: PersonDb
        lateinit private var recordDb: RecordDb

        private val validPlace = PlaceModel("1", "Willis Tower", "77 West Wacker", "18000000000",
                "www.google.com", 45.3909, 23.83291, 1, 1, 3f, System.currentTimeMillis() / 1000)
        private val validPerson = PersonModel("1", "John Doe", "jdoe@gmail.com", "none", System.currentTimeMillis() / 1000)
        private val validHighlight = HighlightModel("my highlight", "Hello world", "1", "1", System.currentTimeMillis() / 1000, System.currentTimeMillis() / 1000)

        @BeforeClass @JvmStatic
        fun initialize() {
            val appContext = InstrumentationRegistry.getTargetContext()
            appContext.deleteDatabase("hilightr.db")
            recordDb = RecordDb(appContext)
            highlightDb = HighlightDb(appContext)
            placeDb = PlaceDb(appContext)
            personDb = PersonDb(appContext)
        }
    }

    @Before
    fun prepareDatabase() {
        // clear table before every test
        recordDb.clearTable()
        highlightDb.clearTable()
        personDb.clearTable()
        placeDb.clearTable()
    }

    @Test
    fun testValidInsertion() {
        placeDb.addItem(validPlace)
        personDb.addItem(validPerson)
        val highlightInsertionId = highlightDb.addItem(validHighlight)

        assertEquals(1, recordDb.getAllItems().size)

        val retrievedRecord = recordDb.getAllItems()[0]
        assertNotNull(retrievedRecord)
        assertEquals(validPerson._id, retrievedRecord.person)
        assertEquals(validPlace._id, retrievedRecord.place)
        assertEquals(highlightInsertionId, retrievedRecord.highlight)
    }

    @Test(expected = SQLiteConstraintException::class)
    fun testInvalidInsertion() {
        // insertion should fail due to foreign key constraints
        recordDb.addItem(RecordModel("1", "1", 999))
    }

    @Test
    fun testCascadeDeleteOnPlace() {
        placeDb.addItem(validPlace)
        personDb.addItem(validPerson)
        highlightDb.addItem(validHighlight)

        assertEquals(1, recordDb.getAllItems().size)

        // delete place
        placeDb.removeItem("_id", validPlace._id)
        assertEquals(0, placeDb.getAllItems().size)
        assertNull(placeDb.getItem("_id", validPlace._id))

        // check if cascade occurred
        assertEquals(0, recordDb.getAllItems().size)
    }

    @Test
    fun testCascadeDeleteOnPerson() {
        placeDb.addItem(validPlace)
        personDb.addItem(validPerson)
        highlightDb.addItem(validHighlight)


        assertEquals(1, recordDb.getAllItems().size)

        // delete person
        personDb.removeItem("_id", validPerson._id)
        assertEquals(0, personDb.getAllItems().size)
        assertNull(personDb.getItem("_id", validPerson._id))

        // check if cascade occurred
        assertEquals(0, recordDb.getAllItems().size)
    }

    @Test
    fun testCascadeDeleteOnHighlight() {
        placeDb.addItem(validPlace)
        personDb.addItem(validPerson)
        val highlightInsertionId = highlightDb.addItem(validHighlight)

        assertEquals(1, recordDb.getAllItems().size)

        // delete highlight
        highlightDb.removeItem("_id", highlightInsertionId)
        assertEquals(0, highlightDb.getAllItems().size)
        assertNull(highlightDb.getItem("_id", highlightInsertionId))

        // check if cascade occurred
        assertEquals(0, recordDb.getAllItems().size)
    }

    @Test
    fun testUpdate() {
        placeDb.addItem(validPlace)
        personDb.addItem(validPerson)

        // add another person with id = "2"
        val updatedPersonMap = HashMap(validPerson.map)
        updatedPersonMap["_id"] = "2"
        updatedPersonMap["name"] = "Sam Smith"
        personDb.addItem(validPerson.copy(updatedPersonMap))

        highlightDb.addItem(validHighlight)

        // copy modified record and show person id
        val validRecord = recordDb.getAllItems()[0]
        val updatedRecordMap = HashMap(validRecord.map)
        updatedRecordMap["person"] = "2"
        val updatedValidRecord = validRecord.copy(updatedRecordMap)
        recordDb.updateItem(updatedValidRecord)

        // assertions
        val retrievedRecord = recordDb.getAllItems()[0]
        assertNotNull("Retrieval failed", retrievedRecord)
        assertEquals("Person ID not updated", updatedRecordMap["person"], retrievedRecord.person)
        assertEquals("Integrity invalid", validRecord.place, retrievedRecord.place)
    }

    @Test
    fun testRetrieveAllRecords() {
        placeDb.addItem(validPlace)
        personDb.addItem(validPerson)
        highlightDb.addItem(validHighlight)

        val records = recordDb.getAllItems()
        assertNotNull(records)
        assertEquals(1, records.size)
    }

}