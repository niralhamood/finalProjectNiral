package com.example.finalprojectniral.data.myTasksTable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.finalprojectniral.MyTask;
import com.example.finalprojectniral.R;

import java.util.ArrayList;

public class MyTaskAdapter extends ArrayAdapter<MyTask> {

    private Context context;
    private ArrayList<MyTask> tasks;

    public MyTaskAdapter(Context context, ArrayList<MyTask> tasks) {
        super(context, 0, tasks);
        this.context = context;
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.task_item_layout, parent, false);
        }

        MyTask task = tasks.get(position);

        TextView tvTitle = convertView.findViewById(R.id.tv_task_title);
        TextView tvPriority = convertView.findViewById(R.id.tv_priority);
        Button btnEdit = convertView.findViewById(R.id.btn_edit);
        Button btnDelete = convertView.findViewById(R.id.btn_delete);

        tvTitle.setText(task.getTitle());
        tvPriority.setText("Priority: " + task.getPriority());

        btnDelete.setOnClickListener(v -> {
            tasks.remove(position);
            notifyDataSetChanged();
        });

        btnEdit.setOnClickListener(v -> {
            tvTitle.setText(task.getTitle() + " (Edited)");
        });

        return convertView;
    }
}
