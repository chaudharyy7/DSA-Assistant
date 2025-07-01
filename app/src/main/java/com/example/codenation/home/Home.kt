package com.example.codenation.home

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.codenation.R
import com.example.codenation.navgraph.NavGraph
import com.example.codenation.viewmodel.NoteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(padding: PaddingValues) {
    val navController = rememberNavController()
    var selectedIndex = remember { mutableStateOf(0) }
    val noteViewModel: NoteViewModel = viewModel()
    val context = LocalContext.current
    val showBottomBar = remember { mutableStateOf(true) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        bottomBar = {
            if (showBottomBar.value) {
                NavigationBar {
                    NavigationBarItem(
                        selected = selectedIndex.value == 0,
                        onClick = {
                            selectedIndex.value = 0
                            navController.navigate("questions")
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.home),
                                modifier = Modifier.size(25.dp),
                                contentDescription = "Home"
                            )
                        },
                        label = { Text("Home") }
                    )
                    NavigationBarItem(
                        selected = selectedIndex.value == 1,
                        onClick = {
                            selectedIndex.value = 1
                            navController.navigate("compiler")
                            Toast.makeText(
                                context,
                                "Please enter input values before running the code. You can't add them afterward.",
                                Toast.LENGTH_LONG
                            ).show()
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.codeicon),
                                modifier = Modifier.size(25.dp),
                                contentDescription = "Code"
                            )
                        },
                        label = { Text("Compiler") }
                    )
                    NavigationBarItem(
                        selected = selectedIndex.value == 2,
                        onClick = {
                            selectedIndex.value = 2
                            navController.navigate("note")
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = R.drawable.book),
                                modifier = Modifier.size(25.dp),
                                contentDescription = "Note"
                            )
                        },
                        label = { Text("Notes") }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavGraph(
                navController = navController,
                noteViewModel = noteViewModel,
                showBottomBar = showBottomBar
            )
        }
    }
}