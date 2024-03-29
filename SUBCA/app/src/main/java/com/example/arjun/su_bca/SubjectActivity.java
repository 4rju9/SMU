package com.example.arjun.su_bca;

import android.content.Intent;
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
import com.example.arjun.su_bca.RoomDatabase.SubjectDatabase;
import com.example.arjun.su_bca.RoomDatabase.SubjectEntity;
import com.example.arjun.su_bca.Utils.LetterImageView;
import java.util.ArrayList;
import java.util.List;

public class SubjectActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private SubjectAdapter adapter;
    private SubjectDatabase database;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        setupUIViews();
        initToolbar();
        setupRecyclerView();
    }

    private void setupUIViews() {
        toolbar = (Toolbar)findViewById(R.id.ToolbarSubject);
        recyclerView = (RecyclerView)findViewById(R.id.rvSubject);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Subjects");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SubjectAdapter();
        recyclerView.setAdapter(adapter);
        database = Room.databaseBuilder(getApplicationContext(), SubjectDatabase.class, "subject_db")
                .fallbackToDestructiveMigration().allowMainThreadQueries().build();
        adapter.setSubjects(database.subjectDao().getAllSubjects());

    }

    public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder> {

        List<SubjectEntity> subjects = new ArrayList<>();

        public void setSubjects(List<SubjectEntity> subjects) {
            this.subjects = subjects;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_single_item, parent, false);
            return new SubjectViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
            SubjectEntity subject = subjects.get(position);
            holder.tvSubject.setText(subject.getName());
            holder.ivLogo.setOval(true);
            holder.ivLogo.setLetter(subject.getName().charAt(0));

            holder.itemView.setOnClickListener((v)-> {
                Intent intent = new Intent(holder.tvSubject.getContext(), SubjectDetails.class);
                intent.putExtra("selected_subject", subject.getUname().trim());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.tvSubject.getContext().startActivity(intent);
            });

        }

        @Override
        public int getItemCount() {
            return subjects.size();
        }

        public class SubjectViewHolder extends RecyclerView.ViewHolder {

            LetterImageView ivLogo;
            TextView tvSubject;

            public SubjectViewHolder(@NonNull View itemView) {
                super(itemView);
                ivLogo = itemView.findViewById(R.id.ivLetterSubject);
                tvSubject = itemView.findViewById(R.id.tvSubject);
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
