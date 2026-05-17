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

    private ListView listTasks;/**
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

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("assignments").child(uid);

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
    private void fetchTasksFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                tasksList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    MyAssignment assignment = postSnapshot.getValue(MyAssignment.class);
                    if (assignment != null) {
                        assignment.setKey(postSnapshot.getKey());
                        tasksList.add(assignment);
                    }
                }
                
                // استدعاء دالة الترتيب لضمان ظهور المهام المهمة أولاً قبل تحديث الواجهة
                sortTasksByImportance();
                
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // التعامل مع الخطأ هنا
            }
        });
    }
}
