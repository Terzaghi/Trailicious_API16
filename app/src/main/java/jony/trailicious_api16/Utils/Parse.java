package jony.trailicious_api16.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Métodos para parsear
 */
public class Parse {

    public static String date2String(Date fecha) {
        String respuesta = "";

        try {
            respuesta = new SimpleDateFormat("dd-MM-yyyy").format(fecha);
        }
        catch(Exception er){}

        return respuesta;
    }

    public static Date string2Date(String fecha) {
        Date respuesta = null;

        try {
            respuesta = new SimpleDateFormat("dd-MM-yyyy").parse(fecha);
        }
        catch(Exception er){}

        return respuesta;

    }

    public static String segundos2String(long duration) {
        String respuesta = "";

        try {
            int hr = (int) (duration/3600);
            int rem = (int) (duration%3600);
            int mn = rem/60;
            int sec = rem%60;
            String hrStr = (hr<10 ? "0" : "")+hr;
            String mnStr = (mn<10 ? "0" : "")+mn;
            String secStr = (sec<10 ? "0" : "")+sec;

            respuesta = hrStr + ":" + mnStr + ":" + secStr;
        }
        catch(Exception er){}

        return respuesta;

    }

    public static String distancia2String(long distance) {
        String respuesta = "";

        try {

            if (distance < 1000) {
                //Metros
                respuesta = distance + " m.";
            }
            else {
                //Km.
                int km = (int) (distance/1000);
                int m = (int) (distance%1000);

                respuesta = km + "." + m + " Km.";
            }

        }
        catch(Exception er){}

        return respuesta;
    }
}
