package my.edu.utar.minichatbot;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GeminiApiService {
    @POST("v1beta/models/gemini-pro:generateContent")
    Call<GeminiResponse> generateContent(
            @Body GeminiRequest request,
            @Query("AIzaSyA3ERRXsLwjvmJPOLpWz4Urlo9PgkHhcyg") String apiKey);
}
