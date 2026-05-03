package com.example.finalprojectniral;


import static android.content.ContentValues.TAG;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.finalprojectniral.data.myTasksTable.MyAssignment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class addAsigment extends AppCompatActivity {

    private TextInputEditText etShortTitle, etImportance, etWorkLoad, etDescription;
    private MaterialButton btnSave, btnSelectImage;
    private ImageView ivAssignment;
    private MaterialCheckBox checkCompleted;

    private Uri imageUri;
    private ActivityResultLauncher<String> requestReadMediaImagesPermission;
    private ActivityResultLauncher<String> requestReadMediaVideoPermission;
    private ActivityResultLauncher<String> requestReadExternalStoragePermission;

    private ActivityResultLauncher<String> imagePickerLauncher;

    private MyAssignment currentAssignment;
    private boolean isEditMode = false;

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

        // تهيئة مُشغّل اختيار الصورة
        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                imageUri = uri;
                ivAssignment.setImageURI(uri);
            }
        });

        // التحقق مما إذا كنا في وضع التعديل
        if (getIntent().hasExtra("isEdit") && getIntent().getBooleanExtra("isEdit", false)) {
            isEditMode = true;
            currentAssignment = (MyAssignment) getIntent().getSerializableExtra("assignment");
            if (currentAssignment != null) {
                populateFields(currentAssignment);
                btnSave.setText("Update Assignment");
            }
        }


        // تسجيل مُشغّل لطلب إذن READ_MEDIA_IMAGES
        requestReadMediaImagesPermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                Log.d(TAG, "READ_MEDIA_IMAGES permission granted");
                Toast.makeText(this, "تم منح إذن قراءة الصور", Toast.LENGTH_SHORT).show();
                // يمكنك الآن المتابعة بالعملية التي تتطلب هذا الإذن
            } else {
                Log.d(TAG, "READ_MEDIA_IMAGES permission denied");
                Toast.makeText(this, "تم رفض إذن قراءة الصور", Toast.LENGTH_SHORT).show();
                // التعامل مع حالة رفض الإذن
            }
        });


// تسجيل مُشغّل لطلب إذن READ_MEDIA_VIDEO
        requestReadMediaVideoPermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                Log.d(TAG, "READ_MEDIA_VIDEO permission granted");
                Toast.makeText(this, "تم منح إذن قراءة الفيديو", Toast.LENGTH_SHORT).show();
                // يمكنك الآن المتابعة بالعملية التي تتطلب هذا الإذن
            } else {
                Log.d(TAG, "READ_MEDIA_VIDEO permission denied");
                Toast.makeText(this, "تم رفض إذن قراءة الفيديو", Toast.LENGTH_SHORT).show();
                // التعامل مع حالة رفض الإذن
            }
        });


// تسجيل مُشغّل لطلب إذن READ_EXTERNAL_STORAGE
        requestReadExternalStoragePermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                Log.d(TAG, "READ_EXTERNAL_STORAGE permission granted");
                Toast.makeText(this, "تم منح إذن قراءة التخزين الخارجي", Toast.LENGTH_SHORT).show();
                // يمكنك الآن المتابعة بالعملية التي تتطلب هذا الإذن
            } else {
                Log.d(TAG, "READ_EXTERNAL_STORAGE permission denied");
                Toast.makeText(this, "تم رفض إذن قراءة التخزين الخارجي", Toast.LENGTH_SHORT).show();
                // التعامل مع حالة رفض الإذن
            }
        });
