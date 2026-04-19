package com.example.finalprojectniral;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class addAsigment extends AppCompatActivity {

    private TextInputEditText etShortTitle, etImportance, etWorkLoad, etDescription;
    private MaterialButton btnSelectTime, btnSave;
    private TextView tvSelectedTime;
    private MaterialCheckBox checkCompleted;

    private Calendar selectedDateTime = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_asigment); // حطي اسم ملف XML هون

        // ربط العناصر
        etShortTitle = findViewById(R.id.edit_text_short_title);
        etImportance = findViewById(R.id.etImprtance);
        etWorkLoad = findViewById(R.id.etWorkLoad);
        etDescription = findViewById(R.id.edit_text_text);

        btnSelectTime = findViewById(R.id.button_select_time);
        btnSave = findViewById(R.id.button_save_assignment);

        tvSelectedTime = findViewById(R.id.text_view_selected_time);
        checkCompleted = findViewById(R.id.checkbox_is_completed);

        // زر اختيار التاريخ والوقت
        btnSelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        // زر الحفظ
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = etShortTitle.getText().toString().trim();
                String importanceStr = etImportance.getText().toString().trim();
                String workloadStr = etWorkLoad.getText().toString().trim();
                String description = etDescription.getText().toString().trim();
                boolean isCompleted = checkCompleted.isChecked();

                if (title.isEmpty() || importanceStr.isEmpty() || workloadStr.isEmpty()) {
                    Toast.makeText(addAsigment.this,
                            "Please fill all required fields",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                int importance = Integer.parseInt(importanceStr);
                int workload = Integer.parseInt(workloadStr);

                // هون بعدين بتحطي كود الحفظ بقاعدة البيانات (Room)

                Toast.makeText(addAsigment.this,
                        "Assignment Saved Successfully!",
                        Toast.LENGTH_SHORT).show();

                finish(); // يرجع للشاشة السابقة
            }
        });
    }

    private void showDatePicker() {

        int year = selectedDateTime.get(Calendar.YEAR);
        int month = selectedDateTime.get(Calendar.MONTH);
        int day = selectedDateTime.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        selectedDateTime.set(Calendar.YEAR, year);
                        selectedDateTime.set(Calendar.MONTH, month);
                        selectedDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        showTimePicker();
                    }
                }, year, month, day);

        datePickerDialog.show();
    }

    private void showTimePicker() {

        int hour = selectedDateTime.get(Calendar.HOUR_OF_DAY);
        int minute = selectedDateTime.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        selectedDateTime.set(Calendar.MINUTE, minute);

                        String formatted = dayFormat();
                        tvSelectedTime.setText("Selected: " + formatted);
                    }
                }, hour, minute, true);

        timePickerDialog.show();
    }

    private String dayFormat() {
        int day = selectedDateTime.get(Calendar.DAY_OF_MONTH);
        int month = selectedDateTime.get(Calendar.MONTH) + 1;
        int year = selectedDateTime.get(Calendar.YEAR);
        int hour = selectedDateTime.get(Calendar.HOUR_OF_DAY);
        int minute = selectedDateTime.get(Calendar.MINUTE);

        return day + "/" + month + "/" + year + "  " + hour + ":" + minute;
    }

}