package com.example.agarimovil;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agarimovil.clases.Estados;
import com.example.agarimovil.cliente.HomeClienteActivity;

import java.util.Calendar;
import java.util.Date;

public class RegistroActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

private DatePickerDialog datePickerDialog;
private EditText nombre;
private Spinner estadoDireccion;
private Button btnRegistro;

private Estados estados = new Estados();

    EditText fecha;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);


        //EditText y botones
        nombre = findViewById(R.id.edtNombre);
        btnRegistro = findViewById(R.id.btnRegistro);

        //Otros elementos
        estadoDireccion = findViewById(R.id.spnEstados);
        estadoDireccion.setOnItemSelectedListener(this);
        estadoDireccion.setAdapter(new ArrayAdapter<String>(this, R.layout.adapter_estados,estados.getEstados()));

        //Crear la vista del calendario
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
        //vista del calendario

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarse(view);
            }
        });

    }


    //Funciones del Spinner
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //Acciones del spinner

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void registrarse(View view){
        Intent intent = new Intent(this, HomeClienteActivity.class);
        startActivity(intent);
        finish();
    }

}