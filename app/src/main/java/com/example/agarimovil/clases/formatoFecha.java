package com.example.agarimovil.clases;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class formatoFecha {
    private String fecha;

    public formatoFecha(String fecha) {
        this.fecha = fecha;
    }

    public String obtenerFecha(){
        String fechas[] = fecha.split("T");
        SimpleDateFormat formatter1=new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1=formatter1.parse(fechas[0]);
            String fechasConv[] = date1.toLocaleString().split(" ");
            return fechasConv[0]+" "+fechasConv[1]+" "+fechasConv[2];
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
