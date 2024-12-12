package com.example.arjun.su_bca;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.arjun.su_bca.RoomDatabase.SyllabusDatabase;
import com.example.arjun.su_bca.RoomDatabase.SyllabusEntity;

import java.util.ArrayList;
import java.util.List;

public class SubjectDetails extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private SubjectDetailsAdapter adapter;
    private SyllabusDatabase database;
    private String selected_subject;
    private ImageView search, close;
    private LinearLayout searchContainer;
    private EditText searchbar;
    private TextView titleToolbar;
    private InputMethodManager imm;

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
        searchContainer = findViewById(R.id.searchContainer);
        searchbar = findViewById(R.id.et_searchbar);
        search = findViewById(R.id.iv_search);
        close = findViewById(R.id.iv_close);
        titleToolbar = findViewById(R.id.tv_title);
        selected_subject = getIntent().getStringExtra("selected_subject").toString();
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        database = Room.databaseBuilder(getApplicationContext(), SyllabusDatabase.class, "syllabus_db")
                .fallbackToDestructiveMigration().allowMainThreadQueries().build();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        search.setOnClickListener(v -> manageSearchBar(true));

        close.setOnClickListener(v -> manageSearchBar(false));

        searchbar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence text, int i, int i1, int i2) {
                String query = text.toString();
                adapter.filter(query);
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

    }

    private void manageSearchBar(boolean showbar) {
        if (showbar) {
            titleToolbar.animate().alpha(0).setDuration(200).withEndAction(() -> titleToolbar.setVisibility(View.GONE)).start();
            search.animate().alpha(0).setDuration(200).withEndAction(() -> search.setVisibility(View.GONE)).start();
            searchContainer.setVisibility(View.VISIBLE);
            searchContainer.animate().alpha(1).setDuration(200).start();
            searchbar.requestFocus();
            if (imm != null) {
                imm.showSoftInput(searchbar, InputMethodManager.SHOW_IMPLICIT);
            }
        } else {
            titleToolbar.setVisibility(View.VISIBLE);
            titleToolbar.animate().alpha(1).setDuration(200).start();
            search.setVisibility(View.VISIBLE);
            search.animate().alpha(1).setDuration(200).start();
            searchContainer.animate().alpha(0).setDuration(200).withEndAction(() -> searchContainer.setVisibility(View.GONE)).start();
            searchbar.setText("");
            if (imm != null) {
                imm.hideSoftInputFromWindow(searchbar.getWindowToken(), InputMethodManager.RESULT_HIDDEN);
            }
            searchbar.clearFocus();
            adapter.filter("");
        }
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SubjectDetailsAdapter();
        recyclerView.setAdapter(adapter);
    }

    public class SubjectDetailsAdapter extends RecyclerView.Adapter<SubjectDetailsAdapter.SubjectDetailsViewHolder> {

        private List<SyllabusEntity> originalSyllabusList = database.syllabusDao().getAllSubjects(selected_subject);
        private List<SyllabusEntity> filteredSyllabusList = new ArrayList<>(originalSyllabusList);
        private String searchQuery = "";

        public void setSyllabusList(List<SyllabusEntity> syllabusList) {
            this.originalSyllabusList = syllabusList;
            this.filteredSyllabusList = new ArrayList<>(syllabusList);
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
            SyllabusEntity syllabus = filteredSyllabusList.get(position);
            holder.title.setText(getHighlightedText(syllabus.getTitle(), searchQuery));
            holder.syllabus.setText(getHighlightedText(syllabus.getSyllabus(), searchQuery));
        }

        @Override
        public int getItemCount() {
            return filteredSyllabusList.size();
        }

        public void filter(String query) {
            searchQuery = query.toLowerCase().trim();
            filteredSyllabusList.clear();

            if (searchQuery.isEmpty()) {
                filteredSyllabusList.addAll(originalSyllabusList);
            } else {
                for (SyllabusEntity syllabus : originalSyllabusList) {
                    if (syllabus.getTitle().toLowerCase().contains(searchQuery) ||
                            syllabus.getSyllabus().toLowerCase().contains(searchQuery)) {
                        filteredSyllabusList.add(syllabus);
                    }
                }
            }

            notifyDataSetChanged();
        }

        private Spanned getHighlightedText(String text, String query) {
            if (query.isEmpty()) {
                return new SpannableStringBuilder(text);
            }

            int startIndex = text.toLowerCase().indexOf(query);
            if (startIndex == -1) {
                return new SpannableStringBuilder(text);
            }

            SpannableStringBuilder spannable = new SpannableStringBuilder(text);
            spannable.setSpan(new BackgroundColorSpan(getColor(R.color.deepSkyBlue)),
                    startIndex, startIndex + query.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
                    startIndex, startIndex + query.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return spannable;
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

    /*public class SubjectDetailsAdapter extends RecyclerView.Adapter<SubjectDetailsAdapter.SubjectDetailsViewHolder> {

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

    }*/

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