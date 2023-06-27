package com.example.agarimovil;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class RegistroActivity extends AppCompatActivity{

private DatePickerDialog datePickerDialog;

    EditText fecha;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        fecha = findViewById(R.id.edtFechaNac);
        fecha.setFocusable(false);
        fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(RegistroActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int month, int day) {
                                // set day of month , month and year value in the edit text
                                fecha.setText(day + "/"
                                        + (month + 1) + "/" + year);

                            }
                        }, year, month, day);
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                //Agregar algo pa limitar que la fecha no sea muy antigua
                datePickerDialog.show();
            }
        });

    }

}