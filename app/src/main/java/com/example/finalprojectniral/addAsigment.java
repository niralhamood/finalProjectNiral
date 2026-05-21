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

        /**
         * imagePickerLauncher: This is an Activity Result Launcher.
         * It acts as a "callback" mechanism that waits for the user to select an image from the gallery.
         *
         * registerForActivityResult: Registers the action (GetContent) and the logic to run
         * when the image is selected (saving the URI and updating the ImageView).
         */
        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) { // إذاتم اختيار صورة بنجاح
                imageUri = uri; // حفظ مسار الصورة
                ivAssignment.setImageURI(uri); // عرض الصورة في الواجهة
            }
        });

        /*
          getIntent(): This method retrieves the Intent that started this Activity.
          An "Intent" is a messaging object you can use to request an action from another app component.
          Think of it as a "letter" or "package" sent from one screen to another.

          hasExtra / getSerializableExtra: These are used to "read" data passed inside that "package"
          from the previous screen (MainActivity). If "isEdit" is true, it means we are modifying
          an existing task rather than creating a new one.
         */
        /**
         * Transition Logic and Intent Handling:
         *
         * 1. getIntent(): Retrieves the Intent that started this activity.
         * 2. hasExtra / getSerializableExtra: Reads data passed from the previous screen.
         *
         * 3. Transition Logic:
         *    To get to this screen for editing, the previous screen used:
         *    Intent i = new Intent(context, addAssignment.class);
         *    i.putExtra("isEdit", true);
         *    startActivity(i);
         *
         * 4. startActivity(Intent): Launches the Activity screen based on instructions.
         *
         * 5. populateFields(currentAssignment): Pre-populates form fields with existing data.
         *
         * 6. setOnClickListener: Sets up a listener to respond to user click events.
         */
        if (getIntent().hasExtra("isEdit") && getIntent().getBooleanExtra("isEdit", false)) {
            isEditMode = true; // تفعيل وضع التعديل
            currentAssignment = (MyAssignment) getIntent().getSerializableExtra("assignment"); // استلام كائن المهمة

            if (currentAssignment != null) {
                populateFields(currentAssignment);
                btnSave.setText("Update Assignment"); // تغيير نص الزر ليتناسب مع التعديل
            }
        }

        setupPermissionLaunchers(); // استدعاء دالة إعداد مشغلات الصلاحيات
        checkAndRequestPermissions(); // فحص وطلب الصلاحيات عند بدء الشاشة

        /*
          setOnClickListener: This is an Interface (specifically View.OnClickListener).
          It is used to catch "Click Events". By calling this method, you are telling the Button:
          "Hey, wait for a user to tap you. When they do, run this specific block of code."
         */
        btnSelectImage.setOnClickListener(v -> imagePickerLauncher.launch("image/*"));

        /**
         * Sets the click listener for the Save button.
         * When clicked, it validates the user input, creates/updates an assignment object,
         * and attempts to save it to the Firebase Database.
         */
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
                // معالجة الخطأ في حال أدخل المستخدم نصاً بدلاً من رقم في الحقول العددية
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
            etShortTitle.setError("Title must contain descriptive text, not just numbers");
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
                etDescription.setError("Description must contain descriptive text, not just numbers");
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
                etWorkLoad.requestFocus(); // ستدعاء requestFocus() يسهل على المستخدم الأمر، فبدلاً من أن يضطر المستخدم للنقر يدوياً على الحقل لتعديله، يقوم البرنامج بنقل التركيز إليه مباشرة ليكون جاهزاً للكتابة وتصحيح الخطأ فوراً.
                return false;
            }

            return true; // جميع المعطيات سليمة وملائمة
        } catch (NumberFormatException e) {
            // إظهار تنبيه للمستخدم في حال فشل تحويل النصوص إلى أرقام صحيحة
            Toast.makeText(this, "Please enter numbers only for importance and workload", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /**
     * دالة saveAssignment: تقوم بحفظ أو تحديث بيانات المهمة في قاعدة بيانات Firebase.
     */
    /**
     * saveAssignment: This method saves the assignment data to the Firebase Realtime Database.
     * It takes a MyAssignment object as a parameter and saves it to the database.
     *
     * @param assignment The MyAssignment object to be saved.
                // UID stands for Unique Identifier. It is a unique identifier for a Firebase user account.
                // Each user is assigned a unique identifier when they sign up, and it remains constant throughout their lifetime.
                // The UID is used to identify a user across different devices and sessions.
                // Firebase uses this UID to store and retrieve user-specific data.
     */
    private void saveAssignment(MyAssignment assignment) { // Save the assignment data to Firebase
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Get the current user's UID
        DatabaseReference assignmentsRef = FirebaseDatabase.getInstance().getReference("assignments").child(uid); // Define the save path

        if (isEditMode && assignment.getKey() != null) { // If we are in edit mode
            assignmentsRef.child(assignment.getKey()).setValue(assignment) // Update the existing value
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Assignment updated successfully", Toast.LENGTH_SHORT).show();
                            finish(); // Close the screen after the data is successfully saved to Firebase
                        }
                    });
        } else { // If we are adding a new assignment
            DatabaseReference newRef = assignmentsRef.push(); // Create a new unique reference (Key)
            assignment.setKey(newRef.getKey()); // Store the key in the object
            newRef.setValue(assignment) // Upload the data to the database
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Assignment added successfully", Toast.LENGTH_SHORT).show();
                            finish(); // Close the screen after the data is successfully saved to Firebase
                        }
                    });
        }
    }

    /**
     * دالة populateFields: تقوم بتعبئة حقول الشاشة ببيانات المهمة المختارة عند الدخول في وضع التعديل.
     * تعمل هذه الدالة كجسر ينقل البيانات من قاعدة البيانات (عبر الكائن) إلى نظر المستخدم (الواجهة).
     */
    private void populateFields(MyAssignment assignment) { // دالة عرض البيانات للتعديل
        // 1. تعبئة النصوص المباشرة
        etShortTitle.setText(assignment.getShortTitle());
        etDescription.setText(assignment.getText());

        // 2. تحويل الأرقام إلى نصوص لعرضها في الـ EditText (ضروري لتجنب Crash)
        etImportance.setText(String.valueOf(assignment.getImportance()));
        etWorkLoad.setText(String.valueOf(assignment.getWorkLoad()));

        // 3. تحديث حالة الـ CheckBox
        checkCompleted.setChecked(assignment.isCompleted());

        // 4. معالجة الصورة: إذا وُجد مسار مخزن، نقوم بتحويله من String إلى Uri لعرضه
        if (assignment.getFile() != null) {
            imageUri = Uri.parse(assignment.getFile());
            ivAssignment.setImageURI(imageUri);
        }
    }
    /**
     * دالة setupPermissionLaunchers: تقوم بتعريف مشغلات طلب الصلاحيات من نظام الأندرويد.
     *
     * تحتوي هذه الدالة على عدة مشغلات طلب الصلاحيات للوصول إلى صور الوسائط والتخزين الخارجي للأجهزة.
     * كل مشغل من هذه المشغلات يتم تعريفه باستخدام المكون ActivityResultContracts.RequestPermission().
     * ويتم تعريف مشغل واحد لكل صلاحية مطلوبة.
     *
     * عند النقر على زر التحميل في وضع الإضافة الجديدة، يتم تعريف مشغل طلب الصلاحية للوصول إلى صور الوسائط.
     * ويتم تعريف مشغل أخر للوصول إلى التخزين الخارجي.
     * وعند النقر على زر التحميل، يتم استدعاء المشغل المناسب، وإذا لم يتم تصريح الصلاحية، يتم عرض رسالة تحذيرية.
     */
    private void setupPermissionLaunchers() { // إعداد مشغلات الصلاحيات
        requestReadMediaImagesPermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (!isGranted)
                Toast.makeText(this, "Photo permission denied", Toast.LENGTH_SHORT).show();
        });
        requestReadExternalStoragePermission = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (!isGranted)
                Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show();
        });
    }
    /**
     * دالة checkAndRequestPermissions: تقوم بفحص وطلب الصلاحيات اللازمة للوصول للصور بناءً على إصدار النظام.
     *
     * تقوم هذه الدالة بالتحقق مما إذا كانت الإصدار المستخدم للأندرويد 13 أو أعلى، وإذا كانت الصلاحية "READ_MEDIA_IMAGES" غير ممنوحة.
     * وإذا كانت الصلاحية غير ممنوحة، يتم طلبها باستخدام المشغل المتعلق بهذه الصلاحية.
     * وعلى اساطي النقر على الزر الذي يتم تعريفه باستخدام setOnClickListener، يتم استدعاء المشغل المناسب لطلب الصلاحية.
     *
     * بعد الحصول على الصلاحية، يتم تنفيذ الكود الموجود داخل بدل النظام.
     * وفي هذا الحالة، تم تعريف زر التحميل باستخدام setOnClickListener، وبعد تصريح الصلاحية، يتم تعريف كائن Intent للقيام بالأنتقال إلى مكان محدد.
     * ويتم استدعاء الدالة startActivity() لبدء الأنتقال إلى المكان المحدد.
     *
     *
     *
     * سير العملية بالترتيب (Workflow):
     * المستخدم يكبس على زر التحميل في تطبيقك.

     * الكود تبعك بيشغّل وحدة من هدول الـ launch().
     *
     * فوراً بتظهر نافذة الأندرويد للمستخدم: "هل تسمح لهذا التطبيق بالوصول إلى الصور؟"
     *
     * هون الكود تبعك بيتوقف مؤقتاً وينتظر رد المستخدم:
     *
     * إذا كبس سماح: النافذة بتختفي والتطبيق بيكمل طريقه (مثلاً بيفتح معرض الصور).
     *
     * إذا كبس رفض: النافذة بتختفي والنظام بروح ينفذ الـ Callback (الـ Toast اللي جهزناه بالدالة الأولى ليقول للمستخدم "Photo permission denied").
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
