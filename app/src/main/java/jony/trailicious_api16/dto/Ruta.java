package jony.trailicious_api16.dto;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;


public class Ruta implements Serializable {
    private int id_ruta;
    private float distancia;
    private String nombre_ruta;
    private ArrayList<LatLng> coordenadas;

    public Ruta(){}

    public Ruta(int id_ruta, String nombra_ruta, ArrayList<LatLng> coordenadas) {
        this.id_ruta = id_ruta;
        this.nombre_ruta = nombra_ruta;
        this.coordenadas = coordenadas;
    }

    public void setId(int id) { this.id_ruta = id; }
    public void setDistancia(float distancia) { this.distancia = distancia; }
    public void setNombre(String nombre_ruta) { this.nombre_ruta = nombre_ruta; }
    public void setCoordenadas(ArrayList<LatLng> coordenadas) { this.coordenadas = coordenadas; }

    public int getId() { return id_ruta; }
    public float getDistancia() { return distancia; }
    public String getNombre() { return nombre_ruta; }
    public ArrayList<LatLng> getCoordenadas() { return coordenadas; }
}
