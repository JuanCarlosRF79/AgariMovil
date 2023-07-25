package com.example.agarimovil.cliente;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.agarimovil.MainActivity;
import com.example.agarimovil.R;
import com.example.agarimovil.clases.CustomBaseAdapterServicioCliente;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MisServiciosActivity extends AppCompatActivity {

    public String ip, idCliente;
    private ListView serviciosProcesados, serviciosCompletados;
    private JSONArray enProceso,completados;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_servicios);
        getSupportActionBar().setTitle("Mis servicios");

        serviciosProcesados = findViewById(R.id.lvEnProceso);
        serviciosCompletados = findViewById(R.id.lvCompletados);

        enProceso = new JSONArray();
        completados = new JSONArray();

        SharedPreferences preferences = getSharedPreferences("agari.dat",MODE_PRIVATE);
        ip = preferences.getString("ipServidor",ip);
        idCliente = preferences.getString("idUsuario",idCliente);

        consultarServiciosProceso();
        serviciosProcesados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    Intent intent = new Intent(MisServiciosActivity.this,DetalleServicioActivity.class);
                    intent.putExtra("idServicio",enProceso.getJSONObject(i).getString("idServicio"));
                    startActivity(intent);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                /*Intent intent = new Intent(MisServiciosActivity.this, DetalleServicioActivity.class);
                intent.putExtra("idServicio",)*/
            }
        });


    }

    private void consultarServiciosProceso() {
        // url para hacer la solicitud
        String url = "http://"+ip+":3000/servicio/buscarCliente";

        RequestQueue queue = Volley.newRequestQueue(MisServiciosActivity.this);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray respo = new JSONArray(response);
                    JSONObject object;

                    if (respo.length()>1) {

                        for (int i = 0; i < respo.length(); i++) {
                            object = new JSONObject(respo.getString(i));
                            if (!object.getString("estadoServicio").equals("Cancelado")) {
                                if (object.getString("estadoServicio").equals("Completado")) {
                                    completados.put(object);
                                } else {
                                    enProceso.put(object);
                                }
                            }
                        }

                        CustomBaseAdapterServicioCliente adapter;
                        if (enProceso.length()>0){
                            adapter = new CustomBaseAdapterServicioCliente(getApplicationContext(),enProceso);
                            serviciosProcesados.setAdapter(adapter);
                        }

                        if (completados.length()>0){
                            adapter = new CustomBaseAdapterServicioCliente(getApplicationContext(),completados);
                            serviciosCompletados.setAdapter(adapter);
                        }

                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MisServiciosActivity.this, "Ha ocurrido un error = " + error, Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Crear un mapa para asignar los valores del post
                Map<String, String> params = new HashMap<String, String>();

                // Asignar los valores con sus claves
                params.put("idCliente", idCliente);

                // Devolvemos los parametros
                return params;
            }
        };
        // Hacer una solicitud JSON
        queue.add(request);
    }

}