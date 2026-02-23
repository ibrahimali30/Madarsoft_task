package com.ibrahim.madarsoft_task.ui.input

import com.ibrahim.madarsoft_task.data.Gender
import com.ibrahim.madarsoft_task.data.User
import com.ibrahim.madarsoft_task.repository.UserRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.slot
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class InputViewModelTest {

    @MockK
    lateinit var repository: UserRepository

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: InputViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = InputViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `saveUser inserts when id is zero`() = runBlocking {
        coEvery { repository.insertUser(any()) } returns 1L

        var saved = false
        viewModel.saveUser(0L, "John", "30", "Developer", Gender.MALE, onSaved = { saved = true }, onError = { error -> throw AssertionError(error) })

        // advance
        testDispatcher.scheduler.advanceUntilIdle()

        val slot = slot<User>()
        coVerify { repository.insertUser(capture(slot)) }
        assertEquals("John", slot.captured.name)
        assertEquals(30, slot.captured.age)
        assertTrue(saved)
    }

    @Test
    fun `saveUser updates when id is provided`() = runBlocking {
        coEvery { repository.updateUser(any()) } returns 1

        var saved = false
        viewModel.saveUser(5L, "Jane", "28", "Engineer", Gender.FEMALE, onSaved = { saved = true }, onError = { error -> throw AssertionError(error) })

        testDispatcher.scheduler.advanceUntilIdle()

        val slot = slot<User>()
        coVerify { repository.updateUser(capture(slot)) }
        assertEquals(5L, slot.captured.id)
        assertEquals("Jane", slot.captured.name)
        assertTrue(saved)
    }

    @Test
    fun `saveUser validation fails for empty name`() = runBlocking {
        var errorMsg: String? = null
        viewModel.saveUser(0L, "", "20", "Job", Gender.MALE, onSaved = { }, onError = { errorMsg = it })
        testDispatcher.scheduler.advanceUntilIdle()
        assertTrue(errorMsg?.contains("Name cannot be empty") == true)
    }
}
