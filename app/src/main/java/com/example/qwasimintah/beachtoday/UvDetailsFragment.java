package com.example.qwasimintah.beachtoday;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.zip.Inflater;


/**
 * A placeholder fragment containing a simple view.
 */
public class UvDetailsFragment extends Fragment {


    private final String LOG_TAG=UvDetailsFragment.class.getSimpleName();
    private static final String FORCAST_SHATE_HASTAG="#BeachToday App";
    private String uvForecast;
    private String passedDate;
    private String passedUv;
    private String passedDes;
    private Integer passedImage;
    public UvDetailsFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        Intent passedIntent =getActivity().getIntent();
        View view = inflater.inflate(R.layout.fragment_uv_details, container, false);

        TextView day=(TextView) view.findViewById(R.id.detail_day_textview);

        TextView uv_value=(TextView) view.findViewById(R.id.uv_value);
        ImageView uv_image=(ImageView) view.findViewById(R.id.uv_image);
        TextView uv_state=(TextView) view.findViewById(R.id.uv_state);
        TextView safety=(TextView) view.findViewById(R.id.precautions);
        TextView precautions=(TextView) view.findViewById(R.id.safety);
        String safetytext="Being outdoors is good for you. Just remember to take these simple precautions to protect yourself from the sun's harmful rays";

        if(passedIntent!=null && passedIntent.hasExtra(Intent.EXTRA_TEXT)){
           uvForecast=passedIntent.getStringExtra(Intent.EXTRA_TEXT);
            passedDate=passedIntent.getStringExtra("date");
            passedUv=passedIntent.getStringExtra("uv");
            passedDes=passedIntent.getStringExtra("des");
            passedImage=passedIntent.getIntExtra("image",R.drawable.low);

            //((TextView) view.findViewById(R.id.uv_detail_text)).setText(uvForecast+" "+passedDate+"  "+passedUv);
            String precaution=safetytext(passedUv);
            day.setText(passedDate);
            uv_value.setText(passedUv);
            uv_image.setImageResource(passedImage);
            uv_state.setText(passedDes);
            safety.setText(safetytext);
            precautions.setText(precaution);

        }
        return view;

    }
        //provides safety tips based on the uv index
    public String safetytext(String uv_index_value){

        String des;

        String[] safety = {
                "Minimal sun protection required for normal activity.\n" +
                        "Wear sunglasses on bright days. If outside for more than one hour, cover up and use sunscreen.\n" +
                        "Reflection off snow can nearly double UV strength, so wear sunglasses and apply sunscreen on\n" +
                        "your face",
                "Take precaution by covering up, and wearing a hat, sunglasses and sunscreen, especially if you\n" +
                        "will be outside for 30 minutes or more.\n" +
                        "Look for shade near midday when the sun is strongest.",
                "Protection required UVdamages\n" +
                        "the skin and can cause sunburn.\n" +
                        "Reduce time in the sun between 11 a.m. and 4 p.m. and take full precaution by seeking shade,\n" +
                        "covering up exposed skin, wearing a hat and sunglasses, and applying sunscreen.",
                "Extra precaution required unprotected\n" +
                        "skin will be damaged and can burn quickly.\n" +
                        "Avoid the sun between 11 a.m. and 4 p.m. and seek shade, cover up, and wear a hat, sunglasses\n" +
                        "and sunscreen",
                "Take full precaution. Unprotected skin will be damaged and can burn in minutes. Avoid the\n" +
                        "sun between 11 a.m. and 4 p.m., cover up, and wear a hat, sunglasses and sunscreen.\n" +
                        "Don’t forget that white sand and other bright surfaces reflect UV and increase UV exposure."
        };

            if(Integer.parseInt(uv_index_value) < 3){
                des  = safety[0];


            }
            else if (Integer.parseInt(uv_index_value) >= 3 && Integer.parseInt(uv_index_value)<=5){
                des  = safety[1];

            }
            else if (Integer.parseInt(uv_index_value) >= 6 && Integer.parseInt(uv_index_value)<=7){
                des  = safety[2];

            }
            else if (Integer.parseInt(uv_index_value) >= 8 && Integer.parseInt(uv_index_value)<10){
                des  = safety[3];
            }
            else  {
                des  = safety[4];

            }

        return des;
    }
    private Intent createShareForecastIntent(){
        Intent shareIntent =new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT,"Today's UV Index is "+passedUv+"\nNature of Exposure is "+ passedDes
                +"\nPlease Follow the following Safety tips"+"\n"+safetytext(passedUv)+FORCAST_SHATE_HASTAG);
        return shareIntent;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detail_fragment, menu);

        MenuItem menuItem= menu.findItem(R.id.action_share);

        ShareActionProvider mProvider =(ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        if(mProvider != null){
            mProvider.setShareIntent(createShareForecastIntent());
        }
        else{
            Log.d(LOG_TAG, "Shared provider is null");
        }
    }
}
