package com.example.agarimovil.clases;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.example.agarimovil.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CustomBaseAdapterServicioAdmin extends BaseAdapter {

    private Context context;
    private JSONArray array;
    private LayoutInflater inflater;

    public formatoFecha formatoFecha;

    public CustomBaseAdapterServicioAdmin(Context context, JSONArray array) {
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
        view = inflater.inflate(R.layout.listview_servicio_admin,null);

        TextView txtCodigo = view.findViewById(R.id.txtCodigoServ);
        TextView txtNombre = view.findViewById(R.id.txtNombreCli);
        TextView txtTelefono = view.findViewById(R.id.txtTelefonoCli);
        TextView txtEstadoServ = view.findViewById(R.id.txtEstadoServ);
        TextView txtMunicipio = view.findViewById(R.id.txtMunicipio);
        TextView txtFechaFin = view.findViewById(R.id.txtFechaFin);
        TextView txtFechaCita = view.findViewById(R.id.txtFechaCita);
        TextView txtFechaSoli = view.findViewById(R.id.txtFechaOrden);
        TextView txtPago = view.findViewById(R.id.txtPagoServ);

        EditText edtCalle = view.findViewById(R.id.edtCalle);
        EditText edtColonia = view.findViewById(R.id.edtColonia);
        EditText edtProblema = view.findViewById(R.id.edtProblema);

        edtCalle.setFocusable(false);
        edtColonia.setFocusable(false);
        edtProblema.setFocusable(false);

        try {
            JSONObject object = new JSONObject(array.getString(i));

            txtCodigo.setText(txtCodigo.getText()+" "+object.getString("idServicio"));

            formatoFecha = new formatoFecha(object.getString("fechaOrden"));
            txtFechaSoli.setText(txtFechaSoli.getText()+"\n"+formatoFecha.obtenerFecha());

            if (!object.getString("fechaFinalizado").equals("null")){
                formatoFecha = new formatoFecha(object.getString("fechaFinalizado"));
                txtFechaFin.setText(txtFechaFin.getText()+"\n"+formatoFecha.obtenerFecha());
            }else txtFechaFin.setVisibility(View.INVISIBLE);

            if (!object.getString("proximaCita").equals("null")){
                formatoFecha = new formatoFecha(object.getString("proximaCita"));
                txtFechaCita.setText(txtFechaCita.getText()+"\n"+formatoFecha.obtenerFecha());
            }

            String format = "%,.2f";
            String costo = String.format(format,object.getDouble("pagoServicio"));
            txtPago.setText(txtPago.getText()+costo);

            txtNombre.setText(txtNombre.getText()+object.getString("nombre_cli")+
                    " "+object.getString("apellidoPat_cli"));

            String telefono = object.getString("telefono").substring(0,2)+" "+
                    object.getString("telefono").substring(2,6)+" "+
                    object.getString("telefono").substring(6,10);
            txtTelefono.setText(txtTelefono.getText()+telefono);


            txtMunicipio.setText(txtMunicipio.getText()+object.getString("ciudad"));
            edtCalle.setText(edtCalle.getText()+"\n"+object.getString("calle"));
            edtColonia.setText(edtColonia.getText()+"\n"+object.getString("colonia"));
            edtProblema.setText(edtProblema.getText()+"\n"+object.getString("descripcionProblema"));

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return view;
    }
}
