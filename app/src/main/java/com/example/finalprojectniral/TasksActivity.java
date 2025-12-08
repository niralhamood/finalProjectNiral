package com.example.finalprojectniral;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.finalprojectniral.data.myTasksTable.MyDataBase;
import com.example.finalprojectniral.data.myTasksTable.MyTaskQuery;
import com.example.finalprojectniral.data.myTasksTable.MyTassk;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class TaskActivity extends AppCompatActivity {

    private RecyclerView rvTasks;
    private FloatingActionButton btnAddTask;

    private MyTaskAdapter adapter;
    private MyTaskQuery taskDao;
    private List<MyTassk> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        rvTasks = findViewById(R.id.rvTasks);
        btnAddTask = findViewById(R.id.btnAddTask);

        // بناء قاعدة البيانات
        MyDataBase db = MyDataBase.getDb(getApplicationContext());
        taskDao = db.myTaskQuery();

        // جلب المهام من الداتا
        taskList = taskDao.getAllTasks();

        // إعداد الريسايكلر
        adapter = new MyTaskAdapter(taskList, this);
        rvTasks.setAdapter(adapter);
        rvTasks.setLayoutManager(new LinearLayoutManager(this));

        // إضافة مهمة جديدة
        btnAddTask.setOnClickListener(view -> {
            MyTassk t = new MyTassk();
            t.title = "New Task";
            t.importance = 1;
            t.isCompleted = false;

            taskDao.insertTask(t);

            refreshList();
        });
    }

    private void refreshList() {
        taskList.clear();
        taskList.addAll(taskDao.getAllTasks());
        adapter.notifyDataSetChanged();
    }
}
