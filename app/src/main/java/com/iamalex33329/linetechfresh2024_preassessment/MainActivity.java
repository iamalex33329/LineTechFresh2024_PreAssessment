package com.iamalex33329.linetechfresh2024_preassessment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.iamalex33329.linetechfresh2024_preassessment.adapters.MessageAdapter;
import com.iamalex33329.linetechfresh2024_preassessment.models.GptMessage;
import com.iamalex33329.linetechfresh2024_preassessment.models.Message;
import com.iamalex33329.linetechfresh2024_preassessment.models.OpenAIResponse;
import com.iamalex33329.linetechfresh2024_preassessment.network.ApiService;
import com.iamalex33329.linetechfresh2024_preassessment.network.RetrofitClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String API_KEY = "XXX";
    private static final String TAG = "MainActivity";

    private static boolean isAiActive = false;

    private final List<GptMessage> gptMessages = new ArrayList<>();
    private List<Message> messages = new ArrayList<>();

    private MessageAdapter messageAdapter;

    private RecyclerView chatHistoryRecyclerView;
    private EditText userInputField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initGptMessages();
        initRecyclerView();
    }

    private void initGptMessages() { gptMessages.add(new GptMessage("system", "你的回覆內容只能有「繁體中文 zh-TW」「英文 en」")); }

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
            addMessage(new Message(true, content, new Date()));
            userInputField.setText("");

            if (isAiActive) sendMessageToOpenAI(content);
        }
    }

    private void addMessage(Message message) {
        messages.add(message);
        messageAdapter.notifyItemInserted(messages.size() - 1);
        chatHistoryRecyclerView.scrollToPosition(messages.size() - 1);
    }

    private void sendMessageToOpenAI(String userInput) {
        gptMessages.add(new GptMessage("user", userInput));

        ApiService apiService = RetrofitClient.getApiService();
        String authHeader = "Bearer " + API_KEY;

        HashMap<String, Object> body = new HashMap<>();
        body.put("model", "gpt-3.5-turbo");
        body.put("messages", gptMessages);
        body.put("temperature", 0.8);

        apiService.sendMessage(authHeader, body).enqueue(new Callback<OpenAIResponse>() {
            @Override
            public void onResponse(Call<OpenAIResponse> call, Response<OpenAIResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (OpenAIResponse.Choice choice : response.body().choices) {
                        addMessage(new Message(false, choice.message.content, new Date()));
                        gptMessages.add(new GptMessage("assistant", choice.message.content));   // 紀錄對話內容
                    }
                }
            }
            @Override
            public void onFailure(Call<OpenAIResponse> call, Throwable t) {
                Log.e(TAG, "API 請求失敗：", t);
            }
        });
    }

    private static final String[] TEST_MESSAGES = {
            "。", "是喔", "我先去洗澡", "哈哈哈哈哈哈哈哈哈哈哈哈哈哈",
            "達文西密碼的上面是什麼？\n.\n.\n.\n.\n.\n.\n.\n.\n.\n.\n.\n.\n達文西帳號"
    };
}