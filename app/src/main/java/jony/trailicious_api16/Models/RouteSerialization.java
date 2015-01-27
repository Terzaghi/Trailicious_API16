package jony.trailicious_api16.Models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import jony.trailicious_api16.dto.Ruta;

/**
 * Guarda y lee rutas
 */
public class RouteSerialization {
    //TODO Guardar y leer todas las rutas de una carpeta del dispositivo

    public static void saveRoute(String nombreFichero, Ruta ruta) {

        try {
            File file = new File(nombreFichero);
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(ruta);

            oos.close();
        }
        catch (FileNotFoundException e) { e.printStackTrace(); }
        catch (IOException e) { e.printStackTrace(); }
    }

    public static Ruta getRuta(String nombreFichero) {
        Ruta ruta = null;

        try {
            File file = new File(nombreFichero);
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);

            ruta = (Ruta) ois.readObject();

            ois.close();
        }
        catch (FileNotFoundException e) { e.printStackTrace(); }
        catch (StreamCorruptedException e) { e.printStackTrace(); }
        catch (IOException e) { e.printStackTrace(); }
        catch (ClassNotFoundException e) { e.printStackTrace(); }

        return ruta;
    }
}
