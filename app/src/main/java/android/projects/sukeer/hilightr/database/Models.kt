package android.projects.sukeer.hilightr.database

import java.util.*

/**
 *
 * Author: Sukeerthi Khadri
 * Created: 10/9/16
 */
interface DbModel {
    val map: MutableMap<String, Any?>
    val _id: Any
}

interface TableConstant {
    val TABLE_NAME: String
    val COL_ID: String
        get() = "_id"
}

data class PlaceModel(override val map: MutableMap<String, Any?>) : DbModel {

    companion object constants : TableConstant {
        override val TABLE_NAME = "place"
        val COL_NAME = "name"
        val COL_ADDR = "address"
        val COL_PHONE = "phone"
        val COL_WEB = "website"
        val COL_LAT = "latitude"
        val COL_LON = "longitude"
        val COL_TYPE = "type"
        val COL_PRICE = "price"
        val COL_RATING = "rating"
    }

    override var _id: String by map
    var name: String by map
    var address: String by map
    var phone: String by map
    var website: String by map
    var latitude: Double by map
    var longitude: Double by map
    var type: Int by map
    var price: Int by map
    var rating: Int by map

    constructor(id: String, name: String, address: String, phone: String, website: String,
                latitude: Double, longitude: Double, type: Int, price: Int, rating: Int) : this(HashMap()) {
        this._id = id
        this.name = name
        this.address = address
        this.phone = phone
        this.website = website
        this.latitude = latitude
        this.longitude = longitude
        this.type = type
        this.price = price
        this.rating = rating
    }
}

data class PersonModel(override val map: MutableMap<String, Any?>) : DbModel {

    companion object constants : TableConstant {
        override val TABLE_NAME = "person"
        val COL_NAME = "name"
        val COL_EMAIL = "email"
        val COL_PHOTO = "photo"
    }

    override var _id: String by map
    var name: String by map
    var email: String by map
    var photo: String by map

    constructor(id: String, name: String, email: String, photo: String) : this(HashMap()) {
        this._id = id
        this.name = name
        this.email = email
        this.photo = photo
    }
}

data class HighlightModel(override val map: MutableMap<String, Any?>) : DbModel {

    companion object constants : TableConstant {
        override val TABLE_NAME = "highlight"
        val COL_MESSAGE = "message"
        val COL_PERSON = "person"
        val COL_PLACE = "place"
        val COL_DATE = "date"
    }

    override var _id: Long by map
    var message: String by map
    var person: String by map
    var place: String by map
    var date: Long by map

    constructor(message: String, person: String, place: String, date: Long) : this(HashMap()) {
        this.message = message
        this.person = person
        this.place = place
        this.date = date
    }
}

data class RecordModel(override val map: MutableMap<String, Any?>) : DbModel {

    companion object constants : TableConstant {
        override val TABLE_NAME = "record"
        val COL_PER_ID = "person"
        val COL_PLACE_ID = "place"
        val COL_HIGHLIGHT_ID = "highlight"
    }

    override var _id: Long by map
    var person: String by map
    var place: String by map
    var highlight: Long by map

    constructor(person: String, place: String, highlight: Long) : this(HashMap()) {
        this.person = person
        this.place = place
        this.highlight = highlight
    }
}