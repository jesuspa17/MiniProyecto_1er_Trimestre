package com.salesianostriana.ad.gsonlistviewsample;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Luismi on 02/11/2015.
 */
public class CountryAdapter extends BaseAdapter {

    private List<Country> list;
    private Context mContext;

    public CountryAdapter(Context context, List<Country> _list) {
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
        ImageView img = (ImageView) convertView.findViewById(R.id.imageView);

        Country c = (Country) getItem(position);



        String flagName = "flag_" + c.getAlpha();
        flagName = flagName.toLowerCase();

        txt.setText(c.getName());

        int id = mContext.getResources().getIdentifier(flagName, "drawable", mContext.getPackageName());

        Log.i("FLAG", flagName + " " + Integer.toString(id));

        if (id != 0)
            img.setImageResource(id);


        return convertView;
    }
}
