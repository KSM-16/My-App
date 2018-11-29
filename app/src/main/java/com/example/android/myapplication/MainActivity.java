package com.example.android.myapplication;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends Activity {
    TextView myText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myText = (TextView) findViewById(R.id.textView);

              //  new MyTask().execute("https://api.androidhive.info/contacts/");
        new MyTask().execute("https://www.googleapis.com/books/v1/volumes?q=android&maxResults=20");


    }
    private class MyTask extends AsyncTask<String, String, String> {


        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            JSONObject jo = null;
            try {
                jo= new JSONObject(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            StringBuilder re = new StringBuilder();
            // Getting JSON Array node
           // JSONObject jo=new JSONObject(result.toString());
            JSONArray item=(JSONArray) jo.optJSONArray("item");
            int ln=20;
            for(int i=0;i<ln;i++){
                String res="";
                if(item.isNull(i)==true) continue;
                JSONObject c=(JSONObject) item.optJSONObject(i);
                JSONObject js=(JSONObject) c.optJSONObject("volumeInfo");
                String title =js.optString("title");
                res+=title+"\n";
               // dt=dt+title;
               // Log.d(TAG,title+"0000");
                JSONArray authrs=(JSONArray) js.optJSONArray("authors");
                ArrayList<String> as=new ArrayList<String>();
                // String[] authors=new String[authrs.length()];
                String authors="";
                for (int j=0;j<authrs.length();j++){
                    authors=authrs.optString(j);
                 //   Log.d(TAG,authors+"1111");
                    as.add(authors);
                    res+=authors+"\n";
                }
                JSONObject imglink=(JSONObject) js.optJSONObject("imageLinks");
                String imagelnk=imglink.optString("thumbnail");
                res+=imagelnk+"\n";
                JSONObject jn=(JSONObject) c.optJSONObject("accessInfo");
                String web=jn.optString("webReaderLink");
                res+=web+"\n";
                re.append(res);
                Log.d("TAG", "onPostExecute: "+res);
            }
            myText.setText(re.toString());

        }
        @Override
        protected String doInBackground(String... strings) {
            StringBuilder result = new StringBuilder();

            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
            return result.toString();
        }

    }

}
