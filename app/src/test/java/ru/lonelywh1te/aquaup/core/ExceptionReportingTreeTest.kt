package ru.lonelywh1te.aquaup.core

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import io.mockk.Called
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class ExceptionReportingTreeTest {
    private lateinit var crashlytics: FirebaseCrashlytics
    private lateinit var throwable: Throwable
    private lateinit var exceptionReportingTree: ExceptionReportingTree

    @BeforeEach
    fun setUp() {
        crashlytics = mockk(relaxed = true)
        throwable = RuntimeException("Test")

        exceptionReportingTree = ExceptionReportingTree(crashlytics)
    }

    @ParameterizedTest
    @ValueSource(ints = [Log.ERROR, Log.WARN])
    fun `record exception if throwable is provided`(priority: Int) {
        exceptionReportingTree.log(priority, throwable)

        verify(exactly = 1) { crashlytics.recordException(throwable) }
    }

    @ParameterizedTest
    @ValueSource(ints = [Log.ERROR, Log.WARN])
    fun `log message if throwable is not provided`(priority: Int) {
        exceptionReportingTree.log(priority, "message")

        verify(exactly = 1) { crashlytics.log("message") }
    }

    @Test
    fun `do nothing if priority not ERROR or WARN`() {
        exceptionReportingTree.i("message")

        verify { crashlytics wasNot Called }
    }

}