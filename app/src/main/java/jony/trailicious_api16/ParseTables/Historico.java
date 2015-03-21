package jony.trailicious_api16.ParseTables;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

/**
 * Guarda los datos relacionados a un entrenamiento en la BD de Parse
 * Datos a guardar:
 * - Id del entrenamiento
 * - Id de la ruta seguida
 * - Fecha del entrenamiento
 * - Duración del entrenamiento (en ms)
 * - Distancia de la ruta (en m)
 */

@ParseClassName("Entrenamientos")
public class Historico  extends ParseObject{



    public long getDuration(){
        return getLong("Duration");
    }
    public void setDuration(long duration) {
        put("Duration", duration);
    }

    public long getDistance() {
        return getLong("Distance");
    }
    public void setDistance(long distance) {
        put("Distance", distance);
    }

    public Date getFecha(){
        return getDate("Fecha");
    }
    public void setFecha(Date fecha) {
        put("Fecha", fecha);
    }

    public String getUserFacebookID() {return getString("userFacebookID");}
    public void setUserFacebookID(String facebookID) {put("userFacebookID", facebookID);}
}
