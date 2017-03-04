package android.projects.sukeer.hilightr.test.database

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