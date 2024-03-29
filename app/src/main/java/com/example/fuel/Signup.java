//this page is for sign up.
package com.example.fuel;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity {
    private Button loginn;
    private EditText Username;
    private EditText Password;
    private EditText Email;
    private Button Signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.signup);
        loginn = findViewById(R.id.button2);
        Username = findViewById(R.id.editTextTextPersonName3);
        Password = findViewById(R.id.editTextTextPersonName5);
        Email = findViewById(R.id.editTextTextPersonName4);
        Signup = findViewById(R.id.button5);
        loginn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(in);
            }
        });
        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = Username.getText().toString();
                String pwd = Password.getText().toString();
                String email = Email.getText().toString();
                if(user.equals("") || pwd.equals("") || email.equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Signup.this);
                    builder.setMessage("Please fill all the Fields!!!").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else{
                GETAPI po = new GETAPI();
                    Intent in = new Intent(getApplicationContext(),MainPage.class);
                    in.putExtra("Username",user);
                    in.putExtra("Email",email);
                po.checkPass(user,pwd,email);}
            }
        });
    }
    class GETAPI {
        private void checkPass(String string1, String string2,String string3) {
            new Signup.GETAPI.CheckPassTask().execute(string1, string2,string3);
        }

        private class CheckPassTask extends AsyncTask<String, Void, String> {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            protected String doInBackground(String... strings) {

                String string1 = strings[0];
                String string2 = strings[1];
                String string3=strings[2];
                int f=0;
                String result = "";

                try {

                    URL url = new URL("https://api-mongodb-putp.onrender.com/");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);

                    String postData = "username=" + string1 + "&password=" + string2+"&mail="+string3;
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
                        if(result.equals("Sent")){
                            Signup.send ob =new Signup.send();
                            ob.se();
                            Intent in = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(in);
                            finish();

                        }
                        else if(result.equals("User already exists")){
                            //Intent in = new Intent(getApplicationContext(),MainPage.class);
                            //startActivity(in);
                            see ob = new see();
                            ob.seeq();
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
        public void se(){
                runOnUiThread(() -> Toast.makeText(Signup.this, "Account created Successfully!!!", Toast.LENGTH_SHORT).show());
            }
    }
    class see{
        public void seeq(){
            runOnUiThread(() -> Toast.makeText(Signup.this, "Username already exists!!!", Toast.LENGTH_SHORT).show());
        }
    }
    }



