package com.example.arjun.su_bca;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.arjun.su_bca.Utils.MessageChatGPT;
import com.example.arjun.su_bca.Utils.MessageChatGPTAdapter;
import com.example.arjun.su_bca.Utils.utility;
import com.example.arjun.su_bca.games.GamesActivity;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ChatGPT extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView welcomeText;
    private EditText messageEditText;
    private ImageButton sendButton;
    private Toolbar toolbar;
    private List<MessageChatGPT> messageList;
    private MessageChatGPTAdapter messageChatGPTAdapter;
    private GenerativeModelFutures geminiModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_gpt);

        setupUIViews();
        initToolbar();
        sendButtonEval();

        // Setup Recycler View
        messageChatGPTAdapter = new MessageChatGPTAdapter(messageList);
        recyclerView.setAdapter(messageChatGPTAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void setupUIViews() {

        // For text-only input, use the gemini-pro model
        GenerativeModel gm = new GenerativeModel(/* modelName */ "gemini-pro",
                // Access your API key as a Build Configuration variable (see "Set up your API key" above)
                /* apiKey */ getResources().getString(R.string.gemini_api_key));
        geminiModel = GenerativeModelFutures.from(gm);

        recyclerView = findViewById(R.id.rv_chat_gpt);
        welcomeText = findViewById(R.id.welcome_text);
        messageEditText = findViewById(R.id.message_edit_text);
        sendButton = findViewById(R.id.send_button);
        toolbar = findViewById(R.id.ToolbarCharGPT);
        messageList = new ArrayList<>();

    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Chat GPT");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void sendButtonEval() {
        sendButton.setOnClickListener(v -> {
            String question = messageEditText.getText().toString().trim();
            if (question != null && question.isEmpty()) return;
            addToChat(question, MessageChatGPT.SENT_BY_ME);
            messageEditText.setText("");
            callApiMethod(question);
            welcomeText.setVisibility(View.GONE);
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void addToChat(String message, String sentBy) {
        runOnUiThread(() -> {
            messageList.add(new MessageChatGPT(message, sentBy));
            messageChatGPTAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(messageChatGPTAdapter.getItemCount());
        });
    }

    private void addResponse(String response) {
        messageList.remove(messageList.size()-1);
        addToChat(response, MessageChatGPT.SENT_BY_BOT);
    }

    private void callApiMethod(String question) {
        String copyQuestion = question.toLowerCase();
        if ((copyQuestion.contains("who")) && ((copyQuestion.contains("god arjun")) || (copyQuestion.contains("arjun gangwar")))) {
            messageList.add(new MessageChatGPT("Typing...", MessageChatGPT.SENT_BY_BOT));
            String newMessage = "Arjun is an Android Developer.\nA Student of Computer Science.\nHe's an Hobbyist Photographer and loves to Code or you can also say a computer hobbyist.\nI am also his beautiful creation.\nFor more details you can visit to: dev-arjun.cf or follow him on instagram: ohi_arj.";
            addResponse(newMessage);
        } else if ( ((copyQuestion.contains("instagram handle")) || (copyQuestion.contains("instagram account"))) && ((copyQuestion.contains("arjun gangwar")) || (copyQuestion.contains("god arjun")))) {
            messageList.add(new MessageChatGPT("Typing...", MessageChatGPT.SENT_BY_BOT));
            String newMessage = "Instagram username of Arjun is:\nohi_arj";
            addResponse(newMessage);
        } else if (copyQuestion.contains(".love")) {
            messageList.add(new MessageChatGPT("Typing...", MessageChatGPT.SENT_BY_BOT));
            addResponse(utility.accessLoveCalculator(copyQuestion));
        } else if (copyQuestion.contains(".game")) {
            Intent intent = new Intent(ChatGPT.this, GamesActivity.class);
            startActivity(intent);
        } else if (copyQuestion.equals(".help")) {
            messageList.add(new MessageChatGPT("Following are the hidden commands:\n\n" +
                    "1. Help\n\nsyntax: .help\n\n(lists downs all the available commands)\n\n" +
                    "2. Love\n\nsyntax: .love <name1> <name2>\n\n(calculates the love between two names through an formula)\n\n" +
                    "3. Game\n\nsyntax: .game\n\n(shows hidden games, which you can play with your mates)\n\n" +
                    "Note: enter the given commands correctly to invoke them. all commands start with a dot before them.\n", MessageChatGPT.SENT_BY_BOT));
        }
        else {
            if(isNetworkAvailable()) {
                messageList.add(new MessageChatGPT("Typing...", MessageChatGPT.SENT_BY_BOT));
                gemini(question);
            } else {
                addToChat("Failed to load message due to no internet connection", MessageChatGPT.SENT_BY_BOT);
            }
        }
    }

    public void gemini (String prompt) {

        try {
            Content content = new Content.Builder()
                    .addText(prompt)
                    .build();

            Executor executor = Executors.newSingleThreadExecutor();

            ListenableFuture<GenerateContentResponse> response = geminiModel.generateContent(content);
            Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
                @Override
                public void onSuccess(GenerateContentResponse result) {
                    addResponse(result.getText());
                }

                @Override
                public void onFailure(Throwable t) {
                    addResponse("Failed to load response due to:\n" + t.getMessage());
                }
            }, executor);
        } catch (Exception ignore) {
            addResponse("Oops an error occurred, Kindly come back later.\nReport to developer otherwise.");
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