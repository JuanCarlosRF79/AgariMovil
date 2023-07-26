package com.example.agarimovil;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.agarimovil.clases.Municipios;
import com.example.agarimovil.clases.Notificaciones;
import com.example.agarimovil.cliente.DetalleServicioActivity;
import com.example.agarimovil.cliente.FormularioServicioActivity;
import com.example.agarimovil.cliente.HomeClienteActivity;
import com.example.agarimovil.cliente.MisServiciosActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RegistroActivity extends AppCompatActivity{

private DatePickerDialog datePickerDialog;
private EditText edtnombre,edtapellido,edtfecha,edtcorreo,edtcontrasena,
        confirmarContra,edttelefono,edtrfc,edtcalle,edtcolonia;
private RadioGroup rgsexo;
private RadioButton seleccionadoSexo;
private Spinner municipioDireccion;
private Button btnRegistro;
private String nombreCli,apellidoPatCli,apellidoMatCli,sexo,fechaNac,rfc,municipioDir,calleDir,coloniaDir,
            telefono,correo,contrasena,ip;
private Municipios municipios = new Municipios();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        //EditText y botones
        edtnombre = findViewById(R.id.edtNombre);
        edtapellido = findViewById(R.id.edtApellido);
        edtfecha = findViewById(R.id.edtFechaNac);
        edtcorreo = findViewById(R.id.edtCorreoRegistro);
        edtcontrasena = findViewById(R.id.edtContrasena);
        confirmarContra = findViewById(R.id.edtConfirmaContrasena);
        edttelefono = findViewById(R.id.edtTelefono);
        edtrfc = findViewById(R.id.edtRFC);
        edtcalle = findViewById(R.id.edtCalle);
        edtcolonia =findViewById(R.id.edtColonia);

        btnRegistro = findViewById(R.id.btnRegistro);

        rgsexo = findViewById(R.id.sexo);

        //Otros elementos
        municipioDireccion = findViewById(R.id.spnMunicipio);
        municipioDireccion.setAdapter(new ArrayAdapter<String>(this, R.layout.adapter_estados, municipios.getMunicipios()));

        SharedPreferences preferences = getSharedPreferences("agari.dat",MODE_PRIVATE);
        ip = preferences.getString("ipServidor",ip);

        //Crear la vista del calendario
        edtfecha.setFocusable(false);
        edtfecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(RegistroActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int month, int day) {
                                // set day of month , month and year value in the edit text
                                edtfecha.setText(day + "/"
                                        + (month + 1) + "/" + year);

                            }
                        }, year, month, day);
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                //Agregar algo pa limitar que la fecha no sea muy antigua
                datePickerDialog.show();
            }
        });
        //vista del calendario

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarse(view);
            }
        });

    }

    public void cambiarVista(View view){
        Intent intent = new Intent(this, HomeClienteActivity.class);
        startActivity(intent);
        finish();
    }

    public boolean llenarVariables(){
        nombreCli = edtnombre.getText().toString();

        String apellidos[] = edtapellido.getText().toString().split(" ");
        apellidoPatCli = apellidos[0];
        if (apellidos.length>1)
            apellidoMatCli = apellidos[1];
        else apellidoMatCli = apellidos[0];

        seleccionadoSexo = findViewById(rgsexo.getCheckedRadioButtonId());

        sexo=seleccionadoSexo.getText().toString();

        if (!edtfecha.getText().toString().isEmpty()){
            String fechas[] = edtfecha.getText().toString().split("/");
            fechaNac=fechas[2]+"-"+fechas[1]+"-"+fechas[0];
        }

        rfc = edtrfc.getText().toString();
        municipioDir = municipioDireccion.getSelectedItem().toString();
        calleDir = edtcalle.getText().toString();
        coloniaDir = edtcolonia.getText().toString();
        telefono = edttelefono.getText().toString();
        correo = edtcorreo.getText().toString();
        contrasena = edtcontrasena.getText().toString();

        if (nombreCli.isEmpty()||apellidoPatCli.isEmpty()||apellidoMatCli.isEmpty()||sexo.isEmpty()||
        telefono.isEmpty()||correo.isEmpty()||contrasena.isEmpty()){
            Toast.makeText(this, "Por favor llena los campos solicitados", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!contrasena.equals(confirmarContra.getText().toString())){
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            edtcontrasena.requestFocus();
            confirmarContra.requestFocus();
            return false;
        }

        return true;
    }

    public void registrarse(View view){

        if (llenarVariables()){
            String url = "http://"+ip+":3000/cliente";

            RequestQueue queue = Volley.newRequestQueue(RegistroActivity.this);

            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray respo = new JSONArray(response);

                        if (respo.length()>0) {
                           Notificaciones notificacion = new Notificaciones(view, "¡Bienvenid@!",
                                    "¿Quieres solicitar tu primer servicio?");
                            notificacion.enviarNotificacion(FormularioServicioActivity.class, HomeClienteActivity.class,"");

                            Intent intent = new Intent(RegistroActivity.this, HomeClienteActivity.class);

                            SharedPreferences preferences = getSharedPreferences("agari.dat",MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("idUsuario",respo.getJSONObject(0).getString("idCliente"));
                            editor.apply();

                            startActivity(intent);
                        }else {
                            Toast.makeText(RegistroActivity.this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(RegistroActivity.this, "Ha ocurrido un error = " + error, Toast.LENGTH_LONG).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() {
                    // Crear un mapa para asignar los valores del post
                    Map<String, String> params = new HashMap<String, String>();

                    // Asignar los valores con sus claves
                    params.put("nombreCli", nombreCli);
                    params.put("apellidoPatCli", apellidoPatCli);
                    params.put("apellidoMatCli", apellidoMatCli);
                    params.put("sexo", sexo);
                    params.put("fechaNac", fechaNac);
                    params.put("municipioDir", municipioDir);
                    params.put("calleDir", calleDir);
                    params.put("coloniaDir", coloniaDir);
                    params.put("telefono", telefono);
                    params.put("rfc", rfc);
                    params.put("correo", correo);
                    params.put("password", contrasena);

                    // Devolvemos los parametros
                    return params;
                }
            };
            // Hacer una solicitud JSON
            queue.add(request);

        }

    }



}