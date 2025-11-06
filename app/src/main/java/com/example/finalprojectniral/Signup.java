package com.example.finalprojectniral;

import android.annotation.SuppressLint;
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

import org.w3c.dom.Text;

public class Signup extends AppCompatActivity {
    private TextView ed;
    private TextView ed2;
    private TextView Un;
    private TextView Pw;
    private Button SignUp;
    private TextView textView4;
    private Button SignIn;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ed=findViewById(R.id.ed);
        ed2=findViewById(R.id.ed2);
        Un=findViewById(R.id.Un);
        Pw=findViewById(R.id.Pw);
        SignUp=findViewById(R.id.SignUp);
        SignIn=findViewById(R.id.SignIn);
        textView4=findViewById(R.id.textView4);
    }

    /**
     * Called when pointer capture is enabled or disabled for the current window.
     *
     * @param hasCapture True if the window has pointer capture.
     */
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    public void onClickGo(View v){
        Intent intent = new Intent(Signup.this, Signin.class);
        startActivity(intent);
        finish();

    }
    public void onClickGo2(View v){
        Intent intent = new Intent(Signup.this, Mainalmain.class);
        startActivity(intent);
        finish();
    }

}