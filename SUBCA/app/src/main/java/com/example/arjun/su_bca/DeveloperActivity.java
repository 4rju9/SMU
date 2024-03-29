package com.example.arjun.su_bca;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DeveloperActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerViewData;
    private ImageView instagram, linkedin, hackerrank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);

        setupUIViews();
        initToolbar();
        initButtons();
        setupAdapter();

    }

    private void setupUIViews() {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        toolbar = findViewById(R.id.ToolbarDeveloperDetails);
        recyclerViewData = findViewById(R.id.rvDeveloper1);
        instagram = findViewById(R.id.instagram);
        linkedin = findViewById(R.id.linkedin);
        hackerrank = findViewById(R.id.hackerrank);

    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initButtons () {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);

        instagram.setOnClickListener( v -> {
            intent.setData(Uri.parse("https://www.instagram.com/ohi_arj/"));
            startActivity(intent);
        });

        linkedin.setOnClickListener( v -> {
            intent.setData(Uri.parse("https://www.linkedin.com/in/4rju9/"));
            startActivity(intent);
        });

        hackerrank.setOnClickListener( v-> {
            intent.setData(Uri.parse("https://www.hackerrank.com/4rju9/"));
            startActivity(intent);
        });

    }

    private void setupAdapter() {
        rvOneAdapter adapter1 = new rvOneAdapter();
        recyclerViewData.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewData.setAdapter(adapter1);
    }

    public class rvOneAdapter extends RecyclerView.Adapter<rvOneAdapter.rvOneViewHolder> {

        String[] titles = getResources().getStringArray(R.array.developerTitles);
        String[] desc = getResources().getStringArray(R.array.developerDesc);

        @NonNull
        @Override
        public rvOneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_developer_single_item, parent, false);
            return new rvOneViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull rvOneViewHolder holder, int position) {
            holder.title.setText(titles[position]);
            holder.desc.setText(desc[position]);
        }

        @Override
        public int getItemCount() {
            return titles.length;
        }

        public class rvOneViewHolder extends RecyclerView.ViewHolder {

            TextView title;
            TextView desc;

            public rvOneViewHolder(@NonNull View itemView) {
                super(itemView);

                title = itemView.findViewById(R.id.tvDeveloperSingleItemTitle);
                desc = itemView.findViewById(R.id.tvDeveloperSingleItemDesc);

            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
