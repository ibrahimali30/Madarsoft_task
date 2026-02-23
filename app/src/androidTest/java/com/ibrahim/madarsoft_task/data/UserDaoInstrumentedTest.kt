package com.ibrahim.madarsoft_task.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.first
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserDaoInstrumentedTest {

    private lateinit var database: AppDatabase
    private lateinit var userDao: UserDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries() // ok for tests
            .build()
        userDao = database.userDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun insertAndGetUser_byId() = runBlocking {
        val user = User(name = "John", age = 30, jobTitle = "Dev", gender = Gender.MALE)
        val id = userDao.insertUser(user)

        val fromDb = userDao.getUserById(id)
        assertEquals("John", fromDb?.name)
        assertEquals(30, fromDb?.age)
        assertEquals("Dev", fromDb?.jobTitle)
        assertEquals(Gender.MALE, fromDb?.gender)
    }

    @Test
    fun updateUser_updatesRow() = runBlocking {
        val user = User(name = "Alice", age = 25, jobTitle = "QA", gender = Gender.FEMALE)
        val id = userDao.insertUser(user)

        val inserted = userDao.getUserById(id)!!
        val updated = inserted.copy(name = "Alice2", age = 26)
        val updatedCount = userDao.updateUser(updated)

        // updateUser returns number of rows affected (should be 1)
        assertEquals(1, updatedCount)

        val fromDb = userDao.getUserById(id)
        assertEquals("Alice2", fromDb?.name)
        assertEquals(26, fromDb?.age)
    }

    @Test
    fun deleteUser_removesRow() = runBlocking {
        val user = User(name = "Mark", age = 40, jobTitle = "PM", gender = Gender.MALE)
        val id = userDao.insertUser(user)

        val inserted = userDao.getUserById(id)!!
        val deletedCount = userDao.deleteUser(inserted)
        assertEquals(1, deletedCount)

        val fromDb = userDao.getUserById(id)
        assertNull(fromDb)
    }

    @Test
    fun getAllUser_flowReturnsList_inDescendingOrder() = runBlocking {
        // insert multiple users and verify getAllUser flow returns latest-first (ORDER BY id DESC)
        val u1 = User(name = "One", age = 1, jobTitle = "A", gender = Gender.MALE)
        val u2 = User(name = "Two", age = 2, jobTitle = "B", gender = Gender.FEMALE)
        val id1 = userDao.insertUser(u1)
        val id2 = userDao.insertUser(u2)

        val all = userDao.getAllUser().first()
        // Expect the list to be in descending id order: id2 (Two), id1 (One)
        assertEquals(2, all.size)
        assertEquals("Two", all[0].name)
        assertEquals(id2, all[0].id)
        assertEquals("One", all[1].name)
        assertEquals(id1, all[1].id)
    }
}

