package com.ibrahim.madarsoft_task.ui.input

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ibrahim.madarsoft_task.data.Gender
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List

fun generateRandomString(length: Int): String {
    val allowedChars = ('a'..'z') + ('A'..'Z') + ('0'..'9') // Define the character set
    return List(length) { allowedChars.random() }.joinToString("") // Create a list of random chars and join them
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputScreen(
    onSaved: () -> Unit,
    userId: Long? = null,
    onNavigateToDisplay: () -> Unit = {},
    viewModel: InputViewModel = hiltViewModel()
) {
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var job by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf(Gender.MALE) }
    val snackbarHostState = remember { SnackbarHostState() }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(errorMessage) {
        val msg = errorMessage
        if (msg != null) {
            snackbarHostState.showSnackbar(msg)
            errorMessage = null
        }
    }

    // If editing an existing user, load and prefill
    LaunchedEffect(userId) {
        userId?.let { id ->
            val user = viewModel.loadUser(id)
            user?.let {
                name = it.name
                age = it.age.toString()
                job = it.jobTitle
                gender = it.gender
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Input") }, actions = {
                IconButton(onClick = onNavigateToDisplay) {
                    Icon(Icons.Filled.List, contentDescription = "Show Users")
                }
            })
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(16.dp)
                .padding(padding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = age,
                onValueChange = { age = it },
                label = { Text("Age") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = job,
                onValueChange = { job = it },
                label = { Text("Job Title") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text("Gender")
            Row {
                Gender.entries.forEach { g ->
                    Row(
                        Modifier
                            .padding(end = 8.dp)
                            .selectable(selected = (gender == g), onClick = { gender = g }),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(selected = (gender == g), onClick = { gender = g })
                        Text(text = g.name, modifier = Modifier.padding(start = 4.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                viewModel.saveUser(id = userId ?: 0L, name = name, ageText = age, jobTitle = job, gender = gender, onSaved = {
                    // navigate
                    onSaved()
                }, onError = { message ->
                    errorMessage = message
                })
            }, modifier = Modifier.align(Alignment.End)) {
                Text("Save")
            }
        }
    }
}
