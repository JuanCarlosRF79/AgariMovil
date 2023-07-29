package com.example.agarimovil.ui.slideshow;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.example.agarimovil.DetalleProductoActivity;
import com.example.agarimovil.R;
import com.example.agarimovil.clases.CustomBaseAdapterProducto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SlideshowFragment extends Fragment {

    private View view;
    private String ip;
    private String marcas[]={"Seleccionar","Generica","Maytag","Samsung"
            ,"Mabe","Whirpool","LG","Frigidaire","Electrolux"};
    private String condiciones[]={"Seleccionar","Nuevo","Semi-nuevo","Usado"};
    private ArrayList<String> nombresProds = new ArrayList<String>();
    private Spinner spnMarca,spnCondicion;
    private AutoCompleteTextView nombreProd;
    private ListView lvProductos;
    private Button btnLimpiar;
    private ImageButton btnBuscar;
    private JSONArray productos;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_slideshow, container, false);
        SharedPreferences preferences = view.getContext().getSharedPreferences("agari.dat", view.getContext().MODE_PRIVATE);
        ip = preferences.getString("ipServidor",ip);

        spnMarca = view.findViewById(R.id.spnMarca);
        spnMarca.setAdapter(new ArrayAdapter<String>(view.getContext(), R.layout.adapter_condicion_filtro, marcas));
        spnMarca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                consultarProductos();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnCondicion = view.findViewById(R.id.spnCondicion);
        spnCondicion.setAdapter(new ArrayAdapter<String>(view.getContext(), R.layout.adapter_condicion_filtro, condiciones));
        spnCondicion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                consultarProductos();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        lvProductos = view.findViewById(R.id.lvProductos);
        lvProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    Intent intent = new Intent(view.getContext(), DetalleProductoActivity.class);
                    intent.putExtra("idProducto",productos.getJSONObject(i).getString("idProducto"));
                    startActivity(intent);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        });

        nombreProd = view.findViewById(R.id.autoCompleteTextView);

        btnLimpiar = view.findViewById(R.id.btnLimpiar);
        btnLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spnMarca.setSelection(0);
                spnCondicion.setSelection(0);
                consultarProductos();
            }
        });

        btnBuscar = view.findViewById(R.id.btnFiltrarNombre);
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!nombreProd.getText().toString().isEmpty()){
                    buscarNombre();
                }
            }
        });

        consultarProductos();

        return view;
    }

    @Override
    public void onResume() {
        consultarProductos();
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        view = null;
    }

    private void consultarProductos() {
        // url para hacer la solicitud
        String url = "http://"+ip+":3000/producto/filtrar";

        RequestQueue queue = Volley.newRequestQueue(view.getContext());

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals("No hay productos registrados")) {
                    lvProductos.setVisibility(View.VISIBLE);
                    ArrayAdapter<String> adapterString =
                            new ArrayAdapter<String>(view.getContext(), R.layout.adapter_condicion_filtro);
                    try {
                        JSONArray respo = new JSONArray(response);
                        productos= new JSONArray();

                        if (respo.length() > 0) {
                            //mensajeVacio.setVisibility(View.INVISIBLE);
                            CustomBaseAdapterProducto adapter;

                            productos=respo;

                            for (int a=0;a<respo.length();a++){
                                adapterString.add(respo.getJSONObject(a).getString("nombre"));
                            }

                            if (productos.length() > 0) {
                                adapter = new CustomBaseAdapterProducto(view.getContext(), productos);
                                lvProductos.setAdapter(adapter);
                                nombreProd.setAdapter(adapterString);
                            }

                        }

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }else {
                    //mensajeVacio.setVisibility(View.VISIBLE);
                    lvProductos.setVisibility(View.INVISIBLE);
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

                String marcaFilt = marcas[spnMarca.getSelectedItemPosition()];
                if (marcaFilt.equals("Seleccionar"))marcaFilt="";

                String condicionFilt = condiciones[spnCondicion.getSelectedItemPosition()];
                if (condicionFilt.equals("Seleccionar"))condicionFilt="";

                // Asignar los valores con sus claves
                params.put("marca", marcaFilt);
                params.put("condicion", condicionFilt);
                params.put("estadoProd","");

                // Devolvemos los parametros
                return params;
            }
        };
        // Hacer una solicitud JSON
        queue.add(request);
    }

    private void buscarNombre() {
        // url para hacer la solicitud
        String url = "http://"+ip+":3000/producto/filtrar/nombre";

        RequestQueue queue = Volley.newRequestQueue(view.getContext());

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals("No hay productos registrados")) {
                    try {
                        JSONObject respo = new JSONObject(response);
                        productos= new JSONArray();

                        if (respo.length() > 0) {
                            //mensajeVacio.setVisibility(View.INVISIBLE);
                            CustomBaseAdapterProducto adapter;

                            productos.put(respo);

                            if (productos.length() > 0) {
                                adapter = new CustomBaseAdapterProducto(view.getContext(), productos);
                                lvProductos.setAdapter(adapter);
                            }

                        }

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }else {
                    //mensajeVacio.setVisibility(View.VISIBLE);
                    lvProductos.setVisibility(View.INVISIBLE);
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

                params.put("nombreProd",nombreProd.getText().toString());

                // Devolvemos los parametros
                return params;
            }
        };
        // Hacer una solicitud JSON
        queue.add(request);
    }


}