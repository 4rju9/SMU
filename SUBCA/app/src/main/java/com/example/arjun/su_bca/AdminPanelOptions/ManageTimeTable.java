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
import com.example.arjun.su_bca.Utils.TimeTableFromFireStoreModel;
import com.example.arjun.su_bca.utility;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class ManageTimeTable extends AppCompatActivity {

    private Toolbar toolbar;
    private Button newButton, deleteAllButton;
    private RecyclerView recyclerView;
    private boolean shouldFetchFirebase = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_time_table);

        setupUIViews();
        initToolbar();
        setupRecyclerView();
        allButtonFunctioning();

    }

    private void setupUIViews () {
        toolbar = (Toolbar) findViewById(R.id.toolbarManageTT);
        newButton = (Button) findViewById(R.id.newButtonManageTT);
        deleteAllButton = (Button) findViewById(R.id.deleteAllButtonManageTT);
        recyclerView = (RecyclerView) findViewById(R.id.rvManageTT);
    }

    private void initToolbar () {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Manage Time Table");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public DocumentReference getDatabaseRef() {
        SharedPreferences sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        String selectedCourse = sharedPreferences.getString("selectedCourse", "");
        String selectedSemester = sharedPreferences.getString("selectedSemester", "");
        return FirebaseFirestore.getInstance()
                .collection("syllabus_database").document("database")
                .collection(selectedCourse).document(selectedSemester);
    }

    private void allButtonFunctioning () {
        newButton.setOnClickListener( v -> startActivity(new Intent(ManageTimeTable.this, NewManageTimeTable.class)));

        deleteAllButton.setOnClickListener( v -> deleteAllTimeTableFromFirebase());
    }

    private void deleteAllTimeTableFromFirebase () {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Delete All Data!!")
                .setMessage("this will delete all the data from database.\nare you sure about it?")
                .setPositiveButton("Delete", (dialog1, which) -> {
                    getDatabaseRef()
                            .collection("time_table_subjects").get()
                            .addOnCompleteListener( task -> {

                                if (task.isSuccessful()) {
                                    // on success.
                                    List<DocumentSnapshot> documents = task.getResult().getDocuments();
                                    documents.forEach(document -> {
                                        if (document.exists() && document != null) {
                                            getDatabaseRef().collection("time_table_subjects")
                                                    .document(document.getId()).delete();
                                        }
                                    });
                                    utility.showToast(ManageTimeTable.this, "Processed");
                                } else {
                                    // on failure.
                                    utility.showToast(ManageTimeTable.this, task.getException().getLocalizedMessage());
                                }

                            });
                })
                .setNegativeButton("Cancel", (dialog2, which) -> {
                    // do nothing.
                }).show();
        dialog.setCancelable(false);
    }

    private void setupRecyclerView () {

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ManageTTAdapter adapter = new ManageTTAdapter();
        recyclerView.setAdapter(adapter);


        if (shouldFetchFirebase) {
            adapter.setDocuments();
            shouldFetchFirebase = false;
        }
    }

    public class ManageTTAdapter extends RecyclerView.Adapter<ManageTTAdapter.ManageTTHolder> {

        List<DocumentSnapshot> documents = new ArrayList<>();

        public void setDocuments () {

            getDatabaseRef()
                    .collection("time_table_subjects").orderBy("day", Query.Direction.ASCENDING).get()
                    .addOnCompleteListener(task -> {

                        if(task.isSuccessful()) {
                            documents = task.getResult().getDocuments();
                            notifyDataSetChanged();
                        }

                    });

        }

        @NonNull
        @Override
        public ManageTTHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_manage_timetable_single_item, parent, false);
            return new ManageTTHolder(view);

        }

        @Override
        public void onBindViewHolder(@NonNull ManageTTHolder holder, int position) {

            DocumentSnapshot document = documents.get(position);

            if (document.exists() && document != null) {
                TimeTableFromFireStoreModel model = document.toObject(TimeTableFromFireStoreModel.class);

                if (model != null) {

                    // setting holder views.

                    holder.title.setText(model.getDay());

                }

            }

            holder.deleteButton.setOnClickListener( (v) -> {
                AlertDialog dialog = new AlertDialog.Builder(ManageTimeTable.this)
                        .setTitle("Delete " + document.getId())
                        .setMessage("This will permanently delete the data of " + document.getId())
                        .setPositiveButton("Delete", (dialog1, which) -> {
                            getDatabaseRef()
                                    .collection("time_table_subjects").document(document.getId())
                                    .delete().addOnCompleteListener( task -> {
                                        if (task.isSuccessful()) {
                                            utility.showToast(ManageTimeTable.this, "Deleted");
                                            setDocuments();
                                        } else {
                                            utility.showToast(ManageTimeTable.this, task.getException().getLocalizedMessage());
                                        }
                                    });
                        })
                        .setNegativeButton("Cancel", (dialog2, which) -> {
                            // do negative task.
                        }).show();
                dialog.setCancelable(false);
            });

        }

        @Override
        public int getItemCount() {
            return documents.size();
        }

        public class ManageTTHolder extends RecyclerView.ViewHolder {

            TextView title;
            ImageButton deleteButton;

            public ManageTTHolder(@NonNull View itemView) {
                super(itemView);
                title = (TextView) itemView.findViewById(R.id.tvManageTT);
                deleteButton = (ImageButton) itemView.findViewById(R.id.deleteButtonManageTTSI);
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