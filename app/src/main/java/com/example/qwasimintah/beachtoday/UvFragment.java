package com.example.qwasimintah.beachtoday;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.content.res.Resources;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by QWASI MINTAH on 7/2/2015.
 */

    public  class UvFragment extends Fragment {

        final static String LOG_TAG=UvFragment.class.getSimpleName();
        private UvAdapter uvForcastAdapter;

        private String[] results;
        ListView uv_view;

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor edit;

    private String[] uv_index_value=new String[3];
    private String[] value=new String[3];
    private String[] date=new String[3];
    private String[] day=new String[3];
    public static final String today = "today";
    public static final String tomorrow = "tomorrow";

        public UvFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater menuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //menuInflater.inflate(R.menu.refresh_fragment, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }


    private String getReadableDateString(long time){

        SimpleDateFormat shortenedDateFormat=new SimpleDateFormat("EEE MMM dd");

        return shortenedDateFormat.format(time);

    }

    //getting uv index from json string
    private String[] getUvDataFromJson(String forcastJsonStr,int numDays) throws JSONException {

        final String WWO_DATA="data";
        final String WWO_DATE="date";
        final String WWO_WEATHER="weather";
        final String WWO_UVINDEX="uvIndex";



        JSONObject forecastJson=new JSONObject(forcastJsonStr).getJSONObject(WWO_DATA);
        JSONArray weatherArray=forecastJson.getJSONArray(WWO_WEATHER);
        JSONArray city=forecastJson.getJSONArray("request");

       String[] resultStrs=new String[numDays];
//        long locationId = addLocation(locationSetting, cityName);

        for (int i=0; i<weatherArray.length(); i++){

            String uvIndex;
            Date day=new Date();

            JSONObject dayForecast=weatherArray.getJSONObject(i);
            uvIndex=dayForecast.getString(WWO_UVINDEX);


            try {
                String date = dayForecast.getString(WWO_DATE);
                SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
                day=formater.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }



                value[i]=uvIndex;
                date[i]=day.toString().substring(0,10);
                resultStrs[i]=day +"- UV Index:"+uvIndex;
        }

                edit.putString(today,value[0]);
                edit.putString(tomorrow,value[1]);
                edit.putString("next",value[2]);

                edit.putString("today_date",date[0]);
                edit.putString("tomorrow_date",date[1]);
                edit.putString("next_date",date[2]);

                edit.apply();
        for(String s : resultStrs){

            Log.v(LOG_TAG,"Forecast entry: "+ s);
        }
        return  resultStrs;
    }






    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            sharedpreferences=PreferenceManager.getDefaultSharedPreferences(getActivity());
            edit=sharedpreferences.edit();

        if(sharedpreferences.getString(today,"")!="null") {
            uv_index_value[0] = sharedpreferences.getString(today, "8");
            uv_index_value[1] = sharedpreferences.getString(tomorrow, "6");
            uv_index_value[2] = sharedpreferences.getString("next", "3");

            day[0] = sharedpreferences.getString("today_date", "Today");
            day[1] = sharedpreferences.getString("tomorrow_date", "Tomorrow");
            day[2] = sharedpreferences.getString("next_date", "Wednesday");

            String[] description = {"Low",
                    "Medium",
                    "High",
                    "Very High",
                    "Extreme"
            };

            final Integer[] image = new Integer[3];

            Integer[] imgid = {
                    R.drawable.low,
                    R.drawable.medium,
                    R.drawable.high,
                    R.drawable.veryhigh,
                    R.drawable.extreme
            };

            //setting description
            final String[] des = new String[3];
            for (int i = 0; i < 3; i++) {
                if (Integer.parseInt(uv_index_value[i]) < 3) {
                    des[i] = description[0];
                    image[i] = imgid[0];

                } else if (Integer.parseInt(uv_index_value[i]) >= 3 && Integer.parseInt(uv_index_value[i]) <= 5) {
                    des[i] = description[1];
                    image[i] = imgid[1];
                } else if (Integer.parseInt(uv_index_value[i]) >= 6 && Integer.parseInt(uv_index_value[i]) <= 7) {
                    des[i] = description[2];
                    image[i] = imgid[2];
                } else if (Integer.parseInt(uv_index_value[i]) >= 8 && Integer.parseInt(uv_index_value[i]) < 10) {
                    des[i] = description[3];
                    image[i] = imgid[3];
                } else {
                    des[i] = description[4];
                    image[i] = imgid[4];
                }
            }
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String location =prefs.getString(getString(R.string.pref_location_key),getString(R.string.pref_location_default));

            Context context = getActivity().getBaseContext();
            uvForcastAdapter = new UvAdapter(context, image, des, day, uv_index_value,location);


            //List<String> uv_forcast=new ArrayList<String>(Arrays.asList(uv_array));

            // uvForcastAdapter= new ArrayAdapter(getActivity(),R.layout.list_uv_forcast,R.id.list_uv_textview,/*new ArrayList<String>()*/uv_forcast);

            uv_view = (ListView) rootView.findViewById(R.id.uv_forcast);

            uv_view.setAdapter(uvForcastAdapter);

            uv_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String uvForcast = uvForcastAdapter.getItem(position).toString();
                    String dates = day[position];
                    String uv = uv_index_value[position];
                    String desc = des[position];
                    Integer img = image[position];

                    Intent intent = new Intent(getActivity(), UvDetails.class).putExtra(Intent.EXTRA_TEXT, uvForcast);
                    intent.putExtra("date", dates);
                    intent.putExtra("uv", uv);
                    intent.putExtra("des", desc);
                    intent.putExtra("image", img);
                    startActivity(intent);

                }
            });
        }
        /*else {
            //String[] warning ={"Device Not Connected To Internet","Please connect and restart the app"};
            //List<String> uv_forcast=new ArrayList<String>(Arrays.asList(warning));
           ArrayAdapter uvAdapter= new ArrayAdapter(getActivity(),R.layout.warning,R.id.warning,new ArrayList<String>());
            uv_view = (ListView) rootView.findViewById(R.id.uv_forcast);
            uv_view.setAdapter(uvAdapter);

            Toast.makeText(getActivity().getApplicationContext(),"Connect to the Internet and Restart app",Toast.LENGTH_LONG).show();

        }*/
            return rootView;
        }

    @Override
    public void onStart() {
        super.onStart();
        updateUv();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUv();
    }

    @Override
    public void onPause() {
        super.onPause();
        updateUv();
    }

    private void updateUv(){
        GetUvIndexForcast uvTask=new GetUvIndexForcast();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String location =prefs.getString(getString(R.string.pref_location_key),getString(R.string.pref_location_default));
        uvTask.execute(location);

    }




    public class GetUvIndexForcast extends AsyncTask< String ,Void ,String[] > {



        @Override
        protected String[] doInBackground(String... params) {

            if(params.length==0)
            {
                return null;
            }


            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String uvJsonStr = null;

            String format="json";

            int numDays=3;

                final String API_KEY ="ffc72234971222f785d9663e38bf7";

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast

                final String UV_BASE_URL="api.worldweatheronline.com/free/v2/weather.ashx?";
                final String LOCATION_PARAM="q";
                final String  FORMAT_PARAM="format";
                final String   DAYS_PARAM="num_of_days";
                final String  KEY_PARAM="key";

              /*  Uri builtUrl= Uri.parse(UV_BASE_URL).buildUpon().appendQueryParameter(LOCATION_PARAM,params[0])
                        .appendQueryParameter(FORMAT_PARAM,format)
                        .appendQueryParameter(DAYS_PARAM,Integer.toString(numDays))
                        .appendQueryParameter(KEY_PARAM,API_KEY)
                        .build();*/
                String builtUrl="http://api.worldweatheronline.com/free/v2/weather.ashx?q="+params[0]+"&format=json&num_of_days="+Integer.toString(numDays)+"&key=ffc72234971222f785d9663e38bf7";
     // URL url = new URL("http://api.worldweatheronline.com/free/v2/weather.ashx?q=accra&format=json&num_of_days=1&key=ffc72234971222f785d9663e38bf7");
                URL url =new URL(builtUrl);
                Log.v(LOG_TAG, "BUILT_URL" + builtUrl);
                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                uvJsonStr = buffer.toString();
                Log.v("UvFragment","forcast json string: "+uvJsonStr);
            } catch (IOException e) {
                Log.e("GetUvIndexForcast", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }

           try{
                return getUvDataFromJson(uvJsonStr,numDays);
            }
            catch (JSONException e){
               Log.e(LOG_TAG,e.getMessage(),e);
                e.printStackTrace();
            }
            return null;
        }

      
        @Override
        protected void onPostExecute(String[] result) {
            if(result!=null){
               // uvForcastAdapter.clear();

                //for(String dayForcastStr :result)
                //uvForcastAdapter.add(dayForcastStr);

            }




        }
    }


    }

