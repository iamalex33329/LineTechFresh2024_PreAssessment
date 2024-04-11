package com.iamalex33329.linetechfresh2024_preassessment.services;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iamalex33329.linetechfresh2024_preassessment.models.Message;

import java.util.ArrayList;
import java.util.List;

public class FirebaseChatService {

    private static final String TAG = "FirebaseChatService";
    private static final String FIREBASE_URL = "https://linetechfresh2024alextsai-default-rtdb.asia-southeast1.firebasedatabase.app/";

    private final DatabaseReference databaseReference;
    private final Context context;

    public interface MessageLoadListener {
        void onMessagesLoaded(List<Message> messages);
        void onError(DatabaseError databaseError);
    }

    public FirebaseChatService(Context context) {
        this.context = context;
        FirebaseDatabase database = FirebaseDatabase.getInstance(FIREBASE_URL);
        this.databaseReference = database.getReference();
    }

    public String getUserDeviceId() {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public void saveMessageToFirebase(Message message) {
        String deviceId = getUserDeviceId();
        databaseReference.child("messages").child(deviceId).push().setValue(message.toMap())
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Saved message to Firebase"))
                .addOnFailureListener(e -> Log.e(TAG, "Error when saving message to Firebase: ", e));
    }

    public void loadMessagesFromFirebase(final MessageLoadListener listener) {
        String deviceId = getUserDeviceId();
        databaseReference.child("messages").child(deviceId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Message> loadedMessages = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Message message = snapshot.getValue(Message.class);
                            if (message != null) {
                                loadedMessages.add(message);
                            }
                        }
                        listener.onMessagesLoaded(loadedMessages);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e(TAG, "Failed to read chat history from Firebase: ", databaseError.toException());
                        listener.onError(databaseError);
                    }
                });
    }
}
