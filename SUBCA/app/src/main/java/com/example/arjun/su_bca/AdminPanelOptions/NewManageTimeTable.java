package com.example.arjun.su_bca.AdminPanelOptions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.arjun.su_bca.R;
import com.example.arjun.su_bca.Utils.TimeTableFromFireStoreModel;
import com.example.arjun.su_bca.Utils.utility;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class NewManageTimeTable extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText editDay, editTime, editSubject;
    private Button addSubButton, nextButton;
    private boolean isFirstEntry = true;
    private SharedPreferences sharedPreferences;

    private TimeTableFromFireStoreModel model = new TimeTableFromFireStoreModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_manage_time_table);

        setupUIViews();
        initToolbar();
        allButtonFunctioning();

    }

    private void setupUIViews () {
        toolbar = (Toolbar) findViewById(R.id.toolbarNewManageTT);
        editDay = (EditText) findViewById(R.id.editDayManageTT);
        editTime = (EditText) findViewById(R.id.editTimeManageTT);
        editSubject = (EditText) findViewById(R.id.editSubjectsManageTT);
        addSubButton = (Button) findViewById(R.id.buttonAddSubsNewManageTT);
        nextButton = (Button) findViewById(R.id.buttonNextNewManageTT);
        sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
    }

    private void initToolbar () {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Insert New Time Table");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void allButtonFunctioning () {

        addSubButton.setOnClickListener( v -> {

            if (isFirstEntry) {
                String day = editDay.getText().toString().toLowerCase().trim().toLowerCase();
                String time = editTime.getText().toString().toLowerCase().trim().toLowerCase();
                if (day.isEmpty() || time.isEmpty()) {
                    if (day.isEmpty()) editDay.setError("day is required!!");
                    if (time.isEmpty()) editTime.setError("time is required!!");
                } else {
                    model.setDay(day);
                    model.setTime(time);
                    editDay.setVisibility(View.GONE);
                    editTime.setVisibility(View.GONE);
                    isFirstEntry = false;
                }
            }
            String subject = editSubject.getText().toString().trim();
            if (subject.isEmpty()) {
                editSubject.setError("subject is required!!");
            } else {

                List<String> subsArray = new ArrayList<>();
                if (model.getSubject_names() == null) {
                    subsArray.add(subject);
                } else {
                    subsArray = model.getSubject_names();
                    subsArray.add(subject);
                }
                model.setSubject_names(subsArray);
                editSubject.setText("");

            }
        });

        nextButton.setOnClickListener( v -> {

            addDataToFireBase(model.getDay());

        });

    }

    private void addDataToFireBase (String documentName) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("syllabus_database").document("database")
                .collection(sharedPreferences.getString("selectedCourse", "")).document(sharedPreferences.getString("selectedSemester", ""))
                .collection("time_table_subjects").document(documentName).set(model).addOnCompleteListener( task -> {

                    if (task.isSuccessful()) {
                        utility.showToast(NewManageTimeTable.this, "Added");
                        finish();
                    } else {
                        utility.showToast(NewManageTimeTable.this, task.getException().getLocalizedMessage());
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