package com.dam.salesianostriana.proyecto.weatherdam;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import com.dam.salesianostriana.proyecto.weatherdam.adaptadores_listas.GooglePersonalizadoAdapter;
import com.dam.salesianostriana.proyecto.weatherdam.pojo_googleplaces.Predictions;
import com.dam.salesianostriana.proyecto.weatherdam.pojo_googleplaces.Region;
import com.dam.salesianostriana.proyecto.weatherdam.utilidades.Url_apis;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private static Toolbar toolbar;
    TabLayout tabLayout;


    /*
     * Servirá para obtener los resultados de la búsqueda.
     */
    AutoCompleteTextView ciudad;

    /**
     * Almacenará el nombre de la ciudad que se quiere buscar.
     */
    private String nombre_ciudad;


    /**
     * Método estático que permite cambiar el titulo del ActionBar
     *
     * @param titulo
     */
    public static void colocarTitulo(String titulo) {
        toolbar.setTitle(titulo);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ciudad = (AutoCompleteTextView) findViewById(R.id.editCiudad);
        ciudad.setVisibility(View.INVISIBLE);

        //Se recoge el nombre de la ciudad buscada.
        Bundle extras = getIntent().getExtras();
        nombre_ciudad = extras.getString("titulo");

        //Se coloca el título de la toolbar a vacío para
        //colocarle el nombre de la ciudad.
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        ciudad.setVisibility(View.VISIBLE);

        //Se obtiene el icono buscar del menú y se le asocia un searchview
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        //Permite modificar el hint que el EditText muestra por defecto
        searchView.setQueryHint("Buscar ciudad...");

        //Dará sugerencias mientras esté escribiendo
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ciudad.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ciudad.showDropDown();
                    }
                }, 0);
                ciudad.setText(newText);
                new GooglePlaceTask().execute(ciudad.getText().toString());
                return false;
            }

        });

        return true;
    }

    /////////////////ASYNTASK QUE REALIZA LA CONSULTA AL API DE GOOGLE PLACES//////////////////

    class GooglePlaceTask extends AsyncTask<String, Void, List<Predictions>> {

        @Override
        protected List<Predictions> doInBackground(String... params) {
            List<Predictions> result = null;
            URL url = null;

            try {
                if (params[0] != null) {

                    url = new URL(Url_apis.URL_GOOGLE_PLACES_BASE() + params[0].replace(" ", "").trim()
                            + Url_apis.URL_GOOGLE_PLACES_APPEND());

                    BufferedReader r = new BufferedReader(new InputStreamReader(url.openStream()));
                    Gson gson = new Gson();
                    Region region = gson.fromJson(r, Region.class);
                    result = Arrays.asList(region.getPredictions());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(List<Predictions> predictionses) {
            super.onPostExecute(predictionses);

            if (predictionses != null) {
                //si el autocompletar no vale descomenta esto y comenta el otro adapter.
                //ciudad.setAdapter(new GoogleplacesAdapter(MainActivity.this, android.R.layout.simple_list_item_1,predictionses));
                ciudad.setAdapter(new GooglePersonalizadoAdapter(MainActivity.this,predictionses));
                ciudad.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Predictions pred = (Predictions) parent.getItemAtPosition(position);
                        MainActivity.this.finish();
                        Intent i = new Intent(MainActivity.this, MainActivity.class);
                        i.putExtra("titulo", pred.getDescription());
                        startActivity(i);
                    }
                });
            }

        }
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
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if (position == 0) {
                return new DetalleTiempoFragment(nombre_ciudad);
            } else if (position == 1) {
                return new ProximosDiasFragment(nombre_ciudad);
            } else if (position == 2) {
                return new FavoritosFragment();
            }

            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "HOY";
                case 1:
                    return "PRÓXIMOS DÍAS";
                case 2:
                    return "FAVORITOS";
            }
            return null;
        }
    }
}
