package com.amiyo.technicise.androidcustomprogress;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends ListActivity implements View.OnClickListener {

    Button Btngetdata;
    LayoutInflater inflater;
    public ProgressBar MyProgressBar;
    private static final String NEWS = "NewsML";
    private static final String NEWS_LINE = "NewsLines";
    private static final String HEAD_LINE = "HeadLine";
    private static final String DATE_LINE = "DateLine";
    JSONArray news = null;
    //URL to get JSON Array
    private static String url = "http://mfeeds.timesofindia.indiatimes.com/Feeds/jsonfeed?newsid=3947071&amp;amp;format=simplejson";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyProgressBar=(ProgressBar)findViewById(R.id.CustomProgressBar);

        Btngetdata = (Button)findViewById(R.id.getdata);
        Btngetdata.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new JSONParse().execute();
            }
        });
        MyProgressBar.setOnClickListener(this);

    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.CustomProgressBar:
                Toast.makeText(getApplicationContext(), "are you want to cancel", Toast.LENGTH_SHORT).show();
                break;


        }
    }
    private class JSONParse extends AsyncTask<String, Integer, ArrayList<HashMap<String, String>>> {

        ArrayList<HashMap<String, String>> newsList = new ArrayList<HashMap<String, String>>();


        private ProgressDialog pDialog;

        protected void onPreExecute() {

            super.onPreExecute();
//            pDialog = new ProgressDialog(MainActivity.this);
//            pDialog.setMessage("Loading Data ...");
//            pDialog.setIndeterminate(false);
//            pDialog.setCancelable(true);
//            pDialog.show();
            MyProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(String... params) {

            Parser jParser = new Parser();
            JSONObject json = jParser.getJSONData(url);
            try{
                news = json.getJSONArray(NEWS);
                for(int i=0;i<news.length();i++){
                    JSONObject object = news.getJSONObject(i);
                    JSONObject newsline = object.getJSONObject(NEWS_LINE);
                    String headLine = newsline.getString(HEAD_LINE);
                    String dateLine = newsline.getString(DATE_LINE);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(HEAD_LINE, headLine);
                    map.put(DATE_LINE, dateLine);
                    newsList.add(map);
                }
            }catch(JSONException e){
                e.printStackTrace();
            }
            return newsList;
        }
        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> result) {


            super.onPostExecute(result);
            // pDialog.dismiss();
            MyProgressBar.setVisibility(View.GONE);
            Btngetdata.setEnabled(false);

            String []from = {HEAD_LINE,DATE_LINE};

            int []to = {R.id.tv_headLn,R.id.tv_dateLn};
            ListAdapter adapter = new SimpleAdapter(MainActivity.this, result, R.layout.listview_item, from, to);
            setListAdapter(adapter);

        }

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
}
