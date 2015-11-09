package com.dam.salesianostriana.di.prueba_autocompletado;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.dam.salesianostriana.di.prueba_autocompletado.pojo_google.Predictions;

import java.util.List;

/**
 * Created by Jesús Pallares on 09/11/2015.
 */
public class GooglePlaceAutocompleteAdapter extends ArrayAdapter{

    private List<Predictions> resultList;

    public GooglePlaceAutocompleteAdapter(Context context, int textViewResourceId, List<Predictions> lista) {
        super(context, textViewResourceId, lista);
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public Object getItem(int index) {
        return resultList.get(index);
    }

}
