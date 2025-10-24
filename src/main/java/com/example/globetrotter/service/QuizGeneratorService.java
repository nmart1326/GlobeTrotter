package com.example.globetrotter.service;

import com.example.globetrotter.model.QuizQuestion;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Handles GPT-based quiz generation.
 * Given a topic (e.g. "Australian Wildlife"), it asks GPT-4o to return
 * 10 multiple-choice questions in JSON format, then parses them into QuizQuestion objects.
 */
public class QuizGeneratorService {

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String apiKey = loadApiKey();

    private static String lastGeneratedTitle;

    public String getLastGeneratedTitle() {
        return lastGeneratedTitle;
    }


    private static String loadApiKey() {
        try {
            String key = Files.readString(Paths.get("openai.key")).trim();
            if (key.isEmpty()) {
                System.err.println("[QuizGeneratorService] openai.key file is empty!");
                return null;
            }
            System.out.println("[QuizGeneratorService] Loaded API key from openai.key");
            return key;
        } catch (IOException e) {
            System.err.println("[QuizGeneratorService] Could not read openai.key");
            return null;
        }
    }

    public QuizGeneratorService() {
        if (apiKey == null) {
            System.err.println("[QuizGeneratorService] No API key loaded — GPT calls will fail.");
        } else {
            System.out.println("[QuizGeneratorService] Using API key from openai.key");
        }
    }

    // Generates a 10-question multiple-choice quiz for the given topic.
    //The callback receives either a success list or an error message.

    public void generateQuizAsync(String topic, Consumer<List<QuizQuestion>> callback, Consumer<String> errorHandler) {
        if (apiKey == null || apiKey.isBlank()) {
            errorHandler.accept("Missing API key. Please add openai.key in project root.");
            return;
        }

        String prompt = """
        You are a teacher preparing quizzes for students aged 8–15.

        Create 10 multiple-choice questions about the topic: "%s".
        Each question must have 4 answer options (A, B, C, D) and one correct answer letter.

        Return ONLY valid JSON (no explanations, no extra text) in this exact format:
        {
          "quiz_title": "<short title>",
          "questions": [
            {
              "question": "string",
              "A": "option A text",
              "B": "option B text",
              "C": "option C text",
              "D": "option D text",
              "correct": "A"
            },
            ...
          ]
        }

        Keep all text concise and suitable for ages 8–15.
        """.formatted(topic);

        try {
            JSONObject body = new JSONObject();
            body.put("model", "gpt-4o");
            JSONArray messages = new JSONArray()
                    .put(new JSONObject().put("role", "system").put("content", "You are a helpful quiz creator for kids."))
                    .put(new JSONObject().put("role", "user").put("content", prompt));
            body.put("messages", messages);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(OPENAI_API_URL))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                    .build();

            HttpClient.newHttpClient()
                    .sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(response -> {
                        try {
                            JSONObject res = new JSONObject(response);
                            if (res.has("error")) {
                                errorHandler.accept(res.getJSONObject("error").optString("message", "Unknown API error"));
                                return;
                            }

                            String content = res.getJSONArray("choices")
                                    .getJSONObject(0)
                                    .getJSONObject("message")
                                    .getString("content");

                            JSONObject json = new JSONObject(content);
                            //capture GPT's quiz title for use elsewhere
                            lastGeneratedTitle = json.optString("quiz_title", null);
                            JSONArray arr = json.getJSONArray("questions");

                            List<QuizQuestion> questions = new ArrayList<>();
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject q = arr.getJSONObject(i);
                                QuizQuestion qq = new QuizQuestion(
                                        0,  // QuestionID will auto-increment
                                        0,  // QuizID will be set later
                                        q.getString("question"),
                                        q.getString("A"),
                                        q.getString("B"),
                                        q.getString("C"),
                                        q.getString("D"),
                                        q.getString("correct")
                                );
                                questions.add(qq);
                            }

                            callback.accept(questions);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            errorHandler.accept("Error parsing GPT response. Possibly invalid JSON.");
                        }
                    })
                    .exceptionally(ex -> {
                        ex.printStackTrace();
                        errorHandler.accept("Network or GPT request error: " + ex.getMessage());
                        return null;
                    });

        } catch (Exception e) {
            e.printStackTrace();
            errorHandler.accept("Unexpected error: " + e.getMessage());
        }
    }
}
