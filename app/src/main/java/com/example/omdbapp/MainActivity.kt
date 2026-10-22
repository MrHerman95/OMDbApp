package com.example.omdbapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.omdbapp.core.designsystem.theme.OMDbAppTheme
import com.example.omdbapp.navigation.OmdbNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OMDbAppTheme {
                OmdbNavHost()
            }
        }
    }
}
