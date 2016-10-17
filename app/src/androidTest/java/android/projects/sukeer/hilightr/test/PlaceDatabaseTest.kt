package android.projects.sukeer.hilightr.test

import android.projects.sukeer.hilightr.database.PlaceDb
import android.projects.sukeer.hilightr.database.PlaceModel
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
 * Created: 10/10/16
 */
@RunWith(AndroidJUnit4::class)
class PlaceDatabaseTest {

    companion object {

        lateinit private var placeDb: PlaceDb
        private val validPlace = PlaceModel("1", "Willis Tower", "77 West Wacker", "18000000000", "www.google.com", 45.3909, 23.83291, 1, 1, 3)

        @BeforeClass @JvmStatic
        fun initialize() {
            val appContext = InstrumentationRegistry.getTargetContext()
            placeDb = PlaceDb(appContext)
        }
    }

    @Before
    fun prepareDatabase() {
        // clear table before every test
        placeDb.clearTable()
    }

    @Test
    fun testValidInsertion() {
        placeDb.addPlace(validPlace)
        val retrievedPlace = placeDb.getPlace(validPlace._id)

        Assert.assertNotNull("Retrieval failed", retrievedPlace)
        Assert.assertEquals("ID invalid", validPlace._id, retrievedPlace!!._id)
        Assert.assertEquals("Name invalid", validPlace.name, retrievedPlace.name)
        Assert.assertEquals("Address invalid", validPlace.address, retrievedPlace.address)
        Assert.assertEquals("Phone invalid", validPlace.phone, retrievedPlace.phone)
        Assert.assertEquals("Web URL invalid", validPlace.website, retrievedPlace.website)
        Assert.assertEquals("Latitude invalid", validPlace.latitude, retrievedPlace.latitude, 0.0)
        Assert.assertEquals("Longitude invalid", validPlace.longitude, retrievedPlace.longitude, 0.0)
        Assert.assertEquals("Type invalid", validPlace.type, retrievedPlace.type)
        Assert.assertEquals("Price invalid", validPlace.price, retrievedPlace.price)
        Assert.assertEquals("Rating invalid", validPlace.rating, retrievedPlace.rating)
    }

    @Test
    fun testUpdate() {
        placeDb.addPlace(validPlace)

        // copy modified place and update in database
        val updatedPlaceMap = HashMap(validPlace.map)
        updatedPlaceMap.put("name", "Sears Tower")
        val updatedValidPlace = validPlace.copy(updatedPlaceMap)
        placeDb.updatePlace(updatedValidPlace)

        // assertions
        val retrievedPlace = placeDb.getPlace(validPlace._id)
        Assert.assertNotNull("Retrieval failed", retrievedPlace)
        Assert.assertEquals("Update name invalid", "Sears Tower", retrievedPlace!!.name)
        Assert.assertEquals("Integrity invalid", validPlace.address, retrievedPlace.address)
    }

    @Test
    fun testRemove() {
        placeDb.addPlace(validPlace)

        // copy modified place and add to database for future deletion
        val placeToDeleteMap = HashMap(validPlace.map)
        placeToDeleteMap["_id"] = "2"
        val id = placeToDeleteMap["_id"] as String

        // add place and check validity
        placeDb.addPlace(validPlace.copy(placeToDeleteMap))
        Assert.assertNotNull("Insertion failed", placeDb.getPlace(id))

        // remove place and check validity
        placeDb.removePlace(id)
        Assert.assertNull("Deletion invalid", placeDb.getPlace(id))
        Assert.assertEquals("Size after deletion incorrect", 1, placeDb.getAllPlaces().size)
    }

    @Test
    fun testRetrieveAllPlaces() {
        placeDb.addPlace(validPlace)

        // add additional place
        val newPlace = HashMap(validPlace.map)
        newPlace["_id"] = "2"
        newPlace["name"] = "Bay Bridge"
        placeDb.addPlace(validPlace.copy(newPlace))

        val places = placeDb.getAllPlaces()
        Assert.assertNotNull("Retrieval failed", places)
        Assert.assertEquals("Size incorrect", 2, places.size)

    }

}
