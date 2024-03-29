package com.example.arjun.su_bca.AdminPanelOptions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import kotlinx.coroutines.flow.SharedFlow;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.example.arjun.su_bca.R;
import com.example.arjun.su_bca.Utils.SubjectsFromFireStoreModel;
import com.example.arjun.su_bca.utility;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class NewAllSubjects extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText name, uname;
    private Button appendButton, nextButton;
    private SubjectsFromFireStoreModel model = new SubjectsFromFireStoreModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_all_subjects);

        setupUIViews();
        initToolbar();
        initButton();

    }

    private void setupUIViews () {

        toolbar = (Toolbar) findViewById(R.id.toolbarNewAS);
        name = (EditText) findViewById(R.id.editNamesAS);
        uname = (EditText) findViewById(R.id.editUnamesAS);
        appendButton = (Button) findViewById(R.id.buttonAddAS);
        nextButton = (Button) findViewById(R.id.buttonNextAS);

    }

    private void initToolbar () {

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Insert New Subjects");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void initButton () {

        appendButton.setOnClickListener( v -> {
            String nameStr, unameStr;
            nameStr = name.getText().toString().trim();
            unameStr = uname.getText().toString().trim().toLowerCase();

            if (nameStr.isEmpty() || unameStr.isEmpty()) {
                if (nameStr.isEmpty()) name.setError("name is required!");
                if (unameStr.isEmpty()) uname.setError("uname is required!");
            } else {
                List<String> names = new ArrayList<>();
                List<String> unames = new ArrayList<>();
                if (model.getNames() == null && model.getUnames() == null) {
                    names.add(nameStr);
                    unames.add(unameStr);
                } else {
                    names = model.getNames();
                    unames = model.getUnames();
                    names.add(nameStr);
                    unames.add(unameStr);
                }
                model.setNames(names);
                model.setUnames(unames);
            }
            name.setText("");
            uname.setText("");

        });

        nextButton.setOnClickListener( v -> addToDatabase());

    }

    private void addToDatabase () {
        SharedPreferences sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        String semester = sharedPreferences.getString("selectedSemester", "");
        String course = sharedPreferences.getString("selectedCourse", "");
        FirebaseFirestore.getInstance().collection("syllabus_database").document("database")
                .collection(course).document(semester)
                .collection("all_subjects").document("subject_names")
                .set(model).addOnCompleteListener( task -> {

                    if (task.isSuccessful()) {
                        utility.showToast(NewAllSubjects.this, "Saved");
                        finish();
                    } else {
                        utility.showToast(NewAllSubjects.this, task.getException().getLocalizedMessage());
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