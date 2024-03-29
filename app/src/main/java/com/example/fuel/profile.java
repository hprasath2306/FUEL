package com.example.fuel;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class profile extends AppCompatActivity {
    public TextView u1;
    private EditText u2;
    private EditText u3;
    private EditText u4;
    private EditText u5;
    private Button bt;
    private Button b7;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_profile);
        u1 = findViewById(R.id.editTextTextPersonName6);
        u2 = findViewById(R.id.editTextTextPersonName7);
        u3 = findViewById(R.id.editTextTextPersonName8);
        u4 = findViewById(R.id.editTextTextPersonName9);
        u5 = findViewById(R.id.editTextTextPersonName10);
        bt = findViewById(R.id.button6);
        b7 = findViewById(R.id.button7);
        String u = getIntent().getStringExtra("Username");
        u1.setText(u);
        b7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(in);
            }
        });
        bt.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                //String a1 = u1.getText().toString();
                String a2 = u2.getText().toString();
                String a3 = u3.getText().toString();
                String a4 = u4.getText().toString();
                String a5 = u5.getText().toString();
                if( a2.equals("") || a3.equals("") || a4.equals("") || a5.equals("") )
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(profile.this);
                    builder.setMessage("Please fill all the Fields!!!").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else{
                    sendPostRequestProfile(u,a2,a3,a4,a5);
                    AlertDialog.Builder builder = new AlertDialog.Builder(profile.this);
                    builder.setMessage("Profile Details Updated Successfully!!!").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                            Intent in = new Intent(getApplicationContext(),MainPage.class);
                            in.putExtra("Username",u);
                            startActivity(in);
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
     static void sendPostRequestProfile(String string1, String string2, String string3, String string4, String string5) {
        SendPostRequestProfileTask task = new SendPostRequestProfileTask();
        task.execute(string1, string2, string3, string4, string5);
    }
    private static class SendPostRequestProfileTask extends AsyncTask<String, Void, Void> {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected Void doInBackground(String... strings) {
            String string1 = strings[0];
            String string2 = strings[1];
            String string3 = strings[2];
            String string4 = strings[3];
            String string5 = strings[4];

            try {
                URL url = new URL("https://api-mongodb-putp.onrender.com/profile");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);

                String postData = "username=" + string1 + "&address=" + string2 + "&shop_name=" + string3 + "&shop_owner_name=" + string4 + "&mobile=" + string5;
                byte[] postDataBytes = postData.getBytes(StandardCharsets.UTF_8);

                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));

                try (OutputStream outputStream = connection.getOutputStream()) {
                    outputStream.write(postDataBytes);
                }

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    System.out.println("POST request sent successfully.");
                } else {
                    System.out.println("Failed to send POST request. Response Code: " + responseCode);
                }

                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;

        }
    }
}