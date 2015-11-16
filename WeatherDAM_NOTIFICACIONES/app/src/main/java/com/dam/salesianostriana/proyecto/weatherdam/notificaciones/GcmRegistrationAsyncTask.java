package com.dam.salesianostriana.proyecto.weatherdam.notificaciones;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.jesus.myapplication.backend.registration.Registration;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.api.client.http.HttpStatusCodes;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Created by miguelcampos on 1/11/15.
 */
public class GcmRegistrationAsyncTask extends AsyncTask<String, Void, String> {
    private static Registration regService = null;
    private GoogleCloudMessaging gcm;
    private Context context;
    private String regId;
    JSONArray response = new JSONArray();
    String ciudadElegida;

    // TODO: change to your own sender ID to Google Developers Console project number, as per instructions above
    private static final String SENDER_ID = "93663396119";

    public GcmRegistrationAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {

        String msg = "";
        String log = params[0];
        Log.i("logAsyn",log);
        ciudadElegida = params[0];
        try {
            if (gcm == null) {
                gcm = GoogleCloudMessaging.getInstance(context);
            }
            regId = gcm.register(SENDER_ID);
            msg = "Device registered, registration ID=" + regId;

            // You should send the registration ID to your server over HTTP,
            // so it can use GCM/HTTP or CCS to send messages to your app.
            // The request to your server should be authenticated if your app
            // is using accounts.
            Log.i("REGISTARDO","registrado");
            guardarRegistrationId(context, ciudadElegida, regId);
            sendRegistrationIdToBackend();

        } catch (IOException ex) {
            ex.printStackTrace();
            msg = "Error: " + ex.getMessage();
        }
        return msg;
    }

    @Override
    protected void onPostExecute(String msg) {

        Toast.makeText(context,
                "Usted ahora recibirá notificaciones sobre el tiempo en: "+ciudadElegida,
                Toast.LENGTH_LONG).show();
        Logger.getLogger("REGISTRATION").log(Level.INFO, msg);
    }

    private static final String REGISTRATION_ID = "registration_id";
    private static final String CIUDAD_DEFECTO = "ciudad_defecto";

    private void guardarRegistrationId(Context context, String ciudad_defecto, String regId) {

        SharedPreferences prefs = context.getSharedPreferences("mispreferencias", Context.MODE_APPEND);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(CIUDAD_DEFECTO, ciudad_defecto);
        editor.putString(REGISTRATION_ID, regId);

        editor.apply();
    }

    private void sendRegistrationIdToBackend() {
        URL url = null;
        HttpURLConnection urlConnection = null;
        Log.v("CatalogClient", "Entra en sendRegistration");

        String log = "http://weather.miguelcr.com/register.php?regId="+regId+"&city="+ciudadElegida;

        Log.i("LogAsyn",log);
        try {
            url = new URL("http://weather.miguelcr.com/register.php?regId="+regId+"&city="+ciudadElegida);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            urlConnection = (HttpURLConnection) url.openConnection();

            int responseCode = urlConnection.getResponseCode();

            if(responseCode == HttpStatusCodes.STATUS_CODE_OK){
                String responseString = readStream(urlConnection.getInputStream());
                Log.v("CatalogClient", responseString);
                try {
                    response = new JSONArray(responseString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                Log.v("CatalogClient", "Response code:"+ responseCode);
            }
        } catch (IOException e) {
            Log.v("CatalogClient", "Error conexión");
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }

    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }
}
