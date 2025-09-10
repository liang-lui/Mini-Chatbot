package my.edu.utar.minichatbot;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText userInput;
    private Button sendButton;
    private ChatAdapter adapter;
    private List<ChatMessage> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        userInput = findViewById(R.id.userInput);
        sendButton = findViewById(R.id.sendButton);

        messageList = new ArrayList<>();
        adapter = new ChatAdapter(messageList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        sendButton.setOnClickListener(v -> {
            String question = userInput.getText().toString().trim();
            if (!question.isEmpty()) {
                addMessage(question, true); // user message
                String response = getBotResponse(question);
                addMessage(response, false); // bot message
                userInput.setText("");
            }
        });
    }

    private void addMessage(String text, boolean isUser) {
        messageList.add(new ChatMessage(text, isUser));
        adapter.notifyItemInserted(messageList.size() - 1);
        recyclerView.scrollToPosition(messageList.size() - 1);
    }
    private String getBotResponse (String question){
        question = question.toLowerCase();

        if (question.contains("where") && question.contains("library")) {
            return "The library is located next to the main building.";
        } else if (question.contains("register") && question.contains("exam")) {
            return "You can register for exams online through the university portal.";
        } else if (question.contains("canteen") && question.contains("when")) {
            return "The canteen is open from 8 AM to 8 PM working day.";
        } else if (question.contains("sports")) {
            return "We have football, basketball, and badminton facilities.";
        } else if (question.contains("contact professor")) {
            return "You can contact your professor via the university email.";
        } else {
            return "Sorry, I don't know the answer. Please ask something else!";
        }
    }
}
