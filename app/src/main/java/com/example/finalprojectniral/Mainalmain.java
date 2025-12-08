package com.example.finalprojectniral;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.finalprojectniral.data.myTasksTable.MyTassk;

import java.util.Calendar;

public class Mainalmain extends AppCompatActivity {

    TextView greetingText, subGreetingText;
    Button btnTasks, btnChat, btnStayInspired; // ← أضفنا الزر الجديد
    String username = "User"; // قيمة افتراضية

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainalmain);

        // ربط العناصر من XML
        greetingText = findViewById(R.id.greetingText);
        subGreetingText = findViewById(R.id.subGreetingText);
        btnTasks = findViewById(R.id.btnTasks);

        btnChat = findViewById(R.id.btnChat);
        btnStayInspired = findViewById(R.id.btnStayInspired); // ← ربط الزر الجديد

        // استقبال اسم المستخدم من شاشة تسجيل الدخول (إن وجد)
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            username = extras.getString("username", "User");
        }

        // تحديد الوقت الحالي
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        String greeting;

        if (hour >= 5 && hour < 12) {
            greeting = "Good Morning, " + username;
        } else if (hour >= 12 && hour < 18) {
            greeting = "Good Afternoon, " + username;
        } else if (hour >= 18 && hour < 22) {
            greeting = "Good Evening, " + username;
        } else {
            greeting = "Good Night, " + username;
        }

        greetingText.setText(greeting);
        subGreetingText.setText("Let's make today productive!");

        // معالجة أحداث الأزرار
        btnTasks.setOnClickListener(v -> {
            Intent intent = new Intent(Mainalmain.this, MyTassk.class);
            startActivity(intent);
        });


        btnChat.setOnClickListener(v -> {
            Intent intent = new Intent(Mainalmain.this, ChatActivity.class);
            startActivity(intent);
        });

        // 🔹 الزر الجديد: Stay Inspired
        btnStayInspired.setOnClickListener(v -> {
            Intent intent = new Intent(Mainalmain.this, StayInspiredActivity.class);
            startActivity(intent);
        });
    }
}
