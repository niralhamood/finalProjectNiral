package com.example.finalprojectniral;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    /**
     * TextView لعرض نص التحميل أو اسم التطبيق أثناء شاشة الترحيب.
     */
    private TextView tvLoading;

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @SuppressLint({"MissingInflatedId", "MissingSuperCall"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tvLoading = findViewById(R.id.tvLoading);


        // נוסף הודעה כדי לספק تفاصيل عن الקוד
        // הקוד כאן מוריד 3 ثانיות בעברית ולאחר מכן יופיע אובר חדש עם הסינון להתחלת הרישום
        // 3000 מילישניים = 3 ثوانי
        // אנו משתמשים במתודה postDelayed שמקבלת אוביקט מסוג Runnable ומתווספת זמן במילישניות
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // כאן נישיא להתחלת הרישום באמצעות התנהגות של האפליקציה
                Intent intent = new Intent(MainActivity.this, Signup.class);
                startActivity(intent);
                finish(); // אם רוצים לסגור את המסך הנוכחי
            }
        }, 3000); // 3000 מילישניים = 3 ثوانי
    }
}




