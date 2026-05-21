package com.example.finalprojectniral;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.ai.FirebaseAI;
import com.google.firebase.vertexai.FirebaseVertexAI;
import com.google.firebase.vertexai.GenerativeModel;
import com.google.firebase.vertexai.java.GenerativeModelFutures;
import com.google.firebase.vertexai.type.Content;
import com.google.firebase.vertexai.type.GenerateContentResponse;

import java.util.concurrent.Executor;

public class ChatActivity extends AppCompatActivity {

    // تعريف العناصر المرئية بناءً على المصدر [1]
    private EditText etStudentInput;
    private Button btnAskAi;
    private ProgressBar pbLoading;
    private TextView tvAiResponse;

    /**
     * The generative AI model instance used to process student queries and provide guidance.
     */ // تعريف متغيرات الذكاء الاصطناعي بناءً على المصدر [2]
    private GenerativeModel ai;
    private GenerativeModelFutures model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat); // تأكد من اسم ملف ה-XML الخاص بك

        // ربط العناصر بالكود
        etStudentInput = findViewById(R.id.etTaskTopic); // استخدمت نفس الـ IDs من المصدر لسهولة التطبيق
        btnAskAi = findViewById(R.id.btnSuggestSteps);
        pbLoading = findViewById(R.id.pbLoading);
        tvAiResponse = findViewById(R.id.tvAiResponse);

        // 1. تهيئة موديل الذكاء الاصطناعي (Gemini) [2]
        // ملاحظة: المصدر ذكر "gemini-3-flash-preview" ولكن الموديل الشائع هو "gemini-1.5-flash"
// إذا كان الخطأ على FirebaseVertexAI، جرب هذا السطر:
        ai = FirebaseAI.getInstance().generativeModel("gemini-1.5-flash");
        model = GenerativeModelFutures.from(ai);

        // 2. تفعيل الزر عند النقر [3]
        btnAskAi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = etStudentInput.getText().toString().trim();
                if (!input.isEmpty()) {
                    askStudentCounselorAI(input);
                } else {
                    Toast.makeText(ChatActivity.this, "من فضلك اكتب سؤالك أو ما تشعر به", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * دالة للتواصل مع الذكاء الاصطناعي مخصصة لدعم الطلاب وتنظيم جداولهم [4]
     */
    private void askStudentCounselorAI(String userInput) {
        pbLoading.setVisibility(View.VISIBLE);
        tvAiResponse.setText("");
        btnAskAi.setEnabled(false);

        // بناء الـ Prompt ليكون مرشداً للطلاب (الدعم المعنوي والجدولة)
        String promptStr = "أنت مرشد طلابي خبير وداعم. الطالب يقول: '" + userInput + "'. " +
                "إذا كان الطالب يطلب تنظيماً لوقته أو جدولة مهامه، فقدم له خطة عمل واضحة ومرتبة. " +
                "وإذا كان الطالب يشعر بالإحباط أو يحتاج لدعم، فقدم له كلمات تشجيعية ونصائح معنوية بأسلوب لطيف.";

        Content prompt = new Content.Builder()
                .addText(promptStr)
                .build();

        // إرسال الطلب واستقبال الإجابة [4]
        ListenableFuture<GenerateContentResponse> response = model.generateContent(prompt);
        Executor executor = this::runOnUiThread;

        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                pbLoading.setVisibility(View.GONE);
                btnAskAi.setEnabled(true);
                // عرض رد الذكاء الاصطناعي في الـ TextView
                tvAiResponse.setText(result.getText());
            }

            @Override
            public void onFailure(Throwable t) {
                pbLoading.setVisibility(View.GONE);
                btnAskAi.setEnabled(true);
                Toast.makeText(ChatActivity.this, "حدث خطأ: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        }, executor);
    }
}