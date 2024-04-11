package com.iamalex33329.linetechfresh2024_preassessment.services;

import android.util.Log;

import com.iamalex33329.linetechfresh2024_preassessment.models.GptMessage;
import com.iamalex33329.linetechfresh2024_preassessment.models.OpenAIResponse;
import com.iamalex33329.linetechfresh2024_preassessment.network.ApiService;
import com.iamalex33329.linetechfresh2024_preassessment.network.RetrofitClient;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OpenAIService {

    private static final String TAG = "OpenAIService";
    private static final String OPENAI_API_KEY = "sk-92jOgMb5iNnJ2eVJcMgQY9SBRKotLN565LxTj87WKiVSsI4n"; // fake api key

    private final ApiService apiService;

    public OpenAIService() {
        apiService = RetrofitClient.getApiService();
    }

    public void sendMessageToOpenAI(List<GptMessage> gptMessages, final ResponseHandler handler) {
        String authHeader = "Bearer " + OPENAI_API_KEY;
        HashMap<String, Object> body = new HashMap<>();
        body.put("model", "gpt-3.5-turbo");
        body.put("messages", gptMessages);
        body.put("temperature", 0.8);

        apiService.sendMessage(authHeader, body).enqueue(new Callback<OpenAIResponse>() {
            @Override
            public void onResponse(Call<OpenAIResponse> call, Response<OpenAIResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    handler.onSuccess(response.body());
                } else {
                    Log.d(TAG, "Response not successful or is empty");
                    handler.onError(new Exception("Response not successful or is empty"));
                }
            }

            @Override
            public void onFailure(Call<OpenAIResponse> call, Throwable t) {
                Log.e(TAG, "API access failure: ", t);
                handler.onError(t);
            }
        });
    }

    public interface ResponseHandler {
        void onSuccess(OpenAIResponse response);
        void onError(Throwable t);
    }
}
