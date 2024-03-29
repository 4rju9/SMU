package com.example.arjun.su_bca;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import com.example.arjun.su_bca.Utils.NoticeBoardContentModel;
import com.example.arjun.su_bca.Utils.ReportAProblemActivity;
import com.example.arjun.su_bca.signup.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.Group;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private FirebaseRemoteConfig remoteConfig;
    private ImageButton menuButton;
    private TextView noticeBoard;
    private boolean shouldUpdateNotice = true;
    public static boolean isNoticeContent = false;
    public static String NoticeBoardContentText;
    private RecyclerView recyclerView;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseMessaging.getInstance().subscribeToTopic("noticeBoard");

        setupUIViews();
        requestPostNotificationPermission();
        setupRecyclerView();
        if (shouldUpdateNotice) {
            checkForUpdates();
            setupNoticeBoard();
            shouldUpdateNotice = false;
        }
        menuButtonFunctioning();

    }

    private void requestPostNotificationPermission () {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return;

        boolean hasPostNotification = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;

        if (!hasPostNotification) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.POST_NOTIFICATIONS}, 7);
        }

    }

    private void showHideNoticeBoard (boolean shouldDisplay) {
        Group group = findViewById(R.id.groupMain1);
        if (shouldDisplay) group.setVisibility(View.VISIBLE);
        else group.setVisibility(View.GONE);
    }

    private void menuButtonFunctioning() {
        menuButton.setOnClickListener((v)-> showMenu());
    }

    private void showMenu() {
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, menuButton);
        popupMenu.getMenu().add("Logout");
        popupMenu.getMenu().add("Refresh Data");
        popupMenu.getMenu().add("Admin Panel");
        popupMenu.getMenu().add("Report a problem");
        popupMenu.getMenu().add("Receive Notifications");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(item -> {

            String title = item.getTitle().toString();

            if (title.equalsIgnoreCase("logout")) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                return true;
            }
            if (title.equalsIgnoreCase("refresh data")) {
                startActivity(new Intent(MainActivity.this, UserClassSpinner.class));
                finish();
                return true;
            }
            if (title.equalsIgnoreCase("admin panel")) {
                if (sharedPreferences.getBoolean("is_user_admin", false)) {
                    startActivity(new Intent(MainActivity.this, AdminPanel.class));
                } else {
                    utility.showToast(MainActivity.this, "Only admins and developers can access.");
                }
                return true;
            }
            if (title.equalsIgnoreCase("report a problem")) {
                startActivity(new Intent(MainActivity.this, ReportAProblemActivity.class));
            }
            if (title.equalsIgnoreCase("Receive Notifications")) {
                requestPostNotificationPermission();
            }
            return false;
        });
    }

    private void checkForUpdates() {
        try {
            int currentVersionCode = getCurrentVersionCode();

            remoteConfig = FirebaseRemoteConfig.getInstance();
            FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                    .setMinimumFetchIntervalInSeconds(5)
                    .build();

            remoteConfig.setConfigSettingsAsync(configSettings);
            remoteConfig.fetchAndActivate().addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    String[] data = remoteConfig.getString("new_version_code").split(" ");
                    int latestVersionCode = Integer.parseInt(data[0]);
                    if(latestVersionCode > currentVersionCode) {
                        showUpdateDialog(currentVersionCode, latestVersionCode, data[1]);
                    }
                }
            });
        } catch (Exception ignored) {
        }
    }

    private void showUpdateDialog(int current, int latest, String url) {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("New Update Available!")
                .setMessage("The current version of your app is " + current + ".\nThe latest version of the app is " + latest + ".\nNew Features to this app has been added.\nKindly update your app to use them.\n\nNote: You have to update this app to the latest version to use it.\nThank You!")
                .setPositiveButton("Update", (dialog1, which) -> {
                    try {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                    } catch(Exception e) {
                        Toast.makeText(getApplicationContext(), "Something went wrong.\nTry again later!", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
        dialog.setCancelable(false);
    }

    private int getCurrentVersionCode() {
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (Exception e) {
            Log.d("myApp", e.getMessage());
        }

        return packageInfo.versionCode;
    }

    @SuppressLint("SetTextI18n")
    private void setupUIViews() {
        TextView userName = findViewById(R.id.tvMainUserName);
        noticeBoard = findViewById(R.id.tvNoticeBoardContent);
        recyclerView = findViewById(R.id.rvMain);
        menuButton = findViewById(R.id.menuButton);
        sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);
        userName.setText(sharedPreferences.getString("user_first_name", "there") + "!");
    }

    @SuppressLint("SetTextI18n")
    private void setupNoticeBoard () {
        noticeBoard.setText("Fetching details please wait...");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("ApplicationData").document("notice")
                .get().addOnCompleteListener(task -> {

                    // if successful
                    if (task.isSuccessful()) {
                        isNoticeContent = false;
                        DocumentSnapshot result = task.getResult();
                        if (result.exists()) {
                            NoticeBoardContentModel model = result.toObject(NoticeBoardContentModel.class);
                            if (model != null) {
                                if (!model.getNoticeContent().isEmpty()) {
                                    isNoticeContent = true;
                                    showHideNoticeBoard(true);
                                    noticeBoard.setText(model.getNoticeContent());
                                    NoticeBoardContentText = model.getNoticeContent();
                                }
                            }
                        }
                    }

                    if (!isNoticeContent) showHideNoticeBoard(false);

                });
    }

    private void setupRecyclerView() {

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        MainAdapter adapter = new MainAdapter();
        recyclerView.setAdapter(adapter);

    }

    public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainHolder> {

        String[] titleArray = getResources().getStringArray(R.array.Main);
        String[] descriptionArray = getResources().getStringArray(R.array.Description);

        @NonNull
        @Override
        public MainHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_activity_single_item, parent, false);
            return new MainHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MainHolder holder, int position) {
            holder.title.setText(titleArray[position]);
            holder.description.setText(descriptionArray[position]);

            Intent intent;

            if(titleArray[position].equalsIgnoreCase("Time Table")) {
                holder.image.setImageResource(R.drawable.timetable);
                intent = new Intent(holder.title.getContext(), WeekActivity.class);
            }else if(titleArray[position].equalsIgnoreCase("Subjects")) {
                holder.image.setImageResource(R.drawable.book);
                intent = new Intent(holder.title.getContext(), SubjectActivity.class);
            }else if(titleArray[position].equalsIgnoreCase("Notes")) {
                holder.image.setImageResource(R.drawable.note);
                intent = new Intent(holder.title.getContext(), NotesActivity.class);
            }
            else if(titleArray[position].equalsIgnoreCase("Ask A.I.")) {
                holder.image.setImageResource(R.drawable.conversation);
                intent = new Intent(holder.title.getContext(), ChatGPT.class);
            }
            else {
                holder.image.setImageResource(R.drawable.developer_icon);
                intent = new Intent(holder.title.getContext(), DeveloperActivity.class);
            }

            holder.cardView.setOnClickListener(v -> {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                holder.cardView.getContext().startActivity(intent);
            });

        }

        @Override
        public int getItemCount() {
            return titleArray.length;
        }

        public class MainHolder extends RecyclerView.ViewHolder {

            TextView title, description;
            ImageView image;
            CardView cardView;

            public MainHolder(@NonNull View itemView) {
                super(itemView);

                title = itemView.findViewById(R.id.tvMain);
                description = itemView.findViewById(R.id.tvDescription);
                image = itemView.findViewById(R.id.ivMain);
                cardView = itemView.findViewById(R.id.cvMainSingleItem);

            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 7) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                utility.showToast(getApplicationContext(), "Please allow Post Notification permission to allow function properly!");
            }
        }

    }
}
