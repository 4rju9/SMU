package com.example.arjun.su_bca;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.arjun.su_bca.AdminPanelOptions.ManageAllSubjects;
import com.example.arjun.su_bca.AdminPanelOptions.ManageNoticeBoard;
import com.example.arjun.su_bca.AdminPanelOptions.ManageSyllabus;
import com.example.arjun.su_bca.AdminPanelOptions.ManageTimeTable;
import com.example.arjun.su_bca.AdminPanelOptions.ManageTimes;
import com.example.arjun.su_bca.Utils.LetterImageView;

public class AdminPanel extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        setupUIViews();
        initToolbar();
        setupRecyclerView();

    }

    private void setupUIViews () {
        toolbar = (Toolbar) findViewById(R.id.adminPanelToolbar);
        recyclerView = (RecyclerView) findViewById(R.id.rvAdminPanel);
    }

    private void initToolbar () {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Admin Panel");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupRecyclerView () {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        AdminPanelAdapter adapter = new AdminPanelAdapter();
        recyclerView.setAdapter(adapter);
    }

    public class AdminPanelAdapter extends RecyclerView.Adapter<AdminPanelAdapter.AdminPanelHolder> {

        String[] titles = getResources().getStringArray(R.array.admin_panel_title);

        @NonNull
        @Override
        public AdminPanelHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_admin_panel_single_item, parent, false);
            return new AdminPanelHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AdminPanelHolder holder, int position) {

            String title = titles[position];
            holder.image.setLetter(title.charAt(0));
            holder.image.setOval(true);
            holder.title.setText(title);

            holder.itemView.setOnClickListener( v -> {

                Intent intent;

                if (title.equalsIgnoreCase("manage notice board")) {
                    intent = new Intent(AdminPanel.this, ManageNoticeBoard.class);
                }
                else if (title.equalsIgnoreCase("send a notification")) {
                    intent = new Intent(AdminPanel.this, CreateNotification.class);
                }
                else if (title.equalsIgnoreCase("manage time table")) {
                    intent = new Intent(AdminPanel.this, ManageTimeTable.class);
                }
                else if (title.equalsIgnoreCase("manage times")) {
                    intent = new Intent(AdminPanel.this, ManageTimes.class);
                }
                else if (title.equalsIgnoreCase("manage subjects")) {
                    intent = new Intent(AdminPanel.this, ManageAllSubjects.class);
                }
                else if (title.equalsIgnoreCase("manage syllabus")) {
                    intent = new Intent(AdminPanel.this, ManageSyllabus.class);
                }
                else {
                    intent = new Intent(AdminPanel.this, MainActivity.class);
                }

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.title.getContext().startActivity(intent);

            });

        }

        @Override
        public int getItemCount() {
            return titles.length;
        }

        public class AdminPanelHolder extends RecyclerView.ViewHolder {

            LetterImageView image;
            TextView title;

            public AdminPanelHolder(@NonNull View itemView) {
                super(itemView);

                image = (LetterImageView) itemView.findViewById(R.id.ivLetterAdminPanel);
                title = (TextView) itemView.findViewById(R.id.tvAdminPanel);

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