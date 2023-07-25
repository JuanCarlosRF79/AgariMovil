package com.example.agarimovil.cliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.agarimovil.MainActivity;
import com.example.agarimovil.R;

public class HomeClienteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_cliente);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cliente,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int opc = item.getItemId();
        if (opc == R.id.menuCerrar){
            SharedPreferences preferences = getSharedPreferences("agari.dat",MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.remove("idUsuario");
            editor.remove("tipoUsuario");
            editor.remove("recordar");
            editor.apply();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
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