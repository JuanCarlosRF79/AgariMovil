package com.example.agarimovil.clases;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.example.agarimovil.R;
import com.example.agarimovil.cliente.HomeClienteActivity;

public class Notificaciones {

    private final static String CHANNEL_ID = "AGARI"; //constante para canal de notificación
    private final static int NOTIFICATION_ID = 3; //Identificador de notificación
    private View viewActual;
    private Class viewNuevaSi,viewNuevaNo;
    private PendingIntent pendingIntentSi, pendingIntentNo;

    private String titulo,subtitulo,extra;

    public Notificaciones(View viewActual, String titulo, String subtitulo) {
        this.viewActual = viewActual;
        this.titulo = titulo;
        this.subtitulo = subtitulo;
    }

    public void enviarNotificacion(Class viewNuevaSi, Class viewNuevaNo,String extra) {

        this.viewNuevaSi = viewNuevaSi;
        this.viewNuevaNo = viewNuevaNo;
        this.extra = extra;

        setPendingIntentSi();
        setPendingIntentNo();
        crearCanalNotificacion();
        crearNotificacion();
    }

    public void enviarNotificacionVacia(){
        crearNotificacionVacia();
        crearCanalNotificacionVacia();
    }

    @SuppressLint("ResourceAsColor")
    private void crearNotificacion() {
        //Instancia para generar la notificación, especificando el contexto de la aplicación
        //y el canal de comunicación
        NotificationCompat.Builder builder = new NotificationCompat.Builder(viewActual.getContext(), CHANNEL_ID);

        //Caracteristicas a incluir en la notificación
        builder.setSmallIcon(R.drawable.baseline_circle_notifications_24);
        builder.setContentTitle(titulo);
        builder.setContentText(subtitulo);
        builder.setColor(R.color.segundo);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setVibrate(new long[]{1000, 1000, 1000, 1000});
        builder.setDefaults(Notification.DEFAULT_SOUND);

        //Especifica la Activity que aparecen en la notificación
        builder.setContentIntent(pendingIntentSi);

        //Se agregan las opciones que aparecen en la notificación
        builder.addAction(R.drawable.baseline_check_24, "Si", pendingIntentSi);
        builder.addAction(R.drawable.baseline_cancel_24, "No", pendingIntentNo);

        //Instancia que gestiona la notificación con el dispositivo
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(viewActual.getContext());
        if (ActivityCompat.checkSelfPermission(viewActual.getContext(), android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
    }

    private void crearCanalNotificacion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            //Nombre del canal
            CharSequence  name = "AGARI";

            //Instancia para gestionar el canal y el servicio de la notificación
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,name,
                    NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager notificationManager = (NotificationManager) viewActual.getContext().getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);

        }
    }

    private void setPendingIntentNo() {
        Intent intent = new Intent(viewActual.getContext(), viewNuevaNo);
        if (!extra.isEmpty()) intent.putExtra("extra",extra);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(viewActual.getContext());

        stackBuilder.addParentStack(viewNuevaNo);

        stackBuilder.addNextIntent(intent);
        pendingIntentNo = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private void setPendingIntentSi() {
        Intent intent = new Intent( viewActual.getContext(), viewNuevaSi);
        if (!extra.isEmpty()) intent.putExtra("extra",extra);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(viewActual.getContext());

        stackBuilder.addParentStack(viewNuevaSi);

        stackBuilder.addNextIntent(intent);
        pendingIntentSi = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    @SuppressLint("ResourceAsColor")
    private void crearNotificacionVacia() {
        //Instancia para generar la notificación, especificando el contexto de la aplicación
        //y el canal de comunicación
        NotificationCompat.Builder builder = new NotificationCompat.Builder(viewActual.getContext(), CHANNEL_ID);

        //Caracteristicas a incluir en la notificación
        builder.setSmallIcon(R.drawable.baseline_circle_notifications_24);
        builder.setContentTitle(titulo);
        builder.setContentText(subtitulo);
        builder.setColor(R.color.segundo);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setVibrate(new long[]{1000, 1000, 1000, 1000});
        builder.setDefaults(Notification.DEFAULT_SOUND);


        //Instancia que gestiona la notificación con el dispositivo
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(viewActual.getContext());
        if (ActivityCompat.checkSelfPermission(viewActual.getContext(), android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
    }

    private void crearCanalNotificacionVacia() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            //Nombre del canal
            CharSequence  name = "AGARI";

            //Instancia para gestionar el canal y el servicio de la notificación
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,name,
                    NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager notificationManager = (NotificationManager) viewActual.getContext().getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);

        }
    }
}
