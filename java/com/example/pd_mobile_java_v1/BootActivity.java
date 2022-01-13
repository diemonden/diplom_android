package com.example.pd_mobile_java_v1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pd_mobile_java_v1.main.MainActivity;

import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;


public class BootActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boot);

        //start CookieManager
        CookieHandler.setDefault( new CookieManager(new SiCookieStore(this), CookiePolicy.ACCEPT_ALL));

        final TextView bootText = findViewById(R.id.boot_text);
        try {
            RequestQueue queue = Volley.newRequestQueue(this);
            String url ="http://192.168.0.107/pd_mobile/login/checkLogged";
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display response string.
                            if(response.equals("false")){
                                Intent intent = new Intent(BootActivity.this, StartActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                try {
                                    startActivity(intent);
                                } catch (Exception e){
                                    bootText.setText("Error1: "+e.toString());
                                }
                            }
                            else {
                                String url1 = "http://192.168.0.107/pd_mobile/main/getData";
                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url1,
                                        null,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                // Display response string.
                                                    Intent intent = new Intent(BootActivity.this, MainActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    intent.putExtra("data",response.toString());
                                                    try {
                                                        //bootText.setText(response.getJSONArray("bookmarks").toString());
                                                        startActivity(intent);
                                                    } catch (Exception e){
                                                        bootText.setText("Error1: "+e.toString());
                                                    }
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                bootText.setText("Error: "+error.getMessage());

                                            }
                                        });

                                // Add the request to the RequestQueue.
                                queue.add(jsonObjectRequest);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            bootText.setText("Error: "+error.getMessage());
                        }
                    });
            // Add the request to the RequestQueue.
            queue.add(stringRequest);
        } catch (Exception e) {
                bootText.setText(e.getMessage());
        }
    }
}