package com.chamer.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.chamer.app.ui.ChamerApp
import com.chamer.app.viewmodel.ChamerViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: ChamerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChamerApp(viewModel = viewModel)
        }
    }
}
