package com.example.agarimovil;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class detalle_producto extends AppCompatActivity {

    private TextView txtNumero;
    private int cantidadSeleccionada = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_producto);
    }

    public void restarProducto(View view) {
        if (cantidadSeleccionada > 0) {
            cantidadSeleccionada--;
        }
        txtNumero.setText(String.valueOf(cantidadSeleccionada));
    }

    public void agregarProducto(View view) {
        cantidadSeleccionada++;
        txtNumero.setText(String.valueOf(cantidadSeleccionada));
    }

}

