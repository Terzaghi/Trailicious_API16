package jony.trailicious_api16.Activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import jony.trailicious_api16.Adapters.NavigationDrawerAdapter;
import jony.trailicious_api16.Fragments.DummyFragment;
import jony.trailicious_api16.Fragments.HistoryFragment;
import jony.trailicious_api16.Fragments.MapFragmentWithService;
import jony.trailicious_api16.R;
import jony.trailicious_api16.TrailiciousApplication;
import jony.trailicious_api16.Utils.LoadProfileImage;
import jony.trailicious_api16.dto.FacebookUser;

public class MainActivity extends ActionBarActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    // Elementos del Menú
    private Menu mOptionsMenu;
    private boolean mostrarRevocar = false;

    private boolean headerIniciado = false; //Especifica si los datos del usuario ya han sido pintados en el Header del Drawer

    // Elementos del ActionBar
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar toolbar;

    // Elementos del NavigationDrawer
    DrawerLayout Drawer;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        //Si el usuario pulsa back desde MainActivity, no vuelvo a FacebookActivity si no que salgo de la app

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iniciaToolbar();
        iniciaNavigationDrawer();

        if (savedInstanceState == null) {
            drawerSelect(1); //Mapa
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_base, menu);

        mOptionsMenu = menu;

        if (mostrarRevocar) {
            MenuItem itemRevocar = (MenuItem) menu.findItem(R.id.action_desconectar);
            if (itemRevocar != null)
                itemRevocar.setVisible(true);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_desconectar) {
            revocarAcceso();
            return true;
        }
        else if (id == R.id.action_settings) {
            //TODO: abrir actividad ajustes
        }

        return super.onOptionsItemSelected(item);
    }

    /* MÉTODOS PRIVADOS */

    /**
     * Asigno el objeto Toolbar a la vista y configuro el ActionBar a nuestro Toolbar
     */
    private void iniciaToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setLogo(R.drawable.logo_blanco);
    }

    /**
     * Inicio los elementos del NavigationDrawer y asigno los eventos asociados a el
     */
    private void iniciaNavigationDrawer() {
        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new NavigationDrawerAdapter(getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);

        //Evento Click del Drawer
        final GestureDetector mGestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {

            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });

        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(),motionEvent.getY());

                if(child != null && mGestureDetector.onTouchEvent(motionEvent)){
                    Drawer.closeDrawers();
                    drawerSelect(recyclerView.getChildPosition(child));
                    return true;

                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

            }
        });

        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager
        int elementos = mLayoutManager.getChildCount();
        mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager

        Drawer = (DrawerLayout) findViewById(R.id.drawer_layout);        // Drawer object Assigned to the view

        // Sombra que se sobrepone al contenido principal cuando el drawer está abierto
        Drawer.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        mDrawerToggle = new ActionBarDrawerToggle(this,Drawer,toolbar,R.string.openDrawer,R.string.closeDrawer){


            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                if (!headerIniciado) {
                    FacebookUser Usuario = TrailiciousApplication.getUsuarioLogueado();
                    if (Usuario != null) {
                        actualizarUsuario(Usuario.getName(), Usuario.getEmail(), Usuario.getImgURL());
                    }
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }



        };
        // Drawer Toggle Object Made
        Drawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //TODO
        if (!TrailiciousApplication.isENTRENAMIENTO_EN_EJECUCION())
            getApplication().onTerminate();
    }

    /**
     * Logea al usuario en un nuevo hilo
     */
    /*private void logeaUsuario() {
        final ApiClientAsyncTask asyncTask = new ApiClientAsyncTask(getApplicationContext()) {
            @Override
            protected Object doInBackgroundConnected(Object[] params) {

                //Usuario conectado: Obtengo sus datos y configuro la aplicación para el
                googleApiClient = this.getGoogleApiClient();

                if (Plus.PeopleApi.getCurrentPerson(googleApiClient) != null) {
                    Person currentPerson = Plus.PeopleApi.getCurrentPerson(googleApiClient);

                    String personName = currentPerson.getDisplayName();
                    String personPhotoUrl = currentPerson.getImage().getUrl();
                    String personEmail = Plus.AccountApi.getAccountName(googleApiClient);

                    actualizarUsuario(personName, personEmail, personPhotoUrl);


                    mostrarRevocar = true;
                    if (mOptionsMenu != null) {
                        MenuItem itemRevocar = (MenuItem) mOptionsMenu.findItem(R.id.action_desconectar);
                        if (itemRevocar != null)
                            itemRevocar.setVisible(true);
                    }
                }
                return null;
            }
        };
        asyncTask.execute();
    }*/

    /**
     * Actualizo los datos del usuario
     * @param personName
     * @param personEmail
     * @param personPhotoUrl
     */
    private void actualizarUsuario(String personName, String personEmail, String personPhotoUrl) {
        try {
            if (mLayoutManager != null) {

                View hijo = mLayoutManager.getChildAt(0);

                TextView txtNombre = (TextView) hijo.findViewById(R.id.drawer_nombre_usuario);
                TextView txtEmail = (TextView) hijo.findViewById(R.id.drawer_email_usuario);
                ImageView imgIcon = (ImageView) hijo.findViewById(R.id.drawer_icon_usuario);

                if (imgIcon != null && personPhotoUrl != null) {
                    /*int PROFILE_PIC_SIZE = 400; // Tamaño de la imagen de perfil del usuairo en pixeles
                    personPhotoUrl = personPhotoUrl.substring(0,
                            personPhotoUrl.length() - 2)
                            + PROFILE_PIC_SIZE;*/

                    new LoadProfileImage(imgIcon).execute(personPhotoUrl);
                }
                if (txtNombre != null && personName != null)
                    txtNombre.setText(personName);

                if (txtEmail != null && personEmail != null)
                    txtEmail.setText(personEmail);


                ProgressBar pb = (ProgressBar) hijo.findViewById(R.id.progress_header);

                txtNombre.setVisibility(View.VISIBLE);
                txtEmail.setVisibility(View.VISIBLE);
                imgIcon.setVisibility(View.VISIBLE);
                pb.setVisibility(View.GONE);


                headerIniciado = true;
            }
        }
        catch(Exception er){
            Log.e("actualizarUsuario", er.toString());
        }
    }

    /**
     * Selección de un item en la lista del navigation drawer
     * @param position posicion pulsada
     */
    private void drawerSelect(int position) {

        Fragment fragment = null;
        Bundle args = new Bundle();

        switch (position) {
            case 0:
                //Perfil
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                break;
            case 1:
                //Entrenamiento
                //fragment = new MapFragment();
                fragment = new MapFragmentWithService();
                //No le paso ningún argumento
                break;
            case 2:
                //Historial
                fragment = new HistoryFragment();
                break;
            case 3:
                //Rutas
                fragment = new DummyFragment();
                break;
            default:
                //Dummy Fragment
                fragment = new DummyFragment();
                //args.putInt(DummyFragment.ARG_MENU_INDEX, position);
                //fragment.setArguments(args);
        }

        //Cambio los colores del fondo de los items y los textos. El perfil no le cuento
        for (int i = 1; i < mLayoutManager.getChildCount(); i++) {
            if (position == i) {
                View hijo = mLayoutManager.getChildAt(i);
                hijo.setBackgroundColor(getResources().getColor(R.color.trailicious_theme_accent));

                TextView txt = (TextView) hijo.findViewById(R.id.rowText);
                if (txt != null) {
                    txt.setTextColor(getResources().getColor(R.color.trailicious_text_icons));
                }
            }
            else {
                View hijo = mLayoutManager.getChildAt(i);
                hijo.setBackgroundColor(getResources().getColor(R.color.trailicious_list_background));

                TextView txt = (TextView) hijo.findViewById(R.id.rowText);
                if (txt != null) {
                    txt.setTextColor(getResources().getColor(R.color.trailicious_text));
                }
            }
        }

        //Cargo el fragmento. Si fragment = null es que no tiene que cargar un fragmento, si no que cambia de actividad
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        }
    }

    /**
     * Revoca el acceso del usuario
     */
    private void revocarAcceso() {
        //TODO Cambiar el revocar acceso por el de Facebook
//        try {
//            if (googleApiClient != null && googleApiClient.isConnected()) {
//                Plus.AccountApi.clearDefaultAccount(googleApiClient);
//                Plus.AccountApi.revokeAccessAndDisconnect(googleApiClient)
//                        .setResultCallback(new ResultCallback<Status>() {
//                            @Override
//                            public void onResult(Status arg0) {
//                                googleApiClient.connect();
//                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                                startActivity(intent);
//                            }
//
//                        });
//            }
//        }
//        catch(Exception er){
//            Log.d("revocarAcceso", er.getMessage());
//        }
    }

}
