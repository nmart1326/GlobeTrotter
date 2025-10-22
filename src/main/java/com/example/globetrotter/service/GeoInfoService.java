package com.example.globetrotter.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Consumer; //added async

import org.json.JSONObject;

public class GeoInfoService {

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";

    // added API key
    private static final String apiKey = "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";
    // end added API key

    public GeoInfoService() {
        System.out.println("[GeoInfoService] Using built-in API key");
    }

    /**
     * Sends ArcGIS feature data to the GPT model for interpretation asynchronously.
     * @param featureData Raw string from clicked ArcGIS features.
     * @param callback Function to receive the GPT response text once ready.
     */
    public void explainFeatureAsync(String featureData, Consumer<String> callback) {
        String prompt = """
You are a geography guide for students aged 7–14.

You will receive several ArcGIS feature data blocks that all describe the SAME general location 
—for example, one may describe the country, another the state, and another the city.

Your task: **write ONE single InfoPanel** that summarizes them together — 
do NOT write separate panels or repeat headings.

💡 Rules:
- Always output only ONE combined summary.
- The final InfoPanel should be centered around the *most specific location* (e.g., the city), 
  and include broader context (state and country) inside its details.
- Merge data logically and remove repetition.
- If multiple blocks share data (like “Country: Australia”), include it once.
- If information is missing, fill it using your world knowledge.
- Keep tone light, educational, and concise (aim for under 12 lines).
- Use Markdown formatting and emojis exactly as shown below.

📋 Output Format (must always match this style):

{Main City or Place Name} — A Quick Look!

Where are we?  
<1–2 fun sentences describing where it is, including state and country context>

Fast Facts:  
Country: …  
State: …  
Area: …  
Population: …  
Density: …  
Other codes: …

Did You Know?  
<1–2 interesting or fun facts about this place>

Now combine and summarize all ArcGIS data into ONE InfoPanel below:
""" + featureData;

        try {
            JSONObject json = new JSONObject();
            json.put("model", "gpt-4o-mini");
            json.put("messages", new org.json.JSONArray()
                    .put(new JSONObject().put("role", "system").put("content", "You are a helpful teaching assistant."))
                    .put(new JSONObject().put("role", "user").put("content", prompt)));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(OPENAI_API_URL))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(body -> {
                        try {
                            JSONObject responseJson = new JSONObject(body);
                            if (responseJson.has("error")) {
                                JSONObject err = responseJson.getJSONObject("error");
                                String message = err.optString("message", "Unknown API error");
                                callback.accept("GPT Service Error: " + message);
                                return;
                            }

                            String content = responseJson.getJSONArray("choices")
                                    .getJSONObject(0)
                                    .getJSONObject("message")
                                    .getString("content");

                            callback.accept(content);
                        } catch (Exception e) {
                            e.printStackTrace();
                            callback.accept("Error parsing GPT response.");
                        }
                    })
                    .exceptionally(ex -> {
                        ex.printStackTrace();
                        callback.accept("Error contacting GPT service.");
                        return null;
                    });

        } catch (Exception e) {
            e.printStackTrace();
            callback.accept("Unexpected error in GeoInfoService: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------------------
    // Added new method for InfoPanel Ask button
    // ---------------------------------------------------------------------
    public void askQuestionAsync(String question, Consumer<String> callback) {
        String prompt = """
You are a friendly geography tutor for students aged 7–14.

Answer only questions about geography, countries, cities, landscapes, population,
languages, flags, or natural features.

If the question is unrelated, adult, political, or inappropriate, respond with:
"I'm only able to answer geography questions for students."

Keep answers concise (under 8 lines) and written in simple, child-friendly language that is rated G.

Question:
""" + question;

        try {
            JSONObject json = new JSONObject();
            json.put("model", "gpt-4o-mini");
            json.put("messages", new org.json.JSONArray()
                    .put(new JSONObject().put("role", "system")
                            .put("content", "You are a helpful, G-rated geography tutor for kids."))
                    .put(new JSONObject().put("role", "user").put("content", prompt)));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(OPENAI_API_URL))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(body -> {
                        try {
                            JSONObject responseJson = new JSONObject(body);
                            if (responseJson.has("error")) {
                                String message = responseJson.getJSONObject("error")
                                        .optString("message", "Unknown API error");
                                callback.accept("GPT Service Error: " + message);
                                return;
                            }

                            String answer = responseJson.getJSONArray("choices")
                                    .getJSONObject(0)
                                    .getJSONObject("message")
                                    .getString("content");

                            callback.accept(answer);
                        } catch (Exception e) {
                            e.printStackTrace();
                            callback.accept("Error parsing GPT response.");
                        }
                    })
                    .exceptionally(ex -> {
                        ex.printStackTrace();
                        callback.accept("Error contacting GPT service.");
                        return null;
                    });

        } catch (Exception e) {
            e.printStackTrace();
            callback.accept("Unexpected error: " + e.getMessage());
        }
    }
}
