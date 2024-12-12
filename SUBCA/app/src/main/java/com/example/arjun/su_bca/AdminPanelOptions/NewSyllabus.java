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
import com.example.arjun.su_bca.Utils.SyllabusFromFireStoreModel;
import com.example.arjun.su_bca.Utils.utility;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class NewSyllabus extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText uname, title, content;
    private Button append, next;
    private boolean isFirstEntry = true;
    private SyllabusFromFireStoreModel model = new SyllabusFromFireStoreModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_syllabus);

        setupUIViews();
        initToolbar();
        initButtons();

    }

    private void setupUIViews () {
        toolbar = (Toolbar) findViewById(R.id.toolbarNewSyllabus);
        uname = (EditText) findViewById(R.id.editUnamesNewSyllabus);
        title = (EditText) findViewById(R.id.editTitleNewSyllabus);
        content = (EditText) findViewById(R.id.editContentNewSyllabus);
        append = (Button) findViewById(R.id.buttonAppendNewSyllabus);
        next = (Button) findViewById(R.id.buttonNextNewSyllabus);
    }

    private void initToolbar () {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Insert New Syllabus");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initButtons () {

        append.setOnClickListener(v -> {

            if (isFirstEntry) {

                String unameStr = uname.getText().toString().trim().toLowerCase();

                if (unameStr.isEmpty()) {
                    uname.setError("uname is required!");
                } else {
                    model.setUname(unameStr);
                    uname.setVisibility(View.GONE);
                    isFirstEntry = false;
                }

            }

            String titleStr, contentStr;
            titleStr = title.getText().toString().trim();
            contentStr = content.getText().toString().trim();

            if (titleStr.isEmpty() || contentStr.isEmpty()) {
                if (titleStr.isEmpty()) title.setError("title is required!");
                if (contentStr.isEmpty()) content.setError("content is required!");
            } else {
                List<String> titleArray = new ArrayList<>();
                List<String> contentArray = new ArrayList<>();
                if (model.getSyllabus_list() == null && model.getTitle_list() == null) {
                    titleArray.add(titleStr);
                    contentArray.add(contentStr);
                } else {
                    titleArray = model.getTitle_list();
                    contentArray = model.getSyllabus_list();
                    titleArray.add(titleStr);
                    contentArray.add(contentStr);
                }
                model.setTitle_list(titleArray);
                model.setSyllabus_list(contentArray);
            }
            title.setText("");
            content.setText("");

        });

        next.setOnClickListener( v -> addToFirebase());

    }

    private void addToFirebase () {
        SharedPreferences sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        String course = sharedPreferences.getString("selectedCourse", "");
        String semester = sharedPreferences.getString("selectedSemester", "");
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("syllabus_database").document("database")
                .collection(course).document(semester)
                .collection("subjects").document(model.getUname())
                .set(model).addOnCompleteListener( task -> {
                    if (task.isSuccessful()) {
                        utility.showToast(NewSyllabus.this, "Saved");
                        finish();
                    } else {
                        utility.showToast(NewSyllabus.this, task.getException().getLocalizedMessage());
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