package com.example.arjun.su_bca;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.arjun.su_bca.RoomDatabase.TimeTableDatabase;
import com.example.arjun.su_bca.RoomDatabase.TimeTableEntitiy;
import com.example.arjun.su_bca.RoomDatabase.TimesDatabase;
import com.example.arjun.su_bca.RoomDatabase.TimesEntity;
import com.example.arjun.su_bca.Utils.LetterImageView;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

public class DayDetail extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TimeTableAdapter adapter;
    private String selected_day;
    private TimeTableDatabase timeTableDatabase;
    private TimesDatabase timesDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_detail);

        setupUIViews();
        initToolbar();
        setupRecyclerView();
    }

    private void setupUIViews() {
        recyclerView = (RecyclerView) findViewById(R.id.rvDayDetail);
        toolbar = (Toolbar) findViewById(R.id.ToolbarDayDetail);
        selected_day = WeekActivity.sharedPreferences.getString(WeekActivity.SEL_DAY, "").toLowerCase();
        timeTableDatabase = Room.databaseBuilder(getApplicationContext(), TimeTableDatabase.class, "time_table_db")
                .fallbackToDestructiveMigration().allowMainThreadQueries().build();
        timesDatabase = Room.databaseBuilder(getApplicationContext(), TimesDatabase.class, "times_db")
                .fallbackToDestructiveMigration().allowMainThreadQueries().build();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(WeekActivity.sharedPreferences.getString(WeekActivity.SEL_DAY, null));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupRecyclerView() {
        try {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new TimeTableAdapter();
            recyclerView.setAdapter(adapter);
        } catch (Exception ignore) {}
    }


    public class TimeTableAdapter extends RecyclerView.Adapter<TimeTableAdapter.TimeTableHolder> {

        List<TimeTableEntitiy> names = timeTableDatabase.getTimeTableDao().getAllTimeTable(selected_day);
        private String time = names.get(0).getTime();
        List<TimesEntity> times = timesDatabase.getTimesDao().getTimes(time);

        @NonNull
        @Override
        public TimeTableHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_detail_single_item, parent, false);
            return new TimeTableHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TimeTableHolder holder, int position) {
            TimeTableEntitiy entity = names.get(position);
            holder.image.setOval(true);
            holder.image.setLetter(entity.getSubject().charAt(0));
            holder.name.setText(entity.getSubject());
            holder.time.setText(times.get(position).getTime().replace(" ", "\n"));
        }

        @Override
        public int getItemCount() {
            return names.size();
        }

        public class TimeTableHolder extends RecyclerView.ViewHolder {

            LetterImageView image;
            TextView name, time;

            public TimeTableHolder(@NonNull View itemView) {
                super(itemView);
                image = itemView.findViewById(R.id.ivDayDetails);
                name = itemView.findViewById(R.id.tvDayDetails);
                time = itemView.findViewById(R.id.tvTimeDayDetails);
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
