package com.example.agarimovil;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.agarimovil.clases.Notificaciones;
import com.example.agarimovil.clases.formatoFecha;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public class DetalleProductoActivity extends AppCompatActivity {

    private TextView txtNombre,txtDescripcion,txtCosto,txtMarca,txtCondicion,txtStock,
    txtUltimaVenta,txtUltimoSurt;
    private String ip,idProducto,imgEncoded;
    private Bitmap bitmap;
    private byte[] bArray;
    private JSONObject producto;

    private ImageView imgProd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_producto);
        idProducto = getIntent().getStringExtra("idProducto");
        SharedPreferences preferences = this.getSharedPreferences("agari.dat", this.MODE_PRIVATE);
        ip = preferences.getString("ipServidor",ip);

        txtNombre = findViewById(R.id.txtNombre);
        txtDescripcion = findViewById(R.id.txtDescripcion);
        txtCosto = findViewById(R.id.txtCosto);
        txtMarca = findViewById(R.id.txtMarca);
        txtCondicion = findViewById(R.id.txtCondicion);
        txtStock = findViewById(R.id.txtStock);
        txtUltimaVenta = findViewById(R.id.txtUltimaVenta);
        txtUltimoSurt = findViewById(R.id.txtUltimoSurt);
        imgProd = findViewById(R.id.imgProd);

        if (bitmap!=null){
            imgProd.setImageBitmap(bitmap);
        }

        imgProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mostrarImagen(view,producto.getString("nombre"),bitmap);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        llenarProducto();
    }

    public void tomarFoto(View view){
        abrirCamara();
    }//tomarFoto

    private void abrirCamara(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent,1);
        }//intent
    }//abirCamara

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, "Tomar foto", Toast.LENGTH_SHORT).show();

        if( requestCode == 1 && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            imgEncoded = Base64.getEncoder().encodeToString(byteArray);

            imgProd.setImageBitmap(bitmap);
        }
    }//onActivityResult

    public void mostrarImagen(View view,String titulo,Bitmap bMap){
        //Crear AlertDialog en la vista
        AlertDialog.Builder builder = new AlertDialog.Builder(DetalleProductoActivity.this);

        //Obtener el content
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //Llenar un objeto con la vista personalizada del Alert
        View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.layout_mostrar_producto, viewGroup, false);

        //Obtener los elementos dentro de la lista personalizada
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        TextView txtMensaje = dialogView.findViewById(R.id.txtMensaje);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        ImageView imgMuestra = dialogView.findViewById(R.id.imgMuestra);

        //Llenar con información los textos
        txtMensaje.setText(titulo);
        imgMuestra.setImageBitmap(bMap);

        //Construir la vista
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();

        //Mostrar la vista
        alertDialog.show();
    }

    public void alertaConfirmacion(View view){
        //Crear AlertDialog en la vista
        AlertDialog.Builder builder = new AlertDialog.Builder(DetalleProductoActivity.this);

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
        txtMensaje.setText("Actualizar imagén del producto");
        txtDescripcion.setText("Para confirmar presiona ACEPTAR");

        //Construir la vista
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();

        //Agregar acciones a los botones
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                actualizarProducto(v);
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



    public void llenarProducto(){
        String url = "http://"+ip+":3000/producto/buscar";

        RequestQueue queue = Volley.newRequestQueue(DetalleProductoActivity.this);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray respo = new JSONArray(response);
                    formatoFecha formatoFecha;

                    if (respo.length()>0) {
                        producto=respo.getJSONObject(0);

                        txtNombre.setText(respo.getJSONObject(0).getString("nombre")+" #"+
                                respo.getJSONObject(0).getString("idProducto"));

                        txtDescripcion.setText(txtDescripcion.getText()+
                                respo.getJSONObject(0).getString("descripcionProd"));

                        String format = "%,.2f";
                        String costo = String.format(format,respo.getJSONObject(0).getDouble("costoIndividual"));
                        txtCosto.setText(txtCosto.getText()+"$"+costo);

                        txtMarca.setText(txtMarca.getText()+respo.getJSONObject(0).getString("marca"));

                        txtCondicion.setText(txtCondicion.getText()+
                                respo.getJSONObject(0).getString("condicion"));

                        txtStock.setText(txtStock.getText()+
                                respo.getJSONObject(0).getString("stock"));

                        if (!respo.getJSONObject(0).getString("ultimaVenta").equals("null")) {
                            formatoFecha = new formatoFecha(respo.getJSONObject(0).getString("ultimaVenta"));
                            txtUltimaVenta.setText(txtUltimaVenta.getText() + formatoFecha.obtenerFecha());
                        }

                        if (!respo.getJSONObject(0).getString("ultimoSurtido").equals("null")) {
                            formatoFecha = new formatoFecha(respo.getJSONObject(0).getString("ultimoSurtido"));
                            txtUltimoSurt.setText(txtUltimoSurt.getText() + formatoFecha.obtenerFecha());
                        }

                        byte[] bytes = Base64.getDecoder().decode(respo.getJSONObject(0).getString("img"));
                        bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);

                        imgProd.setImageBitmap(bitmap);


                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetalleProductoActivity.this, "Ha ocurrido un error = " + error, Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                // Crear un mapa para asignar los valores del post
                Map<String, String> params = new HashMap<String, String>();

                // Asignar los valores con sus claves
                params.put("idProducto", idProducto);

                // Devolvemos los parametros
                return params;
            }
        };
        // Hacer una solicitud JSON
        queue.add(request);
    }

    public void actualizarProducto(View view){
        String url = "http://"+ip+":3000/producto";

        RequestQueue queue = Volley.newRequestQueue(DetalleProductoActivity.this);

        StringRequest request = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals("Error al actualizar producto")){
                    try {
                        Notificaciones notificaciones = null;
                        notificaciones = new Notificaciones(view,"Producto actualizado",
                                "El producto: "+producto.getString("nombre")+" ha sido actualizado");
                        notificaciones.enviarNotificacionVacia();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }else {
                    Toast.makeText(DetalleProductoActivity.this, response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetalleProductoActivity.this, "Ha ocurrido un error = " + error, Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                // Crear un mapa para asignar los valores del post
                Map<String, String> params = new HashMap<String, String>();

                // Asignar los valores con sus claves
                try {
                    String fechas1[] = producto.getString("ultimaVenta").split("T");
                    String fechas2[] = producto.getString("ultimoSurtido").split("T");

                    params.put("idProducto", producto.getString("idProducto"));
                    params.put("nombreProd", producto.getString("nombre"));
                    params.put("descripcionProd", producto.getString("descripcionProd"));
                    params.put("costo", producto.getString("costoIndividual"));
                    params.put("marca", producto.getString("marca"));
                    params.put("condicion", producto.getString("condicion"));
                    params.put("estado", producto.getString("estado"));
                    params.put("stock", producto.getString("stock"));
                    params.put("ultimaVenta", fechas1[0]);
                    params.put("ultimoSurtido", fechas2[0]);
                    params.put("imgProd", imgEncoded);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                // Devolvemos los parametros
                return params;
            }
        };
        // Hacer una solicitud JSON
        queue.add(request);
    }


}

