package com.example.agarimovil;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ipActivity extends AppCompatActivity {

    private EditText edServidor;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip);

        edServidor = findViewById(R.id.edtServidor);
    }


    public void cancelar(View view){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void cambiar(View view){
        String ip = edServidor.getText().toString();

        SharedPreferences preferences = getSharedPreferences("agari.dat", MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ipServidor", ip);
        editor.apply();

        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

}