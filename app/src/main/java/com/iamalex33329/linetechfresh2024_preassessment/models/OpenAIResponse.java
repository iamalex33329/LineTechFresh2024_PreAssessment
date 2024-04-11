package com.iamalex33329.linetechfresh2024_preassessment.models;

import java.util.List;

public class OpenAIResponse {
    public List<Choice> choices;
    public String object;
    public String model;
    public Usage usage;
    public String id;

    public static class Choice {
        public String finish_reason;
        public Message message;
        public Object logprobs;
        public int index;
    }

    public static class Message {
        public String content;
        public String role;
    }

    public static class Usage {
        public int completion_tokens;
        public int prompt_tokens;
        public int total_tokens;
    }
}
