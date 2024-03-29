package com.example.arjun.su_bca;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Query;

public class NotesActivity extends AppCompatActivity {

    private FloatingActionButton addNoteBtn;
    private RecyclerView recyclerView;
    private ImageButton backButton;
    private NoteAdapter noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        setupUIViews();
        allButtonFunctioning();
        setupRecyclerView();

    }

    private void setupUIViews() {
        addNoteBtn = (FloatingActionButton)findViewById(R.id.add_note_button);
        recyclerView = (RecyclerView)findViewById(R.id.notesRecyclerView);
        backButton = (ImageButton)findViewById(R.id.backButton);
    }

    private void allButtonFunctioning() {
        addNoteBtn.setOnClickListener((v)-> startActivity(new Intent(NotesActivity.this, NoteDetailsActivity.class)));
        backButton.setOnClickListener((v)-> super.onBackPressed());
    }

    private void setupRecyclerView() {
        Query query = utility.getCollectionReferenceForNotes().orderBy("timeStamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter = new NoteAdapter(options, this);
        recyclerView.setAdapter(noteAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        noteAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        noteAdapter.notifyDataSetChanged();
    }

}