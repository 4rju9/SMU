package com.example.arjun.su_bca.AdminPanelOptions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.arjun.su_bca.R;
import com.example.arjun.su_bca.utility;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ManageAllSubjects extends AppCompatActivity {

    private Toolbar toolbar;
    private Button newButton, deleteAllButton;
    private ImageButton deleteButton;
    private TextView title;
    private boolean shouldFetchFirebase = true, documentExist = false;
    private DocumentSnapshot document;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_subjects);

        setupUIViews();
        initToolbar();
        fetchFirebase();
        initButtons();


    }

    private void setupUIViews () {

        toolbar = (Toolbar) findViewById(R.id.toolbarAS);
        newButton = (Button) findViewById(R.id.newButtonAS);
        deleteAllButton = (Button) findViewById(R.id.deleteAllButtonAS);
        deleteButton = (ImageButton) findViewById(R.id.deleteButtonAS);
        title = (TextView) findViewById(R.id.tvAS);

    }

    private void initToolbar () {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Manage All Subjects");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private CollectionReference getDatabaseRef () {
        SharedPreferences sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        String semester = sharedPreferences.getString("selectedSemester", "");
        String course = sharedPreferences.getString("selectedCourse", "");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        return db.collection("syllabus_database").document("database")
                .collection(course).document(semester)
                .collection("all_subjects");
    }

    private void fetchFirebase () {

        if (shouldFetchFirebase) {
            getDatabaseRef()
                    .get().addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> documents = task.getResult().getDocuments();
                            if (documents.size() >= 1) {
                                document = documents.get(0);
                                if (document.exists() && document != null) {
                                    title.setText(document.getId());
                                    documentExist = true;
                                    shouldFetchFirebase = false;
                                }
                            } else {
                                title.setText("no data (restart or refresh)");
                            }
                        } else {
                            utility.showToast(ManageAllSubjects.this, task.getException().getLocalizedMessage());
                        }

                    });
        }
    }

    private void deleteAllFromFirebase () {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Delete All Data!!")
                .setMessage("this will delete all data from the database.\nare you sure about it?")
                .setPositiveButton("Deleted", (dialog1, which) -> {
                    getDatabaseRef()
                            .get()
                            .addOnCompleteListener( task -> {

                                if (task.isSuccessful()) {
                                    // on success.
                                    List<DocumentSnapshot> documents = task.getResult().getDocuments();
                                    documents.forEach(document -> {
                                        if (document.exists() && document != null) {
                                            getDatabaseRef()
                                                    .document(document.getId()).delete();
                                        }
                                    });
                                    utility.showToast(ManageAllSubjects.this, "Processed");
                                } else {
                                    // on failure.
                                    utility.showToast(ManageAllSubjects.this, task.getException().getLocalizedMessage());
                                }

                            });
                })
                .setNegativeButton("Cancel", (dialog2, which) -> {
                    // do nothing.
                }).show();
        dialog.setCancelable(false);
    }

    private void initButtons () {

        newButton.setOnClickListener( v -> {
            startActivity(new Intent(ManageAllSubjects.this, NewAllSubjects.class));
        });

        deleteAllButton.setOnClickListener( v -> {
            deleteAllFromFirebase();
        });

        deleteButton.setOnClickListener( v -> {
            if (documentExist) {
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("Delete " + document.getId())
                        .setMessage("this will permanently delete " + document.getId() + ".\nare you sure about it?")
                        .setPositiveButton("Delete", (dialog1, which) -> {
                            getDatabaseRef().document(document.getId())
                                    .delete().addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            utility.showToast(ManageAllSubjects.this, "Deleted");
                                            documentExist = false;
                                        } else {
                                            utility.showToast(ManageAllSubjects.this, task.getException().getLocalizedMessage());
                                        }
                                    });
                        })
                        .setNegativeButton("Cancel", (dialog2, which) -> {
                            // do nothing.
                        }).show();
                dialog.setCancelable(false);
            }
            else {
                utility.showToast(ManageAllSubjects.this, "something went wrong!, please try again after sometime.");
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}