//استدعاء دالة الفحص (سيتم تطبيقها لاحقا)
        checkAndRequestPermissions();




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

                    // 1. إنشاء أو تحديث كائن المهمة
                    MyAssignment assignmentToSave;
                    if (isEditMode && currentAssignment != null) {
                        assignmentToSave = currentAssignment;
                    } else {
                        assignmentToSave = new MyAssignment();
                        assignmentToSave.setTime(System.currentTimeMillis());
                    }

                    assignmentToSave.setShortTitle(title);
                    assignmentToSave.setText(description);
                    assignmentToSave.setImportance(importance);
                    assignmentToSave.setCompleted(isCompleted);

                    // 2. حفظ مسار الصورة إذا تم اختيارها
                    if (imageUri != null) {
                        assignmentToSave.setFile(imageUri.toString());
                    }

                    saveAssignment(assignmentToSave);

                } catch (NumberFormatException e) {
                    Toast.makeText(addAsigment.this, "Please enter valid numbers for Importance", Toast.LENGTH_SHORT).show();
                }

            }

    /**
     * وصف قصير: تقوم بحفظ أو تحديث بيانات المهمة في قاعدة بيانات Firebase.
     * البارامترات (@param assignment): كائن المهمة الذي يحتوي على البيانات المراد حفظها.
     */
    private void saveAssignment(MyAssignment assignment) {
                DatabaseReference database = FirebaseDatabase.getInstance().getReference();
                DatabaseReference assignmentsRef = database.child("assignments");

                if (isEditMode && assignment.getKey() != null) {
                    // تحديث مهمة موجودة
                    assignmentsRef.child(assignment.getKey()).setValue(assignment).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(addAsigment.this, "Assignment updated successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(addAsigment.this, "Failed to update assignment", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // إضافة مهمة جديدة
                    DatabaseReference newAssignmentRef = assignmentsRef.push();
                    assignment.setKey(newAssignmentRef.getKey());
                    newAssignmentRef.setValue(assignment).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(addAsigment.this, "Assignment added successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(addAsigment.this, "Failed to add assignment", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

        });
    }

    /**
     * وصف قصير: تقوم بتعبئة حقول الشاشة ببيانات المهمة المختارة عند الدخول في وضع التعديل.
     * البارامترات (@param assignment): كائن المهمة الذي يحتوي على البيانات المراد عرضها.
     */
    private void populateFields(MyAssignment assignment) {
        etShortTitle.setText(assignment.getShortTitle());
        etImportance.setText(String.valueOf(assignment.getImportance()));
        etDescription.setText(assignment.getText());
        checkCompleted.setChecked(assignment.isCompleted());
        if (assignment.getFile() != null) {
            imageUri = Uri.parse(assignment.getFile());
            ivAssignment.setImageURI(imageUri);
        }
    }

    /**
     * وصف قصير: تقوم بفحص وطلب الصلاحيات اللازمة للوصول إلى الصور والملفات بناءً على إصدار الأندرويد.
     */
    private void checkAndRequestPermissions() {
        // فحص وطلب إذن READ_MEDIA_IMAGES (للإصدارات الحديثة)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // أندرويد 13+
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                requestReadMediaImagesPermission.launch(android.Manifest.permission.READ_MEDIA_IMAGES);
            } else {
                Log.d(TAG, "READ_MEDIA_IMAGES permission already granted");
                Toast.makeText(this, "إذن قراءة الصور ممنوح بالفعل", Toast.LENGTH_SHORT).show();
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { // أندرويد 10 و 11 و 12// على هذه الإصدارات، READ_EXTERNAL_STORAGE له سلوك مختلف
            // إذا كنت تستخدم Scoped Storage بشكل صحيح، قد لا تحتاج إلى هذا الإذن
            // ولكن إذا كنت تحتاج إلى الوصول إلى جميع الصور، فقد تحتاج إلى READ_EXTERNAL_STORAGE
            // في هذا المثال، سنفحص READ_EXTERNAL_STORAGE للإصدارات الأقدم من 13
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestReadExternalStoragePermission.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            } else {
                Log.d(TAG, "READ_EXTERNAL_STORAGE permission already granted (for older versions)");
                Toast.makeText(this, "إذن قراءة التخزين ممنوح بالفعل (للإصدارات الأقدم)", Toast.LENGTH_SHORT).show();
            }
        } else { // أندرويد 9 والإصدارات الأقدم
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestReadExternalStoragePermission.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            } else {
                Log.d(TAG, "READ_EXTERNAL_STORAGE permission already granted (for older versions)");
                Toast.makeText(this, "إذن قراءة التخزين ممنوح بالفعل (للإصدارات الأقدم)", Toast.LENGTH_SHORT).show();
            }
        }


//        // فحص وطلب إذن READ_MEDIA_VIDEO (للإصدارات الحديثة)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // أندرويد 13+
//            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_VIDEO)
//                    != PackageManager.PERMISSION_GRANTED) {
//                requestReadMediaVideoPermission.launch(android.Manifest.permission.READ_MEDIA_VIDEO);
//            } else {
//                Log.d(TAG, "READ_MEDIA_VIDEO permission already granted");
//                Toast.makeText(this, "إذن قراءة الفيديو ممنوح بالفعل", Toast.LENGTH_SHORT).show();
//            }
//        }// ملاحظة: إذن INTERNET لا يحتاج إلى فحص أو
    }
}



