package com.example.agarimovil.clases;

import android.annotation.SuppressLint;
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

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CustomBaseAdapterServicioCliente extends BaseAdapter {
    private Context context;
    private JSONArray array;
    private LayoutInflater inflater;

    public formatoFecha formatoFecha;


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

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.listview_servicio,null);

        TextView codigoServ = view.findViewById(R.id.txtServicioID);
        TextView fechaSoli = view.findViewById(R.id.txtFechaSoli);
        TextView fechaFin = view.findViewById(R.id.txtFechaFin);
        TextView costoServ = view.findViewById(R.id.txtCostoServ);

        try {
            JSONObject object = new JSONObject(array.getString(i));

            codigoServ.setText(codigoServ.getText()+" "+object.getString("idServicio"));

            formatoFecha = new formatoFecha(object.getString("fechaOrden"));
            fechaSoli.setText(fechaSoli.getText()+formatoFecha.obtenerFecha());

            if (!object.getString("fechaFinalizado").equals("null")){
                formatoFecha = new formatoFecha(object.getString("fechaFinalizado"));
                fechaFin.setText(fechaFin.getText()+formatoFecha.obtenerFecha());
            }else if (!object.getString("proximaCita").equals("null")){
                formatoFecha = new formatoFecha(object.getString("proximaCita"));
                fechaFin.setText("Próxima cita: "+formatoFecha.obtenerFecha());
                fechaFin.setBackgroundColor(R.color.tercero);
                fechaFin.setPadding(5,2,5,2);
            } else{
                fechaFin.setText("Pronto nos pondremos en contacto");
            }
            String format = "%,.2f";
            String costo = String.format(format,object.getDouble("pagoServicio"));

            costoServ.setText(costoServ.getText()+"$"+costo);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return view;
    }


}
