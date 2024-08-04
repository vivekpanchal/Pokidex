package com.vivek.pokidex.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.vivek.pokidex.presentation.navigation.Navigation
import com.vivek.pokidex.presentation.ui.theme.PokidexTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokidexTheme {
                val navController = rememberNavController()
                Navigation(navController = navController)
            }
        }
    }
}