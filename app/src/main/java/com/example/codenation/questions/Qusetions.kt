package com.example.codenation.questions

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.colintheshots.twain.MarkdownText
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionScreen() {
    var question by remember { mutableStateOf("") }
    var responseText by remember { mutableStateOf("") }
    var isSubmitted by remember { mutableStateOf(false) }
    var solutionText by remember { mutableStateOf("") }
    var isLoadingSolution by remember { mutableStateOf(false) }
    var count by remember { mutableStateOf(0) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val difficulties = listOf("Easy", "Medium", "Hard")
    var expanded by remember { mutableStateOf(false) }
    var selectedDifficulty by remember { mutableStateOf("Medium") }

    val bottomSheetState = rememberModalBottomSheetState()
    val showBottomSheet = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "DSA Assistant",
                style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = question,
                onValueChange = { question = it },
                label = { Text("Enter any topic or ask") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(fontSize = 18.sp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = selectedDifficulty,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Select Difficulty") },
                    trailingIcon = {
                        IconButton(onClick = { expanded = true }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "Select difficulty")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    difficulties.forEach { difficulty ->
                        DropdownMenuItem(
                            text = { Text(difficulty) },
                            onClick = {
                                selectedDifficulty = difficulty
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                ElevatedButton(
                    onClick = {
                        if (question.isNotBlank()) {
                            responseText = "Loading..."
                            GeminiHelper.getDSAQuestion(
                                "$question - Difficulty: $selectedDifficulty",
                                mode = "question"
                            ) { result ->
                                Handler(Looper.getMainLooper()).post {
                                    responseText = result
                                }
                            }
                            isSubmitted = true
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                ) {
                    Text("Get DSA Question", fontWeight = FontWeight.Bold)
                }

                ElevatedButton(
                    onClick = {
                        showBottomSheet.value = true
                        if(count < 3) {
                            count++
                            isLoadingSolution = true
                            solutionText = ""
                            coroutineScope.launch {
                                GeminiHelper.getDSAQuestion(
                                    userQuery = "",
                                    mode = "motivation"
                                ) { result ->
                                    Handler(Looper.getMainLooper()).post {
                                        solutionText = result
                                        isLoadingSolution = false
                                    }
                                }
                            }
                        }else{
                            isLoadingSolution = true
                            solutionText = ""
                            coroutineScope.launch {
                                GeminiHelper.getDSAQuestion(
                                    userQuery = responseText,
                                    mode = "solution"
                                ) { result ->
                                    Handler(Looper.getMainLooper()).post {
                                        solutionText = result
                                        isLoadingSolution = false
                                    }
                                }
                            }
                            count = 0
                        }
                    },
                    enabled = isSubmitted,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                ) {
                    Text("See Solution")
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(vertical = 10.dp)
        ) {
            if (responseText.isNotBlank()) {
                MarkdownText(
                    markdown = responseText,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 16.sp
                )
            }
        }
    }

    if (showBottomSheet.value) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet.value = false },
            sheetState = bottomSheetState
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight(0.75f)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = "Solution",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    if (isLoadingSolution) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        SelectionContainer {
                            MarkdownText(
                                markdown = solutionText,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.fillMaxWidth(),
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun QuestionScreenPreview() {
    QuestionScreen()
}