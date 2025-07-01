package com.example.codenation.repository

import com.example.codenation.model.Note
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import kotlin.jvm.java

class NoteRepository {
    private val dbRef = FirebaseDatabase.getInstance().getReference("notes")

    suspend fun fetchNotes(): List<Note> {
        val snapshot = dbRef.get().await()
        val notes = mutableListOf<Note>()
        for (child in snapshot.children) {
            val note = child.getValue(Note::class.java)
            note?.let { notes.add(it) }
        }
        return notes
    }
}