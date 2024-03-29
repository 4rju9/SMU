package com.example.arjun.su_bca.games;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arjun.su_bca.R;

public class GamesActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);

        setUiViews();
        initToolbar();
        setupRecyclerView();

    }

    private void setUiViews () {
        toolbar = (Toolbar) findViewById(R.id.ToolbarGamesActivity);
        recyclerView = (RecyclerView) findViewById(R.id.rvGames);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Fun Zone");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupRecyclerView () {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        GamesAdapter adapter = new GamesAdapter();
        recyclerView.setAdapter(adapter);
    }

    public class GamesAdapter extends RecyclerView.Adapter<GamesAdapter.GamesViewHolder> {

        String[] titleArray = getResources().getStringArray(R.array.games_title);
        String[] descArray = getResources().getStringArray(R.array.games_description);

        @NonNull
        @Override
        public GamesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_games_single_item, parent, false);
            return new GamesViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull GamesViewHolder holder, int position) {

            String title = titleArray[position];
            String desc = descArray[position];

            holder.title.setText(title);
            holder.desc.setText(desc);
            Intent intent;

            if (title.equalsIgnoreCase("tic tac toe")) {
                holder.imageView.setImageResource(R.drawable.game_console);
                intent = new Intent(holder.title.getContext(), TicTacToe.class);
            } else {
                intent = new Intent();
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    holder.title.getContext().startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return titleArray.length;
        }

        public class GamesViewHolder extends RecyclerView.ViewHolder {

            ImageView imageView;
            TextView title, desc;

            public GamesViewHolder(@NonNull View itemView) {
                super(itemView);

                imageView = (ImageView) itemView.findViewById(R.id.ivGamesSingleItem);
                title = (TextView) itemView.findViewById(R.id.tv_games_title_single_item);
                desc = (TextView) itemView.findViewById(R.id.tv_games_description_single_item);

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