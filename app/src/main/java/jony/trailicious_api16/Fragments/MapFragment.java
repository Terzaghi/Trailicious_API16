package jony.trailicious_api16.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import jony.trailicious_api16.R;

public class MapFragment extends Fragment {
    TextView txtGPS;
    ImageView imgGPS;
    MapView mapView;
    LocationManager locManager;
    GoogleMap map;
    float zoom = 18; //Zoom inicial. Al cambiar el zoom manualmente se modifica

    Chronometer chrono;
    Button btnPlay;
    Button btnStop;
    Button btnPause;
    TextView txtDistancia;

    private boolean ENTRENAMIENTO_EN_EJECUCION = false;
    private boolean GPS_ACTIVO = false;

    private ArrayList<PolylineOptions> lstPolilineas;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        getActivity().setTitle(getResources().getString(R.string.app_name));

        txtGPS = (TextView) v.findViewById(R.id.txt_gps);
        imgGPS = (ImageView) v.findViewById(R.id.img_gps_state);

        chrono = (Chronometer) v.findViewById(R.id.chronometer);
        btnPlay = (Button) v.findViewById(R.id.btnPlay);
        btnStop = (Button) v.findViewById(R.id.btnStop);
        btnPause = (Button) v.findViewById(R.id.btnPause);
        txtDistancia = (TextView) v.findViewById(R.id.txtDistancia);

