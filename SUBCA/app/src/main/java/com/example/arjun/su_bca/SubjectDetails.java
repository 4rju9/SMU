package com.example.arjun.su_bca;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.arjun.su_bca.RoomDatabase.SyllabusDatabase;
import com.example.arjun.su_bca.RoomDatabase.SyllabusEntity;
import java.util.List;

public class SubjectDetails extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private SubjectDetailsAdapter adapter;
    private SyllabusDatabase database;
    private String selected_subject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_details);

        setupUIViews();
        initToolbar();
        setupRecyclerView();
    }

    private void setupUIViews() {
        toolbar = (Toolbar) findViewById(R.id.ToolbarSubjectDetails);
        recyclerView = (RecyclerView) findViewById(R.id.rvSubjectDetails);
        selected_subject = getIntent().getStringExtra("selected_subject").toString();
        database = Room.databaseBuilder(getApplicationContext(), SyllabusDatabase.class, "syllabus_db")
                .fallbackToDestructiveMigration().allowMainThreadQueries().build();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Syllabus");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SubjectDetailsAdapter();
        recyclerView.setAdapter(adapter);
    }

    public class SubjectDetailsAdapter extends RecyclerView.Adapter<SubjectDetailsAdapter.SubjectDetailsViewHolder> {

        List<SyllabusEntity> syllabusList = database.syllabusDao().getAllSubjects(selected_subject);

        public void setSyllabusList(List<SyllabusEntity> syllabusList) {
            this.syllabusList = syllabusList;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public SubjectDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_details_single_item, parent, false);
            return new SubjectDetailsViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull SubjectDetailsViewHolder holder, int position) {

            SyllabusEntity syllabus = syllabusList.get(position);
            holder.title.setText(syllabus.getTitle().toString().replace("\\n", "\n").trim());
            holder.syllabus.setText(syllabus.getSyllabus().toString().replace("\\n", "\n").trim());

        }

        @Override
        public int getItemCount() {
            return syllabusList.size();
        }

        public class SubjectDetailsViewHolder extends RecyclerView.ViewHolder {

            TextView title, syllabus;

            public SubjectDetailsViewHolder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.tvSubjectTitle);
                syllabus = itemView.findViewById(R.id.tvSyllabus);
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home : {
                onBackPressed();
            }
        }
        return super.onOptionsItemSelected(item);
    }

}