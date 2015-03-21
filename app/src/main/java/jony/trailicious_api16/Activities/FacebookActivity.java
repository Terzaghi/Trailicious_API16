package jony.trailicious_api16.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.Arrays;
import java.util.List;

import jony.trailicious_api16.R;
import jony.trailicious_api16.TrailiciousApplication;
import jony.trailicious_api16.Utils.Format;
import jony.trailicious_api16.dto.FacebookUser;
import jony.trailicious_api16.ParseTables.User;

public class FacebookActivity extends FragmentActivity {

    private static final String TAG = FacebookActivity.class.getSimpleName();

    private LoginButton loginBtn;
    //private TextView username;
    private UiLifecycleHelper uiHelper;

    private ProgressBar progressLogin;
    private TextView txtLoginCargando;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        uiHelper = new UiLifecycleHelper(this, statusCallback);
        uiHelper.onCreate(savedInstanceState);

        setContentView(R.layout.activity_facebook);

        //username = (TextView) findViewById(R.id.username);

        progressLogin = (ProgressBar) findViewById(R.id.progressLogin);
        txtLoginCargando = (TextView) findViewById(R.id.txtLoginCargando);

        loginBtn = (LoginButton) findViewById(R.id.fb_login_button);
        loginBtn.setReadPermissions(Arrays.asList("email"));
        loginBtn.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
                if (user != null) {
                    /*
                        TODO Intent a NewMainActivity
                        Comprobar si el usuario existe en la base de datos Parse y si no crearle
                        Guardar en un fichero todos los datos que queramos del usuario ?? De momento lo guardo en una variable en TrailiciousApplication
                     */

                    //Guardo el usuario logueado
                    FacebookUser UsuarioLogueado = new FacebookUser();
                    UsuarioLogueado.setIdFacebook(user.getId());
                    UsuarioLogueado.setName(user.getName());
                    UsuarioLogueado.setEmail(user.asMap().get("email").toString());
                    UsuarioLogueado.setGender(user.asMap().get("gender").toString());

                    guardaUsuario(UsuarioLogueado);

                    TrailiciousApplication.setUsuarioLogueado(UsuarioLogueado);

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);

                    //username.setText("You are currently logged in as " + user.getName());
                } else {
                    //username.setText("You are not logged in.");
                    muestraBotonLogin();
                }
            }
        });
    }

    private void guardaUsuario(final FacebookUser usuarioLogueado) {
        try {

            ParseObject.registerSubclass(User.class);

            ParseQuery<User> query = ParseQuery.getQuery(User.class);

            query.findInBackground(new FindCallback<User>() {
                @Override
                public void done(List<User> lstUsuarios, ParseException e) {
                    if (lstUsuarios != null) {
                        //Busco el usuario por la Id de facebook

                        boolean encontrado = false;
                        for (User usuario : lstUsuarios) {
                            String usuarioID = usuario.getFacebookID();
                            String usuarioLogueadoID = usuarioLogueado.getIdFacebook();
                            if (usuarioID.equals(usuarioLogueadoID)) {
                                encontrado = true;

                                //UPDATE

                                //Compruebo si hay algún campo modificado
                                boolean update = false;
                                if (!usuarioLogueado.getName().equals(usuario.getUsername())) {
                                    usuario.put("nombre", usuario.getUsername());
                                    update = true;
                                }

                                if (!usuarioLogueado.getEmail().equals(usuario.getEmail())) {
                                    usuario.put("email", usuario.getEmail());
                                    update = true;
                                }

                                if (!usuarioLogueado.getImgURL().equals(usuario.getImageURL())) {
                                    usuario.put("imageURL", usuario.getImageURL());
                                    update = true;
                                }

                                if (update) {
                                    usuario.saveInBackground(new SaveCallback() {
                                        public void done(ParseException e) {
                                            if (e == null) {
                                                // Saved successfully.
                                                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                                            } else {
                                                // The save failed.
                                                Toast.makeText(getApplicationContext(), "Failed to Save", Toast.LENGTH_SHORT).show();
                                                Log.d(TAG, "User update error: " + e);
                                            }
                                        }
                                    });
                                }

                                //Finalizo el bucle
                                break;
                            }
                        }

                        if (!encontrado) {
                            //CREATE
                            User nuevoUser = Format.Facebook2User(usuarioLogueado);

                            if (nuevoUser != null)
                                nuevoUser.saveInBackground();
                        }
                    }
                }
            });


            /*query.getInBackground(usuarioLogueado.getIdFacebook(), new GetCallback<User>() {
                public void done(User usuario, ParseException e) {
                    if (e == null) {

                        //UPDATE

                        //Compruebo si hay algún campo modificado
                        boolean update = false;
                        if (!usuarioLogueado.getName().equals(usuario.getUsername())) {
                            usuario.put("nombre", usuario.getUsername());
                            update = true;
                        }

                        if (!usuarioLogueado.getEmail().equals(usuario.getEmail())) {
                            usuario.put("email", usuario.getEmail());
                            update = true;
                        }

                        if (!usuarioLogueado.getImgURL().equals(usuario.getImageURL())) {
                            usuario.put("imageURL", usuario.getImageURL());
                            update = true;
                        }

                        if (update) {
                            usuario.saveInBackground(new SaveCallback() {
                                public void done(ParseException e) {
                                    if (e == null) {
                                        // Saved successfully.
                                        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // The save failed.
                                        Toast.makeText(getApplicationContext(), "Failed to Save", Toast.LENGTH_SHORT).show();
                                        Log.d(TAG, "User update error: " + e);
                                    }
                                }
                            });
                        }
                    }
                    else {

                        //CREATE
                        User nuevoUser = Format.Facebook2User(usuarioLogueado);

                        nuevoUser.put("objectId", usuarioLogueado.getIdFacebook());


                        nuevoUser.saveInBackground();
                    }
                }
            });*/
        }
        catch(Exception er) {
            Log.e(TAG, er.toString());
        }
    }

    private Session.StatusCallback statusCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state,
                         Exception exception) {
            if (state.isOpened()) {
                Log.d("MainActivity", "Facebook session opened.");
            } else if (state.isClosed()) {
                Log.d("MainActivity", "Facebook session closed.");
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        uiHelper.onSaveInstanceState(savedState);
    }

    private void muestraCargando() {
        try {
            if (loginBtn != null)
                loginBtn.setVisibility(View.GONE);

            if (progressLogin != null)
                progressLogin.setVisibility(View.VISIBLE);

            if (txtLoginCargando != null)
                txtLoginCargando.setVisibility(View.VISIBLE);
        }
        catch(Exception er) {
            Log.e(TAG, er.toString());
        }
    }
    private void muestraBotonLogin() {
        try {
            if (loginBtn != null)
                loginBtn.setVisibility(View.VISIBLE);

            if (progressLogin != null)
                progressLogin.setVisibility(View.GONE);

            if (txtLoginCargando != null)
                txtLoginCargando.setVisibility(View.GONE);
        }
        catch(Exception er) {
            Log.e(TAG, er.toString());
        }
    }
}
