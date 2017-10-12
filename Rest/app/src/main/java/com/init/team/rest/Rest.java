package com.init.team.rest;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Rest extends AppCompatActivity implements View.OnClickListener {
    Button button;
    StringBuffer stringBuffer;
    String city;
    TextView textView;
    EditText editText;
    BufferedReader reader;
    JSONObject jsonObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest);

        button = (Button) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.textView);
        editText = (EditText) findViewById(R.id.editText);

        button.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        city = editText.getText().toString();
        new JsonTask(this).execute();
    }

    class JsonTask extends AsyncTask<Void , Void , JSONObject>
    {

        private Context context;
        public JsonTask(Context context){
            this.context =  context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {

            URL url;
            stringBuffer = new StringBuffer();
            String line;

            String request = "http://api.openweathermap.org/data/2.5/weather?q=" + city
                    + "&appid=1a95fc59d9300009dccfe8e2bf99eb88";


            try {
                url = new URL(request);
                URLConnection  connection = url.openConnection();
              reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                while ((line = reader.readLine()) != null){
                    stringBuffer.append(line);

            jsonObject = new JSONObject(stringBuffer.toString());
            publishProgress();

                }

            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                if(reader != null){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                String temp = jsonObject.getJSONObject("main").getString("temp");
                int tempC = (int) Math.round(Double.valueOf(temp)) - 273;
                temp = String.valueOf(tempC);
                textView.setText(temp + " degrees");


            } catch (JSONException e) {
                e.printStackTrace();
            }
            Toast.makeText(context , "Succes!" , Toast.LENGTH_LONG).show();
            super.onPostExecute(jsonObject);
        }
    }

    public String enterCity(String cityName){
        return cityName;
    }

}
