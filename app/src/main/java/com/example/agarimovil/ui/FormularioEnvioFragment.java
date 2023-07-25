package com.example.agarimovil.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.agarimovil.R;
import com.example.agarimovil.clases.CustomBaseAdapterEnvio;
import com.example.agarimovil.clases.Municipios;
import com.example.agarimovil.DetalleProductoActivity;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class FormularioEnvioFragment extends Fragment implements AdapterView.OnItemSelectedListener {



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FormularioEnvioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment FormularioEnvioFragment.

    // TODO: Rename and change types and number of parameters
    public static FormularioEnvioFragment newInstance(String param1, String param2) {
        FormularioEnvioFragment fragment = new FormularioEnvioFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
     */

    private View view;
    private Spinner spnEstados;
    private Municipios municipiosInfo = new Municipios();

    private ListView lvDetalles;

    private String nombreProd[] = {"Motor Mabe","Flecha Samsung","Agitador Mabe"};
    private String cantidadProd[] = {"2","4","2"};
    private String totalProd[] = {"$1,200.00","$2,550.00","$3,450.00"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_formulario_envio, container, false);

        spnEstados = view.findViewById(R.id.spnEstados);
        spnEstados.setOnItemSelectedListener(this);
        spnEstados.setAdapter(new ArrayAdapter<String>(view.getContext(), R.layout.adapter_estados, municipiosInfo.getMunicipios()));

        CustomBaseAdapterEnvio customBaseAdapterEnvio = new CustomBaseAdapterEnvio(view.getContext(),nombreProd,cantidadProd,totalProd);
        lvDetalles = (ListView) view.findViewById(R.id.lvDetalles);
        lvDetalles.setAdapter(customBaseAdapterEnvio);

        lvDetalles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mostrarDetalle(view);
            }
        });

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void mostrarDetalle(View view){
        Intent intent = new Intent(view.getContext(), DetalleProductoActivity.class);
        startActivity(intent);
    }

}