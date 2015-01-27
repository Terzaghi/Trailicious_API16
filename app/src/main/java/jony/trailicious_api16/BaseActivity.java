package jony.trailicious_api16;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.plus.model.people.Person;

import jony.trailicious_api16.Adapters.NavigationDrawerAdapter;
import jony.trailicious_api16.Fragments.DummyFragment;
import jony.trailicious_api16.Fragments.MapFragment;
import jony.trailicious_api16.Fragments.ProfileFragment;

public class BaseActivity extends ActionBarActivity {

    private DrawerLayout drawerLayoutt;
    private ListView listView;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private Toolbar toolbar;

    Person persona; //Persona Logeada en la App


    //First We Declare Titles And Icons For Our Navigation Drawer List View
    //This Icons And Titles Are holded in an Array as you can see

    //String TITLES[] = getApplicationContext().getResources().getStringArray(R.array.navigation_drawer_items);// {"Home","Events","Mail","Shop","Travel"};
    String TITLES[];// = {"Home","Events","Mail","Shop","Travel"};
    int ICONS[];// = {R.drawable.icon_launcher,R.drawable.icon_launcher,R.drawable.icon_launcher,R.drawable.icon_launcher,R.drawable.icon_launcher};

    //Similarly we Create a String Resource for the name and email in the drawer_header view
    //And we also create a int resource for profile picture in the drawer_header view

    String NAME = "";
    String EMAIL = "";
    //int PROFILE = R.drawable.icon_launcher;

    RecyclerView mRecyclerView;                           // Declaring RecyclerView
    RecyclerView.Adapter mAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;                                  // Declaring DrawerLayout

    ActionBarDrawerToggle mDrawerToggle;                  // Declaring Action Bar Drawer Toggle


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        persona = (Person) getIntent().getParcelableExtra("current_person") ;
        NAME = persona.getDisplayName();
        //EMAIL = persona.getEmails().get(0).getValue();


        //URL de la imagen:
        String url = persona.getImage().getUrl();

        /* Assinging the toolbar object ot the view
        and setting the the Action bar to our toolbar
         */
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setLogo(R.drawable.logo_blanco);

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View

        mRecyclerView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size

        TITLES = getApplicationContext().getResources().getStringArray(R.array.navigation_drawer_items_text);
        ICONS = getApplicationContext().getResources().getIntArray(R.array.navigation_drawer_items_images);

        mAdapter = new NavigationDrawerAdapter(TITLES,ICONS,NAME,EMAIL,url, getApplicationContext());       // Creating the Adapter of MyAdapter class(which we are going to see in a bit)
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



                if(child!=null && mGestureDetector.onTouchEvent(motionEvent)){
                    Drawer.closeDrawers();
                    //Toast.makeText(BaseActivity.this, "The Item Clicked is: " + recyclerView.getChildPosition(child), Toast.LENGTH_SHORT).show();
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
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
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

        //Cargo el fragmento
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

    }
}

