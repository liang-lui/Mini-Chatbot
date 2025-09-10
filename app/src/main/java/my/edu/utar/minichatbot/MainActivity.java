package my.edu.utar.minichatbot;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText userInput;
    private Button sendButton;
    private MessageAdapter adapter;
    private List<Message> messages;
    private GeminiApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化UI
        recyclerView = findViewById(R.id.recyclerView);
        userInput = findViewById(R.id.userInput);
        sendButton = findViewById(R.id.sendButton);

        // 初始化消息列表
        messages = new ArrayList<>();
        adapter = new MessageAdapter(messages);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // 初始化Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://generativelanguage.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(GeminiApiService.class);

        // 添加欢迎消息
        addMessage("Hello! I'm your university assistant. Ask me about library, exams, housing, scholarships, or meal plans!", false);

        // 发送按钮点击事件
        sendButton.setOnClickListener(v -> {
            String question = userInput.getText().toString().trim();
            if (!question.isEmpty()) {
                addMessage(question, true);
                userInput.setText("");
                getResponse(question);
            }
        });
    }

    private void addMessage(String text, boolean isUser) {
        Message message = new Message(text, isUser);
        messages.add(message);
        adapter.notifyItemInserted(messages.size() - 1);
        recyclerView.scrollToPosition(messages.size() - 1);
    }

    private void getResponse(String question) {
        // 首先检查预设问题
        String predefinedAnswer = getPredefinedAnswer(question);
        if (predefinedAnswer != null) {
            addMessage(predefinedAnswer, false);
            return;
        }

        // 使用Gemini API
        GeminiRequest request = new GeminiRequest(question);

        Call<GeminiResponse> call = apiService.generateContent(request, "AIzaSyA3ERRXsLwjvmJPOLpWz4Urlo9PgkHhcyg");
        call.enqueue(new Callback<GeminiResponse>() {
            @Override
            public void onResponse(Call<GeminiResponse> call, Response<GeminiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    addMessage(response.body().getText(), false);
                } else {
                    addMessage("Sorry, I'm having trouble connecting. Please try again.", false);
                }
            }

            @Override
            public void onFailure(Call<GeminiResponse> call, Throwable t) {
                addMessage("Network error. Please check your connection.", false);
            }
        });
    }

    private String getPredefinedAnswer(String question) {
        String lowerQuestion = question.toLowerCase();

        // 预设问答
        if (lowerQuestion.contains("library") || lowerQuestion.contains("where is library")) {
            return "The main library is at the center of campus, next to Student Union. Open 8AM-10PM weekdays, 10AM-6PM weekends.";
        } else if (lowerQuestion.contains("exam") || lowerQuestion.contains("register exam")) {
            return "Register exams through student portal: Academics -> Exam Registration. ";
        } else if (lowerQuestion.contains("housing") || lowerQuestion.contains("dorm")) {
            return "Housing applications online. First-year students guaranteed accommodation if applied before May 1st.";
        } else if (lowerQuestion.contains("scholarship") || lowerQuestion.contains("financial aid")) {
            return "Scholarship applications open January 15th. Visit Financial Aid Information in official website.";
        } else if (lowerQuestion.contains("application") || lowerQuestion.contains("parking")) {
            return "Parking fee: 100 / 1 semester";
        }

        return null;
    }
}
