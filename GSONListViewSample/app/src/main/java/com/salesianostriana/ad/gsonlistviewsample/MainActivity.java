package com.salesianostriana.ad.gsonlistviewsample;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    ListView list;
    List<Country> countriesList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = (ListView) findViewById(R.id.listView);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, countriesList.get(position).toString(), Toast.LENGTH_LONG).show();
            }
        });


        new GetAndParseCountryListTask().execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


    private class GetAndParseCountryListTask extends AsyncTask<Void, Void, List<Country>> {

        @Override
        protected List<Country> doInBackground(Void... params) {

            List<Country> result = null;
            URL url = null;
            try {

                //A partir de la URL construimos un reader
                url = new URL("https://raw.githubusercontent.com/lukes/ISO-3166-Countries-with-Regional-Codes/master/slim-2/slim-2.json");
                BufferedReader r = new BufferedReader(new InputStreamReader(url.openStream()));

                //Instanciamos el objeto Gson
                Gson gson = new Gson();
                //A partir del reader, Gson es capaz de leer el fichero JSON y parsearlo
                //en un array de objetos
                Country[] countries = gson.fromJson(r, Country[].class);

                //Si hemos recogido algún resultado, lo devolvemos; en otro caso,
                //devolveremos null
                if (countries.length > 0)
                    result = Arrays.asList(countries);



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }


        @Override
        protected void onPostExecute(List<Country> countries) {
            super.onPostExecute(countries);
            if (countries != null) {
                list.setAdapter(new CountryAdapter(MainActivity.this, countries));
                countriesList = countries;
            }

        }
    }


}
