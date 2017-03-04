package android.projects.sukeer.hilightr.test.utilities

import android.projects.sukeer.hilightr.utility.toContentValues
import android.support.test.runner.AndroidJUnit4
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

/**
 *
 * Author: Sukeerthi Khadri
 * Created: 2/11/17
 */
@RunWith(AndroidJUnit4::class)
class ExtensionsTests {

    @Test
    fun testArrayToContentValues() {
        val pairArray: Array<Pair<String, Any>> = arrayOf(
                "name" to "John",
                "date_created" to System.currentTimeMillis() / 1000,
                "age" to 24,
                "married" to false,
                "net_worth" to 10.5
        )
        with(pairArray.toContentValues()) {
            Assert.assertEquals(5, size())
            Assert.assertTrue(containsKey("name"))
            Assert.assertTrue(containsKey("date_created"))
            Assert.assertTrue(containsKey("age"))
            Assert.assertTrue(containsKey("married"))
            Assert.assertTrue(containsKey("net_worth"))

            Assert.assertEquals(pairArray[0].second, getAsString("name"))
            Assert.assertEquals(pairArray[1].second, getAsLong("date_created"))
            Assert.assertEquals(pairArray[2].second, getAsInteger("age"))
            Assert.assertEquals(pairArray[3].second, getAsBoolean("married"))
            Assert.assertEquals(pairArray[4].second, getAsDouble("net_worth"))
        }
    }

    @Test(expected = ClassCastException::class)
    fun testArrayToContentValuesException() {
        val pairArray: Array<Pair<String, Any>> = arrayOf(
                "unsupported_type" to Exception()
        )
        pairArray.toContentValues()
    }

    @Test
    fun testMapToContentValues() {
        val valueMap: Map<String, Any> = mapOf(
                "name" to "John",
                "date_created" to System.currentTimeMillis() / 1000,
                "age" to 24,
                "married" to false,
                "net_worth" to 10.5
        )
        with(valueMap.toContentValues()) {
            Assert.assertEquals(5, size())
            Assert.assertTrue(containsKey("name"))
            Assert.assertTrue(containsKey("date_created"))
            Assert.assertTrue(containsKey("age"))
            Assert.assertTrue(containsKey("married"))
            Assert.assertTrue(containsKey("net_worth"))

            Assert.assertEquals(valueMap["name"], getAsString("name"))
            Assert.assertEquals(valueMap["date_created"], getAsLong("date_created"))
            Assert.assertEquals(valueMap["age"], getAsInteger("age"))
            Assert.assertEquals(valueMap["married"], getAsBoolean("married"))
            Assert.assertEquals(valueMap["net_worth"], getAsDouble("net_worth"))
        }
    }

    @Test(expected = ClassCastException::class)
    fun testMapToContentValuesException() {
        val valueMap: Map<String, Any> = mapOf(
                "unsupported_type" to Exception()
        )
        valueMap.toContentValues()
    }

}