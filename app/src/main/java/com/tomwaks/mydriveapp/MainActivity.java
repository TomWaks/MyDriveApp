package com.tomwaks.mydriveapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {


    List<String> folders = new ArrayList<String>();
    List<Integer> n_elements = new ArrayList<Integer>();

    LinearLayout ll_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ll_main = findViewById(R.id.ll_main);

        new LoadingListFolderRoot().execute("http://192.168.1.100?path=");
    }


    private class LoadingListFolderRoot extends AsyncTask<String, Integer, String> {

        OkHttpClient client = new OkHttpClient();

        // Runs in UI before background thread is called
        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            // Do something like display a progress bar
        }

        // This is run in a background thread
        @Override
        protected String doInBackground(String... params) {
            try {
                Response response = client.newCall(new Request.Builder()
                        .url(params[0])
                        .build()).execute();
                if (!response.isSuccessful()) {
                    return null;
                }
                JSONArray jA = new JSONArray(response.body().string());
                for (int i=0; i < jA.length(); i++)
                {
                    try {
                        JSONObject _path = jA.getJSONObject(i);
                        // Pulling items from the array
                        folders.add(_path.getString("folder"));
                        n_elements.add(_path.getInt("n_elements"));

                    } catch (JSONException e) {
                        // Oops
                    }
                }

                Log.d("test-main-activity3", folders.toString());
                Log.d("test-main-activity3", n_elements.toString());
                return "true";
            } catch (Exception e) {
                Log.d("test-main-activity3", e.toString());
                e.printStackTrace();
                return null;
            }
        }

        // This is called from background thread but runs in UI
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            // Do things like update the progress bar
        }

        // This runs in UI when background thread finishes
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if(result.equals("true")){
                for (int i = 0; i < folders.size(); i++){
                    LinearLayout part = new LinearLayout(MainActivity.this);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,0,1.0f);
                    params.setMargins(0, 8, 0, 0);
                    part.setLayoutParams(params);
                    part.setOrientation(LinearLayout.HORIZONTAL);
                    part.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
                    ll_main.addView(part);

                    ImageView icon = new ImageView(MainActivity.this);
                    LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    layoutParams.gravity=Gravity.CENTER;
                    icon.setImageResource(R.drawable.ic_folder_black_24dp);
                    icon.setPadding(10, 10, 10, 10);
                    icon.setLayoutParams(layoutParams);
                    part.addView(icon);

                    TextView name_element = new TextView(MainActivity.this);
                    name_element.setLayoutParams(new TableLayout.LayoutParams(0, TableLayout.LayoutParams.MATCH_PARENT,0.35f));
                    name_element.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorAccentLight));
                    name_element.setTextSize(20);
                    name_element.setGravity(Gravity.CENTER);
                    name_element.setText(folders.get(i));
                    part.addView(name_element);


                    TextView details_element = new TextView(MainActivity.this);
                    details_element.setLayoutParams(new TableLayout.LayoutParams(0, TableLayout.LayoutParams.MATCH_PARENT,0.65f));
                    details_element.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorAccentLight));
                    details_element.setTextSize(15);
                    details_element.setGravity(Gravity.CENTER);
                    details_element.setPadding(10, 0, 10, 0);
                    details_element.setText(n_elements.get(i)+"\nelements");
                    part.addView(details_element);
                }



            }

            // Do things like hide the progress bar or change a TextView
        }
    }
}



