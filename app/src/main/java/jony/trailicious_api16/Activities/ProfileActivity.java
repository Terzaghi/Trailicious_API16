package jony.trailicious_api16.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import jony.trailicious_api16.R;
import jony.trailicious_api16.TrailiciousApplication;
import jony.trailicious_api16.Utils.LoadProfileImage;
import jony.trailicious_api16.dto.FacebookUser;

public class ProfileActivity extends ActionBarActivity {
    private final String TAG = ProfileActivity.class.getSimpleName();

    TextView txtNombre, txtEmail;
    ImageView imgPerfil;//, imgBack;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);

        //Añade la flecha de retroceso al ActionBar
        iniciaToolbar();

        txtNombre = (TextView) findViewById(R.id.profile_nombre_usuario);
        txtEmail = (TextView) findViewById(R.id.profile_email_usuario);
        imgPerfil = (ImageView) findViewById(R.id.profile_icon_usuario);
        /*imgBack = (ImageView) findViewById(R.id.profile_img_back);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volver();
            }
        });*/


        pintaUsuario();

    }

    private void iniciaToolbar() {
        try {
            toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            this.setTitle("");
        }
        catch(Exception er) {
            Log.e(TAG, er.toString());
        }
    }

    /*private void volver() {
        super.onBackPressed();
    }*/

    private void pintaUsuario() {
        try {

            FacebookUser usuario = TrailiciousApplication.getUsuarioLogueado();

            if (usuario != null) {
                String nombre = usuario.getName();
                String email = usuario.getEmail();
                String urlImagen = usuario.getImgURL();

                /*int PROFILE_PIC_SIZE = 400; // Tamaño de la imagen de perfil del usuairo en pixeles
                urlImagen = urlImagen.substring(0,
                        urlImagen.length() - 2)
                        + PROFILE_PIC_SIZE;*/

                new LoadProfileImage(imgPerfil).execute(urlImagen);

                txtNombre.setText(nombre);
                txtEmail.setText(email);

                ProgressBar pb = (ProgressBar) findViewById(R.id.progress_profile);

                txtNombre.setVisibility(View.VISIBLE);
                txtEmail.setVisibility(View.VISIBLE);
                imgPerfil.setVisibility(View.VISIBLE);
                pb.setVisibility(View.GONE);
            }
        }
        catch(Exception er){
            Log.d("pintaUsuario", er.getMessage());
        }
    }
}
