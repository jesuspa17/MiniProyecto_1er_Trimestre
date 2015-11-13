package com.dam.salesianostriana.proyecto.weatherdam;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;

import com.dam.salesianostriana.proyecto.weatherdam.pojo_googleplaces.Predictions;
import com.dam.salesianostriana.proyecto.weatherdam.pojo_googleplaces.Region;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.List;


public class BuscarInicioActivity extends AppCompatActivity{

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyCAfWAIEXkY9aNcEPhR6z0s2pFFiojQjqg";

   //CLAVE LUISMI: AIzaSyCAfWAIEXkY9aNcEPhR6z0s2pFFiojQjqg
    //Mi clave: AIzaSyD8CPZ6CasOYaWLIJd2CJxROXwelLZaLGM

    AutoCompleteTextView ciudad;
    List<Predictions> countriesList;
    FloatingActionButton fab;

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences("NuevasPreferencias", Context.MODE_APPEND);
        final SharedPreferences.Editor editor = prefs.edit();
        final int KEY = 12345;

        if(prefs.getBoolean("registrado",false)){
            this.finish();
            Intent i = new Intent(BuscarInicioActivity.this,MainActivity.class);
            String c = prefs.getString("ciudad_defecto",null);
            i.putExtra("titulo",c);
            startActivity(i);

        }else {

            setContentView(R.layout.activity_buscar_inicio);
            //ciudad = (EditText) findViewById(R.id.editCiudad);
            ciudad = (AutoCompleteTextView) findViewById(R.id.editCiudad);
            fab = (FloatingActionButton) findViewById(R.id.fab);


            ciudad.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String c = ciudad.getText().toString();
                    new GooglePlaceTask().execute(c);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ciudad.getText().toString().isEmpty()) {
                        Snackbar bar = Snackbar.make(v, "Debe buscar alguna ciudad", Snackbar.LENGTH_LONG)
                                .setAction("Action", null);
                        bar.show();
                    } else {
                        String c = ciudad.getText().toString().replace(" ", "");
                        Intent i = new Intent(BuscarInicioActivity.this, MainActivity.class);

                        editor.putString("ciudad_defecto", c);
                        editor.putInt("key", KEY);
                        editor.putBoolean("registrado", true);
                        editor.apply();

                        Utils.listadoCiudadesFav.add(c);
                        Utils.guardarArray(BuscarInicioActivity.this);

                        //i.putExtra("titulo", "Olivares,Spain");
                        i.putExtra("titulo", c);
                        startActivity(i);
                        ciudad.setText("");

                        BuscarInicioActivity.this.finish();

                    }
                }
            });
        }

    }

    class GooglePlaceTask extends AsyncTask<String, Void, List<Predictions>> {

        @Override
        protected List<Predictions> doInBackground(String... params) {
            List<Predictions> result = null;
            URL url = null;

            try {
                if(params[0] != null) {

                    url = new URL(PLACES_API_BASE + TYPE_AUTOCOMPLETE
                            + OUT_JSON + "?input=" + params[0].replace(" ","").trim()
                            + "&types=(cities)&language=es_ES&key=" + API_KEY);

                    // url = new URL("https://maps.googleapis.com/maps/api/place/autocomplete/json?input="+params[0]+"&types=(cities)&language=es_ES&key=AIzaSyAowGYjVlmA1XJl2EZNvKCVgNF8nPk8uHE");

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
                ciudad.setAdapter(new AutoComplete(BuscarInicioActivity.this, android.R.layout.simple_list_item_1));
                countriesList = predictionses;
                ciudad.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String str = (String) parent.getItemAtPosition(position);

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
            return countriesList.size();
        }

        @Override
        public Object getItem(int index) {
            return countriesList.get(index).getDescription();
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {

                        filterResults.values = countriesList;
                        filterResults.count = countriesList.size();
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
}