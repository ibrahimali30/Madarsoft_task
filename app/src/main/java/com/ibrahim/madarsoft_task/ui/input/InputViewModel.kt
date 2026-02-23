package com.ibrahim.madarsoft_task.ui.input

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibrahim.madarsoft_task.data.Gender
import com.ibrahim.madarsoft_task.data.User
import com.ibrahim.madarsoft_task.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InputViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _saving = MutableStateFlow(false)
    val saving: StateFlow<Boolean> = _saving

    suspend fun loadUser(id: Long): User? {
        return repository.getUserById(id)
    }

    fun saveUser(id: Long = 0L, name: String, ageText: String, jobTitle: String, gender: Gender, onSaved: () -> Unit, onError: (String) -> Unit) {
        val age = ageText.toIntOrNull()
        if (name.isBlank()) {
            onError("Name cannot be empty")
            return
        }
        if (age == null || age <= 0) {
            onError("Enter a valid age")
            return
        }
        if (jobTitle.isBlank()) {
            onError("Job title cannot be empty")
            return
        }

        viewModelScope.launch {
            _saving.value = true
            if (id > 0L) {
                // update
                repository.updateUser(User(id = id, name = name.trim(), age = age, jobTitle = jobTitle.trim(), gender = gender))
            } else {
                repository.insertUser(User(name = name.trim(), age = age, jobTitle = jobTitle.trim(), gender = gender))
            }
            _saving.value = false
            onSaved()
        }
    }
}
