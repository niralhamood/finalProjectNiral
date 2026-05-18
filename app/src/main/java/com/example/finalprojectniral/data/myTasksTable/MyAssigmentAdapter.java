package com.example.finalprojectniral.data.myTasksTable;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.finalprojectniral.R;
import com.example.finalprojectniral.TasksActivity;
import com.example.finalprojectniral.addAsigment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * فئة مخصصة لعرض قائمة مهام (MyAssignment) في شاشة أساسية (TasksActivity).
 */
public class MyAssigmentAdapter extends BaseAdapter
{
    /**
     * سياق التطبيق (Context) الذي يوفر الوصول إلى الموارد والنظام الأساسي لربط الواجهة بالأصل.
     */
    private Context context;
    /**
     * قائمة بالمهام التي ستتم عرضها.
     */
    private ArrayList<MyAssignment> assignmentList;
    /**
     * تحميل القوالب المستخدمة لعرض العناصر.
     */
    private LayoutInflater inflater;
    /**
     * المستمع للأحداث التي تتم عليها على المهام (التعديل والحذف).
     */


    // دالكة بنائية
    public MyAssigmentAdapter(Context context, ArrayList<MyAssignment> assignmentList)
    {
        // 1. تخزين سياق التطبيق الممرر (Context) لاستخدامه لاحقاً في تشغيل الأنشطة (Activities) أو الوصول للموارد.
        this.context = context;
        // 2. استقبال قائمة المهام (ArrayList) التي تحتوي على البيانات القادمة من قاعدة البيانات لعرضها في القائمة.
        this.assignmentList = assignmentList;
        // 3. تهيئة كائن 'inflater' الذي يقوم بتحويل ملفات تصميم الـ XML (مثل task_item_layout) إلى كائنات برمجية (View).
        this.inflater = LayoutInflater.from(context);
        // this.listener = listener; // سطر معطل (Commented out): كان يستخدم سابقاً لربط مستمع للأحداث.
    }

    /**
     * وصف قصير: تُرجع عدد العناصر الموجودة في قائمة المهام عشان تفهم الاندرويد قديه يحضر وهكذا.
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

                editAssignment(assignment);

        });

        // زر حذف
        holder.btnDelete.setOnClickListener(v -> {
                deleteAssigment(assignment);

        });

        return convertView;
    }

    private void deleteAssigment(MyAssignment assignment) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference assignmentsRef = database.child("assignments").child(uid);
assignmentsRef.child(assignment.getKey()).removeValue();
    }

    private void editAssignment(MyAssignment assignment) {
        Intent intent = new Intent(context, addAsigment.class);
        intent.putExtra("isEdit", true);
        intent.putExtra("assignment", assignment);
       context. startActivity(intent);

    }

    static class ViewHolder {
        TextView tvTitle, tvPriority;
        Button btnEdit, btnDelete;
    }
}
