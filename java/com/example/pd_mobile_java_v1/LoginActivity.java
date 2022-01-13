package com.example.pd_mobile_java_v1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        final EditText f_email = findViewById(R.id.editTextEmailAddress);
        final EditText f_password = findViewById(R.id.editTextPassword);
        final Button button = findViewById(R.id.button);
        final TextView textView = findViewById(R.id.loginSuccessText);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View root) {
                try {
                    RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                    String url ="http://192.168.0.107/pd_mobile/login/signIn";
                    // Request a string response from the provided URL.
                    JSONObject jsonRequest = new JSONObject();
                    try {
                        jsonRequest.put("email", f_email.getText().toString());
                        jsonRequest.put("pass", f_password.getText().toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                            jsonRequest,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    // Display response string.
                                    try {
                                        if (response.getBoolean("res")){
                                            String url1 = "http://192.168.0.107/pd_mobile/main/getData";
                                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url1,
                                                    null,
                                                    new Response.Listener<JSONObject>() {
                                                        @Override
                                                        public void onResponse(JSONObject response) {
                                                            // Display response string.
                                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                            intent.putExtra("data",response.toString());
                                                            try {
                                                                startActivity(intent);
                                                            } catch (Exception e){
                                                                textView.setText("Error1: "+e.toString());
                                                            }
                                                        }
                                                    },
                                                    new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError error) {
                                                            textView.setText("Error: "+error.getMessage());

                                                        }
                                                    });
                                            queue.add(jsonObjectRequest);
                                        }else {
                                            textView.setText("WRONG PASSWORD OR LOGIN. TRY AGAIN");
                                        }

                                    }catch (JSONException e){
                                        textView.setText("Error: "+e.toString());
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    textView.setText("Error: "+error);
                                }
                            });
                    // Add the request to the RequestQueue.
                    queue.add(jsonObjectRequest);
                } catch (Exception e) {
                    textView.setText(e.getMessage());
                }

            }
        });
    }
}