package com.example.agarimovil.cliente;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.agarimovil.R;
import com.example.agarimovil.clases.Notificaciones;
import com.example.agarimovil.clases.formatoFecha;

import org.json.JSONArray;
import org.json.JSONException;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class DetalleServicioActivity extends AppCompatActivity {

    private formatoFecha formatoFecha;
    private String idServicio,ip,pagoServ,estadoServ;
    private TextView txtCodigo,txtNombre, txtEstado,txtMunicipio,txtCalle,txtColonia,txtEstadoServ,
    txtFechaSol,txtFechaCita,txtFechaFin, txtTotal;
    private Button btnCancelar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_servicio);
        getSupportActionBar().setTitle("Detalle de servicio");

        idServicio = getIntent().getStringExtra("idServicio");
        SharedPreferences preferences = getSharedPreferences("agari.dat",MODE_PRIVATE);
        ip = preferences.getString("ipServidor",ip);

        txtCodigo = findViewById(R.id.txtCodigo);
        txtNombre = findViewById(R.id.txtNombreCli);
        txtEstado = findViewById(R.id.txtEstadoDir);
        txtMunicipio = findViewById(R.id.txtCiudadDir);
        txtCalle = findViewById(R.id.txtColoniaDir);
        txtColonia = findViewById(R.id.txtCalleDir);
        txtEstadoServ = findViewById(R.id.txtEstadoServ);
        txtTotal = findViewById(R.id.txtTotal);
        txtFechaSol = findViewById(R.id.txtFechaSoli);
        txtFechaCita = findViewById(R.id.txtFechaSiguiente);
        txtFechaFin = findViewById(R.id.txtFechaFin);
        btnCancelar = findViewById(R.id.btnAccion);

        if (getIntent().hasExtra("extra")){
            idServicio=getIntent().getStringExtra("exra");
        }

        llenarServicio();
    }

    public void llenarServicio(){
        String url = "http://"+ip+":3000/servicio/buscar";

        RequestQueue queue = Volley.newRequestQueue(DetalleServicioActivity.this);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray respo = new JSONArray(response);

                    if (respo.length()>0) {

                        txtCodigo.setText(txtCodigo.getText()+respo.getJSONObject(0).getString("idServicio"));
                        txtNombre.setText(txtNombre.getText()+respo.getJSONObject(0).getString("nombre_cli")+
                                respo.getJSONObject(0).getString("apellidoPat_cli"));

                        txtEstado.setText(txtEstado.getText()+respo.getJSONObject(0).getString("estado"));
                        txtMunicipio.setText(txtMunicipio.getText()+respo.getJSONObject(0).getString("ciudad"));
                        txtCalle.setText(txtCalle.getText()+respo.getJSONObject(0).getString("calle"));
                        txtColonia.setText(txtColonia.getText()+respo.getJSONObject(0).getString("colonia"));

                        estadoServ=respo.getJSONObject(0).getString("estadoServicio");
                        txtEstadoServ.setText(respo.getJSONObject(0).getString("estadoServicio"));

                        pagoServ=respo.getJSONObject(0).getString("pagoServicio");
                        String format = "%,.2f";
                        String costo = String.format(format,respo.getJSONObject(0).getDouble("pagoServicio"));
                        txtTotal.setText(txtTotal.getText()+costo);

                        formatoFecha = new formatoFecha(respo.getJSONObject(0).getString("fechaOrden"));
                        txtFechaSol.setText(txtFechaSol.getText()+formatoFecha.obtenerFecha());

                        if (!respo.getJSONObject(0).getString("proximaCita").equals("null")) {
                            formatoFecha = new formatoFecha(respo.getJSONObject(0).getString("proximaCita"));
                            txtFechaCita.setText(txtFechaCita.getText() + formatoFecha.obtenerFecha());
                        }else txtFechaCita.setVisibility(View.INVISIBLE);

                        if (!respo.getJSONObject(0).getString("fechaFinalizado").equals("null")) {
                            txtFechaCita.setVisibility(View.INVISIBLE);
                            btnCancelar.setVisibility(View.INVISIBLE);
                            formatoFecha = new formatoFecha(respo.getJSONObject(0).getString("fechaFinalizado"));
                            txtFechaFin.setText(txtFechaFin.getText() + formatoFecha.obtenerFecha());
                        }else txtFechaFin.setVisibility(View.INVISIBLE);

                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetalleServicioActivity.this, "Ha ocurrido un error = " + error, Toast.LENGTH_LONG).show();
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

    public void cancelarServ(View view){
        //Crear AlertDialog en la vista
        AlertDialog.Builder builder = new AlertDialog.Builder(DetalleServicioActivity.this);

        //Obtener el content
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //Llenar un objeto con la vista personalizada del Alert
        View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.layout_alerta_cancelar, viewGroup, false);

        //Obtener los elementos dentro de la lista personalizada
        Button btnAceptar=dialogView.findViewById(R.id.btnAceptarAlert);
        Button btnCancelar=dialogView.findViewById(R.id.btnCanelarAlert);
        TextView txtMensaje=dialogView.findViewById(R.id.txtMensaje);
        TextView txtDescripcion=dialogView.findViewById(R.id.txtDescripcion);

        //Llenar con información los textos
        txtMensaje.setText("Cancelar servicio");
        txtDescripcion.setText("Para confirmar la acción, presiona "+btnAceptar.getText().toString());
        if (estadoServ.equals("En proceso")){
            txtDescripcion.setText(Html.fromHtml("Para cancelar un servicio en proceso ponte en contacto con nosotros:"+"<br>"+
                    "<u>33 1335 3167</u>"));
            btnAceptar.setVisibility(View.INVISIBLE);
        }

        //Construir la vista
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        //Agregar acciones a los botones
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                cancelarServicio(v);
            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        
        //Mostrar la vista
        alertDialog.show();
    }

    public void cancelarServicio(View view){
        String url = "http://"+ip+":3000/servicio/cancelar";

        RequestQueue queue = Volley.newRequestQueue(DetalleServicioActivity.this);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals("Error al cancelar servicio")){
                    Notificaciones notificaciones = new Notificaciones(view,"Servicio cancelado",
                            "El servicio se ha cancelado exitosamente");
                    notificaciones.enviarNotificacionVacia();
                    Intent intent = new Intent(DetalleServicioActivity.this, HomeClienteActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(DetalleServicioActivity.this, response, Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetalleServicioActivity.this, "Ha ocurrido un error = " + error, Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                // Crear un mapa para asignar los valores del post
                if (txtEstadoServ.getText().toString().equals("Solicitado"))
                    pagoServ="0.0";
                Map<String, String> params = new HashMap<String, String>();

                // Asignar los valores con sus claves
                params.put("idServicio", idServicio);
                params.put("pagoServicio",pagoServ);

                // Devolvemos los parametros
                return params;
            }
        };
        // Hacer una solicitud JSON
        queue.add(request);
    }





}