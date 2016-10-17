package android.projects.sukeer.hilightr.test

import android.projects.sukeer.hilightr.database.PersonDb
import android.projects.sukeer.hilightr.database.PersonModel
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.Assert
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

/**
 * Author: Sukeerthi Khadri
 * Created: 10/15/16
 */
@RunWith(AndroidJUnit4::class)
class PersonDatabaseTest {

    companion object {

        lateinit private var personDb: PersonDb
        private val validPerson = PersonModel("1", "John Doe", "jdoe@gmail.com", "none", "a")

        @BeforeClass @JvmStatic
        fun initialize() {
            val appContext = InstrumentationRegistry.getTargetContext()
            appContext.deleteDatabase("hilightr.db")
            personDb = PersonDb(appContext)
        }
    }

    @Before
    fun prepareDatabase() {
        // clear table before every test
        personDb.clearTable()
    }

    @Test
    fun testValidInsertion() {
        personDb.addPerson(validPerson)
        val retrievedPerson = personDb.getPerson(validPerson._id)

        Assert.assertNotNull("Retrieval failed", retrievedPerson)
        Assert.assertEquals("ID invalid", validPerson._id, retrievedPerson!!._id)
        Assert.assertEquals("Name invalid", validPerson.name, retrievedPerson.name)
        Assert.assertEquals("Email invalid", validPerson.email, retrievedPerson.email)
        Assert.assertEquals("Photo URL invalid", validPerson.photo, retrievedPerson.photo)
        Assert.assertEquals("Token invalid", validPerson.token, retrievedPerson.token)
    }

    @Test
    fun testUpdate() {
        personDb.addPerson(validPerson)

        // copy modified person and update in database
        val updatedPersonMap = HashMap(validPerson.map)
        updatedPersonMap["name"] = "Mary Doe"
        val updatedValidPerson = validPerson.copy(updatedPersonMap)
        personDb.updatePerson(updatedValidPerson)

        // assertions
        val retrievedPerson = personDb.getPerson(validPerson._id)
        Assert.assertNotNull("Retrieval failed", retrievedPerson)
        Assert.assertEquals("Update name invalid", "Mary Doe", retrievedPerson!!.name)
        Assert.assertEquals("Integrity invalid", validPerson.email, retrievedPerson.email)
    }

    @Test
    fun testRemove() {
        personDb.addPerson(validPerson)

        // copy modified person and add to database for future deletion
        val personToDeleteMap = HashMap(validPerson.map)
        personToDeleteMap["_id"] = "2"
        personToDeleteMap["token"] = "b"
        val id = personToDeleteMap["_id"] as String

        // add person and check validity
        personDb.addPerson(validPerson.copy(personToDeleteMap))
        Assert.assertNotNull("Insertion failed", personDb.getPerson(id))

        // remove person and check validity
        personDb.removePerson(id)
        Assert.assertNull("Deletion invalid", personDb.getPerson(id))
        Assert.assertEquals("Size after deletion incorrect", 1, personDb.getAllPersons().size)
    }

    @Test
    fun testRetrieveAllPersons() {
        personDb.addPerson(validPerson)

        // add additional person
        val newPerson = HashMap(validPerson.map)
        newPerson["_id"] = "2"
        newPerson["name"] = "Clark Kent"
        newPerson["token"] = "b"
        personDb.addPerson(validPerson.copy(newPerson))

        val persons = personDb.getAllPersons()
        Assert.assertNotNull("Retrieval failed", persons)
        Assert.assertEquals("Size incorrect", 2, persons.size)
    }

}