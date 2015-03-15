package jony.trailicious_api16;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import jony.trailicious_api16.dto.Coordenada;

public abstract class LocationReceiver extends BroadcastReceiver {
    private static final String TAG = LocationReceiver.class.getSimpleName();


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Coord received");
        double latitud = intent.getExtras().getDouble("Latitude");
        double longitud = intent.getExtras().getDouble("Longitude");
        String provider = intent.getExtras().getString("Provider");

        Coordenada coordenada = new Coordenada();
        coordenada.setLatitud(latitud);
        coordenada.setLongitud(longitud);

        nuevaCoordenada(coordenada);
    }
    protected abstract void nuevaCoordenada(Coordenada coordenada);
}
