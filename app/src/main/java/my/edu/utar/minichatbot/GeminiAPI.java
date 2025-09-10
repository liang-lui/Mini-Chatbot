package my.edu.utar.minichatbot;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class GeminiAPI {
    private static final String API_KEY = "AIzaSyA3ERRXsLwjvmJPOLpWz4Urlo9PgkHhcyg";
    private static final String ENDPOINT =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=";

    public static String askGemini(String question) throws Exception {
        OkHttpClient client = new OkHttpClient();

        // ✅ FAQ Prompt
        String faqPrompt =
                "You are a helpful university assistant. " +
                        "Here are the correct answers for specific questions:\n" +
                        "1. Where is the library? -> 📚 The library is located next to the main building.\n" +
                        "2. How to register for exams? -> 📝 You can register for exams online through the university portal.\n" +
                        "3. When is the canteen open? -> 🍽️ The canteen is open from 8 AM to 8 PM on working days.\n" +
                        "4. What sports are available? -> ⚽ We have football, basketball, and badminton facilities.\n" +
                        "5. How to contact a professor? -> 📧 You can contact your professor via the university email.\n\n" +
                        "If the question is one of these, always reply with the exact given answer. " +
                        "If not, answer normally as a university chatbot.\n\n" +
                        "User: " + question;

        JSONObject json = new JSONObject();
        JSONArray contents = new JSONArray();
        JSONObject part = new JSONObject();
        part.put("text", faqPrompt);

        JSONObject partsWrapper = new JSONObject();
        partsWrapper.put("parts", new JSONArray().put(part));
        contents.put(partsWrapper);

        json.put("contents", contents);

        RequestBody body = RequestBody.create(
                json.toString(),
                MediaType.get("application/json")
        );
        Request request = new Request.Builder()
                .url(ENDPOINT + API_KEY)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        String res = response.body().string();

        JSONObject root = new JSONObject(res);
        JSONArray candidates = root.getJSONArray("candidates");
        JSONObject first = candidates.getJSONObject(0);
        String answer = first.getJSONObject("content")
                .getJSONArray("parts")
                .getJSONObject(0)
                .getString("text");

        return answer.trim();
    }
}
