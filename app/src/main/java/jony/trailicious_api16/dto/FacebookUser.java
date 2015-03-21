package jony.trailicious_api16.dto;

/*
    Variables que me interesa tener el usuario logeado
 */
public class FacebookUser {
    public String idFacebook;
    public String name;
    public String email;
    public String gender;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIdFacebook() {
        return idFacebook;
    }

    public void setIdFacebook(String idFacebook) {
        this.idFacebook = idFacebook;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImgURL() {
        //return "http://graph.facebook.com/" + idFacebook + "/picture?type=large";
        return "https://graph.facebook.com/" + idFacebook + "/picture?type=large";
    }
}
