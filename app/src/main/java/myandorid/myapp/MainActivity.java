package myandorid.myapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
//    ctrl + / ==> //註解
//    ctrl + Alt + L ==> 排版
    // ctrl + B ==> 移至定義

    //宣告私有物件變數
    private EditText editAccount, editPassword;
    private Button butLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //指定變數 =以view上的物件id名稱取得物件並轉為物件型態
        editAccount = (EditText) findViewById(R.id.EditAccount);
        editPassword = (EditText) findViewById(R.id.EditPassword);
        butLogin = (Button) findViewById(R.id.BtnLogin);
        //設定Button Click事件 (setOnClickListener)傳入LoginOnClick名稱
        butLogin.setOnClickListener(LoginOnClick);
    }

    //宣告View的OnClickListener為LoginOnClick
    private View.OnClickListener LoginOnClick = new View.OnClickListener() {
        //Override onCick 方法 傳入View參數
        @Override
        public void onClick(View v) {
            try {
                //檢查帳號或密碼是否為空
                if (editAccount.getText().toString().trim().equals("") || editPassword.getText().toString().trim().equals("")) {
                    //以Toast方式顯示訊息
                    Toast.makeText(getApplicationContext(), "資料不能為空...", Toast.LENGTH_SHORT).show();

                } else {
                    //定義API URI
                    //?account=abc&password=12
                    String apiUri = "http://apisample20160313120456.azurewebsites.net/Login/Auth?";
                    String account = "account=" + editAccount.getText().toString();
                    String password = "&password=" + editPassword.getText().toString();
                    apiUri = apiUri + account + password;


                    GetAsyncTask httpwork = new GetAsyncTask();
                    httpwork.execute(apiUri);

                }


            } catch (Exception ex) {

                Log.e("the error：", ex.getMessage());

            }

        }
    };


    //JSON只是一個通稱，它的組成是由JSONObject或JSONArray所組成的，
    // 而其中的JSONObject其實就是一個JSON格式的物件，
    // JSONArray就是JSONObject所組成的List列表，
    // 一個JSONObject都是包含一個欄位Key與內容(格式如- "欄位Key":"資訊內容")，
    // JSONArray則是許多的"{ }"每個都是由JSONObject組成，每個{ }與{ }間用逗號區隔(格式如- {{"欄位Key":"資訊內容"},{"欄位Key":"資訊內容"},{"欄位Key":"資訊內容"}})

//    AsyncTask使用方法
//    構造函數 可以自定義public類型的構造函數，和普通的類沒有差別。
//    AsyncTask每次使用必須new 一個對象，並且執行excute(Params...)方法。 Params參數解釋見下文。
//    參數解釋
//    這三個參數會在類繼承時被指定，參數都使用泛型構造。
//
//    Params 後台線程所需要的參數列表，與構造類時所指定的第一個參數類型匹配。
//    Progress 後台線程執行過程中發送的進度信息， 與構造類時所指定的第二個參數類型匹配。
//    Result 後台線程執行完後的返回值， 與構造類時所指定的第三個參數類型匹配。

//    AsyncTask 執行的4個階段
//    step1
//    onPreExecute() 在主線程（UI線程）調用，用於開始任務前的初始化工作。例如顯示一個進度條。
//    step2
//    doInBackground(Params...) 後台線程調用，onPreExecute() 執行完之後立即執行。用於執行耗時較長的操作。並且提供publishProgress(Progress...)方法用於顯示進度信息。
//    step3
//    onProgressUpdate (Progress...)在主線程（UI線程）調用，用於響應publishProgress(Progress...)方法。
//    step4
//    onPostExecute(Result) 在主線程調用，doInBackground(Params...)方法執行完後立即調用。 Result是doInBackground(Params...)方法的返回值。
//

    //AsyncTask 是一個輕量級(耗時幾秒或者幾十秒的任務)的異步處理類。使用是需繼承自該類。可以方便的執行異步任務並且在將進度顯示在UI上。
    //宣告類別繼承AsyncTask
    private class GetAsyncTask extends AsyncTask<String, Void, JSONObject> {

        //doInBackground(Params...) 後台線程調用，onPreExecute() 執行完之後立即執行。
        //用於執行耗時較長的操作。並且提供publishProgress(Progress...)方法用於顯示進度信息。
        @Override
        protected JSONObject doInBackground(String... url) {

            JSONObject json = null;
            try {

                String result = GET(url[0]);
                json = new JSONObject(result);
                return json;

            } catch (Exception ex) {

                //Log.e("JSONObject：", ex.getMessage());
                return json;
            }
        }


        @Override
        protected void onPostExecute(JSONObject jsonResult) {

            String isSuccess = null; //定義為String
            String errorMessage = null;

            try {

                //{"IsSuccess": "false","ErrorMessage": "login failed" }或 {"IsSuccess":"true","ErrorMessage":""}
                isSuccess = jsonResult.getString("IsSuccess");
                errorMessage = jsonResult.getString("ErrorMessage");

                if (isSuccess.equals("true")) {

                    Toast.makeText(getApplicationContext(), "登入成功...", Toast.LENGTH_SHORT).show();

                    //Intent 是一種訊息傳遞的物件，可用來請求其它的 app 元件所提供的功能。作用:啟動 activity、啟動 service、傳送 broadcast
                    Intent intent = new Intent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setClass(MainActivity.this, MasterListViewActivity.class);
                    startActivity(intent);
                    finish(); //設定不要 Back 回登入頁面。

                } else {
                    editAccount.setText("");
                    editPassword.setText("");
                    Toast.makeText(getApplicationContext(), "登入失敗..." + errorMessage, Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException ex) {

                //ex.printStackTrace();
                Log.e("call api error：", ex.getMessage());
            }
        }

        public String GET(String url) {
            //InputStream
            InputStream inputStream = null;
            String result = "";
            try {

                //Connecting to the Network
                //创建HttpCilent对象
                HttpClient httpclient = new DefaultHttpClient();
                //创建一个HttpGet对象
                HttpGet httpGet = new HttpGet(url);
                //发送GET请求
                HttpResponse httpResponse = httpclient.execute(httpGet);

                //如果服务器成功地返回响应
                StatusLine statusLine = httpResponse.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200) {
                    //获取响应的字符串
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
