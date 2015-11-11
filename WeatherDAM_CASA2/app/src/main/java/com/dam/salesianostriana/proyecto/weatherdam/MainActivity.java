package com.dam.salesianostriana.proyecto.weatherdam;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dam.salesianostriana.proyecto.weatherdam.model.OpenWeatherDaily;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private String nombre_ciudad;
    private static Toolbar toolbar;
    AutoCompleteTextView tabs;
    public static void colocarTitulo(String titulo){
        toolbar.setTitle(titulo);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Bundle extras = getIntent().getExtras();
        nombre_ciudad = extras.getString("titulo");
        toolbar.setTitle(nombre_ciudad);


        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);

        mViewPager.setAdapter(mSectionsPagerAdapter);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        //permite modificar el hint que el EditText muestra por defecto
        searchView.setQueryHint("Buscar...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //se oculta el EditText
                searchView.setQuery("", false);
                searchView.setIconified(true);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
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
                return new PlaceholderFragment(nombre_ciudad);
            } else if(position==2) {
                return new PronosticoFragment();
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
            imgTiempo = (ImageView) rootView.findViewById(R.id.imgTiempo);
            txtMaxima = (TextView) rootView.findViewById(R.id.txtMaxima);
            txtMinima = (TextView) rootView.findViewById(R.id.txtMinima);
            txtHumedad = (TextView) rootView.findViewById(R.id.txtHumedad);


            //new GetDataTask().execute();

            return rootView;
        }

        private class GetDataTask extends AsyncTask<Void, Void, OpenWeatherDaily> {

            @Override
            protected OpenWeatherDaily doInBackground(Void... params) {

                String URL_REQUEST = "http://api.openweathermap.org/data/2.5/weather?q="+nombre_ciudad.replace(" ","").trim()+"&appid=3bcfcde9b7438aa7696f020ed75f5673&lang=es&units=metric";

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
