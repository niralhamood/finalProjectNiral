package com.example.finalprojectniral;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity3 extends AppCompatActivity {
    private TextView SignInn;
    private TextView edt;
    private TextView edt2;
    private TextView Un2;
    private TextView Psw2;
    private Button Enter;
    private TextView FG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main3);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        SignInn= findViewById(R.id.SignInn);
        edt=findViewById(R.id.edt);
        edt2=findViewById(R.id.edt2);
        Un2=findViewById(R.id.Un2);
        Psw2=findViewById(R.id.Pw2);
        Enter=findViewById(R.id.Enter);
        FG=findViewById(R.id.FG);
    }
    public void onClickGo22(View v){
        Intent intent = new Intent(MainActivity3.this, Mainalmain.class);
        startActivity(intent);
        finish();
    }
}