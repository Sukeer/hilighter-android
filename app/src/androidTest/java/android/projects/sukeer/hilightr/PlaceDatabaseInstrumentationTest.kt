package android.projects.sukeer.hilightr

import android.projects.sukeer.hilightr.database.PlaceDb
import android.projects.sukeer.hilightr.database.PlaceModel
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.junit.Assert
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Author: Sukeerthi Khadri
 * Created: 10/10/16
 */
@RunWith(AndroidJUnit4::class)
class PlaceDatabaseInstrumentationTest {

    companion object {
        lateinit private var placeDb: PlaceDb

        private val validPlace = PlaceModel("1234", "Willis Tower", "77 West Wacker", "18000000000", "www.google.com", 45.3909, 23.83291, 1, 1, 3)

        @BeforeClass @JvmStatic
        fun initialize() {
            val appContext = InstrumentationRegistry.getTargetContext()
            placeDb = PlaceDb(appContext)
            placeDb.clearTable()
        }
    }

    @Test
    fun testValidInsertion() {
        placeDb.addPlace(validPlace)
        val retrievedPlace = placeDb.getPlace(validPlace._id)

        // assertions
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
        // copy modified place and update in database
        val placeMap = validPlace.map
        placeMap.put("name", "Sears Tower")
        val updatedValidPlace = validPlace.copy(placeMap)
        placeDb.updatePlace(updatedValidPlace)

        // assertions
        val retrievedPlace = placeDb.getPlace(updatedValidPlace._id)
        Assert.assertNotNull("Retrieval failed", retrievedPlace)
        Assert.assertEquals("Update name invalid", "Sears Tower", retrievedPlace!!.name)
        Assert.assertEquals("Update invalid", retrievedPlace.name, updatedValidPlace.name)
        Assert.assertEquals("Integrity invalid", retrievedPlace.address, validPlace.address)
    }
}
