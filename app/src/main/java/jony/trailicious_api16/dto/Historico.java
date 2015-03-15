package jony.trailicious_api16.dto;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

/**
 * Guarda los datos relacionados a un entrenamiento
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


    /*private int idEntrenamiento;
    private int idRuta;
    private Date fecha;
    private String duracion;
    private String distancia;*/

/*    public int getIdEntrenamiento() {
        return idEntrenamiento;
    }

    public void setIdEntrenamiento(int idEntrenamiento) {
        this.idEntrenamiento = idEntrenamiento;
    }

    public int getIdRuta() {
        return idRuta;
    }

    public void setIdRuta(int idRuta) {
        this.idRuta = idRuta;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getDistancia() {
        return distancia;
    }

    public void setDistancia(String distancia) {
        this.distancia = distancia;
    }*/
}
