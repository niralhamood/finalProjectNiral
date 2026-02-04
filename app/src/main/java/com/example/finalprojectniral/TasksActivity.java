package com.example.finalprojectniral;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalprojectniral.data.myTasksTable.MyAssignment;
import com.example.finalprojectniral.data.myTasksTable.MyAssignmentQuery;
import com.example.finalprojectniral.data.myTasksTable.MyAssigmentAdapter;

import java.util.ArrayList;

public class TasksActivity extends AppCompatActivity {

    private ImageView btnBack;
    private ImageButton btnAddTask;
    private ListView listTasks;

    private ArrayList<MyAssignment> tasksList;
    private MyAssigmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasksactivity); // تأكدي من اسم ملف XML

        // ربط العناصر
        btnBack = findViewById(R.id.btn_back);
        btnAddTask = findViewById(R.id.btn_add_task);
        listTasks = findViewById(R.id.list_tasks);

        // إنشاء قائمة المهام
        tasksList = new ArrayList<>();
        tasksList.add(new MyAssignment("Finish homework", "High"));
        tasksList.add(new MyAssignment("Study Math", "Medium"));
        tasksList.add(new MyAssignment("Read Book", "Low"));

        // ربط Adapter مع ListView
        adapter = new MyAssigmentAdapter(this, tasksList);
        listTasks.setAdapter(adapter);

        // زر الرجوع
        btnBack.setOnClickListener(v -> finish());

        // زر إضافة مهمة جديدة
        btnAddTask.setOnClickListener(v -> {
            // إضافة مهمة جديدة (يمكن لاحقًا تفتح شاشة لإدخال الاسم والأولوية)
            tasksList.add(new MyAssignment("New Task", "Low"));
            adapter.notifyDataSetChanged(); // تحديث القائمة
        });
    }
}
