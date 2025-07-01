package com.example.codenation.navgraph

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.codenation.editor.CompilerScreen
import com.example.codenation.questions.QuestionScreen
import com.example.codenation.uipdf.NotesScreen
import com.example.codenation.uipdf.PdfViewerScreen
import com.example.codenation.viewmodel.NoteViewModel
import java.net.URLDecoder
import androidx.compose.runtime.DisposableEffect

@Composable
fun NavGraph(
    navController: NavHostController,
    noteViewModel: NoteViewModel,
    showBottomBar: MutableState<Boolean>
) {
    NavHost(navController = navController, startDestination = "questions") {
        composable("questions") { QuestionScreen() }
        composable("compiler") { CompilerScreen() }
        composable("note") { NotesScreen(navController = navController, viewModel = noteViewModel) }
        composable("pdf_viewer/{url}") { backStackEntry ->
            showBottomBar.value = false
            DisposableEffect(Unit) {
                onDispose {
                    showBottomBar.value = true
                }
            }
            val encodedUrl = backStackEntry.arguments?.getString("url")
            encodedUrl?.let {
                val decodedUrl = URLDecoder.decode(it, "UTF-8")
                PdfViewerScreen(url = decodedUrl)
            }
        }
    }
}
