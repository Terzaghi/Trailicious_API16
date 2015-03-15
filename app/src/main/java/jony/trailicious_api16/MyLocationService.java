package jony.trailicious_api16;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import jony.trailicious_api16.Utils.ConstantsUtils;

public class MyLocationService extends Service implements LocationListener {

    private final String TAG = MyLocationService.class.getSimpleName();

    private boolean xmlSuccessful = false;
    private boolean locationTimeExpired = false;

    private LocationManager locManager;
    private double latitude;
    private double longitude;
    private double accuracy;

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged");

        latitude = location.getLatitude();
        longitude = location.getLongitude();

        accuracy = location.getAccuracy();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d(TAG, "onProviderDisabled");
        Toast.makeText(
                getApplicationContext(),
                "Attempted to ping your location, and GPS was disabled.",
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d(TAG, "onProviderEnabled");
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, ConstantsUtils.LOCATION_MIN_TIME, ConstantsUtils.LOCATION_MIN_DISTANCE, this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(TAG, "onStatusChanged");

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
    public void onCreate() {
        Log.d(TAG, "onCreate");
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");

        return null;
    }

    @Override
    public void onStart(Intent intent, int startid) {
        Log.d(TAG, "onStart");

        locManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, ConstantsUtils.LOCATION_MIN_TIME, ConstantsUtils.LOCATION_MIN_DISTANCE, this);
        //locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000,300f, this);

        Log.d(TAG, locManager.toString());

        new SubmitLocationTask(MyLocationService.this).execute();
    }

    /*private void locationTimer() {

        new Handler().postDelayed(new Runnable() {
            // @Override
            @Override
            public void run() {
                locationTimeExpired = true;
            }
        }, 12000);
    }*/

    private class SubmitLocationTask extends AsyncTask<String, Void, Boolean> {

        /**
         * application context.
         */
        private Context context;

        private Service service;

        public SubmitLocationTask(Service service) {
            this.service = service;
            context = service;
        }

        @Override
        protected void onPreExecute() {
            //locationTimer(); // Start 12 second timer
        }

        @Override
        protected void onPostExecute(final Boolean success) {
/*
            if (success && xmlSuccessful) {
                locManager.removeUpdates(MyLocationService.this);
                onDestroy();
            } else {
                if (!GlobalsUtil.DEBUG_ERROR_MSG.equals(""))
                    Toast.makeText(getBaseContext(),
                            GlobalsUtil.DEBUG_ERROR_MSG, Toast.LENGTH_SHORT)
                            .show();
                GlobalsUtil.DEBUG_ERROR_MSG = "";
            }*/
        }

        @Override
        protected Boolean doInBackground(final String... args) {
            /*try {

                DateFormat df = null;
                df = new SimpleDateFormat("M/d/yy h:mm a");
                Date todaysDate = new Date();// get current date time with
                // Date()
                String currentDateTime = df.format(todaysDate);

                while ((accuracy > 100f || accuracy == 0.0)
                        && !locationTimeExpired) {
                    // We just want it to sit here and wait.
                }

                return xmlSuccessful = SendToServerUtil.submitGPSPing(
                        0, longitude,
                        latitude, accuracy, currentDateTime);
            } catch (Exception e) {*/

                return false;
            //}
        }
    }
}