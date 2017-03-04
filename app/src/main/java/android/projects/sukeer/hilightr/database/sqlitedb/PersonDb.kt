package android.projects.sukeer.hilightr.database.sqlitedb

import android.content.Context
import android.projects.sukeer.hilightr.utility.App
import org.jetbrains.anko.db.rowParser

/**
 *
 * Author: Sukeerthi Khadri
 * Created: 10/15/16
 */
class PersonDb(override val context: Context = App.instance) : DbDao<PersonModel>() {

    // parse row of Cursor into model object
    override val parser = rowParser { _id: String, name: String, email: String, photo: String, date_created: Long ->
        PersonModel(_id, name, email, photo, date_created)
    }

    override val constants = PersonModel.constants
}