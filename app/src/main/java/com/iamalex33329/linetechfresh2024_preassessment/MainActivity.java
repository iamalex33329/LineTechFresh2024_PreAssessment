package com.iamalex33329.linetechfresh2024_preassessment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView chatHistoryRecyclerView;
    private ImageView simulateMessageButton;
    private Button sendMessageButton;
    private EditText userInputField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chatHistoryRecyclerView = findViewById(R.id.chatHistoryRecyclerView);
        simulateMessageButton = findViewById(R.id.simulateMessageButton);
        sendMessageButton = findViewById(R.id.sendMessageButton);
        userInputField = findViewById(R.id.userInputField);

        simulateMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Receive");
            }
        });

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Send");
            }
        });
    }
}