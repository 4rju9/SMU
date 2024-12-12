package com.example.arjun.su_bca;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.toolbox.JsonObjectRequest;
import com.example.arjun.su_bca.Utils.VolleyRequestQueue;
import com.example.arjun.su_bca.Utils.utility;
import com.google.auth.oauth2.GoogleCredentials;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

public class CreateNotification extends AppCompatActivity {

    EditText title, body;
    private String ACCESS_TOKEN;
    private static final String MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
    private static final String[] SCOPES = { MESSAGING_SCOPE };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notification);

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                ACCESS_TOKEN = getAccessToken();
            } catch (IOException ignore) {}
        });

        Button nextButton = findViewById(R.id.nextButtonCreateNotification);
        title = findViewById(R.id.editTitleCreateNotification);
        body = findViewById(R.id.editMessageCreateNotification);

        nextButton.setOnClickListener( v -> {

            String sTitle = title.getText().toString();
            String sBody = body.getText().toString();
            if (!(sTitle.isEmpty() || sBody.isEmpty())) {
                sendNotification(
                        sTitle,
                        sBody
                );
            }

        });

    }

    private String getAccessToken() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(getResources().openRawResource(R.raw.firebase))
                .createScoped(Arrays.asList(SCOPES));
        googleCredentials.refresh();
        return googleCredentials.getAccessToken().getTokenValue();
    }

    private void sendNotification (String sTitle, String sBody) {

        VolleyRequestQueue queue = VolleyRequestQueue.getInstance(this);
        JSONObject mainBody = new JSONObject();

        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("topic", "noticeBoard");
            JSONObject notification = new JSONObject();
            notification.put("title", sTitle);
            notification.put("body", sBody);
            jsonBody.put("notification", notification);
            mainBody.put("message", jsonBody);
        } catch (Exception ignore) {}

        JsonObjectRequest request = new JsonObjectRequest(
                getResources().getString(R.string.firebase_push_notification_api),
                mainBody,
                response -> {
                    runOnUiThread(() -> utility.showToast(CreateNotification.this, "Notification sent."));
                    title.setText("");
                    body.setText("");
                    finish();
                },
                error -> {
                    runOnUiThread(() -> utility.showToast(CreateNotification.this, error.getLocalizedMessage()));
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; UTF-8");
                headers.put("Authorization", "Bearer " + ACCESS_TOKEN);
                return headers;
            }
        };

        queue.getQueue().add(request);

    }

}