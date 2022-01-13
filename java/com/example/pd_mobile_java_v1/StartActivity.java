package com.example.pd_mobile_java_v1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pd_mobile_java_v1.main.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        final TextView errText = (TextView) findViewById(R.id.err_text);
        Button login_button = (Button) findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View root) {
                Intent intent = new Intent(StartActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        Button reg_button = (Button) findViewById(R.id.reg_button);
        reg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View root) {
                Intent intent = new Intent(StartActivity.this,RegActivity.class);
                startActivity(intent);
            }
        });

        Button fast_login_button = (Button) findViewById(R.id.fast_login_button);
        fast_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View root) {
                try {
                    RequestQueue queue = Volley.newRequestQueue(StartActivity.this);
                    String url ="http://192.168.0.107/pd_mobile/login/signIn";
                    // Request a string response from the provided URL.
                    JSONObject jsonRequest = new JSONObject();
                    try {
                        jsonRequest.put("email", "sh@u.e");
                        jsonRequest.put("pass", "aboba322");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                            jsonRequest,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    // Display response string.
                                    //errText.setText (response.toString());
                                    String url1 = "http://192.168.0.107/pd_mobile/main/getData";
                                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url1,
                                            null,
                                            new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    // Display response string.
                                                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    intent.putExtra("data",response.toString());
                                                    try {
                                                        startActivity(intent);
                                                    } catch (Exception e){
                                                        errText.setText("Error1: "+e.toString());
                                                    }
                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    errText.setText("Error: "+error.getMessage());

                                                }
                                            });

                                    // Add the request to the RequestQueue.
                                    queue.add(jsonObjectRequest);

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    errText.setText("Error: "+error);
                                }
                            });
                    // Add the request to the RequestQueue.
                    queue.add(jsonObjectRequest);
                } catch (Exception e) {
                    errText.setText(e.getMessage());
                }
            }
        });
    }

}