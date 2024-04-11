package com.iamalex33329.linetechfresh2024_preassessment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.iamalex33329.linetechfresh2024_preassessment.adapters.MessageAdapter;
import com.iamalex33329.linetechfresh2024_preassessment.models.Message;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String[] TEST_MESSAGES;

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView chatHistoryRecyclerView;
    private ImageView simulateMessageButton;
    private Button sendMessageButton;
    private EditText userInputField;

    private List<Message> messages;

    private MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initTestSentence();

        chatHistoryRecyclerView = findViewById(R.id.chatHistoryRecyclerView);
        simulateMessageButton = findViewById(R.id.simulateMessageButton);
        sendMessageButton = findViewById(R.id.sendMessageButton);
        userInputField = findViewById(R.id.userInputField);

        messages = new ArrayList<>();
        messageAdapter = new MessageAdapter(this, messages);
        chatHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatHistoryRecyclerView.setAdapter(messageAdapter);

        simulateMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = (int) (Math.random() * TEST_MESSAGES.length);

                messages.add(new Message(false, TEST_MESSAGES[i], new Date()));
                messageAdapter.notifyItemInserted(messages.size() - 1);
                chatHistoryRecyclerView.scrollToPosition(messages.size() - 1);
            }
        });

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = userInputField.getText().toString().trim();

                if (!content.isEmpty()) {
                    messages.add(new Message(true, content, new Date()));
                    messageAdapter.notifyItemInserted(messages.size() - 1);
                    chatHistoryRecyclerView.scrollToPosition(messages.size() - 1);
                }

                userInputField.setText("");
            }
        });
    }

    private void initTestSentence() {
        TEST_MESSAGES = new String[]{
                "Hey there! Just dropping by to say hello!",
                "How's your day treating you so far?",
                "I hope today brings you all the joy you deserve!",
                "Any exciting plans on the agenda for today?",
                "Sending you a virtual hug and some positivity!",
                "Have you tried any new activities recently?",
                "Let's strive to make today even better than yesterday!",
                "What's one thing that brought a smile to your face today?",
                "Keep your head up high, the sky's the limit!",
                "Remember to take some time for self-care and relaxation!",
                "I'm eager to hear all about your day!",
                "Every small step forward is progress worth celebrating!",
                "What's one thing you're thankful for in this moment?",
                "Stay focused on your goals, you're doing great!",
                "May your day be filled with laughter and good vibes!",
                "Enjoy the journey and cherish every moment along the way!",
                "You're more resilient than you realize!",
                "Trust in your abilities, you're capable of achieving anything!",
                "Let's make the most out of today and create memories!",
                "Wishing you a day brimming with happiness and positivity!",
                "Life is an adventure waiting to unfold!",
                "Never underestimate the power of taking a break and recharging!",
                "Every obstacle you overcome makes you stronger!",
                "Believe in yourself, you're capable of overcoming any challenge!",
                "Carpe diem! Seize the day and make it extraordinary!",
                "Sending you a cascade of good vibes and warm wishes!",
                "The world is full of opportunities, go out and seize them!",
                "Let's embrace the day with open arms and a positive mindset!",
                "You're a beacon of strength and resilience!",
                "Even on tough days, remember that brighter days are ahead!",
                "Trust in the journey and enjoy the process!",
                "Your positive energy can light up the darkest of days!",
                "Remember to give yourself credit for how far you've come!",
                "Every setback is just a setup for a comeback!"
        };
    }
}