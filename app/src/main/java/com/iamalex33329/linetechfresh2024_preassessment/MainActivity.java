package com.iamalex33329.linetechfresh2024_preassessment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;

import com.iamalex33329.linetechfresh2024_preassessment.adapters.MessageAdapter;
import com.iamalex33329.linetechfresh2024_preassessment.models.GptMessage;
import com.iamalex33329.linetechfresh2024_preassessment.models.Message;
import com.iamalex33329.linetechfresh2024_preassessment.models.OpenAIResponse;
import com.iamalex33329.linetechfresh2024_preassessment.services.FirebaseChatService;
import com.iamalex33329.linetechfresh2024_preassessment.services.OpenAIService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static boolean isAiActive = false;

    private final List<GptMessage> gptMessages = new ArrayList<>();
    private List<Message> messages = new ArrayList<>();

    private MessageAdapter messageAdapter;
    private FirebaseChatService firebaseChatService;
    private OpenAIService openAIService;

    private RecyclerView chatHistoryRecyclerView;
    private EditText userInputField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseChatService = new FirebaseChatService(this);
        openAIService = new OpenAIService();

        initViews();
        initRecyclerView();
        loadMessagesFromFirebase();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_check) {
            View menuItemView = findViewById(R.id.action_check);
            PopupMenu popup = new PopupMenu(this, menuItemView);
            popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    if (item.getItemId() == R.id.option1) {
                        Toast.makeText(MainActivity.this, "已開啟 AI 對話", Toast.LENGTH_SHORT).show();
                        isAiActive = true;
                        return true;
                    } else if (item.getItemId() == R.id.option2) {
                        Toast.makeText(MainActivity.this, "已關閉 AI 對話", Toast.LENGTH_SHORT).show();
                        isAiActive = false;
                        return true;
                    }
                    return true;
                }
            });
            popup.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        chatHistoryRecyclerView = findViewById(R.id.chatHistoryRecyclerView);
        ImageView simulateMessageButton = findViewById(R.id.simulateMessageButton);
        Button sendMessageButton = findViewById(R.id.sendMessageButton);
        userInputField = findViewById(R.id.userInputField);

        simulateMessageButton.setOnClickListener(this::simulateMessage);
        sendMessageButton.setOnClickListener(this::sendMessage);
    }

    private void initRecyclerView() {
        messageAdapter = new MessageAdapter(this, messages);
        chatHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatHistoryRecyclerView.setAdapter(messageAdapter);
    }

    private void simulateMessage(View view) {
        String message = TEST_MESSAGES[(int) (Math.random() * TEST_MESSAGES.length)];
        addMessage(new Message(false, message, new Date()));
    }

    private void sendMessage(View view) {
        String content = userInputField.getText().toString().trim();
        if (!content.isEmpty()) {
            Message newMessage = new Message(true, content, new Date());
            addMessage(newMessage);
            userInputField.setText("");

            if (isAiActive) {
                gptMessages.add(new GptMessage("user", content));
                openAIService.sendMessageToOpenAI(gptMessages, new OpenAIService.ResponseHandler() {
                    @Override
                    public void onSuccess(OpenAIResponse response) {
                        for (OpenAIResponse.Choice choice : response.choices) {
                            runOnUiThread(() -> addMessage(new Message(false, choice.message.content, new Date())));
                            gptMessages.add(new GptMessage("assistant", choice.message.content));
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.e(TAG, "API access failure: ", t);
                    }
                });
            }
        }
    }

    private void addMessage(Message message) {
        messages.add(message);
        messageAdapter.notifyItemInserted(messages.size() - 1);
        chatHistoryRecyclerView.scrollToPosition(messages.size() - 1);
        firebaseChatService.saveMessageToFirebase(message);
    }

    private void loadMessagesFromFirebase() {
        firebaseChatService.loadMessagesFromFirebase(new FirebaseChatService.MessageLoadListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onMessagesLoaded(List<Message> loadedMessages) {
                messages.clear();
                messages.addAll(loadedMessages);
                messageAdapter.notifyDataSetChanged();
                chatHistoryRecyclerView.scrollToPosition(messages.size() - 1);
            }

            @Override
            public void onError(DatabaseError databaseError) {
                Log.e(TAG, "Failed to load chat history: ", databaseError.toException());
            }
        });
    }

    private static final String[] TEST_MESSAGES = {
            "。", "是喔", "我先去洗澡", "哈哈哈哈哈哈哈哈哈哈哈哈哈哈",
            "達文西密碼的上面是什麼？\n.\n.\n.\n.\n.\n.\n.\n.\n.\n.\n.\n.\n達文西帳號"
    };
}