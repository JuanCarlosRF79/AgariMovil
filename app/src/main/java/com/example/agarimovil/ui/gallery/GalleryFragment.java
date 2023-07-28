package com.example.agarimovil.ui.gallery;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.agarimovil.DetalleServicioAdminActivity;
import com.example.agarimovil.R;
import com.example.agarimovil.clases.CustomBaseAdapterServicioAdmin;
import com.example.agarimovil.clases.formatoFecha;
import com.example.agarimovil.cliente.DetalleServicioActivity;
import com.example.agarimovil.cliente.MisServiciosActivity;
import com.example.agarimovil.databinding.FragmentGalleryBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GalleryFragment extends Fragment {

    private View view;
    private String ip,fechaServ="";
    private Button btnLimpiar;
    private int dia,ano,mes;
    private boolean seleccionado=false;
    private TextView mensajeVacio;
    private EditText fechaFiltro;
    private Spinner estadoFiltro;
    private String estadosServ[]={"Seleccionar","Solicitado","En proceso","Completado","Cancelado"};
    private DatePickerDialog datePickerDialog;
    private JSONArray servicios;
    private ListView lvServicios;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        view = inflater.inflate(R.layout.fragment_gallery, container, false);
        SharedPreferences preferences = view.getContext().getSharedPreferences("agari.dat", view.getContext().MODE_PRIVATE);
        ip = preferences.getString("ipServidor",ip);

        mensajeVacio = view.findViewById(R.id.txtMensajeVacio);
        lvServicios = view.findViewById(R.id.lsvServicios);
        btnLimpiar = view.findViewById(R.id.btnLimpiar);

        lvServicios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    Intent intent = new Intent(view.getContext(), DetalleServicioAdminActivity.class);
                    intent.putExtra("idServicio",servicios.getJSONObject(i).getString("idServicio"));
                    startActivity(intent);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        btnLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limpiar(view);
            }
        });

        estadoFiltro = view.findViewById(R.id.spnEstadosServ);
        estadoFiltro.setAdapter(new ArrayAdapter<String>(view.getContext(), R.layout.adapter_condicion_filtro, estadosServ));

        estadoFiltro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                consultarServicios();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //Crear la vista del calendario
        fechaFiltro = view.findViewById(R.id.edtFechaFiltro);
        fechaFiltro.setFocusable(false);
        fechaFiltro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int year,month,day;
                if (seleccionado) {
                    year=ano;
                    month=mes;
                    day=dia;
                }else {
                    year = c.get(Calendar.YEAR);
                    month = c.get(Calendar.MONTH);
                    day = c.get(Calendar.DAY_OF_MONTH);
                }
                datePickerDialog = new DatePickerDialog(view.getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int month, int day) {
                                dia=day;
                                ano=year;
                                mes=month;
                                seleccionado=true;

                                if (month+1<10){
                                    fechaServ = year+"-0"+(month+1)+"-"+day+"T";
                                }else {
                                    fechaServ = year+"-"+(month+1)+"-"+day+"T";
                                }

                                formatoFecha formatoFecha = new formatoFecha(fechaServ);
                                fechaFiltro.setText(formatoFecha.obtenerFecha());

                                fechaServ = year+"-"+(month+1)+"-"+day;
                                consultarServicios();
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });//Mostrar datepicker

        consultarServicios();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        view = null;
    }

    public void limpiar(View view){
        fechaFiltro.setText("");
        fechaServ="";
        estadoFiltro.setSelection(0);
        seleccionado=false;
        consultarServicios();
    }

    private void consultarServicios() {
        // url para hacer la solicitud
        String url = "http://"+ip+":3000/servicio/filtrar";

        RequestQueue queue = Volley.newRequestQueue(view.getContext());

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals("No hay servicios registrados")) {
                    lvServicios.setVisibility(View.VISIBLE);
                    try {
                        JSONArray respo = new JSONArray(response);
                        servicios= new JSONArray();

                        if (respo.length() > 0) {
                            mensajeVacio.setVisibility(View.INVISIBLE);
                            CustomBaseAdapterServicioAdmin adapter;
                            //Limitar el número de servicios que se muestra
                            //Para tener un buen rendimiento
                            int i=0;
                            do {
                                servicios.put(respo.getJSONObject(i));
                                i++;
                            }while (i<respo.length() && i<11);

                            if (servicios.length() > 0) {
                                adapter = new CustomBaseAdapterServicioAdmin(view.getContext(), servicios);
                                lvServicios.setAdapter(adapter);
                            }

                        }

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }else {
                    mensajeVacio.setVisibility(View.VISIBLE);
                    lvServicios.setVisibility(View.INVISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(view.getContext(), "Ha ocurrido un error = " + error, Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                // Crear un mapa para asignar los valores del post
                Map<String, String> params = new HashMap<String, String>();
                String estadoFilt = estadosServ[estadoFiltro.getSelectedItemPosition()];

                if (estadoFilt.equals("Seleccionar"))estadoFilt="";

                // Asignar los valores con sus claves
                params.put("estadoServ", estadoFilt);
                params.put("fechaSoli", fechaServ);

                // Devolvemos los parametros
                return params;
            }
        };
        // Hacer una solicitud JSON
        queue.add(request);
    }




}