package com.example.finalprojectniral;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

// استيراد المكتبات اللازمة للتعامل مع العمليات الخلفية (Guava)
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.ai.FirebaseAI;
import com.google.firebase.ai.GenerativeModel;
import com.google.firebase.ai.java.GenerativeModelFutures;
import com.google.firebase.ai.type.Content;
import com.google.firebase.ai.type.GenerateContentResponse;
import com.google.firebase.ai.type.GenerativeBackend;

import java.util.concurrent.Executor;

public class ChatActivity extends AppCompatActivity {
    private EditText etTaskTopic;
    private Button btnSuggestSteps;
    private TextView tvAiResponse;
    private ProgressBar pbLoading;

    private GenerativeModel ai;
    private GenerativeModelFutures model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // ربط العناصر بالواجهة (كل العناصر الموجودة في XML)
        etTaskTopic = findViewById(R.id.etTaskTopic);
        btnSuggestSteps = findViewById(R.id.btnSuggestSteps);
        tvAiResponse = findViewById(R.id.tvAiResponse);
        pbLoading = findViewById(R.id.pbLoading);

        // تهيئة Gemini
        ai = FirebaseAI.getInstance(GenerativeBackend.googleAI())
                .generativeModel("gemini-3-flash-preview");

        model = GenerativeModelFutures.from(ai);

        // عند الضغط على الزر
        btnSuggestSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String task = etTaskTopic.getText().toString().trim();

                if (!task.isEmpty()) {
                    askFirebaseAiGeminiForSteps(task);
                } else {
                    Toast.makeText(ChatActivity.this, "Please enter a topic", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * تقوم هذه الدالة بإرسال طلب إلى نموذج Gemini AI للحصول على خطوات مقترحة لموضوع معين.
     * 
     * @param topic الموضوع الذي سيتم طلب خطوات تنفيذه من الذكاء الاصطناعي.
     */
    private void askFirebaseAiGeminiForSteps(String topic) {
        pbLoading.setVisibility(View.VISIBLE);
        tvAiResponse.setText("");
        btnSuggestSteps.setEnabled(false);

        String promptStr = "I want to perform the following task: '" + topic + "'. " +
                "Can you suggest a clear, step-by-step checklist to complete this task effectively?";

        Content prompt = new Content.Builder()
                .addText(promptStr)
                .build();

        ListenableFuture<GenerateContentResponse> response = model.generateContent(prompt);

        Executor executor = this::runOnUiThread;

        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                pbLoading.setVisibility(View.GONE);
                btnSuggestSteps.setEnabled(true);
                tvAiResponse.setText(result.getText());
            }

            @Override
            public void onFailure(Throwable t) {
                pbLoading.setVisibility(View.GONE);
                btnSuggestSteps.setEnabled(true);
                Toast.makeText(ChatActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        }, executor);
    }
}
