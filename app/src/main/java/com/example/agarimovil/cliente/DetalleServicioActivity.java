package com.example.agarimovil.cliente;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.agarimovil.R;

public class DetalleServicioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_servicio);
        getSupportActionBar().setTitle("Detalle de servicio");
    }
}