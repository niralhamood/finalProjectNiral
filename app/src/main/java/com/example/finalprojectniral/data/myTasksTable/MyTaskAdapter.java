package com.example.finalprojectniral.data.myTasksTable;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

public class MyTaskAdapter extends ArrayAdapter<MyTassk> {
    public MyTaskAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }
}

public class MyTaskAdapter extends ArrayAdapter<MyTask> {
    //המזהה של קובץ עיצוב הפריט
    private final int itemLayout;
    /**
     * פעולה בונה מתאם
     * @param context קישור להקשר (מסך- אקטיביטי)
     * @param resource עיצוב של פריט שיציג הנתונים של העצם
     */
    public MyTaskAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        this.itemLayout =resource;
    }
    }



