package jony.trailicious_api16;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.parse.Parse;

import jony.trailicious_api16.Utils.ConstantsUtils;
import jony.trailicious_api16.dto.FacebookUser;

public class TrailiciousApplication extends Application {
    private static final String TAG = TrailiciousApplication.class.getSimpleName();

    //Variable del Usuario Logueado
    private static FacebookUser UsuarioLogueado;

    // Bandera para definir si el servicio LocationService esta en ejecucion o no
    private boolean serviceRunningFlag;

    private GoogleApiClient googleApiClient;

    //Variables del entrenamiento
    public static boolean ENTRENAMIENTO_EN_EJECUCION = false;
    public static boolean RECEIVER_REGISTRADO = false;

    public static FacebookUser getUsuarioLogueado(){
        return UsuarioLogueado;
    }
    public static void setUsuarioLogueado(FacebookUser Usuario) {
        UsuarioLogueado = Usuario;
    }

    public static boolean isRECEIVER_REGISTRADO() {
        return RECEIVER_REGISTRADO;
    }

    public static void setRECEIVER_REGISTRADO(boolean REVEIVER_REGISTRADO) {
        TrailiciousApplication.RECEIVER_REGISTRADO = REVEIVER_REGISTRADO;
    }



    public static boolean isENTRENAMIENTO_EN_EJECUCION() {
        return ENTRENAMIENTO_EN_EJECUCION;
    }

    public static void setENTRENAMIENTO_EN_EJECUCION(boolean EN_EJECUCION) {
        ENTRENAMIENTO_EN_EJECUCION = EN_EJECUCION;
    }

    public GoogleApiClient getGoogleApiClient(){
        return googleApiClient;
    }
    public void setGoogleApiClient(GoogleApiClient googleApiClient) {
        this.googleApiClient = googleApiClient;
    }

    public boolean isServiceRunningFlag() {
        return serviceRunningFlag;
    }
    public void setServiceRunningFlag(boolean serviceRunningFlag) {
        this.serviceRunningFlag = serviceRunningFlag;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //Inicio el servicio
        startService(new Intent(this, LocationService.class));

        //Registro Parse
        Parse.initialize(this, ConstantsUtils.PARSE_APP_ID, ConstantsUtils.PARSE_CLIENT_KEY);

        Log.d(TAG,"Servicio iniciado");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        //Cierro el servicio
        stopService(new Intent(this, LocationService.class));

        Log.d(TAG, "Servicio parado");
    }
}
