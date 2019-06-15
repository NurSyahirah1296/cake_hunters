package com.example.tiamobakery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class customadaptercake extends SimpleAdapter {

    private Context mContext;
    public LayoutInflater inflater=null;
    public customadaptercake(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        mContext = context;
        inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        try{
            if(convertView==null)
                vi = inflater.inflate(R.layout.cakelistbakery, null);
            HashMap<String, Object> data = (HashMap<String, Object>) getItem(position);
            TextView tvcakename = vi.findViewById(R.id.tv1);
            TextView tvcakeprice = vi.findViewById(R.id.tv2);
            TextView tvquantity = vi.findViewById(R.id.tv3);
            CircleImageView imgcake =vi.findViewById(R.id.imageView2);
            String dfname = (String) data.get("cakename");
            String dcakeprice =(String) data.get("cakeprice");
            String dcakequan =(String) data.get("cakequantity");
            String dfid=(String) data.get("cakeid");

            tvcakename.setText(dfname);
            tvcakeprice.setText(dcakeprice);
            tvquantity.setText(dcakequan);
            String image_url = "https://tiamobakery.000webhostapp.com/cakehunters/images/"+dfid+".jpg";
            Picasso.with(mContext).load(image_url)
                    .fit().into((Target) imgcake);


        }catch (IndexOutOfBoundsException e){

        }

        return vi;
    }
}
