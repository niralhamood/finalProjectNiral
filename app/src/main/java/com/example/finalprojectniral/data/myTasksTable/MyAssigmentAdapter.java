package com.example.finalprojectniral.data.myTasksTable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.finalprojectniral.R;

import java.util.ArrayList;

public class MyAssigmentAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<MyAssignment> assignmentList;
    private LayoutInflater inflater;

    public MyAssigmentAdapter(Context context, ArrayList<MyAssignment> assignmentList) {
        this.context = context;
        this.assignmentList = assignmentList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return assignmentList.size();
    }

    @Override
    public Object getItem(int position) {
        return assignmentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.task_item_layout, parent, false);
            holder = new ViewHolder();
            holder.tvTitle = convertView.findViewById(R.id.tv_task_title);
            holder.tvPriority = convertView.findViewById(R.id.tv_priority);
            holder.btnEdit = convertView.findViewById(R.id.btn_edit);
            holder.btnDelete = convertView.findViewById(R.id.btn_delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MyAssignment assignment = assignmentList.get(position);

        holder.tvTitle.setText(assignment.getShortTitle());
        holder.tvPriority.setText("Priority: " + assignment.getImportance());

        // زر تعديل
        holder.btnEdit.setOnClickListener(v -> {
            // هنا ممكن تفتح شاشة لتعديل المهمة
            // لاحقاً يمكن تمرير البيانات للشاشة الثانية
        });

        // زر حذف
        holder.btnDelete.setOnClickListener(v -> {
            assignmentList.remove(position);
            notifyDataSetChanged();
            // لاحقاً يمكن حذفها من قاعدة البيانات
        });

        return convertView;
    }

    static class ViewHolder {
        TextView tvTitle, tvPriority;
        Button btnEdit, btnDelete;
    }
}
