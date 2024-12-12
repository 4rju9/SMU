package com.example.arjun.su_bca.AdminPanelOptions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.arjun.su_bca.R;
import com.example.arjun.su_bca.Utils.TimesFromFireStoreModel;
import com.example.arjun.su_bca.Utils.utility;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ManageTimes extends AppCompatActivity {

    private Toolbar toolbar;
    private Button newButton, deleteAllButton;
    private RecyclerView recyclerView;
    private boolean shouldFetchFirebase = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_times);

        setupUIViews();
        initToolbar();
        setupRecyclerView();
        allButtonFunctioning();

    }

    private void setupUIViews () {

        toolbar = (Toolbar) findViewById(R.id.toolbarMT);
        newButton = (Button) findViewById(R.id.newButtonMT);
        deleteAllButton = (Button) findViewById(R.id.deleteAllButtonMT);
        recyclerView = (RecyclerView) findViewById(R.id.rvMT);

    }

    private void initToolbar () {

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Manage Times");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private CollectionReference getDatabaseRef () {
        return FirebaseFirestore.getInstance()
                .collection("syllabus_database").document("database")
                .collection("smu_times");
    }

    private void allButtonFunctioning () {

        newButton.setOnClickListener( v -> startActivity(new Intent(ManageTimes.this, NewTimes.class)));

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
                                    utility.showToast(ManageTimes.this, "Processed");
                                } else {
                                    // on failure.
                                    utility.showToast(ManageTimes.this, task.getException().getLocalizedMessage());
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
        ManageTimesAdapter adapter = new ManageTimesAdapter();
        recyclerView.setAdapter(adapter);
        if (shouldFetchFirebase) {
            adapter.setDocuments();
            shouldFetchFirebase = false;
        }
    }

    public class ManageTimesAdapter extends RecyclerView.Adapter<ManageTimesAdapter.Holder> {

        List<DocumentSnapshot> documents = new ArrayList<>();

        public void setDocuments () {
            getDatabaseRef()
                    .get().addOnCompleteListener( task -> {

                        if (task.isSuccessful()) {

                            documents = task.getResult().getDocuments();
                            notifyDataSetChanged();

                        } else {
                            utility.showToast(ManageTimes.this, task.getException().getLocalizedMessage());
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

                TimesFromFireStoreModel model = document.toObject(TimesFromFireStoreModel.class);

                if (model != null) {

                    holder.title.setText(model.getName());

                }

            }

            holder.deleteButton.setOnClickListener( v -> {

                AlertDialog dialog = new AlertDialog.Builder(ManageTimes.this)
                        .setTitle("Delete " + document.getId())
                        .setMessage("this will permanently delete the data of " + document.getId())
                        .setPositiveButton("Delete", (dialog1, which) -> {
                            getDatabaseRef()
                                    .document(document.getId())
                                    .delete()
                                    .addOnCompleteListener( task -> {

                                        if (task.isSuccessful()) {
                                            utility.showToast(ManageTimes.this, "Deleted");
                                        } else {
                                            utility.showToast(ManageTimes.this, task.getException().getLocalizedMessage());
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