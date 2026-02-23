package com.ibrahim.madarsoft_task.repository

import com.ibrahim.madarsoft_task.data.User
import com.ibrahim.madarsoft_task.data.UserDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val userDao: UserDao
) {
    suspend fun insertUser(user: User) = userDao.insertUser(user)
    suspend fun updateUser(user: User) = userDao.updateUser(user)
    suspend fun deleteUser(user: User) = userDao.deleteUser(user)
    suspend fun getUserById(id: Long): User? = userDao.getUserById(id)
    fun getAllUsers(): Flow<List<User>> = userDao.getAllUser()
}
