package com.example.finalprojectniral;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.finalprojectniral.data.myTasksTable.MyAssignment;
import com.example.finalprojectniral.data.myTasksTable.MyAssigmentAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections; // مكتبة لترتيب القوائم
import java.util.Comparator;  // واجهة للمقارنة بين الكائنات

public class TasksActivity extends AppCompatActivity {

    /**
     * يحتوي هذا المتغير على إمكانية الرجوع إلى صفحة الملف الرئيسي (MainActivity) بالضغط على زر "العودة".
     */
    private ImageView btnBack; /** * يحتوي هذا المتغير على زر إضافة مهمة جديدة للمهام
     */
    private ImageButton btnAddTask;    /**  * يحتوي هذا المتغير على قائمة المهام التي ستظهر في شاشة المهام.*/

    private ListView listTasks; // for showing a list of tasks (scrollable list)[[
    /**
     * يحتوي هذا المتغير على قائمة بالمهام التي ستظهر في شاشة المهام.
     */
    private ArrayList<MyAssignment> tasksList;/**
     * يحتوي هذا المتغير على Adapter للمهام التي ستظهر في قائمة المهام (listTasks).
     */
    private MyAssigmentAdapter adapter;/**
     * يحتوي هذا المتغير على مرجع لقاعدة بيانات Firebase للمهام.
     */

    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasksactivity);

        // ربط العناصر
        btnBack = findViewById(R.id.btn_back);
        btnAddTask = findViewById(R.id.btn_add_task);
        listTasks = findViewById(R.id.list_tasks);

        /*
         * شرح معمق للسطرين التاليين:
         * --------------------------
         */

        // 1. استخراج الهوية الفريدة للمستخدم (UID):
        // FirebaseAuth.getInstance(): هو "المدير" المسؤول عن المصادقة في التطبيق.
        // .getCurrentUser(): تعيد كائناً يمثل المستخدم الذي قام بتسجيل الدخول حالياً.
        // .getUid(): تعيد سلسلة نصية (String) فريدة جداً لا تتكرر لكل مستخدم (مثل بصمة الإصبع الرقمية).
        // الفائدة: نستخدم الـ UID لضمان أن كل مستخدم يشاهد مهامه الخاصة فقط ولا يشاهد مهام غيره.
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // 2. تحديد المسار في قاعدة البيانات (Database Reference):
        // FirebaseDatabase.getInstance(): هو "المدير" المسؤول عن الوصول لقاعدة بيانات Realtime.
        // .getReference("assignments"): نقوم بفتح (أو إنشاء) مجلد رئيسي في قاعدة البيانات يسمى "assignments".
        // .child(uid): داخل مجلد المهام، نفتح مجرداً فرعياً يحمل اسم الـ UID الخاص بهذا المستخدم تحديداً.
        // النتيجة: العنوان النهائي يشبه "assignments/ABC123XYZ..."
        // الفائدة: هذا يسمى "الهيكلة المسطحة"، حيث يتم عزل بيانات كل مستخدم في مسار منفصل تماماً، مما يسهل عملية القراءة والحماية.
        databaseReference = FirebaseDatabase.getInstance().getReference("assignments").child(uid);

        /*
         * كيف يعمل هذا تقنياً؟
         * ------------------
         * عندما يتم استدعاء fetchTasksFromFirebase() لاحقاً، فإن 'databaseReference' لا يقوم بتحميل
         * كل المهام الموجودة في Firebase، بل يذهب مباشرة وبسرعة فائقة إلى "الغرفة" (Path)
         * الخاصة بهذا المستخدم فقط.
         *
         * هذا النمط يوفر:
         * 1. الأداء (Performance): تحميل بيانات قليلة وسريعة.
         * 2. الأمان (Security): عند كتابة "قواعد الأمان" (Security Rules) في Firebase، يمكننا
         *    بسهولة منع أي مستخدم من الوصول إلى مسار لا يتطابق اسمه مع الـ UID الخاص به.
         */

        // إنشاء قائمة المهام
        tasksList = new ArrayList<>();

        // ربط Adapter مع ListView
        adapter = new MyAssigmentAdapter(this, tasksList );
        listTasks.setAdapter(adapter);

        // جلب البيانات الحقيقية من فايربيس
        fetchTasksFromFirebase();

        // زر الرجوع
        btnBack.setOnClickListener(v -> finish());

        // زر إضافة مهمة جديدة
        btnAddTask.setOnClickListener(v -> {
            Intent intent = new Intent(TasksActivity.this, addAsigment.class);
            intent.putExtra("isEdit", false);
            startActivity(intent);
        });
    }

    /**
     * دالة بسيطة لترتيب قائمة المهام حسب الأهمية (الأولوية).
     * المهمة ذات الرقم الأعلى (مثل 5) تظهر في البداية، والأقل تظهر في النهاية.
     * تم استخدام أسلوب Comparator البسيط المتبع في منهاج البرمجة للمرحلة الثانوية.
     */
    private void sortTasksByImportance() {
        Collections.sort(tasksList, new Comparator<MyAssignment>() {
            @Override
            public int compare(MyAssignment task1, MyAssignment task2) {
                // ترتيب تنازلي: الرقم الأكبر في الأهمية يأتي أولاً
                // إذا طرحنا أهمية المهمة الأولى من الثانية، سنحصل على ترتيب من الأكبر للأصغر
                return task2.getImportance() - task1.getImportance();
            }
        });
    }

    /**
     * وصف قصير: تقوم بجلب قائمة المهام من قاعدة بيانات Firebase وتحديث الواجهة تلقائياً عند حدوث أي تغيير.
     * الهدف منها: عرض المهام الحقيقية المخزنة في السحاب مرتبة حسب الأهمية.
     */
    /**
     * Fetches tasks from Firebase and updates the list of tasks.
     * This method listens for changes in the Firebase database and updates the list of tasks accordingly.
     * It retrieves all tasks from the database and adds them to the list.
     * The list of tasks is then sorted by importance.
     * The method {@link #sortTasksByImportance()} is called to ensure that the tasks are displayed in the correct order.
     * After sorting, the adapter is notified that the data has changed, triggering a refresh of the displayed data.
     */
    private void fetchTasksFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                tasksList.clear(); // Clear the list of tasks before fetching new data
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    MyAssignment assignment = postSnapshot.getValue(MyAssignment.class);
                    if (assignment != null) {
                        assignment.setKey(postSnapshot.getKey()); // Set the key for the task
                        tasksList.add(assignment); // Add the task to the list
                    }
                }
                
                // استدعاء دالة الترتيب لضمان ظهور المهام المهمة أولاً قبل تحديث الواجهة
                sortTasksByImportance();
                
                // إبلاغ الـ Adapter بأن البيانات قد تغيرت.
                // بدون هذا السطر، لن تظهر البيانات الجديدة على الشاشة حتى لو تم تحميلها بنجاح
                // في قائمة tasksList. هذا السطر يجبر الشاشة على إعادة رسم القائمة بالبيانات المحدثة والمرتبة.
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // التعامل مع الخطأ هنا
            }
        });
    }
}
