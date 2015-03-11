package com.anaroc.anaro.myapplication;

import android.app.Fragment;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;

import contenedores.Planta;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, EntreFragments {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Planta miPlanta= new Planta();
    String myId;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();


        //Coge las credenciales y guardalas
        Intent intent = getIntent();
        String email = intent.getStringExtra("user");
        String password = intent.getStringExtra("pass");
        myId = intent.getStringExtra("id");

        PrefUtils.saveToPrefs(this, "PREFS_LOGIN_USERNAME_KEY", myId);
        PrefUtils.saveToPrefs(this, "PREFS_LOGIN_PASSWORD_KEY", password);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        Fragment fragment = null;
        switch (position) {

            case 0:
                fragment = new Perfil();
                Intent intent = getIntent();
                String myId = intent.getStringExtra("id");
                ((Perfil)fragment).cargarPerfilUsuario(myId);
                break;
            case 1:
                fragment = new Descubre();
                break;
            case 2:
                fragment = new Top();
                break;
            case 3:
                fragment = new Followed();
                break;


        }


        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
        this.onSectionAttached(position);
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 0:
                mTitle = "Perfil";
                break;
            case 1:
                mTitle = "Descubre";
                break;
            case 2:
                mTitle = "Top";
                break;
            case 3:
                mTitle = "Flowers";
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.color_verde));

        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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

        if (id == R.id.logout) {
            SQLite sqlu = new SQLite(this, "BDUsuarios", null, 1);
            SQLiteDatabase db = sqlu.getWritableDatabase();
            ContentValues valores = new ContentValues();
            valores.put("flag",0);
            db.update("Usuarios", valores, "ID > 0", null);
            db.close();

            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


    public void sendID_estdisticas(int IDplanta){
        FragmentManager fragmentManager = getFragmentManager();
        EstadisticasFragment fragment = new EstadisticasFragment();
        fragment.setIDPlanta(IDplanta);
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack("A_B_TAG")
                .commit();
    }

    public void sendPlanta(Planta planta){

        FragmentManager fragmentManager = getFragmentManager();
        Perfil fragment = new Perfil();
        fragment.setPlantaPerfil(planta);
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack("A_B_TAG")
                .commit();

    }

    @Override
    public void onBackPressed(){
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Log.i("MainActivity", "popping backstack");
            fm.popBackStack();
        } else {
            Log.i("MainActivity", "nothing on backstack, calling super");
            super.onBackPressed();
        }
    }



}
