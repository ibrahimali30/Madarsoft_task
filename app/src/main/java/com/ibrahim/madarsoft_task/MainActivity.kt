package com.ibrahim.madarsoft_task

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ibrahim.madarsoft_task.ui.theme.Madarsoft_taskTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.ibrahim.madarsoft_task.ui.input.InputScreen
import com.ibrahim.madarsoft_task.ui.display.DisplayScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Madarsoft_taskTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavHost(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "input") {
        composable(
            route = "input?userId={userId}",
            arguments = listOf(navArgument("userId") { type = NavType.LongType; defaultValue = -1L })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getLong("userId") ?: -1L
            InputScreen(
                onSaved = { navController.navigate("display") },
                userId = if (userId > 0L) userId else null,
                onNavigateToDisplay = { navController.navigate("display") }
            )
        }
        composable("display") {
            DisplayScreen(onBack = { navController.navigateUp() }, onEdit = { id ->
                navController.navigate("input?userId=$id")
            }, onAdd = {
                navController.navigate("input")
            })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Madarsoft_taskTheme {
    }
}