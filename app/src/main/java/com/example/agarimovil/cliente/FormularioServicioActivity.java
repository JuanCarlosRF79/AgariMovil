package com.example.agarimovil.cliente;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.agarimovil.R;

public class FormularioServicioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_servicio);
        getSupportActionBar().setTitle("Formulario de servicio");
    }
}