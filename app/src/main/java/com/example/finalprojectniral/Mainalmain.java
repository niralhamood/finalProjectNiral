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

    /** نص الترحيب الرئيسي */
    TextView greetingText;
    /** نص الترحيب الفرعي */
    TextView subGreetingText;
    /** أزرار التنقل للمهام، المحادثة، والإلهام */
    Button btnTasks, btnChat, btnStayInspired;
    /** اسم المستخدم المعروض في الترحيب، القيمة الافتراضية هي "User" */
    String username = "User";

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

        // تعريف الدوال المستخدمة في هذا المكون

        /*
          setOnClickListener: This is a method used to register a callback (a listener) that will be
          invoked when the view (in this case, a Button) is clicked by the user. It enables
          interactivity for the UI elements.
         */

        /**
         * Sets up the click listener for the tasks button.
         * When clicked, it creates an {@link Intent} to navigate from the current activity
         * to the TasksActivity.
         * {@code startActivity(intent)} is then called to execute the transition and launch
         * the new screen.
         */
        btnTasks.setOnClickListener(v -> {
            // Intent: Represents a 'description' of an operation to be performed.
            // Here, it defines the source (Mainalmain.this) and the destination (TasksActivity.class).
            Intent intent = new Intent(Mainalmain.this, TasksActivity.class);
            // startActivity: A method provided by the Activity class that takes an Intent
            // and sends it to the Android System to launch the specified activity.
            startActivity(intent);
        });

        /**
         * Sets up the click listener for the chat button.
         * <p>
         * {@code setOnClickListener} is an interface that "listens" for user interaction.
         * The {@link Intent} acts as a message to the Android system indicating which
         * activity should be started next.
         * {@link #startActivity(Intent)} starts an instance of the {@code ChatActivity}.
         */
        btnChat.setOnClickListener(v -> {
            Intent intent = new Intent(Mainalmain.this, ChatActivity.class);
            startActivity(intent);
        });

        // 🔹 الزر الجديد: Stay Inspired
        /**
         * Sets up the click listener for the inspiration button.
         * <p>
         * This block creates a new {@code Intent} targeting {@code StayInspired.class}.
         * The {@code startActivity} method is the actual command that tells the
         * Android OS to bring the {@code StayInspired} screen to the foreground.
         */
        btnStayInspired.setOnClickListener(v -> {
           Intent intent = new Intent(Mainalmain.this, StayInspired.class);
           startActivity(intent);
        });

    }

    /**
     * هذه الدالة تُستدعى عندما تتغير حالة "التقاط المؤشر" (Pointer Capture).
     *
     * شرح مبسط:
     * "التقاط المؤشر" هي ميزة تُستخدم غالباً في الألعاب أو التطبيقات التي تتطلب تحكماً دقيقاً بالفأرة (Mouse).
     * - عندما يتم تفعيلها (hasCapture = true): يختفي مؤشر الفأرة العادي، وتصبح كل حركات الفأرة
     *   محصورة داخل التطبيق حتى لو خرجت عن حدود النافذة.
     * - تُفيد في الحصول على حركات الفأرة الخام (Relative movement) بدلاً من إحداثيات الشاشة.
     *
     * ملاحظة: في معظم تطبيقات الأندرويد العادية (مثل تطبيق المهام هذا)، لا نحتاج لتعديل هذه الدالة
     * وتُترك كما هي لاستدعاء السلوك الافتراضي للنظام.
     *
     * @param hasCapture قيمتها true إذا تم تفعيل التقاط المؤشر، وfalse إذا فُقد.
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

