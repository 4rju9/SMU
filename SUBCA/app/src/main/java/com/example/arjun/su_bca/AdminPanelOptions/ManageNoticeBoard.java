package com.example.arjun.su_bca.AdminPanelOptions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.media.Image;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.arjun.su_bca.MainActivity;
import com.example.arjun.su_bca.R;
import com.example.arjun.su_bca.Utils.NoticeBoardContentModel;
import com.example.arjun.su_bca.utility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

public class ManageNoticeBoard extends AppCompatActivity {

    private ImageButton backButton, saveButton, deleteButton;
    private EditText notice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_notice_board);

        setupUIViews();
        checkIfNoticeAvailable();
        allButtonFunctioning();

    }

    private void setupUIViews () {
        backButton = (ImageButton) findViewById(R.id.backButtonNoticeBoard);
        saveButton = (ImageButton) findViewById(R.id.saveButtonNoticeBoard);
        deleteButton = (ImageButton) findViewById(R.id.deleteButtonNoticeBoard);
        notice = (EditText) findViewById(R.id.editTextNoticeBoard);
    }

    private void checkIfNoticeAvailable () {
        if (MainActivity.isNoticeContent) {
            notice.setText(MainActivity.NoticeBoardContentText);
        } else {
            notice.setText("");
        }
    }

    private void allButtonFunctioning () {
        backButton.setOnClickListener((v)-> super.onBackPressed() );
        saveButton.setOnClickListener( (v) -> updateNoticeBoard() );
        deleteButton.setOnClickListener( (v) -> deleteNoticeBoardContent() );
    }

    private void updateNoticeBoard () {
        String content = notice.getText().toString();
        if (content == null || content.isEmpty()) {
            notice.setError("Empty field detected!");
            return;
        } else {
            // update notice board.
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            NoticeBoardContentModel model = new NoticeBoardContentModel(content);

            db.collection("ApplicationData").document("notice")
                    .set(model).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            utility.showToast(ManageNoticeBoard.this, "Saved");
                            finish();
                        } else {
                            utility.showToast(ManageNoticeBoard.this, "Something went wrong, Please try again after sometime");
                        }
                    });
        }
    }

    private void deleteNoticeBoardContent () {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Delete Notice!")
                .setMessage("this will remove the content from notice.\nAre you sure?")
                .setPositiveButton("Delete", (dialog1, which) -> {
                    // delete the content form notice board.

                    db.collection("ApplicationData").document("notice")
                            .delete().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    notice.setText("");
                                    utility.showToast(this, "Deleted");

                                } else {
                                    utility.showToast(this, "Something went wrong, Please try again after sometime");
                                }
                            });

                })
                .setNegativeButton("Cancel", (dialog2, which) -> {
                    // Do nothing.
                })
                .show();
        dialog.setCancelable(false);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}