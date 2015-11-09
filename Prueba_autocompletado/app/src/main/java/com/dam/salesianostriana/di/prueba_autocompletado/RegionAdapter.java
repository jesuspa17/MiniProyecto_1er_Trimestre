package com.dam.salesianostriana.di.prueba_autocompletado;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dam.salesianostriana.di.prueba_autocompletado.pojo_google.Predictions;

import java.util.List;

/**
 * Created by Jesús Pallares on 09/11/2015.
 */
public class RegionAdapter extends BaseAdapter {

    private List<Predictions> list;
    private Context mContext;

    public RegionAdapter(Context context, List<Predictions> _list) {
        list = _list;
        mContext = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(mContext).inflate(R.layout.item_layout, null);
        TextView txt = (TextView) convertView.findViewById(R.id.textView);
        Predictions reg = (Predictions) getItem(position);

        txt.setText(reg.getDescription());


        return convertView;
    }
}


