package jony.trailicious_api16;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public abstract class GpsReceiver extends BroadcastReceiver {

    private static final String TAG = GpsReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "GPS State received");

        boolean gpsEnable = intent.getExtras().getBoolean("GPS_ENABLED");
        gpsState(gpsEnable);
    }
    protected abstract void gpsState(boolean gpsEnabled);
}
