package com.example.pd_mobile_java_v1.util;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;

public class AppUtility {
    public static JSONObject getDataFromIntent(AppCompatActivity context, String data_string){
        JSONObject data = new JSONObject();
        Intent intent = context.getIntent();
        if(intent.hasExtra(data_string)) {
            try {
                data = new JSONObject(Objects.requireNonNull(intent.getStringExtra(data_string)));
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return data;
    }
    public static ArrayList<View> getAllChildren(View v) {

        if (!(v instanceof ViewGroup)) {
            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            return viewArrayList;
        }

        ArrayList<View> result = new ArrayList<View>();

        ViewGroup viewGroup = (ViewGroup) v;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {

            View child = viewGroup.getChildAt(i);

            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            viewArrayList.addAll(getAllChildren(child));

            result.addAll(viewArrayList);
        }
        return result;
    }
}
