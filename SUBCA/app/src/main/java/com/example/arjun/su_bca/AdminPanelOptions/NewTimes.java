package com.example.arjun.su_bca.AdminPanelOptions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.arjun.su_bca.R;
import com.example.arjun.su_bca.Utils.TimesFromFireStoreModel;
import com.example.arjun.su_bca.Utils.utility;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class NewTimes extends AppCompatActivity {

    private EditText name, times;
    private Toolbar toolbar;
    private Button addButton, nextButton;
    private boolean isFirstEntry = true;
    private TimesFromFireStoreModel model = new TimesFromFireStoreModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_times);

        setupUIViews();
        initToolbar();
        initButtons();

    }

    private void setupUIViews () {

        name = (EditText) findViewById(R.id.editNameMT);
        times = (EditText) findViewById(R.id.editTimesMT);
        toolbar = (Toolbar) findViewById(R.id.toolbarNewMT);
        addButton = (Button) findViewById(R.id.buttonAddMT);
        nextButton = (Button) findViewById(R.id.buttonNextMT);

    }

    private void initToolbar () {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Insert New Time");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initButtons () {

        addButton.setOnClickListener( v -> {

            if (isFirstEntry) {
                String nameStr = name.getText().toString().trim().toLowerCase();
                if (nameStr.isEmpty()) {
                    name.setError("Name is required!");
                } else {
                    model.setName(nameStr);
                    isFirstEntry = false;
                    name.setVisibility(View.GONE);
                }
            }

            String time = times.getText().toString().trim();
            if (time.isEmpty()) {
                times.setError("Time is require!");
            } else {
                List<String> timeArray = new ArrayList<>();
                if (model.getTimes() == null) {
                    timeArray.add(time);
                } else {
                    timeArray = model.getTimes();
                    timeArray.add(time);
                }
                model.setTimes(timeArray);
                times.setText("");
            }

        });

        nextButton.setOnClickListener( v -> addTimeToFirebase());

    }

    private void addTimeToFirebase () {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("syllabus_database").document("database")
                .collection("smu_times").document(model.getName())
                .set(model).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        utility.showToast(NewTimes.this, "Saved");
                        finish();
                    } else {
                        utility.showToast(NewTimes.this, task.getException().getLocalizedMessage());
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