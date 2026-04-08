package com.example.finalprojectniral;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalprojectniral.data.myTasksTable.MyAssignment;

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
            Intent intent = new Intent(Mainalmain.this, TasksActivity.class);
            startActivity(intent);
        });


        btnChat.setOnClickListener(v -> {
            Intent intent = new Intent(Mainalmain.this, ChatActivity.class);
            startActivity(intent);
        });
        // 🔹 الزر الجديد: Stay Inspired
        btnStayInspired.setOnClickListener(v -> {
            //todo complete notification...
//            Intent intent = new Intent(Mainalmain.this, StayIN.class);
//            startActivity(intent);
        });

    }

    /**
     * @param hasCapture
     */
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }


//    public boolean onCreateOptionMenu (Menu menu){
//        getMenuInflater().inflate(R.menu.main_menu, menu);
//        return true;
//    }
//    @Override
//    public boolean onOptionsItemSelected (MenuItem item){
//        if (item.getItemId() == R.id.itemSettings) {
//            Toast.makeText(this, "Setting", Toast.LENGTH_SHORT).show();
//        }
//        if (item.getItemId() == R.id.itemSignUp) {
//            Toast.makeText(this, "Sign out", Toast.LENGTH_SHORT).show();
//        }
//        if (item.getItemId() == R.id.itemHistory) {
//            Toast.makeText(this, "History", Toast.LENGTH_SHORT).show();
//        }
//        return true;
//    }


}

