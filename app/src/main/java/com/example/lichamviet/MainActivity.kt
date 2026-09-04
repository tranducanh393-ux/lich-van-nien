package com.example.lichamviet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.lichamviet.data.auth.AuthManager
import com.example.lichamviet.data.repository.UserPreferencesRepository
import com.example.lichamviet.theme.LichAmVietTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    AuthManager.init(this)

    enableEdgeToEdge()
    setContent {
      val prefs by UserPreferencesRepository.preferences.collectAsState()
      LichAmVietTheme(theme = prefs.theme, dynamicColor = prefs.useDynamicColor) {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
          MainNavigation()
        }
      }
    }
  }
}
