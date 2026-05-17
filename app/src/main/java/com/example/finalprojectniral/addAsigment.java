package com.example.finalprojectniral; // اسم الحزمة التي ينتمي إليها الملف

import static android.content.ContentValues.TAG; // استيراد وسم لتتبع السجلات والأخطاء

import android.content.pm.PackageManager; // استيراد أداة فحص صلاحيات النظام
import android.net.Uri; // استيراد فئة التعامل مع روابط الملفات والصور
import android.os.Build; // استيراد معلومات عن إصدار نظام الأندرويد
import android.os.Bundle; // استيراد كائن نقل البيانات بين الحالات
import android.util.Log; // استيراد أداة تدوين السجلات البرمجية
import android.view.View; // استيراد فئة التعامل مع واجهات العرض
import android.widget.ImageView; // استيراد عنصر عرض الصور
import android.widget.Toast; // استيراد أداة عرض الرسائل القصيرة للمستخدم

import androidx.activity.result.ActivityResultLauncher; // استيراد مشغل نتائج الأنشطة
import androidx.activity.result.contract.ActivityResultContracts; // استيراد عقود نتائج الأنشطة
import androidx.annotation.NonNull; // استيراد وسم لضمان عدم وجود قيم فارغة
import androidx.appcompat.app.AppCompatActivity; // استيراد فئة الأنشطة المتوافقة مع النظام
import androidx.core.content.ContextCompat; // استيراد أداة المساعدة للتعامل مع الموارد والصلاحيات

import com.example.finalprojectniral.data.myTasksTable.MyAssignment; // استيراد موديل المهمة
import com.google.android.gms.tasks.OnCompleteListener; // استيراد مستمع اكتمال عمليات جوجل
import com.google.android.gms.tasks.Task; // استيراد فئة المهام من خدمات جوجل
import com.google.android.material.button.MaterialButton; // استيراد زر من نمط Material Design
import com.google.android.material.checkbox.MaterialCheckBox; // استيراد صندوق اختيار من نمط Material
import com.google.android.material.textfield.TextInputEditText; // استيراد حقل إدخال نصوص متقدم
import com.google.firebase.Firebase; // استيراد فئة فايربيس الأساسية
import com.google.firebase.auth.FirebaseAuth; // استيراد أداة المصادقة بفايربيس
import com.google.firebase.database.DatabaseReference; // استيراد فئة مرجع قاعدة البيانات
import com.google.firebase.database.FirebaseDatabase; // استيراد كائن قاعدة بيانات فايربيس

public class addAsigment extends AppCompatActivity { // تعريف كلاس شاشة إضافة المهمة

    // تعريف متغيرات عناصر الواجهة (الحقول، الأزرار، الصور)
    private TextInputEditText etShortTitle, etImportance, etWorkLoad, etDescription; // حقول النص للعنوان، الأهمية، العمل، والوصف
    private MaterialButton btnSave, btnSelectImage; // أزرار الحفظ واختيار الصورة
    private ImageView ivAssignment; // عنصر عرض صورة المهمة
    private MaterialCheckBox checkCompleted; // صندوق اختيار حالة الإكمال

    private Uri imageUri; // متغير لتخزين مسار الصورة المرفقة
    private ActivityResultLauncher<String> requestReadMediaImagesPermission; // مشغل طلب إذن الصور
    private ActivityResultLauncher<String> requestReadExternalStoragePermission; // مشغل طلب إذن التخزين
    private ActivityResultLauncher<String> imagePickerLauncher; // مشغل اختيار صورة من الاستوديو

    private MyAssignment currentAssignment; // كائن لتخزين بيانات المهمة في حال التعديل
    private boolean isEditMode = false; // متغير يحدد ما إذا كنا في وضع الإضافة أو التعديل

