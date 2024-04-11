package com.iamalex33329.linetechfresh2024_preassessment.network;

import com.iamalex33329.linetechfresh2024_preassessment.models.OpenAIResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {
    @Headers({"Content-Type: application/json"})
    @POST("v1/chat/completions")
    Call<OpenAIResponse> sendMessage(@Header("Authorization") String authHeader, @Body Map<String, Object> body);
}
