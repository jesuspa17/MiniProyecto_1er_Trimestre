package com.dam.salesianostriana.di.prueba_autocompletado;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;

import com.dam.salesianostriana.di.prueba_autocompletado.pojo_google.Predictions;
import com.dam.salesianostriana.di.prueba_autocompletado.pojo_google.Region;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyBHntekX8fl61Bj1n9aUYbaKxVQa3frT-o";

   // EditText ciudad;

    AutoCompleteTextView ciudad;

    ListView list;
    List<Predictions> countriesList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ciudad = (EditText) findViewById(R.id.editCiudad);
        ciudad = (AutoCompleteTextView) findViewById(R.id.editCiudad);
        list = (ListView) findViewById(R.id.listView);

        ciudad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String c = ciudad.getText().toString();
                new DataTask().execute(c);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    class DataTask extends AsyncTask<String, Void, List<Predictions>>{

        @Override
        protected List<Predictions> doInBackground(String... params) {
            List<Predictions> result = null;
            URL url = null;

            try {

//                StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
//                sb.append("?input=Olivares");
//                sb.append("&types=(cities)");
//                sb.append("&language=es_ES");
//                sb.append("&key=" + API_KEY);

                url = new URL(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON + "?input="+params[0]+"&types=(cities)&language=es_ES&key="+API_KEY);

               // url = new URL("https://maps.googleapis.com/maps/api/place/autocomplete/json?input="+params[0]+"&types=(cities)&language=es_ES&key=AIzaSyAowGYjVlmA1XJl2EZNvKCVgNF8nPk8uHE");

                BufferedReader r = new BufferedReader(new InputStreamReader(url.openStream()));

                Gson gson = new Gson();

                Region region  = gson.fromJson(r, Region.class);

                result = Arrays.asList(region.getPredictions());


            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(List<Predictions> predictionses) {
            super.onPostExecute(predictionses);
            super.onPostExecute(predictionses);
            if (predictionses != null) {
                ciudad.setAdapter(new AutoComplete(MainActivity.this,android.R.layout.simple_list_item_1));
                countriesList = predictionses;
            }
        }
    }

    class AutoComplete extends ArrayAdapter implements Filterable{

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
                        // Assign the data to the FilterResults
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
