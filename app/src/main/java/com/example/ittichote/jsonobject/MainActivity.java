package com.example.ittichote.jsonobject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView jsonListview ;
    private ArrayList<String> exData;
    private ProgressDialog ProgressDialog;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        jsonListview=(ListView)findViewById(R.id.json_listview);

        exData = new ArrayList<String>();


        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                ProgressDialog = new ProgressDialog(MainActivity.this);
                ProgressDialog.setCancelable(false);
                ProgressDialog.setMessage("Downloading.....");
                ProgressDialog.show();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    URL url = new URL("http://thaicfp.com/webservices/json-example.php");
                    URLConnection urlConnection = url.openConnection();

                    HttpURLConnection httpURLConnection = (HttpURLConnection)urlConnection;
                    httpURLConnection.setFollowRedirects(true);
                    httpURLConnection.setAllowUserInteraction(false);
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.connect();
                    InputStream inputStream = null;
                    if(httpURLConnection.getResponseCode()==HttpURLConnection.HTTP_OK)
                        inputStream = httpURLConnection.getInputStream();

                    BufferedReader reader =new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"),8);
                    StringBuilder stringBuilder = new StringBuilder();
                    String line = null;

                    while ((line=reader.readLine()) != null)
                    {
                        stringBuilder.append(line+"\n");

                    }
                    inputStream.close();
                    Log.d("JSON Result",stringBuilder.toString());

                    JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                    JSONArray exArray = jsonObject.getJSONArray("students");

                    for(int i=0 ; i<exArray.length();++i)
                    {
                         JSONObject jsonObj = exArray.getJSONObject(i);
                        exData.add(jsonObj.getString("student_name"));
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                ArrayAdapter<String> myAdapter = new ArrayAdapter<String> (MainActivity.this,android.R.layout.simple_list_item_1,android.R.id.text1,exData);
                jsonListview.setAdapter(myAdapter);
                ProgressDialog.dismiss();

            }
        }.execute();

    }
}
