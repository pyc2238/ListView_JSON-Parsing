package com.example.administrator.listview_study;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    public String myJson;
    private Button menu;
    private ListView listView;

    public static final int REQ_NUM = 1;
    public static final int RESULT_OK = 11;
    public static final int RESULT_NG = 12;
    public static final String TAG_RESULT = "result";
    public static final String TAG_ID = "menuName";
    public static final String TAG_NAME = "price";
    public static final String TAG_ADD = "isSpecial";
    public static String  ip = null;
    private ArrayList<HashMap<String,String>> personList;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        menu = (Button)findViewById(R.id.menu);
        listView = (ListView)findViewById(R.id.listview);
        personList = new ArrayList<HashMap<String,String>>();

        preferences = getSharedPreferences("ip",MODE_PRIVATE);
        String getip = preferences.getString("ip",null);

        if(getip != null){
            getData("http://"+ip+"/ajax/android.php");
        }

        
    menu.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            startActivity(new Intent(MainActivity.this, Pop.class));
            Intent intent = new Intent(MainActivity.this, Pop.class);
            intent.putExtra("message", "Hi!");
            startActivityForResult(intent, REQ_NUM);
        }
    });
}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQ_NUM){
            if(resultCode == RESULT_OK){
                if(data != null) {
                    String result = data.getStringExtra("result");
                    ip = result;
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("ip",ip);
                    editor.commit();

                    if(result != null){
                        Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
                        getData("http://"+ip+"/ajax/android.php");
                    }
                    else
                        Toast.makeText(this, "null result", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(this, "null data", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, "Result NG", Toast.LENGTH_SHORT).show();
        }
    }

    public void getData(String url){
        class GetDataJson extends AsyncTask<String,Void,String>{

            @Override
            protected String doInBackground(String... params) {
                String uri = params[0];
                if(params==null || params.length<1)
                    return null;
                BufferedReader bufferedReader = null;
                try{
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setDoInput(true);
                    con.setDefaultUseCaches(false);
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while((json = bufferedReader.readLine()) !=null){
                        sb.append(json+"\n");
                    }

                    return sb.toString().trim();

                }catch (Exception e){
                    return null;
                }
            }
            @Override
            protected void onPostExecute( String result){
                myJson = result;
                showList();
            }

        }

        GetDataJson g = new GetDataJson();
        g.execute(url);

    }

    protected  void showList(){
        try{
            JSONObject jsonObj = new JSONObject(myJson);
            JSONArray peoples = jsonObj.getJSONArray(TAG_RESULT);

            for(int i=0; i<peoples.length(); i++){
                JSONObject c = peoples.getJSONObject(i);
                String id = c.getString(TAG_ID);
                String name = c.getString(TAG_NAME);
                String address = c.getString(TAG_ADD);

                HashMap<String,String> persons = new HashMap<>();

                if(address == "true"){
                    persons.put(TAG_ID,id);
                    persons.put(TAG_NAME,name);
                    personList.add(persons);
                }
            }

            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this,personList,R.layout.text,
                    new String[]{TAG_ID,TAG_NAME},
                    new int []{R.id.menuName,R.id.price}
            );

            listView.setAdapter(adapter);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}