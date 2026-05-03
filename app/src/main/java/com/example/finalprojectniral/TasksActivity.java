package com.example.finalprojectniral;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.finalprojectniral.data.myTasksTable.MyAssignment;
import com.example.finalprojectniral.data.myTasksTable.MyAssigmentAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TasksActivity extends AppCompatActivity {

    private ImageView btnBack;
    private ImageButton btnAddTask;
    private ListView listTasks;

    private ArrayList<MyAssignment> tasksList;
    private MyAssigmentAdapter adapter;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasksactivity);

        // ربط العناصر
        btnBack = findViewById(R.id.btn_back);
        btnAddTask = findViewById(R.id.btn_add_task);
        listTasks = findViewById(R.id.list_tasks);

        databaseReference = FirebaseDatabase.getInstance().getReference("assignments");

        // إنشاء قائمة المهام
        tasksList = new ArrayList<>();

        // ربط Adapter مع ListView
        adapter = new MyAssigmentAdapter(this, tasksList, new MyAssigmentAdapter.OnAssignmentClickListener() {
            @Override
            public void onEditClick(MyAssignment assignment) {
                Intent intent = new Intent(TasksActivity.this, addAsigment.class);
                intent.putExtra("isEdit", true);
                intent.putExtra("assignment", assignment.isCompleted());
                startActivity(intent);
            }

            @Override
            public void onDeleteClick(MyAssignment assignment) {
                if (assignment.getKey() != null) {
                    databaseReference.child(assignment.getKey()).removeValue();
                }
            }
        });
        listTasks.setAdapter(adapter);

        // جلب البيانات الحقيقية من فايربيز
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
     * وصف قصير: تقوم بجلب قائمة المهام من قاعدة بيانات Firebase وتحديث الواجهة تلقائياً عند حدوث أي تغيير.
     * الهدف منها: عرض المهام الحقيقية المخزنة في السحاب بدلاً من البيانات الافتراضية.
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
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // التعامل مع الخطأ هنا
            }
        });
    }
}
