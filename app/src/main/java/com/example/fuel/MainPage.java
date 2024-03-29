package com.example.fuel;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MainPage extends AppCompatActivity {
    private EditText tt;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main_page);
        tt = findViewById(R.id.editTextTextPersonName11);
        btn = findViewById(R.id.button8);
        String u = getIntent().getStringExtra("Username");
        System.out.println(u);
        btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                String t = tt.getText().toString();
                System.out.println(t.length());
                if(t.equals("") || t.equals("0")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainPage.this);
                    builder.setMessage("Please enter the Valid Quantity!!!").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();

                }

                else if(t.length()>=3){

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainPage.this);
                    builder.setMessage("Enter the quantity below 100kg!!!").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else{
//                    email ob = new email();
//                    ob.checkPass(u,t,"");
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainPage.this);
                    builder.setMessage("Our team will reach you through E-mail!!!").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });
    }
    class email {
        private void checkPass(String string1, String string2,String string3) {
            new MainPage.email.CheckPassTask().execute(string1, string2,string3);
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
                System.out.println(string1+" "+string2+" "+string3);
                try {

                    URL url = new URL("https://api-mongodb-putp.onrender.com/sendEmail");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);

                    String postData = "username=" + string1 + "&email=" + string3+"&kg="+string2;
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
                        System.out.println(response.toString());
                        reader.close();

                        result = response.toString();



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
}