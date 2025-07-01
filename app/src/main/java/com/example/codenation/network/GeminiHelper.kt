package com.example.codenation.questions

import android.os.Handler
import android.os.Looper
import android.service.autofill.Validators.or
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

object GeminiHelper {
    private val client = OkHttpClient()
    private const val apiKey = "AIzaSyC2Cc0_FfiIdd1yGcxf8izEWHIIqNLpl60"

    fun getDSAQuestion(
        userQuery: String,
        mode: String,
        onResult: (String) -> Unit
    ) {
        val prompt = if (mode == "question") {
                    "        -You are a DSA (Data Structures & Algorithms) teacher.\n" +
                    "        -if user ask your name then Your name is Kaira, if user ask for developer detail then tell this about developer with better format,His is Vimal Chaudhary And his telegram id is @The_deadMad and instagram id his is @__.chaudhary07.__"+
                            "   Dont tell anything about developer and your name untill user havent ask for it\n"+
                    "        - If someone ask about you and developer, that time don't provide any question regarding DSA\n"+
                    "        - Give one unique and meaningful DSA question based on the user's topic.Question should be like leetcode.\n" +
                    "        - ❌ Do NOT provide any solution and test cases. Just give one example for better underrating of question\n" +
                    "        - ✅ If the user asks for an answer, say: \"Try solving it first\n" +
                    "        - Format your response using *Markdown* syntax. Use code blocks, bullet points, and bold where helpful\n"+
                    "        - If user ask any other question rather than DSA related question, reply them rudely\n"+
                    "        - Use english or Hinglish only for reply according to which language used by user"
        } else if (mode == "solution") {
            "You are a helpful AI. Give a clean and fully working **DSA solution in Java and C++ only** with step by step code explanation for the following question. " +
                    "Also provide theory explanation of the solution\n"+
                    "Use Markdown formatting and code blocks. Do not explain unless asked. Keep it clean and clear."
        }else{
            "You are a motivational guide who inspires users to become better coders and programmers by solving DSA (Data Structures & Algorithms) questions on their own.\n" +
                    "Use a mix of Hinglish and English to make it relatable and fun.\n" +
                    "Encourage users to try solving that question at least 3 times before asking for the solution. and i will unlock solution if you still needed"
        }

        val requestBodyJson = JSONObject().apply {
            put("contents", JSONArray().apply {
                put(JSONObject().apply {
                    put(JSONObject().apply {
                        put("role", "model")
                        put("parts", JSONArray().put(JSONObject().put("text", prompt)))
                    })
                    put("role", "user")
                    put("parts", JSONArray().put(JSONObject().put("text", userQuery)))
                })
            })
        }

        val requestBody = RequestBody.create(
            "application/json".toMediaTypeOrNull(),
            requestBodyJson.toString()
        )

        val request = Request.Builder()
            .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=$apiKey")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Handler(Looper.getMainLooper()).post {
                    onResult("\u274C Error: ${e.message}")
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val raw = response.body?.string()

                if (raw.isNullOrBlank()) {
                    Handler(Looper.getMainLooper()).post {
                        onResult("\u274C Empty response from Gemini.")
                    }
                    return
                }

                try {
                    val json = JSONObject(raw)

                    if (json.has("error")) {
                        Handler(Looper.getMainLooper()).post {
                            onResult("Something is wrong, please try again later!")
                        }
                        return
                    }

                    val reply = json
                        .getJSONArray("candidates")
                        .getJSONObject(0)
                        .getJSONObject("content")
                        .getJSONArray("parts")
                        .getJSONObject(0)
                        .getString("text")

                    Handler(Looper.getMainLooper()).post {
                        onResult(reply)
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                    Handler(Looper.getMainLooper()).post {
                        onResult("\u274C Failed to parse Gemini response: ${e.message}")
                    }
                }
            }
        })
    }
}
