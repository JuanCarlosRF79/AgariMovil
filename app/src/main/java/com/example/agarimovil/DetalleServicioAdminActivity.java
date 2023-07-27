package com.example.agarimovil;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.agarimovil.clases.formatoFecha;
import com.example.agarimovil.cliente.DetalleServicioActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DetalleServicioAdminActivity extends AppCompatActivity {

    private String idServicio,fechaSiguiente,ip,estadoServicio;
    private TextView codigo,nombre,telefono,estadoDir,ciudad,colonia,calle,descripcionDir,
            descripcionProblema,fechaSoli,fechaFin,estadoServ;
    private EditText total,fechaCita;
    private DatePickerDialog datePickerDialog;
    private LinearLayout layoutCita;
    private Button btnConfirmar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_servicio_admin);
        idServicio = getIntent().getStringExtra("idServicio");

        codigo = findViewById(R.id.txtCodigo);
        nombre = findViewById(R.id.txtNombreCli);
        estadoDir = findViewById(R.id.txtEstadoDir);
        ciudad = findViewById(R.id.txtCiudadDir);
        colonia = findViewById(R.id.txtColoniaDir);
        calle = findViewById(R.id.txtCalleDir);
        descripcionDir = findViewById(R.id.txtDescripcionDir);
        descripcionProblema = findViewById(R.id.txtDescripcioProblema);
        estadoServ = findViewById(R.id.txtEstadoServ);
        fechaSoli = findViewById(R.id.txtFechaSoli);
        fechaFin = findViewById(R.id.txtFechaFin);
        total = findViewById(R.id.txtTotal);
        telefono = findViewById(R.id.txtTelefonoCli);
        fechaCita = findViewById(R.id.edtFechaSiguiente);
        btnConfirmar = findViewById(R.id.btnAccion);
        layoutCita = findViewById(R.id.lyCambiarFecha);

        SharedPreferences preferences = getSharedPreferences("agari.dat",MODE_PRIVATE);
        ip = preferences.getString("ipServidor",ip);

        //Crear la vista del calendario
        fechaCita.setFocusable(false);
        fechaCita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(DetalleServicioAdminActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int month, int day) {
                                String localDate;
                                if (month+1<10){
                                    localDate = year+"-0"+(month+1)+"-"+day+"T";
                                }else {
                                    localDate = year+"-"+(month+1)+"-"+day+"T";
                                }

                                formatoFecha formatoFecha = new formatoFecha(localDate);
                                // set day of month , month and year value in the edit text
                                fechaCita.setText(formatoFecha.obtenerFecha());

                                fechaSiguiente = year+"-"+(month+1)+"-"+day;
                            }
                        }, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
                //Agregar algo pa limitar que la fecha no sea muy antigua
                datePickerDialog.show();
            }
        });//Mostrar datepicker
        llenarServicio();

    }

    public void llenarServicio(){
        String url = "http://"+ip+":3000/servicio/buscar";

        RequestQueue queue = Volley.newRequestQueue(DetalleServicioAdminActivity.this);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray respo = new JSONArray(response);
                    formatoFecha formatoFecha;

                    if (respo.length()>0) {



                        codigo.setText(codigo.getText()+respo.getJSONObject(0).getString("idServicio"));
                        nombre.setText(nombre.getText()+respo.getJSONObject(0).getString("nombre_cli")+
                                respo.getJSONObject(0).getString("apellidoPat_cli"));

                        String txttelefono = respo.getJSONObject(0).getString("telefono").substring(0,2)+" "+
                                respo.getJSONObject(0).getString("telefono").substring(2,6)+" "+
                                respo.getJSONObject(0).getString("telefono").substring(6,10);
                        telefono.setText(telefono.getText()+txttelefono);

                        estadoDir.setText(estadoDir.getText()+respo.getJSONObject(0).getString("estado"));
                        ciudad.setText(ciudad.getText()+respo.getJSONObject(0).getString("ciudad"));
                        calle.setText(calle.getText()+respo.getJSONObject(0).getString("calle"));
                        colonia.setText(colonia.getText()+respo.getJSONObject(0).getString("colonia"));

                        descripcionDir.setText(descripcionDir.getText()+"\n"
                                +respo.getJSONObject(0).getString("descripcionDireccion"));

                        descripcionProblema.setText(descripcionProblema.getText()+"\n"
                                +respo.getJSONObject(0).getString("descripcionProblema"));

                        estadoServicio=respo.getJSONObject(0).getString("estadoServicio");
                        estadoServ.setText(respo.getJSONObject(0).getString("estadoServicio"));

                        total.setText(respo.getJSONObject(0).getString("pagoServicio"));

                        formatoFecha = new formatoFecha(respo.getJSONObject(0).getString("fechaOrden"));
                        fechaSoli.setText(fechaSoli.getText()+formatoFecha.obtenerFecha());

                        if (!respo.getJSONObject(0).getString("proximaCita").equals("null")) {
                            formatoFecha = new formatoFecha(respo.getJSONObject(0).getString("proximaCita"));
                            fechaCita.setText(fechaCita.getText() + formatoFecha.obtenerFecha());
                        }

                        if (!respo.getJSONObject(0).getString("fechaFinalizado").equals("null")) {
                            btnConfirmar.setVisibility(View.INVISIBLE);
                            layoutCita.setVisibility(View.INVISIBLE);
                            total.setFocusable(false);
                            total.setClickable(false);

                            formatoFecha = new formatoFecha(respo.getJSONObject(0).getString("fechaFinalizado"));
                            fechaFin.setText(fechaFin.getText() + formatoFecha.obtenerFecha());
                        }else fechaFin.setVisibility(View.INVISIBLE);

                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetalleServicioAdminActivity.this, "Ha ocurrido un error = " + error, Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                // Crear un mapa para asignar los valores del post
                Map<String, String> params = new HashMap<String, String>();

                // Asignar los valores con sus claves
                params.put("idServicio", idServicio);

                // Devolvemos los parametros
                return params;
            }
        };
        // Hacer una solicitud JSON
        queue.add(request);
    }



}