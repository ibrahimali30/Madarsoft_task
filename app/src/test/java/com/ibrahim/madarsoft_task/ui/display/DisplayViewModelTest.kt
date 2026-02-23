package com.ibrahim.madarsoft_task.ui.display

import com.ibrahim.madarsoft_task.data.Gender
import com.ibrahim.madarsoft_task.data.User
import com.ibrahim.madarsoft_task.repository.UserRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description


@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    val dispatcher: TestDispatcher = StandardTestDispatcher()
) : TestWatcher() {

    override fun starting(description: Description) {
        Dispatchers.setMain(dispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}


@OptIn(ExperimentalCoroutinesApi::class)
class DisplayViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    lateinit var repository: UserRepository

    private lateinit var viewModel: DisplayViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `users flow emits from repository`() = runTest {

        val sample = listOf(
            User(
                id = 1,
                name = "A",
                age = 10,
                jobTitle = "J",
                gender = Gender.MALE
            )
        )

        coEvery { repository.getAllUsers() } returns flowOf(sample)

        viewModel = DisplayViewModel(repository, SharingStarted.Eagerly)

        advanceUntilIdle()

        assertEquals(sample, viewModel.users.value)
    }

    @Test
    fun `deleteUser calls repository delete`() = runTest {

        val user = User(
            id = 2,
            name = "B",
            age = 20,
            jobTitle = "T",
            gender = Gender.FEMALE
        )

        coEvery { repository.getAllUsers() } returns flowOf(emptyList())
        coEvery { repository.deleteUser(user) } returns 1

        viewModel = DisplayViewModel(repository, SharingStarted.Eagerly)

        viewModel.deleteUser(user)

        advanceUntilIdle()

        coVerify(exactly = 1) { repository.deleteUser(user) }
    }
}