    @Override
    protected void onCreate(Bundle savedInstanceState) { // الدالة الأساسية عند إنشاء الشاشة
        super.onCreate(savedInstanceState); // استدعاء دالة الحالة الأصلية
        setContentView(R.layout.activity_add_asigment); // ربط الكود بملف التصميم XML

        // ربط المتغيرات البرمجية بالعناصر الموجودة في التصميم عبر الـ ID
        etShortTitle = findViewById(R.id.edit_text_short_title); // ربط حقل العنوان
        etImportance = findViewById(R.id.etImprtance); // ربط حقل الأهمية (1-5)
        etWorkLoad = findViewById(R.id.etWorkLoad); // ربط حقل مدى العمل (1-10)
        etDescription = findViewById(R.id.edit_text_text); // ربط حقل الوصف
        btnSave = findViewById(R.id.button_save_assignment); // ربط زر الحفظ
        checkCompleted = findViewById(R.id.checkbox_is_completed); // ربط صندوق الإكمال
        ivAssignment = findViewById(R.id.image_view_assignment); // ربط عنصر الصورة
        btnSelectImage = findViewById(R.id.button_select_image); // ربط زر اختيار الصورة

        // إعداد مشغل اختيار الصورة من معرض الصور في الهاتف
        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) { // إذا تم اختيار صورة بنجاح
                imageUri = uri; // حفظ مسار الصورة
                ivAssignment.setImageURI(uri); // عرض الصورة في الواجهة
            }
        });

        // التحقق مما إذا كانت الشاشة قد فُتحت بغرض "التعديل" بدلاً من "الإضافة"
        if (getIntent().hasExtra("isEdit") && getIntent().getBooleanExtra("isEdit", false)) {
            isEditMode = true; // تفعيل وضع التعديل
            currentAssignment = (MyAssignment) getIntent().getSerializableExtra("assignment"); // استلام كائن المهمة
            if (currentAssignment != null) {
                populateFields(currentAssignment); // تعبئة الحقول بالبيانات الحالية
                btnSave.setText("Update Assignment"); // تغيير نص الزر ليتناسب مع التعديل
            }
        }

        setupPermissionLaunchers(); // استدعاء دالة إعداد مشغلات الصلاحيات
        checkAndRequestPermissions(); // فحص وطلب الصلاحيات عند بدء الشاشة

        // إعداد مستمع النقر لزر اختيار الصورة
        btnSelectImage.setOnClickListener(v -> imagePickerLauncher.launch("image/*")); // فتح المعرض لاختيار الصور

        // إعداد مستمع النقر لزر الحفظ أو التحديث
        btnSave.setOnClickListener(v -> {
            // استخراج النصوص المدخلة من الحقول
            String title = etShortTitle.getText().toString().trim(); // جلب العنوان
            String importanceStr = etImportance.getText().toString().trim(); // جلب نص الأهمية
            String workloadStr = etWorkLoad.getText().toString().trim(); // جلب نص مدى العمل
            String description = etDescription.getText().toString().trim(); // جلب الوصف
            boolean isCompleted = checkCompleted.isChecked(); // جلب حالة الإكمال

            // التأكد من أن الحقول الأساسية ليست فارغة
            if (title.isEmpty() || importanceStr.isEmpty() || workloadStr.isEmpty()) {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show(); // تنبيه المستخدم
                return;
            }

            // استدعاء دالة فحص ملاءمة المعطيات (العنوان والوصف نصوص، والأرقام ضمن المجال)
            if (!validateInputs(title, description, importanceStr, workloadStr)) {
                return; // التوقف عن الحفظ في حال كانت المعطيات غير ملائمة
            }

            try {
                int importance = Integer.parseInt(importanceStr); // تحويل نص الأهمية لرقم
                int workload = Integer.parseInt(workloadStr); // تحويل نص العمل لرقم

                MyAssignment assignmentToSave; // تعريف كائن المهمة للحفظ
                if (isEditMode && currentAssignment != null) {
                    assignmentToSave = currentAssignment; // استخدام المهمة الحالية في حال التعديل
                } else {
                    assignmentToSave = new MyAssignment(); // إنشاء مهمة جديدة في حال الإضافة
                }

                // تعبئة البيانات في كائن المهمة
                assignmentToSave.setShortTitle(title); // تعيين العنوان
                assignmentToSave.setText(description); // تعيين الوصف
                assignmentToSave.setImportance(importance); // تعيين الأهمية (1-5)
                assignmentToSave.setWorkLoad(workload); // تعيين مدى العمل (1-10)
                assignmentToSave.setCompleted(isCompleted); // تعيين حالة الإكمال

                if (imageUri != null) { // إذا تم اختيار صورة
                    assignmentToSave.setFile(imageUri.toString()); // حفظ مسار الصورة في الكائن
                }

                saveAssignment(assignmentToSave); // استدعاء دالة الحفظ الفعلي في قاعدة البيانات

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Please enter valid numeric values", Toast.LENGTH_SHORT).show(); // خطأ في تحويل النص لرقم
            }
        });
    }

    /**
     * دالة validateInputs: تقوم بفحص المعطيات المدخلة بأسلوب بسيط (حلقات for) المتوافق مع منهاج المدرسة.
     * تضمن السماح بكافة اللغات (عربي، عبري، إنجليزي...) وتمنع الحقول التي تتكون من أرقام فقط.
     */
    private boolean validateInputs(String title, String description, String importance, String workload) { 
        
        // 1. فحص العنوان: يجب أن يحتوي على حرف لغوي واحد على الأقل (أي لغة) وليس أرقاماً فقط
        boolean hasLetterInTitle = false;
        for (int i = 0; i < title.length(); i++) {
            char c = title.charAt(i);
            if (Character.isLetter(c)) { // دالة Character.isLetter تفحص إذا كان الرمز حرفاً بأي لغة (عربي، عبري، إنجليزي...)
                hasLetterInTitle = true;
                break; // وجدنا حرفاً واحداً على الأقل، يمكن التوقف
            }
        }
        
        if (hasLetterInTitle == false) {
            etShortTitle.setError("العنوان يجب أن يحتوي على نصوص وصفية وليس أرقاماً فقط");
            etShortTitle.requestFocus();
            return false;
        }

        // 2. فحص الوصف: إذا كتب المستخدم شيئاً، يجب أن يحتوي على حرف لغوي واحد على الأقل
        if (description.length() > 0) {
            boolean hasLetterInDesc = false;
            for (int i = 0; i < description.length(); i++) {
                char c = description.charAt(i);
                if (Character.isLetter(c)) {
                    hasLetterInDesc = true;
                    break;
                }
            }
            if (hasLetterInDesc == false) {
                etDescription.setError("الوصف يجب أن يحتوي على نصوص وصفية وليس أرقاماً فقط");
                etDescription.requestFocus();
                return false;
            }
        }

        // 3. فحص الأهمية ومدى العمل (يجب أن تظل أرقاماً وضمن المجال المطلوب)
        try {
            int imp = Integer.parseInt(importance); // محاولة تحويل الأهمية لرقم
            int work = Integer.parseInt(workload); // محاولة تحويل مدى العمل لرقم

            if (imp < 1 || imp > 5) {
                etImportance.setError("Importance must be between 1 and 5");
                etImportance.requestFocus();
                return false;
            }

            if (work < 1 || work > 10) {
                etWorkLoad.setError("Workload must be between 1 and 10");
                etWorkLoad.requestFocus();
                return false;
            }

            return true; // جميع المعطيات سليمة وملائمة
        } catch (NumberFormatException e) {
            Toast.makeText(this, "يرجى إدخال أرقام فقط للأهمية ومدى العمل", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /**
     * دالة saveAssignment: تقوم بحفظ أو تحديث بيانات المهمة في قاعدة بيانات Firebase.
     */
    private void saveAssignment(MyAssignment assignment) { // دالة حفظ البيانات في السحاب
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid(); // جلب معرف المستخدم الحالي
        DatabaseReference assignmentsRef = FirebaseDatabase.getInstance().getReference("assignments").child(uid); // تحديد مسار الحفظ

        if (isEditMode && assignment.getKey() != null) { // إذا كنا في وضع التعديل
            assignmentsRef.child(assignment.getKey()).setValue(assignment).addOnCompleteListener(task -> { // تحديث القيمة الحالية
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Assignment updated successfully", Toast.LENGTH_SHORT).show();
                    finish(); // إغلاق الشاشة والعودة
                }
            });
        } else { // إذا كنا في وضع الإضافة الجديدة
            DatabaseReference newRef = assignmentsRef.push(); // إنشاء مرجع فريد جديد (Key)
            assignment.setKey(newRef.getKey()); // حفظ المفتاح داخل الكائن
            newRef.setValue(assignment).addOnCompleteListener(task -> { // رفع البيانات للقاعدة
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Assignment added successfully", Toast.LENGTH_SHORT).show();
                    finish(); // إغلاق الشاشة والعودة
                }
            });
        }
    }

    /**
     * دالة populateFields: تقوم بتعبئة حقول الشاشة ببيانات المهمة المختارة عند الدخول في وضع التعديل.
     */
    private void populateFields(MyAssignment assignment) { // دالة عرض البيانات للتعديل
        etShortTitle.setText(assignment.getShortTitle()); // تعبئة العنوان
        etImportance.setText(String.valueOf(assignment.getImportance())); // تعبئة الأهمية
        etWorkLoad.setText(String.valueOf(assignment.getWorkLoad())); // تعبئة مدى العمل
        etDescription.setText(assignment.getText()); // تعبئة الوصف
        checkCompleted.setChecked(assignment.isCompleted()); // تعبئة حالة الإكمال
        if (assignment.getFile() != null) { // إذا كان هناك صورة مرفقة سابقاً
            imageUri = Uri.parse(assignment.getFile()); // تحويل مسار الصورة لرابط
            ivAssignment.setImageURI(imageUri); // عرض الصورة في الواجهة
        }
    }

    /**
     * دالة setupPermissionLaunchers: تقوم بتعريف مشغلات طلب الصلاحيات من نظام الأندرويد.
     */
    private void setupPermissionLaunchers() { // إعداد مشغلات الصلاحيات
        requestReadMediaImagesPermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (!isGranted) Toast.makeText(this, "Photo permission denied", Toast.LENGTH_SHORT).show();
        });
        requestReadExternalStoragePermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (!isGranted) Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * دالة checkAndRequestPermissions: تقوم بفحص وطلب الصلاحيات اللازمة للوصول للصور بناءً على إصدار النظام.
     */
    private void checkAndRequestPermissions() { // فحص وطلب الأذونات
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // للأجهزة التي تعمل بنظام أندرويد 13+
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                requestReadMediaImagesPermission.launch(android.Manifest.permission.READ_MEDIA_IMAGES);
            }
        } else { // للإصدارات الأقدم من أندرويد 13
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestReadExternalStoragePermission.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }
    }
}
