package com.github.pidsamhai.covid19thailand

import com.github.pidsamhai.covid19thailand.db.Result
import com.github.pidsamhai.covid19thailand.network.networkBoundResource
import com.github.pidsamhai.covid19thailand.network.response.ddc.TodayByProvince
import com.github.pidsamhai.covid19thailand.repository.Repository
import io.mockk.every
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.Clock
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class RepositoryTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `fetch getTodayByProvince lastItem check`(): Unit = runBlocking {
        var updateDate = Clock.System.now().toString()
        val today = TodayByProvince(
            province = "province",
            newCase = 10,
            newCaseExcludeAbroad = 5,
            totalCase = 100,
            totalCaseExcludeAbroad = 95,
            txnDate = "30-2-2021 7:11:11",
            updateDate = updateDate
        )

        val repository: Repository = mockk()

        every {
            repository.getTodayByProvince(any(), any())
        } returns networkBoundResource(
            query = {
                flow {
                    updateDate = Clock.System.now().toString()
                    emit(today.copy(updateDate = updateDate))
                }
            },
            saveFetchResult = { },
            fetch = {
                today.copy()
            }
        )

        val latestItem = repository.getTodayByProvince("", false).last()

        assert(latestItem is Result.Success)
        assertEquals((latestItem as Result.Success<TodayByProvince>).data.updateDate, updateDate)

    }
}