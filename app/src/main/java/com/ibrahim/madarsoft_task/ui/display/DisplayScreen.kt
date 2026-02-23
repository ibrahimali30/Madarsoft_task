package com.ibrahim.madarsoft_task.ui.display

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ibrahim.madarsoft_task.data.User
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Text
import androidx.compose.material3.IconButton

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DisplayScreen(
    onBack: () -> Unit,
    onEdit: (Long) -> Unit,
    onAdd: () -> Unit,
    viewModel: DisplayViewModel = hiltViewModel()
) {
    val users = viewModel.users.collectAsState()
    var userToDelete by remember { mutableStateOf<User?>(null) }

    DeleteConfirmDialog(user = userToDelete, onConfirm = { user ->
        viewModel.deleteUser(user)
        userToDelete = null
    }, onDismiss = { userToDelete = null })

    Scaffold(topBar = {
        TopAppBar(title = { Text("Users") }, navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        })
    }) { padding ->
        // padding is Scaffold's inner PaddingValues; use it as contentPadding for LazyColumn
        if (users.value.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding) // respect scaffold top bar
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .wrapContentSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "No users",
                        modifier = Modifier.size(72.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("No users saved yet")
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(onClick = onAdd) {
                        Icon(Icons.Filled.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add New User")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = padding
            ) {
                items(users.value, key = { it.id }) { user ->
                    UserRow(user, modifier = Modifier.animateItem(), onEdit = { onEdit(user.id) }, onDelete = { userToDelete = user })
                }
            }
        }
    }
}

@Composable
fun DeleteConfirmDialog(user: User?, onConfirm: (User) -> Unit, onDismiss: () -> Unit) {
    if (user == null) return

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirm delete") },
        text = { Text("Are you sure you want to delete ${user.name}?") },
        confirmButton = {
            TextButton(onClick = { onConfirm(user) }) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun UserRow(user: User, modifier: Modifier = Modifier, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(modifier = modifier
        .fillMaxWidth()
        .padding(8.dp)) {
        Row(modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = user.name, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Age: ${user.age}")
                Text(text = "Job: ${user.jobTitle}")
                Text(text = "Gender: ${user.gender.name}")
            }
            Row(modifier = Modifier.align(Alignment.CenterVertically)) {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Filled.Edit, contentDescription = "Edit")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete")
                }
            }
        }
    }
}
