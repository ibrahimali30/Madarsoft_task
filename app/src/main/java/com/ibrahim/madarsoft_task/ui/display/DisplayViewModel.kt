package com.ibrahim.madarsoft_task.ui.display

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ibrahim.madarsoft_task.data.User
import com.ibrahim.madarsoft_task.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DisplayViewModel @Inject constructor(
    private val repository: UserRepository,
    private val started: SharingStarted = SharingStarted.WhileSubscribed(5000)
) : ViewModel() {

    val users: StateFlow<List<User>> = repository.getAllUsers()
        .stateIn(viewModelScope, started, emptyList())

    fun deleteUser(user: User, onDeleted: () -> Unit = {}) {
        viewModelScope.launch {
            repository.deleteUser(user)
            onDeleted()
        }
    }

}
