package com.example.codenation.editor

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.codenation.model.JDoodleRequest
import com.example.codenation.model.JDoodleResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompilerScreen() {
    val coroutineScope = rememberCoroutineScope()
    val bottomSheetState = rememberModalBottomSheetState()
    val showBottomSheet = remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var code by remember { mutableStateOf("") }
    var language by remember { mutableStateOf("java") }
    var outputText by remember { mutableStateOf("") }
    val context = LocalContext.current

    val langList = listOf("java", "python3", "cpp", "javascript")

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text="Online Compiler",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
            )
        DropdownMenuBox(
            options = langList,
            selected = language,
            onSelected = { language = it }
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(vertical = 10.dp)
        ) {
            OutlinedTextField(
                value = code,
                onValueChange = { code = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
                    .padding(top = 10.dp),
                label = { Text("Write Your Code")}
            )
        }

        FloatingActionButton(
            onClick = {

                if (code.isBlank()) {
                    Toast.makeText(context, "Please enter some code", Toast.LENGTH_SHORT).show()
                }else{
                    isLoading = true
                    outputText = ""
                    showBottomSheet.value = true
                    coroutineScope.launch {
                        bottomSheetState.show()
                    }
                    val request = JDoodleRequest(
                        clientId = "638e32cddca3f2b642c4183cb12dece8",
                        clientSecret = "86ed715207ed1db1f1f24dce89a2ccdc4a5193f998a2fb398636f2f4781607b9",
                        script = code,
                        language = language,
                        versionIndex = "0"
                    )

                    RetrofitClient.api.executeCode(request).enqueue(object : Callback<JDoodleResponse> {
                        override fun onResponse(call: Call<JDoodleResponse>, response: Response<JDoodleResponse>) {
                            outputText = response.body()?.output ?: "Error"
                            isLoading = false
                        }

                        override fun onFailure(call: Call<JDoodleResponse>, t: Throwable) {
                            outputText = "Failed: ${t.message}"
                            isLoading = false
                        }
                    })
                }

            },
            modifier = Modifier.padding(top = 8.dp)
                .align(Alignment.End),
        ) {
            Icon(Icons.Filled.PlayArrow, "Run Code")
        }
    }

    if (showBottomSheet.value) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet.value = false },
            sheetState = bottomSheetState
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Output", style = MaterialTheme.typography.titleLarge)

                if (isLoading) {
                    Spacer(modifier = Modifier.height(24.dp))
                    CircularProgressIndicator()
                } else {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(outputText)
                }
            }
        }
    }
}
@Composable
fun DropdownMenuBox(
    options: List<String>,
    selected: String,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var customInputVisible by remember { mutableStateOf(false) }
    var customText by remember { mutableStateOf("") }

    Column {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { Text("Select Language") },
            trailingIcon = {
                IconButton(onClick = { expanded = true }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Select language")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach {
                DropdownMenuItem(
                    text = { Text(it) },
                    onClick = {
                        onSelected(it)
                        expanded = false
                    }
                )
            }
            Divider()
            DropdownMenuItem(
                text = { Text("Other...") },
                onClick = {
                    customInputVisible = true
                    expanded = false
                }
            )
        }

        if (customInputVisible) {
            OutlinedTextField(
                value = customText,
                onValueChange = { customText = it },
                label = { Text("Enter custom language") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )
            ElevatedButton(
                onClick = {
                    if (customText.isNotBlank()) {
                        onSelected(customText.trim())
                        customInputVisible = false
                        customText = ""
                    }
                },
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Text("Set Language")
            }
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CompilerScreenPreview() {
    CompilerScreen()
}