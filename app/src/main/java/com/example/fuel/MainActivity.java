//This is the backend page for login.
package com.example.fuel;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //Required fields are declared here.
    private Button signupp;
    private Button login;
    private EditText username;
    private EditText password;
    String q;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //First 4 lines connects the xml file to the backend and makes the view to fullscreen.
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        //the declared fields are connected with the frontend.
        signupp = findViewById(R.id.button);
        login = findViewById(R.id.button3);
        username = findViewById(R.id.editTextTextPersonName);
        password = findViewById(R.id.editTextTextPersonName2);
        signupp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(),Signup.class);
                startActivity(in);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String u = username.getText().toString();
                String p = password.getText().toString();
                if(u.equals("") || p.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Please fill all the Fields!!!").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else {
//                    GETAPI getApi = new GETAPI();
//                    String url = "https://api-mongodb-putp.onrender.com/";
//                    getApi.checkPass(u, p);
                    Intent in = new Intent(getApplicationContext(),MainPage.class);
                    startActivity(in);
                    //finish();
                }

            }
        });
    }
     class GETAPI {
         private void checkPass(String string1, String string2) {
             new GETAPI.CheckPassTask().execute(string1, string2);
         }

    private class CheckPassTask extends AsyncTask<String, Void, String> {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... strings) {

            String string1 = strings[0];
            String string2 = strings[1];
            int f=0;
            String result = "";

            try {

                URL url = new URL("https://api-mongodb-putp.onrender.com/check");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);

                String postData = "username=" + string1 + "&password=" + string2;
                byte[] postDataBytes = postData.getBytes(StandardCharsets.UTF_8);

                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));

                try (OutputStream outputStream = connection.getOutputStream()) {
                    outputStream.write(postDataBytes);
                }

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    //System.out.println(response.toString());
                    reader.close();

                    result = response.toString();
                    if(result.equals("true")){
                        //profile ob = new profile();
                        //ob.u1.setText(string1);
                        Intent in = new Intent(getApplicationContext(),profile.class);
                        Intent inn = new Intent(getApplicationContext(),Signup.class);
                        in.putExtra("Username",string1);
                        inn.putExtra("Username",string1);
                        startActivity(in);
                        finish();

                    }
                    else if(result.equals("GoTo")){
                        Intent in = new Intent(getApplicationContext(),MainPage.class);
                        in.putExtra("Username",string1);
                        startActivity(in);
                        finish();
                    }
                    else{
                        //System.out.println(result);
                        send ob =new send();
                        ob.se(result);
                    }

                } else {
                    Log.e("CheckPassTask", "Failed to send POST request. Response Code: " + responseCode);
                }

                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }


    }
           }

    class send{
        public void se(String a){
            q=new String(a);
            //System.out.println(q);
            if(q.equals("false")){
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show());
            }
        }
    }
}

