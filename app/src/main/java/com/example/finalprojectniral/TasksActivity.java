package com.example.finalprojectniral;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalprojectniral.data.myTasksTable.MyTaskAdapter;

import java.util.ArrayList;

public class TasksActivity extends AppCompatActivity {

    ImageView btnBack;
    ImageButton btnAddTask;
    ListView listTasks;

    ArrayList<String> tasksList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasksactivity);
        // ⚠️ غيري الاسم حسب اسم ملف XML عندك

        btnBack = findViewById(R.id.btn_back);
        btnAddTask = findViewById(R.id.btn_add_task);
        listTasks = findViewById(R.id.list_tasks);

        // قائمة تجريبية للمهام
        tasksList = new ArrayList<>();
        tasksList.add("Finish homework");
        tasksList.add("Study math");
        tasksList.add("Read book");

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                tasksList
        );

        listTasks.setAdapter(adapter);

        // زر الرجوع
        btnBack.setOnClickListener(v -> finish());

        // زر إضافة مهمة
        btnAddTask.setOnClickListener(v -> {
            tasksList.add("New Task");
            adapter.notifyDataSetChanged();
            ArrayList<MyTask> tasks;
            MyTaskAdapter adapter;

            tasks = new ArrayList<>();
            tasks.add(new MyTask("Study Math", "High"));
            tasks.add(new MyTask("Clean Room", "Medium"));
            tasks.add(new MyTask("Read Book", "Low"));

            adapter = new MyTaskAdapter(this, tasks);
            listTasks.setAdapter(adapter);

        });
    }
}
