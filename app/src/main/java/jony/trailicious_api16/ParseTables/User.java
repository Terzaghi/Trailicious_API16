package jony.trailicious_api16.ParseTables;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Guarda los datos relacionados a un usuario en la BD de Parse
 * Datos a guardar:
 * - Id del entrenamiento
 * - Id de la ruta seguida
 * - Fecha del entrenamiento
 * - Duración del entrenamiento (en ms)
 * - Distancia de la ruta (en m)
 */

@ParseClassName("UsuariosFacebook")
public class User extends ParseObject{
    public String getObjectId() { return getString("objectId");}
    public String getUsername() { return getString("nombre");}
    public String getEmail() {return getString("email");}
    public String getImageURL() {return getString("imageURL");}
    public String getFacebookID() {return getString("facebookID");}

    public void setObjectID(String ObjectID) {put("objectId",ObjectID);}
    public void setUsername(String Username) {put("nombre",Username);}
    public void setEmail(String Email) {put("email",Email);}
    public void setImageURL(String ImageURL) {put("imageURL",ImageURL);}
    public void setFacebookID(String FacebookID) {put("facebookID",FacebookID);}
}
