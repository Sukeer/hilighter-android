package android.projects.sukeer.hilightr.test

import android.projects.sukeer.hilightr.database.*
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.Assert
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

        private val validPlace = PlaceModel("1", "Willis Tower", "77 West Wacker", "18000000000", "www.google.com", 45.3909, 23.83291, 1, 1, 3)
        private val validPerson = PersonModel("1", "John Doe", "jdoe@gmail.com", "none")
        private val validHighlight = HighlightModel("Hello world", "1", "1", 1000)

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

        val validRecord = RecordModel(validPerson._id, validPlace._id, highlightInsertionId)
        val recordInsertionId = recordDb.addItem(validRecord)
        Assert.assertNotEquals("Insertion ID not valid", -1, recordInsertionId)

        val retrievedRecord = recordDb.getItem("_id", recordInsertionId)
        Assert.assertNotNull("Record not found", retrievedRecord)
        Assert.assertEquals("Record id mismatch", recordInsertionId, retrievedRecord!!._id)
        Assert.assertEquals("Person id mismatch", validPerson._id, retrievedRecord.person)
        Assert.assertEquals("Place id mismatch", validPlace._id, retrievedRecord.place)
        Assert.assertEquals("Highlight id mismatch", highlightInsertionId, retrievedRecord.highlight)
    }

    @Test
    fun testInvalidInsertion() {
        // insertion should fail due to foreign key constraints
        val insertionId = recordDb.addItem(RecordModel("1", "1", 999))
        Assert.assertEquals("Foreign key constraint not met", -1, insertionId)

        val retrievedRecord = recordDb.getItem("_id", insertionId)
        Assert.assertNull("Retrieval incorrectly successful, foreign key constraint not met", retrievedRecord)
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

        val highlightInsertionId = highlightDb.addItem(validHighlight)
        val recordInsertionId = recordDb.addItem(RecordModel(validPerson._id, validPlace._id, highlightInsertionId))
        val validRecord = recordDb.getItem("_id", recordInsertionId)

        // copy modified record and update person id
        val updatedRecordMap = HashMap(validRecord!!.map)
        updatedRecordMap["_id"] = recordInsertionId
        updatedRecordMap["person"] = "2"
        val updatedValidRecord = validRecord.copy(updatedRecordMap)
        recordDb.updateItem(updatedValidRecord)

        // assertions
        val retrievedRecord = recordDb.getItem("_id", recordInsertionId)
        Assert.assertNotNull("Retrieval failed", retrievedRecord)
        Assert.assertEquals("Person ID not updated", updatedValidRecord.person, retrievedRecord!!.person)
        Assert.assertEquals("Integrity invalid", validRecord.place, retrievedRecord.place)
    }

    @Test
    fun testRemove() {
        placeDb.addItem(validPlace)
        personDb.addItem(validPerson)
        highlightDb.addItem(validHighlight)
        val highlightInsertionId = highlightDb.addItem(validHighlight)
        val recordInsertionId = recordDb.addItem(RecordModel(validPerson._id, validPlace._id, highlightInsertionId))

        // assert existence
        val retrievedRecord = recordDb.getItem("_id", recordInsertionId)
        Assert.assertNotEquals("Insertion failure", -1L, recordInsertionId)
        Assert.assertNotNull("Insertion failure: record not found", retrievedRecord)

        // remove record and check validity
        recordDb.removeItem("_id", recordInsertionId)
        Assert.assertNull("Deletion invalid", recordDb.getItem("_id", recordInsertionId))
        Assert.assertEquals("Size after deletion incorrect", 0, recordDb.getAllItems().size)
    }

    @Test
    fun testRetrieveAllRecords() {
        placeDb.addItem(validPlace)
        personDb.addItem(validPerson)
        highlightDb.addItem(validHighlight)
        val highlightInsertionId = highlightDb.addItem(validHighlight)
        val recordInsertionId = recordDb.addItem(RecordModel(validPerson._id, validPlace._id, highlightInsertionId))

        val records = recordDb.getAllItems()
        Assert.assertNotNull("Retrieval failed", records)
        Assert.assertEquals("Size incorrect", 1, records.size)
    }

}