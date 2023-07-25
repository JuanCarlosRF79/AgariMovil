package com.example.agarimovil;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.agarimovil.cliente.HomeClienteActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private TextView registro;
    private CheckBox recordar;
    private EditText edtCorreo,edtPass;

    public String tipoEmp, ip;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Quitar la barra de arriba
        //getSupportActionBar().hide();

        SharedPreferences preferences = getSharedPreferences("agari.dat",MODE_PRIVATE);
        ip = preferences.getString("ipServidor",ip);

        registro = findViewById(R.id.txtRegistro);

        edtCorreo = findViewById(R.id.edtCorreo);
        edtPass = findViewById(R.id.edtPassword);
        recordar = findViewById(R.id.cbRecordar);

        registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { registro(view); }
        });

        TimerTask tarea =new TimerTask() {
            @Override
            public void run() {
                Intent intent;
                if(usuarioRegistrado()){
                    tipoEmp = preferences.getString("tipoUsuario",tipoEmp);
                    if (tipoEmp.equals("Empleado")){
                        intent = new Intent(MainActivity.this, MenuActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (tipoEmp.equals("Cliente")) {
                        intent = new Intent(MainActivity.this, HomeClienteActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        };
        Timer tiempo = new Timer();

        tiempo.schedule(tarea,1500);

    }

    private boolean usuarioRegistrado() {
        SharedPreferences preferences = getSharedPreferences("agari.dat",MODE_PRIVATE);

        return preferences.getBoolean("recordar",false);
    }

    public void registro(View view){
        Intent intent = new Intent(this, RegistroActivity.class);
        startActivity(intent);
    }

    public void cambiarVista(View view,String tipo){
        Intent intent;
        if (tipo.equals("Empleado")) {
            intent = new Intent(this, MenuActivity.class);
        }else{
            intent = new Intent(this, HomeClienteActivity.class);
        }
        startActivity(intent);
        finish();

    }

    public void ingresar(View view){
        iniciarSesion(view);
    }

    public void preferencias(String id,String tipo){
        SharedPreferences preferences = getSharedPreferences("agari.dat", MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("idUsuario", id);
        editor.putString("tipoUsuario", tipo);

        if (recordar.isChecked()) editor.putBoolean("recordar", true);

        editor.apply();
    }

    private void iniciarSesion(View view) {
        // url para hacer la solicitud
        String url = "http://"+ip+":3000/usuario/iniciarsesion";

        //Mostrar barra de carga
        //loadingPB.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Ocultar barra de estado
                //loadingPB.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "¡Bienvenid@!", Toast.LENGTH_SHORT).show();

                try {
                    //Pasar la respuesta a un objeto JSON
                    JSONArray respo = new JSONArray(response);

                    JSONObject respObj = new JSONObject(respo.getString(0));


                    tipoEmp = respObj.getString("tipoUsuario");

                    //Extraer la información del objeto JSON
                    if (respObj.getString("tipoUsuario").equals("Empleado")){
                        preferencias(respObj.getString("idEmpleado"), tipoEmp);

                    }else if (respObj.getString("tipoUsuario").equals("Cliente")){
                        preferencias(respObj.getString("idCliente"), tipoEmp);

                    }

                    cambiarVista(view,tipoEmp);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // method to handle errors.
                Toast.makeText(MainActivity.this, "Ha ocurrido un error = " + error, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Crear un mapa para asignar los valores del post
                Map<String, String> params = new HashMap<String, String>();

                // Asignar los valores con sus claves
                params.put("correo", edtCorreo.getText().toString());
                params.put("password", edtPass.getText().toString());

                // Devolvemos los parametros
                return params;
            }
        };
        // Hacer una solicitud JSON
        queue.add(request);
    }

}