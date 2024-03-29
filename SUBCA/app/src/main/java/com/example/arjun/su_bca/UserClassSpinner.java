package com.example.arjun.su_bca;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.arjun.su_bca.RoomDatabase.SubjectDatabase;
import com.example.arjun.su_bca.RoomDatabase.SubjectEntity;
import com.example.arjun.su_bca.RoomDatabase.SyllabusDatabase;
import com.example.arjun.su_bca.RoomDatabase.SyllabusEntity;
import com.example.arjun.su_bca.RoomDatabase.TimeTableDatabase;
import com.example.arjun.su_bca.RoomDatabase.TimeTableEntitiy;
import com.example.arjun.su_bca.RoomDatabase.TimesDatabase;
import com.example.arjun.su_bca.RoomDatabase.TimesEntity;
import com.example.arjun.su_bca.Utils.SubjectsFromFireStoreModel;
import com.example.arjun.su_bca.Utils.SyllabusFromFireStoreModel;
import com.example.arjun.su_bca.Utils.TimeTableFromFireStoreModel;
import com.example.arjun.su_bca.Utils.TimesFromFireStoreModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

public class UserClassSpinner extends AppCompatActivity {

    private String selectedCourse, selectedSemester;
    private TextView tvCourse, tvSemester;
    private Spinner courseSpinner, semesterSpinner;
    private ArrayAdapter<CharSequence> courseAdapter, semesterAdapter;
    private Button nextButton, skipButton;
    private ProgressBar progressBar;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_class_spinner);

        setupUIViews();
        setupSpinner();
        allButtonFunctioning();


    }

    private void setupUIViews() {

        sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);

        TextView helloText = (TextView) findViewById(R.id.spinnerHelloTxt);
        nextButton = (Button) findViewById(R.id.spinnerNextButton);
        skipButton = (Button) findViewById(R.id.spinnerSkipButton);
        progressBar = (ProgressBar) findViewById(R.id.spinnerProgressBar);
        courseSpinner = (Spinner) findViewById(R.id.selectCourse);
        tvCourse = (TextView) findViewById(R.id.tvCourseLabel);
        tvSemester = (TextView) findViewById(R.id.tvSemesterLabel);
        String name = sharedPreferences.getString("user_first_name", "there");
        name = "Hello " + name + "!";
        helloText.setText(name);
    }

    private void allButtonFunctioning() {
        nextButton.setOnClickListener((v)-> fetchData());
        skipButton.setOnClickListener( (v) -> {
            startActivity(new Intent(UserClassSpinner.this, MainActivity.class));
            finish();
        } );
    }

    private void fetchData() {

        boolean isValidated = validateSpinnerSelection();

        if(!isValidated) {
            return;
        }

        refreshRoomDatabase();

        sharedPreferences.edit().putString("selectedCourse", selectedCourse).putString("selectedSemester", selectedSemester).apply();

        startActivity(new Intent(UserClassSpinner.this, MainActivity.class));
        finish();

    }

    private boolean validateSpinnerSelection() {

        if(selectedCourse.equalsIgnoreCase("select your course")) {
            utility.showToast(UserClassSpinner.this, "Please select your course from the list!");
            tvCourse.setError("Course is required!");
            tvCourse.requestFocus();
            return false;
        } else if(selectedSemester.equalsIgnoreCase("select your semester")) {
            utility.showToast(UserClassSpinner.this, "Please select your semester from the list!");
            tvSemester.setError("Course is required!");
            tvSemester.requestFocus();
            tvCourse.setError(null);
            return false;
        }
        tvCourse.setError(null);
        tvSemester.setError(null);
        return true;

    }

    private void setupSpinner() {

        courseAdapter = ArrayAdapter.createFromResource(this, R.array.course_name_array, R.layout.login_spinner_list_item);
        courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpinner.setAdapter(courseAdapter);

        courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                semesterSpinner = (Spinner) findViewById(R.id.selectSemester);

                int parentId = parent.getId();
                if(parentId==R.id.selectCourse) {

                    switch(position) {
                        case 0: {
                            selectedCourse = "select your course";
                            semesterAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array
                                    .default_semester_array, R.layout.login_spinner_list_item);
                            break;
                        }
                        case 1: {
                            selectedCourse = "bca";
                            semesterAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array
                                    .six_semester_array, R.layout.login_spinner_list_item);
                            break;
                        }
                        case 2: {
                            selectedCourse = "btech_cs";
                            semesterAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array
                                    .eight_semester_array, R.layout.login_spinner_list_item);
                            break;
                        }
                        case 3: {
                            selectedCourse = "bba";
                            semesterAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array
                                    .six_semester_array, R.layout.login_spinner_list_item);
                            break;
                        }
                        case 4: {
                            selectedCourse = "foodt";
                            semesterAdapter = ArrayAdapter.createFromResource(parent.getContext(), R.array
                                    .six_semester_array, R.layout.login_spinner_list_item);
                            break;
                        }
                        default:
                            selectedCourse = null;
                            break;
                    }
                    semesterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    semesterSpinner.setAdapter(semesterAdapter);

                    semesterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            int parent_id = parent.getId();
                            if(parent_id == R.id.selectSemester) {
                                switch(position) {
                                    case 0: {
                                        selectedSemester = "select your semester";
                                        break;
                                    }
                                    case 1: {
                                        selectedSemester = "first_sem";
                                        break;
                                    }
                                    case 2: {
                                        selectedSemester = "second_sem";
                                        break;
                                    }
                                    case 3: {
                                        selectedSemester = "third_sem";
                                        break;
                                    }
                                    case 4: {
                                        selectedSemester = "fourth_sem";
                                        break;
                                    }
                                    case 5: {
                                        selectedSemester = "fifth_sem";
                                        break;
                                    }
                                    case 6: {
                                        selectedSemester = "sixth_sem";
                                        break;
                                    }
                                    case 7: {
                                        selectedSemester = "seventh_sem";
                                        break;
                                    }
                                    case 8: {
                                        selectedSemester = "eight_sem";
                                        break;
                                    }
                                    default:
                                        selectedSemester = null;
                                        break;
                                }
                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void changeInProgress(boolean inProgress) {
        if(inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            nextButton.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            nextButton.setVisibility(View.VISIBLE);
        }
    }

    private void refreshRoomDatabase() {
        changeInProgress(true);
        try {
            loadTimeTableFromFirebase();
            loadTimesFromFirebase();
            loadSubjectsFromFirebase();
            loadSyllabusFromFirebase();
        } catch(Exception e) {
            Log.d("exception_e", e.getLocalizedMessage());
        }

        changeInProgress(false);

    }

    private void loadTimesFromFirebase() throws Exception {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("syllabus_database").document("database")
                .collection("smu_times").get()
                .addOnCompleteListener(task -> {

                    if(task.isSuccessful()) {
                        List<DocumentSnapshot> list = task.getResult().getDocuments();
                        insertTimesIntoDatabase(new TimesEntity(), true);
                        for(DocumentSnapshot each : list) {
                            if(each != null && each.exists()) {
                                TimesFromFireStoreModel model = each.toObject(TimesFromFireStoreModel.class);
                                if(model != null) {
                                    createTimesEntity(model);
                                }
                            }
                        }
                    }

                });
    }

    private void createTimesEntity(TimesFromFireStoreModel model) {

        try {

            List<String> times = model.getTimes();
            for(String time : times) {
                TimesEntity entity = new TimesEntity(time, model.getName());
                insertTimesIntoDatabase(entity, false);
            }

        } catch(Exception e) { Log.d("exception_e", e.getLocalizedMessage()); }

    }

    private void insertTimesIntoDatabase(TimesEntity entity, boolean isNew) {

        try {

            TimesDatabase database = Room.databaseBuilder(getApplicationContext(), TimesDatabase.class, "times_db")
                    .fallbackToDestructiveMigration().allowMainThreadQueries().build();

            if(isNew) {
                database.getTimesDao().deleteTimes();
            } else {
                database.getTimesDao().insertTimes(entity);
            }

        } catch(Exception e) { Log.d("exception_e", e.getLocalizedMessage()); }

    }

    private void loadTimeTableFromFirebase() throws Exception {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("syllabus_database").document("database")
                .collection(selectedCourse).document(selectedSemester)
                .collection("time_table_subjects").get()
                .addOnCompleteListener(task -> {

                    if(task.isSuccessful()) {
                        List<DocumentSnapshot> list = task.getResult().getDocuments();
                        insertTimeTableIntoDatabase(new TimeTableEntitiy(), true);
                        for(DocumentSnapshot each : list) {
                            if(each != null && each.exists()) {
                                TimeTableFromFireStoreModel model = each.toObject(TimeTableFromFireStoreModel.class);
                                if(model != null) {
                                    createTimeTableEntity(model);
                                }
                            }
                        }
                    }

                });

    }

    private void createTimeTableEntity(TimeTableFromFireStoreModel model) {
        try {
            int i = 0;
            List<String> names = model.getSubject_names();
            for (String name : names) {
                TimeTableEntitiy entity = new TimeTableEntitiy(name, model.getTime(), model.getDay());
                i++;
                insertTimeTableIntoDatabase(entity, false);
            }
        } catch(Exception e) {
            Log.d("exception_e", e.getLocalizedMessage());
        }
    }

    private void insertTimeTableIntoDatabase(TimeTableEntitiy timeTable, boolean isNew) {
        try {
            TimeTableDatabase database = Room.databaseBuilder(getApplicationContext(), TimeTableDatabase.class, "time_table_db")
                    .fallbackToDestructiveMigration().allowMainThreadQueries().build();

            if (isNew) {
                database.getTimeTableDao().deleteAllTimeTable();
            } else {
                database.getTimeTableDao().insertTimeTable(timeTable);
            }
        } catch(Exception e) {
            Log.d("exception_e", e.getLocalizedMessage());
        }
    }

    private void loadSyllabusFromFirebase() throws Exception {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("syllabus_database").document("database")
                .collection(selectedCourse).document(selectedSemester)
                .collection("subjects").get()
                .addOnCompleteListener(task -> {

                    if(task.isSuccessful()) {
                        List<DocumentSnapshot> list = task.getResult().getDocuments();
                        insertSyllabusIntoDatabase(new SyllabusEntity(), true);
                        for(DocumentSnapshot each : list) {
                            if(each != null && each.exists()) {
                                SyllabusFromFireStoreModel model = each.toObject(SyllabusFromFireStoreModel.class);
                                if(model != null) {
                                    createSyllabusEntities(model);
                                }
                            }
                        }
                    }

                });

    }

    private void createSyllabusEntities(SyllabusFromFireStoreModel Model) {
        try {
            int i = 0;
            List<String> modelList = Model.getTitle_list();
            for (String modelItem : modelList) {
                SyllabusEntity syllabus = new SyllabusEntity(modelItem, Model.getSyllabus_list().get(i), Model.getUname());
                i++;
                insertSyllabusIntoDatabase(syllabus, false);
            }
        } catch(Exception e) {
            Log.d("exception_e", e.getLocalizedMessage());
        }
    }

    private void insertSyllabusIntoDatabase(SyllabusEntity syllabus, boolean isNew) {
        try {
            SyllabusDatabase database = Room.databaseBuilder(getApplicationContext(), SyllabusDatabase.class, "syllabus_db")
                    .fallbackToDestructiveMigration().allowMainThreadQueries().build();

            if (isNew) {
                database.syllabusDao().deleteAllSubjects();
            } else {
                database.syllabusDao().insertAll(syllabus);
            }
        } catch(Exception e) {
            Log.d("exception_e", e.getLocalizedMessage());
        }
    }

    private void loadSubjectsFromFirebase() throws Exception {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("syllabus_database").document("database")
                .collection(selectedCourse).document(selectedSemester)
                .collection("all_subjects").document("subject_names").get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if(document != null && document.exists()) {
                            SubjectsFromFireStoreModel model = document.toObject(SubjectsFromFireStoreModel.class);
                            if(model != null) {
                                createSubjectEntities(model);
                            }
                        }
                    }
                });

    }

    private void createSubjectEntities(SubjectsFromFireStoreModel Model) {
        try {
            insertSubjectsIntoDatabase(new SubjectEntity(), true);
            int i = 0;
            List<String> modelList = Model.getNames();
            for (String modelItem : modelList) {
                SubjectEntity subject = new SubjectEntity(modelItem, Model.getUnames().get(i));
                i++;
                insertSubjectsIntoDatabase(subject, false);
            }
        } catch(Exception e) {
            Log.d("exception_e", e.getLocalizedMessage());
        }
    }

    private void insertSubjectsIntoDatabase(SubjectEntity subjects, boolean isNew) {
        try {
            SubjectDatabase database = Room.databaseBuilder(getApplicationContext(), SubjectDatabase.class, "subject_db")
                    .fallbackToDestructiveMigration().allowMainThreadQueries().build();

            if (isNew) {
                database.subjectDao().deleteAllSubjects();
            } else {
                database.subjectDao().insertAll(subjects);
            }
        } catch(Exception e) {
            Log.d("exception_e", e.getLocalizedMessage());
        }

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(UserClassSpinner.this, MainActivity.class));
        finish();
    }

}