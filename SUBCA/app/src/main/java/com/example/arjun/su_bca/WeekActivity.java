package com.example.arjun.su_bca;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.arjun.su_bca.Utils.LetterImageView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class WeekActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    public static SharedPreferences sharedPreferences;
    public static String SEL_DAY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week);

        setupUIViews();
        initToolbar();
        setupRecyclerView();
    }

    private void setupUIViews() {

        toolbar = (Toolbar) findViewById(R.id.ToolbarWeek);
        recyclerView = (RecyclerView) findViewById(R.id.rvWeek);
        sharedPreferences = getSharedPreferences("MY_DAY", MODE_PRIVATE);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Week");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupRecyclerView () {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        weekAdapter adapter = new weekAdapter();
        recyclerView.setAdapter(adapter);
    }

    public class weekAdapter extends RecyclerView.Adapter<WeekActivity.weekAdapter.weekViewHolder> {

        String[] titleArray = getResources().getStringArray(R.array.Week);

        @NonNull
        @Override
        public weekViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_week_single_item, parent, false);
            return new weekViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull weekViewHolder holder, int position) {

            String title = titleArray[position];
            holder.ivLetter.setLetter(title.charAt(0));
            holder.ivLetter.setOval(true);
            holder.tvWeek.setText(title);

            holder.itemView.setOnClickListener( v -> {
                Intent intent = new Intent(holder.tvWeek.getContext(), DayDetail.class);
                sharedPreferences.edit().putString(SEL_DAY, title).apply();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.tvWeek.getContext().startActivity(intent);
            });

        }

        @Override
        public int getItemCount() {
            return titleArray.length;
        }

        public class weekViewHolder extends RecyclerView.ViewHolder {

            LetterImageView ivLetter;
            TextView tvWeek;

            public weekViewHolder(@NonNull View itemView) {
                super(itemView);

                ivLetter = (LetterImageView) itemView.findViewById(R.id.ivLetter);
                tvWeek = (TextView) itemView.findViewById(R.id.tvWeek);

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
