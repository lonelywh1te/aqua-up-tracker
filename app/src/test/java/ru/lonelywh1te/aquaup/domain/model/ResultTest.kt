package ru.lonelywh1te.aquaup.domain.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import ru.lonelywh1te.aquaup.domain.result.AppError
import ru.lonelywh1te.aquaup.domain.result.Result
import ru.lonelywh1te.aquaup.domain.result.combineResult
import ru.lonelywh1te.aquaup.domain.result.handle
import ru.lonelywh1te.aquaup.domain.result.map
import java.io.IOException

class ResultTest {

    @Nested
    inner class map {
        @Test
        fun `map transforms Success`() {
            val expected = Result.Success(4)
            val actual = Result.Success(2).map { it * 2 }

            assertEquals(expected, actual)
        }

        @Test
        fun `map keeps Loading`() {
            val expected = Result.Loading
            val actual = Result.Loading.map { 123 }

            assertEquals(expected, actual)
        }

        @Test
        fun `map keeps Failure`() {
            val exception = IOException()

            val expected = Result.Failure(AppError.Unknown(exception))
            val actual = Result.Failure(AppError.Unknown(exception)).map { 123 }

            assertEquals(expected, actual)
        }

        @Test
        fun `transform is not invoked for Failure`() {
            var invoked = false

            Result.Failure(AppError.Unknown(IOException())).map { invoked = true }

            assertFalse(invoked)
        }

        @Test
        fun `transform is not invoked for Loading`() {
            var invoked = false

            Result.Loading.map { invoked = true }

            assertFalse(invoked)
        }
    }

    @Nested
    inner class combineResult {
        @Test
        fun `Success + Success returns transformed Success`() {
            val expected = Result.Success(5)
            val actual = combineResult(
                Result.Success(2),
                Result.Success(3)
            ) { a, b -> a + b }

            assertEquals(expected, actual)
        }

        @Test
        fun `Failure if first Result is Failure`() {
            val exception = IOException()

            val expected = Result.Failure(AppError.Unknown(exception))
            val actual = combineResult(
                Result.Failure(AppError.Unknown(exception)),
                Result.Success(0)
            ) { a, b -> return@combineResult  }

            assertEquals(expected, actual)
        }

        @Test
        fun `Failure if second Result is Failure`() {
            val exception = IOException()

            val expected = Result.Failure(AppError.Unknown(exception))
            val actual = combineResult(
                Result.Success(0),
                Result.Failure(AppError.Unknown(exception))
            ) { a, b -> return@combineResult  }

            assertEquals(expected, actual)
        }

        @Test
        fun `Loading if one is Loading and none is Failure`() {
            val expected = Result.Loading
            val actual = combineResult(
                Result.Success(0),
                Result.Loading
            ) { a, b -> return@combineResult  }

            assertEquals(expected, actual)
        }

        @Test
        fun `Loading returned if both are Loading`() {
            val expected = Result.Loading
            val actual = combineResult(
                Result.Loading,
                Result.Loading
            ) { a, b -> return@combineResult  }

            assertEquals(expected, actual)
        }

        @Test
        fun `transform not invoked if one result is Failure`() {
            var invoked = false

            combineResult(
                Result.Failure(AppError.Unknown(IOException())),
                Result.Success(3)
            ) { _, _ -> invoked = true }

            assertFalse(invoked)
        }

        @Test
        fun `transform not invoked if one result is Loading`() {
            var invoked = false

            combineResult(
                Result.Loading,
                Result.Success(3)
            ) { _, _ -> invoked = true }

            assertFalse(invoked)
        }
    }

    @Nested
    inner class handle {
        @Test
        fun `Success calls onSuccess`() {
            val expected = 84
            val actual = Result.Success(42).handle(
                onSuccess = { it * 2 },
                onFailure = { throw AssertionError("onFailure called") },
                onLoading = { throw AssertionError("onLoading called") }
            )

            assertEquals(expected, actual)
        }

        @Test
        fun `Failure calls onFailure`() {
            val exception = IOException()
            val result = Result.Failure(AppError.Unknown(exception))

            val expected = exception
            val actual = result.handle(
                onSuccess = { throw AssertionError("onSuccess called") },
                onFailure = { it.e },
                onLoading = { throw AssertionError("onLoading called") }
            )

            assertEquals(expected, actual)
        }

        @Test
        fun `Loading calls onLoading`() {
            val expected = 0
            val actual = Result.Loading.handle(
                onSuccess = { throw AssertionError("onSuccess called") },
                onFailure = { throw AssertionError("onFailure called") },
                onLoading = { 0 }
            )

            assertEquals(expected, actual)
        }
    }

}