        ENTRENAMIENTO_EN_EJECUCION = false;
        GPS_ACTIVO = false;
        lstPolilineas = new ArrayList<>();

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciaEntrenamiento();
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalizaEntrenamiento();
            }
        });
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Entrenamiento_Pause();
            }
        });

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) v.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        // Gets to GoogleMap from the MapView and does initialization stuff
        map = mapView.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setMyLocationEnabled(true);

        MapsInitializer.initialize(this.getActivity());


        iniciaProveedor();

        return v;
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    public void iniciaProveedor() {
        try {
            //Obtenemos una referencia al LocationManager
            locManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

            //Comprobamos si el GPS está encencido
            boolean isGPSEnabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            // Cambio el TextView del GPS dependiendo del estado
            if (!isGPSEnabled)
                modificaEstadoGPS(false);
            else
                modificaEstadoGPS(true);

            //Obtenemos la última posición conocida
            android.location.Location loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            //Mostramos la última posición conocida
            mostrarPosicion(loc);

            //Nos registramos para recibir actualizaciones de la posición
            LocationListener locListener = new LocationListener() {
                public void onLocationChanged(android.location.Location location) {
                    mostrarPosicion(location);
                    pintaPolilinea(location);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                    switch(status) {
                        case LocationProvider.AVAILABLE:
                            // TODO
                            break;
                        case LocationProvider.OUT_OF_SERVICE:
                            // TODO
                            break;
                        case LocationProvider.TEMPORARILY_UNAVAILABLE:
                            // TODO
                            break;
                    }
                }

                @Override
                public void onProviderEnabled(String provider) {
                    modificaEstadoGPS(true);
                }

                @Override
                public void onProviderDisabled(String provider) {
                    modificaEstadoGPS(false);
                }
            };

            locManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, 1000, 1, locListener); //Se actualiza al 1 segundo y cuando se mueva 1 metro
        }
        catch(NullPointerException ex) {
                Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_LONG);
        }
    }
    public void mostrarPosicion(android.location.Location loc) {
        try {
            if (loc != null) { //Si el GPS está desactivado loc = null
                double latitud = loc.getLatitude();
                double longitud = loc.getLongitude();

                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitud, longitud), zoom);
                map.animateCamera(cameraUpdate);


                map.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                    @Override
                    public void onCameraChange(CameraPosition cameraPosition) {
                        zoom = cameraPosition.zoom;
                    }
                });
            }
        }
        catch(NullPointerException ex) {
            Toast.makeText(getActivity(), ex.toString(), Toast.LENGTH_LONG);
        }
    }

    public void modificaEstadoGPS(boolean encendido){
        String mensaje = "";
        int icono;
        if (encendido) {
            mensaje = getResources().getString(R.string.gps_encendido);
            icono = R.drawable.gps_on;
            GPS_ACTIVO = true;
        }
        else {
            mensaje = getResources().getString(R.string.gps_apagado);
            icono = R.drawable.gps_off;
            GPS_ACTIVO = false;
        }
        txtGPS.setText(mensaje);
        imgGPS.setImageResource(icono);
    }

    private void iniciaEntrenamiento() {
        try {
            if (GPS_ACTIVO) {
                //chrono.start();
                Chrono_Start();
                ENTRENAMIENTO_EN_EJECUCION = true;


                PolylineOptions lineOptions = new PolylineOptions();

                lineOptions.width(4);
                lineOptions.color(Color.RED);


                lstPolilineas.add(lineOptions);

                btnPlay.setVisibility(View.GONE);
                btnStop.setVisibility(View.VISIBLE);
                btnPause.setVisibility(View.VISIBLE);
            }
        }
        catch (Exception er) {
            Log.e("MapFragment", er.toString());
        }
    }
    private void finalizaEntrenamiento() {
        try {
            //chrono.stop();
            //chrono.setBase(SystemClock.elapsedRealtime());

            Chrono_Pause();
            Chrono_Restart();

            ENTRENAMIENTO_EN_EJECUCION = false;
            btnPlay.setVisibility(View.VISIBLE);
            btnStop.setVisibility(View.GONE);
            btnPause.setVisibility(View.GONE);
        }
        catch (Exception er) {
            Log.e("MapFragment", er.toString());
        }
    }
    private void Entrenamiento_Pause(){
        try{
            Chrono_Pause();

            ENTRENAMIENTO_EN_EJECUCION = false;
            btnPlay.setVisibility(View.VISIBLE);
            btnStop.setVisibility(View.GONE);
            btnPause.setVisibility(View.GONE);
        }
        catch (Exception er) {
            Log.e("MapFragment", er.toString());
        }
    }

    /* FUNCIONES CHRONOMETER */
    private void showElapsedTime() {
        long elapsedMillis = SystemClock.elapsedRealtime() - chrono.getBase();

        //chrono.setText();
    }
    public void Chrono_Start() {

        int stoppedMilliseconds = 0;

        String chronoText = chrono.getText().toString();
        String array[] = chronoText.split(":");
        if (array.length == 2) {
            stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 1000
                    + Integer.parseInt(array[1]) * 1000;
        } else if (array.length == 3) {
            stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 60 * 1000
                    + Integer.parseInt(array[1]) * 60 * 1000
                    + Integer.parseInt(array[2]) * 1000;
        }

        chrono.setBase(SystemClock.elapsedRealtime() - stoppedMilliseconds);
        chrono.start();
    }

    public void Chrono_Pause() {
        chrono.stop();
        //showElapsedTime();
    }

    public void Chrono_Restart() {
        chrono.setBase(SystemClock.elapsedRealtime());
        //showElapsedTime();
    }

    public void pintaPolilinea (android.location.Location location) {
        try {
            if (ENTRENAMIENTO_EN_EJECUCION) {
                PolylineOptions lineOptions = lstPolilineas.get(lstPolilineas.size()-1);
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                lineOptions.add(ll);

                map.addPolyline(lineOptions);

                actualizaDistancia();
            }
        }catch(Exception er) {
            Log.e("MapFragment", er.toString());
        }
    }

    private void actualizaDistancia() {
        try {
            int distanciaEnMetros = 0;
            for(PolylineOptions poli: lstPolilineas) {
                List<LatLng> lstPuntos= poli.getPoints();

                if (lstPuntos.size() >= 2) {
                    for (int i=1; i<lstPuntos.size(); i++) {
                        double lat1 = lstPuntos.get(i-1).latitude;
                        double lon1 = lstPuntos.get(i-1).longitude;
                        double lat2 = lstPuntos.get(i).latitude;
                        double lon2 = lstPuntos.get(i).longitude;

                        distanciaEnMetros += calculateDistanceByHaversineFormula(lon1, lat1, lon2, lat2);
                    }
                }
            }

            //muestro metros
            txtDistancia.setText(distanciaEnMetros + "m.");
        }
        catch(Exception er) {
            Log.e("MapFragment", er.toString());
        }
    }

    private static int calculateDistanceByHaversineFormula (double lon1, double lat1,double lon2, double lat2) {

        double earthRadius = 6371; // km

        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);

        double dlon = (lon2 - lon1);
        double dlat = (lat2 - lat1);

        double sinlat = Math.sin(dlat / 2);
        double sinlon = Math.sin(dlon / 2);

        double a = (sinlat * sinlat) + Math.cos(lat1)*Math.cos(lat2)*(sinlon*sinlon);
        double c = 2 * Math.asin (Math.min(1.0, Math.sqrt(a)));

        double distanceInMeters = earthRadius * c * 1000;

        return (int)distanceInMeters;

    }
}