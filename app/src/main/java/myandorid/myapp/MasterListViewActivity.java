package myandorid.myapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MasterListViewActivity extends AppCompatActivity {


    private ListView listView;
    private List<HashMap<String, Object>> list;
    //private JSONAdapter jSONAdapter;
    private SimpleAdapter simpleAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_master_list_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.listViewMaster);


        String apiUri = "http://apisample20160313120456.azurewebsites.net/Product/GetProducts";
        GetAsyncTask httpwork = new GetAsyncTask();
        httpwork.execute(apiUri);
//

//        String[] from =  { "name","price" };
//        int[] to = {android.R.id.text1, android.R.id.text2 };
//
//        simpleAdapter = new SimpleAdapter(
//                this,
//                list,
//                android.R.layout.simple_list_item_2,
//                from,
//                to);
//        listView.setAdapter(simpleAdapter);

//        ListAdapter listAdapter = new SimpleAdapter(
//                this,
//                list,
//                android.R.layout.simple_list_item_2,
//                new String[]{"name","price"},
//                new int[]{android.R.id.text1, android.R.id.text2});
//        listView.setAdapter(listAdapter);


    }


    private class GetAsyncTask extends AsyncTask<String, Void, JSONArray> {

        @Override
        protected JSONArray doInBackground(String... url) {

            JSONArray jsonArray = null;
            try {

                String result = GET(url[0]);
                jsonArray = new JSONArray(result);
                return jsonArray;

            } catch (Exception ex) {

                //Log.e("JSONArray：", ex.getMessage());
                return jsonArray;
            }
        }


        @Override
        protected void onPostExecute(JSONArray jsonResult) {


//            [
//            {
//                "Name": "Apple Mac",
//                    "Price": 1000,
//                    "ImageUrl": null
//            },
//            {
//                "Name": "Mircosoft Book",
//                    "Price": 500,
//                    "ImageUrl": null
//            },
//            {
//                "Name": "Google Book",
//                    "Price": 800,
//                    "ImageUrl": null
//            }
//            ]


            try {


//              List<HashMap<String,String>> list = new ArrayList<>();
//                list = new ArrayList<>();
                JSONArray jsonArray = new JSONArray(jsonResult);
                for (int i=0;i<jsonArray.length();i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    HashMap<String,  Object> hashMap = new HashMap<>();
                    hashMap.put("name",jsonObject.getString("Name"));
//                    hashMap.put("price","Price:" + jsonObject.getInt("Price"));
//                    list.add(hashMap);
                }

            } catch (JSONException ex) {
                //E/OpenGLRenderer: Getting MAX_TEXTURE_SIZE from Caches::initConstraints()
                //E/OpenGLRenderer: MAX_TEXTURE_SIZE: 16384

                //ex.printStackTrace();
                 Log.d("JSONException", ex.getLocalizedMessage());
            }


        }

        public String GET(String url) {

            InputStream inputStream = null;
            String result = "";
            try {

                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);
                HttpResponse httpResponse = httpclient.execute(httpGet);

                StatusLine statusLine = httpResponse.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200) {

                    inputStream = httpResponse.getEntity().getContent();
                    if (inputStream != null) {

                        result = convertInputStreamToString(inputStream);
                    } else {

                        result = "Did not work!";
                    }
                }
            } catch (Exception ex) {

                //Log.d("InputStream", ex.getLocalizedMessage());
            }
            return result;
        }

        private String convertInputStreamToString(InputStream inputStream) throws IOException {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            String result = "";
            while ((line = bufferedReader.readLine()) != null)
                result += line;

            inputStream.close();
            return result;
        }
    }

}


//http://stackoverflow.com/questions/10879592/how-to-load-data-to-custom-listview-from-json-array


