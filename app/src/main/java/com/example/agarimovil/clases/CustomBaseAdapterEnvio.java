package com.example.agarimovil.clases;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.agarimovil.R;

public class CustomBaseAdapterEnvio extends BaseAdapter {

    Context context;
    String[] nombres;
    String[] cantidades;
    String[] total;
    LayoutInflater inflater;

    public CustomBaseAdapterEnvio(Context context, String[] nombres, String[] cantidades, String[] total) {
        this.context = context;
        this.nombres = nombres;
        this.cantidades = cantidades;
        this.total = total;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return nombres.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.listview_envio, null);

        TextView nombreProd = (TextView) view.findViewById(R.id.txtNombre);
        TextView cantidadProd = (TextView) view.findViewById(R.id.txtCantidad);
        TextView totalProd = (TextView) view.findViewById(R.id.txtTotal);
        ImageView imgProd = (ImageView) view.findViewById(R.id.imgProd);

        nombreProd.setText(nombres[i]);
        cantidadProd.setText(cantidadProd.getText().toString()+cantidades[i]);
        totalProd.setText(total[i]);
        imgProd.setImageResource(R.drawable.motor);

        return view;
    }
}
