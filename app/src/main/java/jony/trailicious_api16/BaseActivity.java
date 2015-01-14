package jony.trailicious_api16;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.plus.model.people.Person;

import jony.trailicious_api16.Adapters.NavigationDrawerAdapter;
import jony.trailicious_api16.Fragments.DummyFragment;
import jony.trailicious_api16.Fragments.MapFragment;

public class BaseActivity extends ActionBarActivity {

    private DrawerLayout drawerLayoutt;
    private ListView listView;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private Toolbar toolbar;

    Person persona; //Persona Logeada en la App

    //Instancio el Adaptador de prueba:
    private NavigationDrawerAdapter drawerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        //Obtengo los datos de la persona logeada
        persona = (Person) getIntent().getParcelableExtra("current_person") ;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setLogo(R.drawable.logo_blanco);

        drawerLayoutt = (DrawerLayout) findViewById(R.id.drawer_layout);
        listView = (ListView) findViewById(R.id.left_drawer);

        // Sombra que se sobrepone al contenido principal cuando el drawer está abierto
        drawerLayoutt.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        // Establezco el Adaptador del ListView del NavigationDrawer y creo el Listener Click
        drawerAdapter = new NavigationDrawerAdapter(this);
        listView.setAdapter(drawerAdapter);

        listView.setOnItemClickListener(new DrawerItemClickListener());

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayoutt, toolbar, R.string.app_name, R.string.app_name);
        drawerLayoutt.setDrawerListener(actionBarDrawerToggle);

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        if (savedInstanceState == null) {
            selectItem(0);
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

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
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
                //Mapa
                fragment = new MapFragment();

                //No le paso ningún argumento
                break;
            default:
                //Dummy Fragment
                fragment = new DummyFragment();
                args.putInt(DummyFragment.ARG_MENU_INDEX, position);
                fragment.setArguments(args);
        }

        //Cargo el fragmento
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        //Selecciono el elemento en el Navigation Drawer y le cierro
        listView.setItemChecked(position, true);
        drawerLayoutt.closeDrawer(listView);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

}

