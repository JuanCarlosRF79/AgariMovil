package com.example.agarimovil.clases;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.agarimovil.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CustomBaseAdapterProducto extends BaseAdapter {

    private Context context;
    private JSONArray array;
    private LayoutInflater inflater;
    public formatoFecha formatoFecha;

    public CustomBaseAdapterProducto(Context context, JSONArray array) {
        this.context = context;
        this.array = array;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return array.length();
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
        view = inflater.inflate(R.layout.listview_producto,null);

        TextView txtNombre = view.findViewById(R.id.txtNombreProd);
        TextView txtMarca = view.findViewById(R.id.txtMarca);
        TextView txtCondicion = view.findViewById(R.id.txtCondicion);
        TextView txtStock = view.findViewById(R.id.txtStock);
        TextView txtCosto = view.findViewById(R.id.txtCosto);

        ImageView imgProd = view.findViewById(R.id.imgProd);
        try {
            JSONObject object = new JSONObject(array.getString(i));

            txtNombre.setText(object.getString("nombre")+" #"+object.getString("idProducto"));
            txtCondicion.setText(txtCondicion.getText()+object.getString("condicion"));
            txtMarca.setText(txtMarca.getText()+object.getString("marca"));
            txtStock.setText(txtStock.getText()+object.getString("stock"));

            String format = "%,.2f";
            String costo = String.format(format,object.getDouble("costoIndividual"));
            txtCosto.setText(txtCosto.getText()+costo);

            /*imgProd.setImageResource(0);*/

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return view;
    }
}
