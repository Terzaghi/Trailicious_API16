package jony.trailicious_api16;

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
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import jony.trailicious_api16.Adapters.NavigationDrawerAdapter;
import jony.trailicious_api16.Fragments.DummyFragment;
import jony.trailicious_api16.Fragments.MapFragment;
import jony.trailicious_api16.Fragments.ProfileFragment;
import jony.trailicious_api16.Utils.LoadProfileImage;

public class BaseActivity extends ActionBarActivity {
    private Menu mOptionsMenu;
    private boolean mostrarRevocar = false;

    private ActionBarDrawerToggle actionBarDrawerToggle;

    private Toolbar toolbar;

    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout

    ActionBarDrawerToggle mDrawerToggle;                  // Declaring Action Bar Drawer Toggle

    TrailiciousApplication application;
    GoogleApiClient googleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        /*String nombreUsuario = getIntent().getStringExtra("user_name");
        String urlFotoUsuario = getIntent().getStringExtra("user_photo");
        String emailUsuario = getIntent().getStringExtra("user_email");*/


        /* Assinging the toolbar object ot the view
        and setting the the Action bar to our toolbar
         */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setLogo(R.drawable.logo_blanco);

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View

        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size

        //TITLES = getApplicationContext().getResources().getStringArray(R.array.navigation_drawer_items_text);
        //ICONS = getResources().obtainTypedArray(R.array.navigation_drawer_items_images);

        //mAdapter = new NavigationDrawerAdapter(TITLES,ICONS,nombreUsuario,emailUsuario,urlFotoUsuario, getApplicationContext());
        //mAdapter = new NavigationDrawerAdapter(TITLES,ICONS, getApplicationContext());
        mAdapter = new NavigationDrawerAdapter(getApplicationContext());
        // And passing the titles,icons,header view name, header view email,
        // and header view profile picture

        mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView

        //Evento Click del Drawer
        final GestureDetector mGestureDetector = new GestureDetector(BaseActivity.this, new GestureDetector.SimpleOnGestureListener() {

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
                    selectItem(recyclerView.getChildPosition(child));
                    return true;

                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

            }
        });

        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager

        mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager


        Drawer = (DrawerLayout) findViewById(R.id.drawer_layout);        // Drawer object Assigned to the view

        // Sombra que se sobrepone al contenido principal cuando el drawer está abierto
        Drawer.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        //Drawer.setDrawerListener(mDrawerToggle);

        mDrawerToggle = new ActionBarDrawerToggle(this,Drawer,toolbar,R.string.openDrawer,R.string.closeDrawer){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }



        }; // Drawer Toggle Object Made
        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);



        if (savedInstanceState == null) {
            selectItem(1); //Mapa
        }

        /*final ApiClientAsyncTask asyncTask = new ApiClientAsyncTask(getApplicationContext()) {
            @Override
            protected Object doInBackgroundConnected(Object[] params) {
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
        asyncTask.execute();*/
        application = (TrailiciousApplication) getApplication();
        googleApiClient = application.getGoogleApiClient();

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
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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

    /**
     * Selección de un item en la lista del navigation drawer
     * @param position posicion pulsada
     */
    private void selectItem(int position) {

        Fragment fragment;
        Bundle args = new Bundle();


        switch (position) {
            case 0:
                //Perfil
                fragment = new ProfileFragment();
                break;
            case 1:
                //Mapa
                fragment = new MapFragment();
                //fragment = new MapFragmentWithServices();
                //No le paso ningún argumento
                break;
            case 2:
                //Entrenamientos
                fragment = new DummyFragment();
                break;
            case 3:
                //Rutas
                fragment = new DummyFragment();
                break;
            case 4:
                //Amigos
                fragment = new DummyFragment();
                break;
            case 5:
                //Eventos
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

        //Cargo el fragmento
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }
    private void actualizarUsuario(String personName, String personEmail, String personPhotoUrl) {
        try {
            //Actualizo los datos del usuario
            View hijo = mLayoutManager.getChildAt(0);
            TextView txtNombre = (TextView) hijo.findViewById(R.id.drawer_nombre_usuario);
            TextView txtEmail = (TextView) hijo.findViewById(R.id.drawer_email_usuario);
            ImageView imgIcon = (ImageView) hijo.findViewById(R.id.drawer_icon_usuario);


            int PROFILE_PIC_SIZE = 400; // Tamaño de la imagen de perfil del usuairo en pixeles
            personPhotoUrl = personPhotoUrl.substring(0,
                    personPhotoUrl.length() - 2)
                    + PROFILE_PIC_SIZE;

            new LoadProfileImage(imgIcon).execute(personPhotoUrl);

            txtNombre.setText(personName);
            txtEmail.setText(personEmail);
        }
        catch(Exception er){
            Log.d("actualizarUsuario", er.getMessage());
        }
    }
    private void revocarAcceso() {
        try {
            if (googleApiClient != null && googleApiClient.isConnected()) {
                Plus.AccountApi.clearDefaultAccount(googleApiClient);
                Plus.AccountApi.revokeAccessAndDisconnect(googleApiClient)
                        .setResultCallback(new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status arg0) {
                                googleApiClient.connect();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                            }

                        });
            }
        }
        catch(Exception er){
            Log.d("revocarAcceso", er.getMessage());
        }
    }
}