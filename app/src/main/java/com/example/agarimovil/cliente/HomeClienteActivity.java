package com.example.agarimovil.cliente;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.agarimovil.R;

public class HomeClienteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_cliente);
    }

    public void formularioView(View view){
        Intent intent = new Intent(this, FormularioServicioActivity.class);
        startActivity(intent);
    }

    public void misServiciosView(View view){
        Intent intent = new Intent(this, MisServiciosActivity.class);
        startActivity(intent);
    }


}