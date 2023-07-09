package com.example.agarimovil;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView registro;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Quitar la barra de arriba
        //getSupportActionBar().hide();


        registro = findViewById(R.id.txtRegistro);

        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registro(view);
            }
        });

    }

    public void registro(View view){
        Intent intent = new Intent(this, RegistroActivity.class);
        startActivity(intent);
    }

    public void ingresar(View view){
        Intent intent = new Intent(this, MenuActivity.class);
        intent.putExtra("cliente",true);
        startActivity(intent);
    }

}