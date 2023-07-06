package com.example.agarimovil.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.agarimovil.R;
import com.example.agarimovil.clases.Estados;


public class FormularioServicioFragment extends Fragment  implements AdapterView.OnItemSelectedListener {

    private View view;
    private Estados estadosInfo = new Estados();
    private Spinner estados;

    public FormularioServicioFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_formulario_servicio, container, false);


        estados = view.findViewById(R.id.spnEstadosServicio);
        estados.setOnItemSelectedListener(this);
        estados.setAdapter(new ArrayAdapter<String>(view.getContext(), R.layout.adapter_estados,estadosInfo.getEstados()));



        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //Acciones al seleccionar Spinner

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}