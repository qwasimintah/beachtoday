package com.example.qwasimintah.beachtoday;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;

/**
 * Created by QWASI MINTAH on 8/9/2015.
 */
public class UvAdapter  extends ArrayAdapter<String> {

    private final Context context;
    private final Integer[] imgid;
    private final String[] description;
    private final String[] value;
    private final String[] date;
    private String loc;

    public UvAdapter(Context contexts, Integer[] imgid,String[] desc,String[] date,String[] value,String location) {
        super(contexts, R.layout.list_uv_forcast, value);
        // TODO Auto-generated constructor stub
        this.context=contexts;
        this.imgid=imgid;
        this.description=desc;
        this.value=value;
        this.date=date;
        this.loc=location;
    }
    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View rowView;
        if(position!=0) {
            rowView = mInflater.inflate(R.layout.list_uv_forcast, null);

        ImageView image=(ImageView) rowView.findViewById(R.id.list_item_icon);

        TextView tomorrow = (TextView) rowView.findViewById(R.id.list_item_date_textview);
        TextView des=(TextView) rowView.findViewById(R.id.list_item_uv_description_textview);
        TextView uv=(TextView) rowView.findViewById(R.id.list_item_uv_textview);

         TextView uv_value = (TextView) rowView.findViewById(R.id.list_item_uv_value_textview);

        tomorrow.setText(date[position]);
        image.setImageResource(imgid[position]);
        uv.setText("UV Index");
        uv_value.setText(value[position]);
            des.setText(this.description[position]);
        }
        else{
            rowView=mInflater.inflate(R.layout.uv_today,null);
            ImageView image=(ImageView) rowView.findViewById(R.id.sun_icon);

            TextView today = (TextView) rowView.findViewById(R.id.list_item_date_textview);
            TextView des=(TextView) rowView.findViewById(R.id.uv_state);
            TextView location=(TextView) rowView.findViewById(R.id.location);
            TextView uv_value = (TextView) rowView.findViewById(R.id.list_item_index_textview);
            today.setText(date[position]);
            image.setImageResource(imgid[position]);
            uv_value.setText(value[position]);
            des.setText(this.description[position]);
            location.setText(this.loc);
        }
        return rowView;

    };

}