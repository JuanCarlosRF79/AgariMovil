package com.example.agarimovil.clases;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.agarimovil.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.Locale;

public class CustomBaseAdapterServicioCliente extends BaseAdapter {
    Context context;
    JSONArray array;
    LayoutInflater inflater;

    public CustomBaseAdapterServicioCliente(Context context, JSONArray array) {
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
        view = inflater.inflate(R.layout.listview_servicio,null);

        TextView codigoServ = view.findViewById(R.id.txtServicioID);
        TextView fechaSoli = view.findViewById(R.id.txtFechaSoli);
        TextView fechaFin = view.findViewById(R.id.txtFechaFin);
        TextView costoServ = view.findViewById(R.id.txtCostoServ);

        try {
            JSONObject object = new JSONObject(array.getString(i));

            codigoServ.setText(codigoServ.getText()+object.getString("idServicio"));
            fechaSoli.setText(fechaSoli.getText()+object.getString("fechaOrden"));

            if (object.getString("estadoServicio").equals("Completado")){
                fechaFin.setText(fechaFin.getText()+object.getString("fechaFinalizado"));

            }else if (object.getString("estadoServicio").equals("En proceso")){
                fechaFin.setText("Próxima cita: "+object.getString("fechaFinalizado"));

            } else if (object.getString("estadoServicio").equals("Solicitado")) {
                fechaFin.setText("Pronto nos pondremos en contacto");

            }
            NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);

            costoServ.setText(costoServ.getText()+"$"+numberFormat.format(object.getString("pagoServicio")));

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return view;
    }


}
