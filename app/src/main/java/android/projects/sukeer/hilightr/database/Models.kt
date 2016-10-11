package android.projects.sukeer.hilightr.database

import java.util.*

/**
 *
 * Author: Sukeerthi Khadri
 * Created: 10/9/16
 */
data class PlaceModel(val map: MutableMap<String, Any?>) {
    var _id: String by map
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

data class PersonModel(val map: MutableMap<String, Any?>) {
    var _id: String by map
    var name: String by map
    var email: String by map
    var photo: String by map
    var token: String by map

    constructor(id: String, name: String, email: String, photo: String, token: String) : this(HashMap()) {
        this._id = id
        this.name = name
        this.email = email
        this.photo = photo
        this.token = token
    }
}

data class HighlightModel(val map: MutableMap<String, Any?>) {
    var _id: Long by map
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

data class RecordModel(val map: MutableMap<String, Any?>) {
    var _id: Long by map
    var person: String by map
    var highlight: Long by map
    var place: String by map

    constructor(person: String, highlight: Long, place: String) : this(HashMap()) {
        this.person = person
        this.highlight = highlight
        this.place = place
    }
}