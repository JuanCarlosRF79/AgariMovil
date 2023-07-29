package com.example.agarimovil.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.example.agarimovil.clases.CustomBaseAdapterServicioCliente;
import com.example.agarimovil.cliente.DetalleServicioActivity;
import com.example.agarimovil.cliente.MisServiciosActivity;
import com.example.agarimovil.databinding.FragmentHomeBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    private ListView lvServicios;
    private LinearLayout layout;
    private JSONArray servicios;
    private View view;
    private TextView txtCantidad;
    private String ip;

    @SuppressLint("MissingInflatedId")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);

        SharedPreferences preferences = view.getContext().getSharedPreferences("agari.dat", view.getContext().MODE_PRIVATE);
        ip = preferences.getString("ipServidor",ip);

        layout = view.findViewById(R.id.mesajeVacio);
        txtCantidad = view.findViewById(R.id.txtCantidadServ);

        lvServicios = view.findViewById(R.id.lvServiciosHoy);
        consultarServiciosProceso();
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

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        view = null;
    }

    private void consultarServiciosProceso() {
        // url para hacer la solicitud
        String url = "http://"+ip+":3000/servicio/inicio";

        RequestQueue queue = Volley.newRequestQueue(view.getContext());

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (!response.equals("No hay servicios registrados")) {
                    try {
                        JSONArray respo = new JSONArray(response);
                        JSONObject object;

                        if (respo.length() > 0) {
                            txtCantidad.setText(txtCantidad.getText() + "" + respo.length());
                            layout.setVisibility(View.INVISIBLE);
                            servicios = respo;
                            CustomBaseAdapterServicioAdmin adapter;
                            if (servicios.length() > 0) {
                                adapter = new CustomBaseAdapterServicioAdmin(view.getContext(), servicios);
                                lvServicios.setAdapter(adapter);
                            }

                        }

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }else {
                    layout.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(view.getContext(), "Ha ocurrido un error = " + error, Toast.LENGTH_LONG).show();
            }
        });
        // Hacer una solicitud JSON
        queue.add(request);
    }

}