package com.example.finalprojectniral;


import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalprojectniral.data.myTasksTable.MyAssignment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;

public class addAsigment extends AppCompatActivity {

    private TextInputEditText etShortTitle, etImportance, etWorkLoad, etDescription;
    private MaterialButton btnSave, btnSelectImage;
    private ImageView ivAssignment;
    private MaterialCheckBox checkCompleted;

    private Uri imageUri;
    private ActivityResultLauncher<String> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_asigment);

        // ربط العناصر
        etShortTitle = findViewById(R.id.edit_text_short_title);
        etImportance = findViewById(R.id.etImprtance);
        etWorkLoad = findViewById(R.id.etWorkLoad);
        etDescription = findViewById(R.id.edit_text_text);

        btnSave = findViewById(R.id.button_save_assignment);
        checkCompleted = findViewById(R.id.checkbox_is_completed);
        ivAssignment = findViewById(R.id.image_view_assignment);
        btnSelectImage = findViewById(R.id.button_select_image);

        // تهيئة أداة اختيار الصور
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        if (uri != null) {
                            imageUri = uri;
                            ivAssignment.setImageURI(uri);
                        }
                    }
                }
        );

        // زر اختيار الصورة
        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePickerLauncher.launch("image/*");
            }
        });

        // زر الحفظ
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = etShortTitle.getText().toString().trim();
                String importanceStr = etImportance.getText().toString().trim();
                String workloadStr = etWorkLoad.getText().toString().trim();
                String description = etDescription.getText().toString().trim();
                boolean isCompleted = checkCompleted.isChecked();

                if (title.isEmpty() || importanceStr.isEmpty() || workloadStr.isEmpty()) {
                    Toast.makeText(addAsigment.this,
                            "Please fill all required fields",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    int importance = Integer.parseInt(importanceStr);

                    // 1. إنشاء كائن المهمة
                    MyAssignment newAssignment = new MyAssignment();
                    newAssignment.setShortTitle(title);
                    newAssignment.setText(description);
                    newAssignment.setImportance(importance);
                    newAssignment.setCompleted(isCompleted);
                    
                    // استخدام وقت النظام الحالي لأننا حذفنا اختيار الوقت
                    newAssignment.setTime(System.currentTimeMillis());

                    // 2. حفظ مسار الصورة إذا تم اختيارها
                    if (imageUri != null) {
                        newAssignment.setFile(imageUri.toString());
                    }

                    // 3. الحفظ في قاعدة البيانات باستخدام Room
                    AppDatabase db = AppDatabase.getDatabase(addAsigment.this);
                    db.myAssignmentQuery().insertTask(newAssignment);

                    Toast.makeText(addAsigment.this,
                            "Assignment Saved Successfully!",
                            Toast.LENGTH_SHORT).show();

                    finish();

                } catch (NumberFormatException e) {
                    Toast.makeText(addAsigment.this, "Please enter valid numbers for Importance", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}