package ru.lonelywh1te.aquaup.data.error

import android.database.sqlite.SQLiteException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import ru.lonelywh1te.aquaup.TestTimberTree
import ru.lonelywh1te.aquaup.domain.result.AppError
import ru.lonelywh1te.aquaup.domain.result.DatabaseError
import ru.lonelywh1te.aquaup.domain.result.Result
import timber.log.Timber

class dbResultExtensionsTest {
    private val data: String = "data"
    private lateinit var testTree: TestTimberTree
    private lateinit var sqliteException: SQLiteException
    private lateinit var unknownException: Exception

    @BeforeEach
    fun setUp() {
        testTree = TestTimberTree()
        Timber.Forest.plant(testTree)

        sqliteException = SQLiteException()
        unknownException = Exception()

    }

    @AfterEach
    fun tearDown() {
        Timber.Forest.uproot(testTree)
    }

    @Nested
    inner class dbRunCatchingWithResultTest() {

        @Test
        fun `return Result_Success when action completes successfully`() = runTest {
            val expected = data
            val actual = dbRunCatchingWithResult { data }

            Assertions.assertTrue(actual is Result.Success)
            Assertions.assertEquals(expected, (actual as Result.Success).data)
        }

        @Test
        fun `throws CancellationException when coroutine canceled`() = runTest {
            assertThrows<CancellationException> {
                dbRunCatchingWithResult { throw CancellationException("Cancelled") }
            }
        }

        @Test
        fun `log exception with Timber_e when action fails`() = runTest {
            dbRunCatchingWithResult { throw unknownException }

            Assertions.assertEquals(1, testTree.logs.size)
        }

        @Test
        fun `return Result_Failure with DatabaseError when action throws SQLiteException`() =
            runTest {
                val expected = DatabaseError.SQLite(sqliteException)
                val actual = dbRunCatchingWithResult { throw sqliteException }

                Assertions.assertTrue { actual is Result.Failure }
                Assertions.assertEquals(expected, (actual as Result.Failure).error)
            }

        @Test
        fun `return Result_Failure with AppError when action throws unknown exception`() = runTest {
            val expected = AppError.Unknown(unknownException)
            val actual = dbRunCatchingWithResult { throw unknownException }

            Assertions.assertTrue { actual is Result.Failure }
            Assertions.assertEquals(expected, (actual as Result.Failure).error)
        }

    }

    @Nested
    inner class asDbSafeFlowTest() {

        @Test
        fun `return Result_Success when flow completes successfully`() = runTest {
            val expected = data

            val flow = flowOf(expected)
                .map { Result.Success(data) }
                .asDbSafeFlow()

            val actual = flow.first()

            Assertions.assertTrue(actual is Result.Success)
            Assertions.assertEquals(expected, (actual as Result.Success).data)
        }

        @Test
        fun `throws CancellationException when coroutine canceled`() = runTest {
            val flow = flowOf(Unit)
                .map { Result.Success(data) }
                .onEach { throw CancellationException("Cancelled") }
                .asDbSafeFlow()

            assertThrows<CancellationException> {
                flow.first()
            }
        }

        @Test
        fun `log exception with Timber_e when flow fails`() = runTest {
            val flow = flowOf(Unit)
                .map { Result.Success(data) }
                .onEach { throw unknownException }
                .asDbSafeFlow()

            flow.first()

            Assertions.assertEquals(1, testTree.logs.size)
        }

        @Test
        fun `return Result_Failure with DatabaseError when flow throws SQLiteException`() =
            runTest {
                val expected = DatabaseError.SQLite(sqliteException)

                val flow = flowOf(Unit)
                    .map { Result.Success(data) }
                    .onEach { throw sqliteException }
                    .asDbSafeFlow()

                val actual = flow.first()

                Assertions.assertTrue { actual is Result.Failure }
                Assertions.assertEquals(expected, (actual as Result.Failure).error)
            }

        @Test
        fun `return Result_Failure with AppError when flow throws unknown exception`() = runTest {
            val expected = AppError.Unknown(unknownException)

            val flow = flowOf(Unit)
                .map { Result.Success(data) }
                .onEach { throw unknownException }
                .asDbSafeFlow()

            val actual = flow.first()

            Assertions.assertTrue { actual is Result.Failure }
            Assertions.assertEquals(expected, (actual as Result.Failure).error)
        }

    }

}