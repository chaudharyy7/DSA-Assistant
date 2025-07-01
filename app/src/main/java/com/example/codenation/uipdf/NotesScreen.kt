package com.example.codenation.uipdf

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.codenation.viewmodel.NoteViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import coil.compose.rememberAsyncImagePainter

@Composable
fun NotesScreen(navController: NavController, viewModel: NoteViewModel) {
    val notes by viewModel.notes.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(8.dp)
    ) {
        Text(
            text="Quick Revision",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (notes.isEmpty()) {
            Text(
                text = "No notes available",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
        }
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(notes.size) { index ->
                val note = notes[index]
                Card(
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .fillMaxWidth()
                        .clickable {
                            val encodedUrl =
                                URLEncoder.encode(note.url, StandardCharsets.UTF_8.toString())
                            navController.navigate("pdf_viewer/$encodedUrl")
                        }
                ) {
                    Row(modifier = Modifier.padding(8.dp)) {
                        Image(
                            painter = rememberAsyncImagePainter(note.imageUrl),
                            contentDescription = note.title,
                            modifier = Modifier
                                .size(60.dp)
                                .padding(end = 8.dp)
                        )

                        Text(
                            text = note.title,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier
                                .padding(vertical = 16.dp)
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}
