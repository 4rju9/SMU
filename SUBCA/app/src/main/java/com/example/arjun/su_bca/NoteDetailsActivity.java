package com.example.arjun.su_bca;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class NoteDetailsActivity extends AppCompatActivity {

    private EditText titleEditText, contentEditText;
    private ImageButton saveNoteButton, deleteNoteButton;
    private TextView pageTitleTextView;
    private String title, content, docId;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        setupUIViews();
        saveNoteButtonFunctioning();

    }

    private void receiveData() {
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");

        if(docId!=null && !docId.isEmpty()) {
            isEditMode = true;
        }

        if(isEditMode) {
            titleEditText.setText(title);
            contentEditText.setText(content);
            pageTitleTextView.setText("Edit Your Note");
            deleteNoteButton.setVisibility(View.VISIBLE);
        }
    }

    private void setupUIViews() {
        titleEditText = (EditText)findViewById(R.id.add_notes_title_text);
        contentEditText = (EditText)findViewById(R.id.add_notes_content_text);
        saveNoteButton = (ImageButton)findViewById(R.id.save_note_button);
        deleteNoteButton = (ImageButton)findViewById(R.id.delete_note_button);
        pageTitleTextView = (TextView)findViewById(R.id.page_title);
    }

    private void saveNoteButtonFunctioning() {
        receiveData();
        saveNoteButton.setOnClickListener((v)-> saveNote());
        deleteNoteButton.setOnClickListener((v)-> deleteNoteFromFirebase());
    }

    private void saveNote() {
        String noteTitle = titleEditText.getText().toString();
        String noteContent = contentEditText.getText().toString();

        if(noteTitle == null || noteTitle.isEmpty()) {
            titleEditText.setError("Title is required!");
            return;
        }

        Note note = new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimeStamp(Timestamp.now());

        saveNoteToFirebase(note);

    }

    private void saveNoteToFirebase(Note note) {
        DocumentReference documentReference;

        if(isEditMode) {
            documentReference = utility.getCollectionReferenceForNotes().document(docId);
        } else {
            documentReference = utility.getCollectionReferenceForNotes().document();
        }

        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    utility.showToast(NoteDetailsActivity.this, "Saved");
                    finish();
                } else {
                    utility.showToast(NoteDetailsActivity.this, "Someting went wrong, Please try again after sometime");
                }
            }
        });
    }

    private void deleteNoteFromFirebase() {
        DocumentReference documentReference;
        documentReference = utility.getCollectionReferenceForNotes().document(docId);

        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    utility.showToast(NoteDetailsActivity.this, "Deleted");
                    finish();
                } else {
                    utility.showToast(NoteDetailsActivity.this, "Someting went wrong, Please try again after sometime");
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}