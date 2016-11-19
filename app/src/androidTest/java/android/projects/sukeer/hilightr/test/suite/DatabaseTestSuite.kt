package android.projects.sukeer.hilightr.test.suite

import android.projects.sukeer.hilightr.test.HighlightDatabaseTest
import android.projects.sukeer.hilightr.test.PersonDatabaseTest
import android.projects.sukeer.hilightr.test.PlaceDatabaseTest
import android.projects.sukeer.hilightr.test.RecordDatabaseTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

/**
 * Author: Sukeerthi Khadri
 * Created: 10/16/16
 */
@RunWith(Suite::class)
@Suite.SuiteClasses(PlaceDatabaseTest::class, PersonDatabaseTest::class,
        HighlightDatabaseTest::class, RecordDatabaseTest::class)
class DatabaseTestSuite