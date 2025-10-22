package com.example.globetrotter.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Consumer;

import org.json.JSONObject;

public class GeoInfoService {

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String apiKey = loadApiKey();

    // Load API key from local file "openai.key"
    private static String loadApiKey() {
        try {
            String key = Files.readString(Paths.get("openai.key")).trim();
            if (key.isEmpty()) {
                System.err.println("[GeoInfoService] openai.key file is empty!");
                return null;
            }
            System.out.println("[GeoInfoService] ✅ Loaded API key from openai.key");
            return key;
        } catch (IOException e) {
            System.err.println("[GeoInfoService] ❌ Could not read openai.key — place your key file in the project root.");
            return null;
        }
    }

    public GeoInfoService() {
        if (apiKey == null) {
            System.err.println("[GeoInfoService] ⚠ No API key loaded — requests will fail.");
        } else {
            System.out.println("[GeoInfoService] Using external API key from openai.key");
        }
    }

    // ------------------------------------------------------------
    //  1️⃣ ArcGIS InfoPanel generator
    // ------------------------------------------------------------
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

        sendRequestAsync(prompt, callback);
    }

    // ------------------------------------------------------------
    //  2️⃣ Ask-question mode for “InfoPanel Ask” button
    // ------------------------------------------------------------
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

        sendRequestAsync(prompt, callback);
    }

    // ------------------------------------------------------------
    //  3️⃣ Shared request handler
    // ------------------------------------------------------------
    private void sendRequestAsync(String prompt, Consumer<String> callback) {
        if (apiKey == null || apiKey.isBlank()) {
            callback.accept("❌ API key missing. Please create an openai.key file in the project root.");
            return;
        }

        try {
            JSONObject json = new JSONObject();
            json.put("model", "gpt-4o-mini");
            json.put("messages", new org.json.JSONArray()
                    .put(new JSONObject().put("role", "system")
                            .put("content", "You are a helpful geography tutor for kids."))
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
                            JSONObject res = new JSONObject(body);
                            if (res.has("error")) {
                                callback.accept("GPT Service Error: " +
                                        res.getJSONObject("error").optString("message", "Unknown API error"));
                            } else {
                                String answer = res.getJSONArray("choices")
                                        .getJSONObject(0)
                                        .getJSONObject("message")
                                        .getString("content");
                                callback.accept(answer);
                            }
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
