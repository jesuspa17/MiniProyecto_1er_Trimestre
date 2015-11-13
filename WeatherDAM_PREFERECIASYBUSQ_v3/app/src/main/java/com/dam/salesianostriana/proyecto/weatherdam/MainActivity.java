package com.dam.salesianostriana.proyecto.weatherdam;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dam.salesianostriana.proyecto.weatherdam.pojo_googleplaces.Predictions;
import com.dam.salesianostriana.proyecto.weatherdam.pojo_googleplaces.Region;
import com.dam.salesianostriana.proyecto.weatherdam.pojo_weather_dia.OpenWeatherDaily;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {


    /**
     * COPIAR EL ASYNTASK DE BUSCAR AQUÍ Y EN EL ONQUERY TEXTCHANGED EJECUTAR EL ONPOSTEXECUTE
     */

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private static Toolbar toolbar;
    TabLayout tabLayout;

    /**
     *Elementos de la URL que se va a utilizar para obtener datos del Api de GooglePlaces.
     */
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyCAfWAIEXkY9aNcEPhR6z0s2pFFiojQjqg";

   //https://maps.googleapis.com/maps/api/place/autocomplete/json?input=&types=(cities)&language=es_ES&key=AIzaSyBGFR2Q59XgnmOUMWoBtevZQCPKLUo2iiw

    /*
     * Servirá para obtener los resultados de la búsqueda.
     */
    AutoCompleteTextView ciudad;
    /**
     * Servirá para almacenar una lista con los datos obtenidos de la consulta al Api de Google.
     */
    List<Predictions> listaRegiones;
    /**
     * Almacenará el nombre de la ciudad que se quiere buscar.
     */
    private String nombre_ciudad;


    /**
     * Método estático que permite cambiar el titulo del ActionBar
     * @param titulo
     */
    public static void colocarTitulo(String titulo){
        toolbar.setTitle(titulo);
    }

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ItemCiudad> ciudades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ciudad = (AutoCompleteTextView) findViewById(R.id.editCiudad);
        ciudad.setVisibility(View.INVISIBLE);


        Bundle extras = getIntent().getExtras();
        nombre_ciudad = extras.getString("titulo");
        toolbar.setTitle("");

        setSupportActionBar(toolbar);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
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

        // Get the SearchView and set the searchable configuration
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        //permite modificar el hint que el EditText muestra por defecto
        searchView.setQueryHint("Buscar...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // new DataTask().execute(city);
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
    class GooglePlaceTask extends AsyncTask<String, Void, List<Predictions>> {

        @Override
        protected List<Predictions> doInBackground(String... params) {
            List<Predictions> result = null;
            URL url = null;

            try {
                if(params[0] != null) {

                    url = new URL(Utils.URL_GOOGLE_PLACES_BASE() + params[0].replace(" ","%20")
                            + Utils.URL_GOOGLE_PLACES_APPEND());

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
                listaRegiones = predictionses;
                ciudad.setAdapter(new AutoComplete(MainActivity.this, android.R.layout.simple_list_item_1));
                ciudad.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String str = (String) parent.getItemAtPosition(position);
                        MainActivity.this.finish();
                        Intent i = new Intent(MainActivity.this, MainActivity.class);
                        i.putExtra("titulo", str);
                        startActivity(i);
                    }
                });
            }

        }
    }

    class AutoComplete extends ArrayAdapter implements Filterable {

        public AutoComplete(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return listaRegiones.size();
        }

        @Override
        public Object getItem(int index) {
            return listaRegiones.get(index).getDescription();
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {

                        filterResults.values = listaRegiones;
                        filterResults.count = listaRegiones.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
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
            if(position==0) {
                return new DetalleTiempoFragment(nombre_ciudad);
            } else if(position==1) {
                return new ProximosDiasFragment(nombre_ciudad);
            } else if(position==2) {
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

    /**
     * A placeholder fragment containing a simple view.
     */
            public static class PlaceholderFragment extends Fragment {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */

        final String URL_BASE_IMG_WEATHER = "http://openweathermap.org/img/w/";
        final String EXTENSION_IMG_WEATHER = ".png";

        ProgressDialog progressDialog;

        private String nombre_ciudad;

        TextView txtCiudad, txtFechaHora, txtTiempo, txtTemperatura, txtAmanecer, txtAnochecer, txtMaxima, txtMinima, txtHumedad;
        ImageView imgTiempo;

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {

        }

        public PlaceholderFragment(String nombre_ciudad) {
                this.nombre_ciudad = nombre_ciudad;
        }



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_tiempo_hoy, container, false);

            txtFechaHora = (TextView) rootView.findViewById(R.id.txtFechayHora);
            txtTiempo = (TextView) rootView.findViewById(R.id.txtTiempo);
            txtTemperatura = (TextView) rootView.findViewById(R.id.txtTemperatura);
            txtAmanecer = (TextView) rootView.findViewById(R.id.txtAmanecer);
            txtAnochecer = (TextView) rootView.findViewById(R.id.txtAnochecer);
            imgTiempo = (ImageView) rootView.findViewById(R.id.imgTiempoFav);
            txtMaxima = (TextView) rootView.findViewById(R.id.txtMaxima);
            txtMinima = (TextView) rootView.findViewById(R.id.txtMinima);
            txtHumedad = (TextView) rootView.findViewById(R.id.txtHumedad);


            //new GetDataTask().execute();

            return rootView;
        }

        private class GetDataTask extends AsyncTask<Void, Void, OpenWeatherDaily> {

            @Override
            protected OpenWeatherDaily doInBackground(Void... params) {

                String URL_REQUEST = "http://api.openweathermap.org/data/2.5/weather?q="+nombre_ciudad.replace(" ", "%20")+"&appid=3bcfcde9b7438aa7696f020ed75f5673&lang=es&units=metric";

                OpenWeatherDaily result = null;
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                    @Override
                    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        //return null;
                        Long dateAsLong = json.getAsLong();

                        dateAsLong = dateAsLong * 1000L;

                        return new Date(dateAsLong);


                    }
                });

                Gson gson = gsonBuilder.create();

                try {
                    Reader r = Utils.Url2BufferedReader(URL_REQUEST);
                    result = gson.fromJson(r, OpenWeatherDaily.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                return result;
            }

            @Override
            protected void onPostExecute(OpenWeatherDaily openWeatherDaily) {

                DateFormat dfFechayHora = new SimpleDateFormat("HH:mm dd/MM/yy");
                dfFechayHora.setTimeZone(TimeZone.getTimeZone("GMT+1"));

                DateFormat dfHora = new SimpleDateFormat("HH:mm");
                dfHora.setTimeZone(TimeZone.getTimeZone("GMT+1"));


                if (openWeatherDaily != null) {

                    toolbar.setTitle(openWeatherDaily.getName());
                    txtFechaHora.setText(dfFechayHora.format(openWeatherDaily.getDt()));
                    txtTiempo.setText(openWeatherDaily.getWeather().get(0).getDescription().toUpperCase());
                    txtTemperatura.setText(openWeatherDaily.getMain().getTemp() + "º");
                    txtAmanecer.setText(dfHora.format(openWeatherDaily.getSys().getSunrise()));
                    txtAnochecer.setText(dfHora.format(openWeatherDaily.getSys().getSunset()));
                    txtMaxima.setText(openWeatherDaily.getMain().getTempMax() + "º");
                    txtMinima.setText(openWeatherDaily.getMain().getTempMin() + "º");
                    txtHumedad.setText(openWeatherDaily.getMain().getHumidity() + "%");

                    String url_image = URL_BASE_IMG_WEATHER + openWeatherDaily.getWeather().get(0).getIcon() + EXTENSION_IMG_WEATHER;

                    Picasso.with(getActivity())
                            .load(url_image)
                            .fit()
                            .into(imgTiempo);

                } else {
                    Toast.makeText(getActivity(), "Error en la descarga y procesamiento de la información", Toast.LENGTH_LONG).show();
                }
            }

        }
    }
}
