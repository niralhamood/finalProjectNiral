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
    private ImageView btnBack;
    /** * يحتوي هذا المتغير على زر إضافة مهمة جديدة للمهام
     */
    private ImageButton btnAddTask;
    /**
     * يحتوي هذا المتغير على واجهة القائمة (ListView) التي تقوم بعرض المهام بشكل متتابع وقابل للتمرير.
     */
    private ListView listTasks; // for showing a list of tasks (scrollable list)[[
    /**
     * يحتوي هذا المتغير على قائمة برمجية من نوع ArrayList لتخزين كائنات المهام (MyAssignment) التي يتم جلبها من قاعدة البيانات.
     */
    private ArrayList<MyAssignment> tasksList;
    /**
     * يحتوي هذا المتغير على الوسيط (Adapter) المسؤول عن ربط البيانات الموجودة في القائمة البرمجية بالعناصر المرئية في الـ ListView.
     */
    private MyAssigmentAdapter adapter;
    /**
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

        /**
         * Initialize and connect the Data Bridge (Adapter):
         *
         * What it does:
         * It creates a new instance of MyAssigmentAdapter and links it to our ListView.
         *
         * What it is for:
         * A ListView cannot "talk" directly to an ArrayList. The Adapter acts as a middleman
         * that takes each data object from 'tasksList' and converts it into a visual row
         * layout that the user can see on the screen.
         *
         * Logic:
         * 1. 'new MyAssigmentAdapter(this, tasksList)': Creates the bridge, giving it the
         *    current screen (context) and the data source (the list).
         * 2. 'listTasks.setAdapter(adapter)': Plugs this bridge into the UI component
         *    (ListView) so it knows where to get its rows from.
         */
        adapter = new MyAssigmentAdapter(this, tasksList );
        listTasks.setAdapter(adapter);

        //hay astd3aa dalah جلب البيانات الحقيقية من فايربيس
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
     * شرح معمق للدالة fetchTasksFromFirebase:
     * ---------------------------------------
     * لماذا نستخدمها؟ لجلب بيانات المهام الخاصة بالمستخدم من السحاب (Firebase) وعرضها.
     * كيف تعمل؟ تستخدم "مراقب بيانات" (EventListener) يظل متصلاً بفايربيس طوال فترة فتح الشاشة.
     * متى تعمل؟ بمجرد تشغيل النشاط (Activity) وأيضاً تلقائياً عند حدوث أي تغيير في قاعدة البيانات.
     */
    private void fetchTasksFromFirebase() {
        // إضافة مستمع (Listener) يراقب المسار المحدد في databaseReference
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // 1. تفريغ القائمة الحالية لتجنب تكرار البيانات عند كل تحديث
                tasksList.clear();

                /*
                 * 2. المرور عبر جميع "العقد" (Nodes) الموجودة تحت مسار هذا المستخدم:
                 * -----------------------------------------------------------
                 * snapshot.getChildren() تعيد قائمة بكل "الأبناء" الموجودين داخل مسار المستخدم.
                 * نستخدم حلقة التكرار (for) للمرور على كل مهمة (postSnapshot) واحدة تلو الأخرى.
                 */
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    /*
                     * تحويل البيانات من JSON (تنسيق فايربيس) إلى كائن جافا (MyAssignment):
                     * ---------------------------------------------------------------
                     * getValue(MyAssignment.class): هي دالة سحرية في فايربيس تبحث عن أسماء المفاتيح
                     * في قاعدة البيانات (مثل title, importance) وتضع قيمها داخل المتغيرات المقابلة
                     * لها في كائن الـ Java تلقائياً، بشرط أن تكون الأسماء متطابقة.
                     */
                    MyAssignment assignment = postSnapshot.getValue(MyAssignment.class);

                    /* التحقق من أن عملية التحويل نجحت ولم ينتج كائن فارغ */
                    if (assignment != null) {

                        /*
                         * تخزين المفتاح الفريد (Key) للمهمة:
                         * -------------------------------
                         * postSnapshot.getKey() يعيد المعرف العشوائي الذي ولده فايربيس للمهمة (مثل -Nxyz...).
                         * هذا المفتاح ليس جزءاً من البيانات الداخلية للكائن، لذا نحتاج لتخزينه يدوياً
                         * عبر دالة setKey()، لنتمكن مستقبلاً من معرفة "أي مهمة بالضبط" نريد حذفها أو تعديلها.
                         */
                        assignment.setKey(postSnapshot.getKey());

                        /*
                         * إضافة المهمة إلى القائمة المحلية:
                         * ------------------------------
                         * بعد أن أصبح كائن الـ Java جاهزاً ويحتوي على البيانات والمفتاح (Key)،
                         * نقوم بإضافته إلى ArrayList (tasksList) التي سيستخدمها الـ Adapter للعرض.
                         */
                        tasksList.add(assignment);
                    }
                }
                
                // 3. الترتيب: وضع المهام الأكثر أهمية في بداية القائمة
                sortTasksByImportance();
                
                // 4. التحديث البصري: إخبار الـ Adapter أن البيانات جاهزة ليقوم برسمها على الشاشة
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // يتم استدعاء هذا الجزء في حال فشل الوصول للبيانات (مثل انقطاع الإنترنت أو رفض الصلاحيات)
                android.util.Log.e("FirebaseError", "Error fetching data", error.toException());
            }
        });
    }
}
