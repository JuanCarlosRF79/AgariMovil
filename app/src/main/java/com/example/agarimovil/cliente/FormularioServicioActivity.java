package com.example.agarimovil.cliente;


import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.agarimovil.R;
import com.example.agarimovil.clases.Municipios;
import com.example.agarimovil.clases.Notificaciones;

import org.json.JSONArray;
import org.json.JSONException;
import java.util.HashMap;
import java.util.Map;

public class FormularioServicioActivity extends AppCompatActivity {

    private EditText etCalle,etColonia, etDireccion, etProblema;
    public String ip, idCliente;

    private Spinner spnMunicipio;

    private Municipios municipios = new Municipios();

    private Notificaciones notificacion;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_servicio);
        getSupportActionBar().setTitle("Formulario de servicio");

        etCalle = findViewById(R.id.edtCalle);
        etColonia = findViewById(R.id.edtColonia);
        etDireccion = findViewById(R.id.edtDireccion);
        etProblema = findViewById(R.id.edtProblema);

        spnMunicipio = findViewById(R.id.spnMunicipiosServicio);
        spnMunicipio.setAdapter(new ArrayAdapter<String>(this, R.layout.adapter_estados, municipios.getMunicipios()));

        SharedPreferences preferences = getSharedPreferences("agari.dat",MODE_PRIVATE);
        ip = preferences.getString("ipServidor",ip);
        idCliente = preferences.getString("idUsuario",idCliente);
        llenarDireccion();
    }

    public void llenarDireccion(){
        String url = "http://"+ip+":3000/cliente/buscar";

        RequestQueue queue = Volley.newRequestQueue(FormularioServicioActivity.this);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    int posicion = 0;
                    JSONArray respo = new JSONArray(response);

                    if (respo.length()>0) {
                        etCalle.setText(respo.getJSONObject(0).getString("calle"));
                        etColonia.setText(respo.getJSONObject(0).getString("colonia"));

                        for (int i=0; i<municipios.getMunicipios().length;i++){
                            if (municipios.getMunicipios()[i].equals
                                    (respo.getJSONObject(0).getString("ciudad")))
                                posicion=i;
                        }

                        spnMunicipio.setSelection(posicion);
                    }else {
                        Toast.makeText(FormularioServicioActivity.this, "Ingresa tu dirección por favor", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FormularioServicioActivity.this, "Ha ocurrido un error = " + error, Toast.LENGTH_LONG).show();
            }
        }){
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

    public void solicitarServicio(View view){
        String url = "http://"+ip+":3000/servicio/insertar";

        RequestQueue queue = Volley.newRequestQueue(FormularioServicioActivity.this);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray respo = new JSONArray(response);

                    if (respo.length()>0) {
                        notificacion = new Notificaciones(view,MisServiciosActivity.class, DetalleServicioActivity.class,
                                "Servicio registrado","Mostrar servicios solicitados",respo.getJSONObject(0).getString("idServicio"));
                        notificacion.enviarNotificacion();

                        Intent intent = new Intent(FormularioServicioActivity.this, DetalleServicioActivity.class);
                        intent.putExtra("idServicio",respo.getJSONObject(0).getString("idServicio"));
                        startActivity(intent);
                    }else {
                        Toast.makeText(FormularioServicioActivity.this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FormularioServicioActivity.this, "Ha ocurrido un error = " + error, Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                // Crear un mapa para asignar los valores del post
                Map<String, String> params = new HashMap<String, String>();

                // Asignar los valores con sus claves
                params.put("idCliente", idCliente);
                params.put("estado", "");
                params.put("municipio", spnMunicipio.getSelectedItem().toString());
                params.put("calle", etCalle.getText().toString());
                params.put("colonia", etColonia.getText().toString());
                params.put("descDire", etDireccion.getText().toString());
                params.put("descProb", etProblema.getText().toString());
                params.put("pago", "Efectivo");
                params.put("costo", "0.0");

                // Devolvemos los parametros
                return params;
            }
        };
        // Hacer una solicitud JSON
        queue.add(request);
    }

    public void volverMenu(View view){
        Intent intent = new Intent(this,HomeClienteActivity.class);
        startActivity(intent);
        finish();
    }


}