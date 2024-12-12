package com.example.arjun.su_bca.AdminPanelOptions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.arjun.su_bca.R;
import com.example.arjun.su_bca.Utils.utility;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class ManageSyllabus extends AppCompatActivity {

    private Toolbar toolbar;
    private Button newButton, deleteAllButton;
    private RecyclerView recyclerView;
    private boolean shouldFetchFirebase = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_syllabus);

        setupUIViews();
        initToolbar();
        setupRecyclerView();
        initButtons();

    }

    private void setupUIViews () {
        toolbar = (Toolbar) findViewById(R.id.toolbarMS);
        newButton = (Button) findViewById(R.id.newButtonMS);
        deleteAllButton = (Button) findViewById(R.id.deleteAllButtonMS);
        recyclerView = (RecyclerView) findViewById(R.id.rvMS);
    }

    private void initToolbar () {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Manage Syllabus");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupRecyclerView () {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ManageSyllabusAdapter adapter = new ManageSyllabusAdapter();
        recyclerView.setAdapter(adapter);
        if (shouldFetchFirebase) {
            adapter.setDocuments();
            shouldFetchFirebase = false;
        }
    }

    private void initButtons () {

        newButton.setOnClickListener( v -> startActivity(new Intent(ManageSyllabus.this, NewSyllabus.class)));

        deleteAllButton.setOnClickListener( v -> deleteAllFromFirebase());
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
                                    utility.showToast(ManageSyllabus.this, "Processed");
                                } else {
                                    // on failure.
                                    utility.showToast(ManageSyllabus.this, task.getException().getLocalizedMessage());
                                }

                            });
                })
                .setNegativeButton("Cancel", (dialog2, which) -> {
                    // do nothing.
                }).show();
        dialog.setCancelable(false);
    }
    private CollectionReference getDatabaseRef () {
        SharedPreferences sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        String course = sharedPreferences.getString("selectedCourse", "");
        String semester = sharedPreferences.getString("selectedSemester", "");
        return FirebaseFirestore.getInstance().collection("syllabus_database").document("database")
                .collection(course).document(semester)
                .collection("subjects");
    }

    public class ManageSyllabusAdapter extends RecyclerView.Adapter<ManageSyllabusAdapter.Holder> {

        List<DocumentSnapshot> documents = new ArrayList<>();

        public void setDocuments () {
            getDatabaseRef()
                    .get().addOnCompleteListener( task -> {

                        if (task.isSuccessful()) {

                            documents = task.getResult().getDocuments();
                            notifyDataSetChanged();

                        } else {
                            utility.showToast(ManageSyllabus.this, task.getException().getLocalizedMessage());
                        }

                    });
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_manage_times_single_item, parent, false);
            return new Holder(view);

        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {

            DocumentSnapshot document = documents.get(position);

            if (document.exists() && document != null) {

                holder.title.setText(document.getId());

            }

            holder.deleteButton.setOnClickListener( v -> {

                AlertDialog dialog = new AlertDialog.Builder(ManageSyllabus.this)
                        .setTitle("Delete " + document.getId())
                        .setMessage("this will permanently the data of " + document.getId() + ".\nare you sure about it?")
                        .setPositiveButton("Delete", (dialog1, which) -> {

                            getDatabaseRef().document(document.getId())
                                    .delete().addOnCompleteListener( task -> {

                                        if (task.isSuccessful()) {
                                            utility.showToast(ManageSyllabus.this, "Deleted");
                                        } else {
                                            utility.showToast(ManageSyllabus.this, task.getException().getLocalizedMessage());
                                        }

                                    });

                        })
                        .setNegativeButton("Cancel", (dialog2, which) -> {
                            // do nothing.
                        }).show();
                dialog.setCancelable(false);

            });

        }

        @Override
        public int getItemCount() {
            return documents.size();
        }

        public class Holder extends RecyclerView.ViewHolder {

            TextView title;
            ImageButton deleteButton;

            public Holder(@NonNull View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.tvMT);
                deleteButton = (ImageButton) itemView.findViewById(R.id.deleteButtonMTSI);
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}