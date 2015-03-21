package jony.trailicious_api16.Models;

import android.content.Context;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

import jony.trailicious_api16.ParseTables.Historico;
import jony.trailicious_api16.ParseTables.User;

/**
 * Acceso a datos para Parse
 */
public class ParseDA {
    private static final String TAG = ParseDA.class.getSimpleName();

    public ParseDA(Context context) {

    }

    public void setUser(User usuario){
        try {
            //Registro la clase
            ParseObject.registerSubclass(User.class);

            //Si el usuario no tiene conexion a internet, lo guardará cuando vuelva a estar online
            usuario.saveEventually();

        }
        catch(Exception er) {
            Log.d(TAG, er.toString());
        }
    }
    public void setEntrenamiento(Historico entrenamiento) {
        try {
            //Registro la clase
            ParseObject.registerSubclass(Historico.class);

            //Si el usuario no tiene conexion a internet, lo guardará cuando vuelva a estar online
            entrenamiento.saveEventually();
        }
        catch(Exception er) {
            Log.d(TAG, er.toString());
        }
    }
    public List<Historico> getEntrenamientos() {
        List<Historico> lstEntrenamientos = null;
        try {
            ParseQuery<Historico> query = ParseQuery.getQuery(Historico.class);
        }
        catch(Exception er) {

        }

        return lstEntrenamientos;
    }

    /******* ******************************************
     *************** EJEMPLOS PARSE *******************
     **************************************************/
    public void ejemploGuardar(){
        ParseObject testObject = new ParseObject("TablaParse");
        testObject.put("columna", "valor");
        testObject.saveInBackground();

        //Si quiero hacer algo cuando se guarde o no:
        /*
        testObject.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Saved successfully.
                    Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                } else {
                    // The save failed.
                    Toast.makeText(getApplicationContext(), "Failed to Save", Toast.LENGTH_SHORT).show();
                    Log.d(getClass().getSimpleName(), "User update error: " + e);
                }
            }
        });*/
    }

    //Update es igual que Create, pero primero obtiene el entrenamiento por su ID y le actualiza
    public void ejemploUpdateEntrenamiento(final Historico entrenamiento) {

        //Reemplazar ParseObject con la clase que extiende de Parse (Historico, User...)
        ParseQuery<Historico> query = ParseQuery.getQuery("Entrenamientos");

        // Retrieve the object by id
        query.getInBackground(entrenamiento.getObjectId(), new GetCallback<Historico>() {
            public void done(Historico resultado, ParseException e) {
                if (e == null) {
                    // Now let's update it with some new data.
                    resultado.put("Duration", entrenamiento.getDuration());
                    resultado.put("Distance", entrenamiento.getDistance());
                    resultado.saveInBackground(new SaveCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                // Saved successfully.
                                //Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                            } else {
                                // The save failed.
                                //Toast.makeText(getApplicationContext(), "Failed to Save", Toast.LENGTH_SHORT).show();
                                Log.d(getClass().getSimpleName(), "User update error: " + e);
                            }
                        }
                    });
                }
            }
        });
    }

    public void ejemploGetData() {

        //Reemplazar ParseObject con la clase que extiende de Parse (Historico, User...)
        ParseQuery<ParseObject> query = ParseQuery.getQuery("TablaParse");

        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> listaResultados, ParseException e) {
                if (e == null) {
                    for (ParseObject objeto : listaResultados) {
                    }
                } else {
                    Log.d(getClass().getSimpleName(), "Error: " + e.getMessage());
                }
            }
        });
    }
}
