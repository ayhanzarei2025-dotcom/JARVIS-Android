package com.jarvis.ai;

import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * Multi-provider HTTP client.
 * Supports OpenAI Chat Completions-compatible APIs (OpenAI, OpenRouter, Groq,
 * Together, compatible gateways, etc.) and Google's native Gemini generateContent API.
 */
public class AIClient {
    public interface Callback { void done(String text); }

    private String endpoint = "https://api.openai.com/v1/chat/completions";
    private String key = "";
    private String model = "gpt-4o-mini";

    public void configure(String endpoint, String key, String model) {
        if (endpoint != null && !endpoint.trim().isEmpty()) this.endpoint = endpoint.trim();
        this.key = key == null ? "" : key.trim();
        if (model != null && !model.trim().isEmpty()) this.model = model.trim();
    }

    public void ask(String prompt, Callback callback) { post(prompt, null, callback); }
    public void vision(String prompt, byte[] jpeg, Callback callback) { post(prompt, jpeg, callback); }

    private boolean isGemini(String url) {
        String u = url.toLowerCase(Locale.ROOT);
        return u.contains("generativelanguage.googleapis.com")
                || u.contains("://") && u.contains("gemini") && u.contains("generatecontent");
    }

    private String geminiUrl() throws Exception {
        String u = endpoint.trim();
        if (u.contains("{model}")) u = u.replace("{model}", model);
        if (u.endsWith("/v1beta") || u.endsWith("/v1beta/")) {
            if (u.endsWith("/")) u = u.substring(0, u.length() - 1);
            u += "/models/" + model + ":generateContent";
        } else if (u.endsWith("/v1") || u.endsWith("/v1/")) {
            if (u.endsWith("/")) u = u.substring(0, u.length() - 1);
            u += "/models/" + model + ":generateContent";
        } else if (!u.contains(":generateContent")) {
            if (!u.endsWith("/")) u += "/";
            u += "models/" + model + ":generateContent";
        }
        String separator = u.contains("?") ? "&" : "?";
        if (!key.isEmpty() && !u.matches("(?i).*([?&])key=.*")) {
            u += separator + "key=" + URLEncoder.encode(key, "UTF-8");
        }
        return u;
    }

    private void post(String prompt, byte[] image, Callback callback) {
        new Thread(() -> {
            String output;
            HttpURLConnection connection = null;
            try {
                boolean gemini = isGemini(endpoint);
                String requestUrl = gemini ? geminiUrl() : endpoint;
                connection = (HttpURLConnection) new URL(requestUrl).openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setConnectTimeout(20000);
                connection.setReadTimeout(90000);
                connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                if (!gemini && !key.isEmpty()) {
                    connection.setRequestProperty("Authorization", "Bearer " + key);
                }

                JSONObject body = gemini ? buildGeminiBody(prompt, image) : buildOpenAiBody(prompt, image);
                try (OutputStream stream = connection.getOutputStream()) {
                    stream.write(body.toString().getBytes(StandardCharsets.UTF_8));
                }

                int code = connection.getResponseCode();
                InputStream input = code >= 200 && code < 300
                        ? connection.getInputStream() : connection.getErrorStream();
                String response = readAll(input);
                if (code < 200 || code >= 300) {
                    output = "AI error " + code + ": " + extractError(response);
                } else {
                    output = gemini ? parseGemini(response) : parseOpenAiCompatible(response);
                }
            } catch (Exception exception) {
                output = "AI connection error: " + exception.getMessage();
            } finally {
                if (connection != null) connection.disconnect();
            }
            String result = output;
            new Handler(Looper.getMainLooper()).post(() -> callback.done(result));
        }).start();
    }

    private JSONObject buildOpenAiBody(String prompt, byte[] image) throws Exception {
        JSONObject body = new JSONObject();
        body.put("model", model);
        JSONArray messages = new JSONArray();
        JSONObject message = new JSONObject().put("role", "user");
        if (image == null) {
            message.put("content", prompt);
        } else {
            JSONArray parts = new JSONArray();
            parts.put(new JSONObject().put("type", "text").put("text", prompt));
            parts.put(new JSONObject().put("type", "image_url").put("image_url",
                    new JSONObject().put("url", "data:image/jpeg;base64,"
                            + Base64.encodeToString(image, Base64.NO_WRAP))));
            message.put("content", parts);
        }
        messages.put(message);
        body.put("messages", messages);
        return body;
    }

    private JSONObject buildGeminiBody(String prompt, byte[] image) throws Exception {
        JSONArray parts = new JSONArray();
        parts.put(new JSONObject().put("text", prompt));
        if (image != null) {
            parts.put(new JSONObject().put("inline_data", new JSONObject()
                    .put("mime_type", "image/jpeg")
                    .put("data", Base64.encodeToString(image, Base64.NO_WRAP))));
        }
        JSONArray contents = new JSONArray();
        contents.put(new JSONObject().put("role", "user").put("parts", parts));
        return new JSONObject().put("contents", contents)
                .put("generationConfig", new JSONObject().put("temperature", 0.7));
    }

    private String parseOpenAiCompatible(String response) throws Exception {
        JSONObject json = new JSONObject(response);
        JSONArray choices = json.optJSONArray("choices");
        if (choices != null && choices.length() > 0) {
            JSONObject choice = choices.getJSONObject(0);
            JSONObject message = choice.optJSONObject("message");
            if (message != null) {
                Object content = message.opt("content");
                if (content instanceof String) return (String) content;
                if (content instanceof JSONArray) return contentParts((JSONArray) content);
            }
            JSONObject delta = choice.optJSONObject("delta");
            if (delta != null) return delta.optString("content", response);
        }
        return json.optString("output_text", response);
    }

    private String parseGemini(String response) throws Exception {
        JSONObject json = new JSONObject(response);
        JSONArray candidates = json.optJSONArray("candidates");
        if (candidates != null && candidates.length() > 0) {
            JSONObject content = candidates.getJSONObject(0).optJSONObject("content");
            if (content != null) {
                JSONArray parts = content.optJSONArray("parts");
                if (parts != null) {
                    StringBuilder text = new StringBuilder();
                    for (int i = 0; i < parts.length(); i++) {
                        String part = parts.getJSONObject(i).optString("text", "");
                        if (!part.isEmpty()) {
                            if (text.length() > 0) text.append("\n");
                            text.append(part);
                        }
                    }
                    if (text.length() > 0) return text.toString();
                }
            }
        }
        JSONObject error = json.optJSONObject("error");
        if (error != null) return "Gemini error: " + error.optString("message", response);
        return response;
    }

    private String contentParts(JSONArray parts) throws Exception {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < parts.length(); i++) {
            JSONObject part = parts.optJSONObject(i);
            if (part != null) {
                String value = part.optString("text", "");
                if (!value.isEmpty()) {
                    if (text.length() > 0) text.append("\n");
                    text.append(value);
                }
            }
        }
        return text.length() == 0 ? parts.toString() : text.toString();
    }

    private String extractError(String response) {
        try {
            JSONObject json = new JSONObject(response);
            JSONObject error = json.optJSONObject("error");
            if (error != null) return error.optString("message", response);
            return response;
        } catch (Exception ignored) {
            return response;
        }
    }

    private String readAll(InputStream input) throws Exception {
        if (input == null) return "";
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(input, StandardCharsets.UTF_8))) {
            StringBuilder text = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) text.append(line);
            return text.toString();
        }
    }
}
