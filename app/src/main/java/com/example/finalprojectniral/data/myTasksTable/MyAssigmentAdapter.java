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
    private OnAssignmentClickListener listener;

    public interface OnAssignmentClickListener {
        void onEditClick(MyAssignment assignment);
        void onDeleteClick(MyAssignment assignment);
    }

    public MyAssigmentAdapter(Context context, ArrayList<MyAssignment> assignmentList, OnAssignmentClickListener listener) {
        this.context = context;
        this.assignmentList = assignmentList;
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    /**
     * وصف قصير: تُرجع عدد العناصر الموجودة في قائمة المهام.
     * القيمة المُرجعة (@return): عدد المهام (int).
     */
    @Override
    public int getCount() {
        return assignmentList.size();
    }

    /**
     * وصف قصير: تُرجع كائن المهمة الموجود في موقع معين في القائمة.
     * البارامترات (@param position): ترتيب العنصر في القائمة.
     * القيمة المُرجعة (@return): كائن المهمة (Object) في هذا الموقع.
     */
    @Override
    public Object getItem(int position) {
        return assignmentList.get(position);
    }

    /**
     * وصف قصير: تُرجع المعرف الفريد للعنصر بناءً على موقعه.
     * البارامترات (@param position): ترتيب العنصر.
     * القيمة المُرجعة (@return): المعرف (long).
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * وصف قصير: تقوم بإنشاء وتجهيز واجهة العرض (View) لكل عنصر في القائمة.
     * الهدف منها: ربط بيانات المهمة بالعناصر المرئية (العنوان، الأهمية) وإعداد أزرار التعديل والحذف.
     * البارامترات (@param position): موقع العنصر في القائمة.
     * البارامترات (@param convertView): واجهة عرض قديمة يمكن إعادة استخدامها.
     * البارامترات (@param parent): الحاوية الأب التي ستحتوي على الواجهة.
     * القيمة المُرجعة (@return): واجهة العرض الجاهزة (View).
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        // تحديد طريقة تحميل القالب المناسب لكل عنصر في قائمة المهام
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.task_item_layout, parent, false);
            holder = new ViewHolder();
            holder.tvTitle = convertView.findViewById(R.id.tv_task_title);
            holder.tvPriority = convertView.findViewById(R.id.tv_priority);
            holder.btnEdit = convertView.findViewById(R.id.btn_edit);
            holder.btnDelete = convertView.findViewById(R.id.btn_delete);

            // تخزين العناصر التي تم تحميلها في هذا العنصر للاستخدام في المرة القادمة
            convertView.setTag(holder);
        } else {
            // إذا كان العنصر موجودًا بالفعل، فإننا نستخرج بيانات العناصر المخزنة منه
            holder = (ViewHolder) convertView.getTag();
        }
        MyAssignment assignment = assignmentList.get(position);

        holder.tvTitle.setText(assignment.getShortTitle());
        holder.tvPriority.setText("Priority: " + assignment.getImportance());

        // زر تعديل
        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEditClick(assignment);
            }
        });

        // زر حذف
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteClick(assignment);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView tvTitle, tvPriority;
        Button btnEdit, btnDelete;
    }
}
