package com.example.pd_mobile_java_v1.main;

import android.content.Intent;
import android.view.Menu;

import com.example.pd_mobile_java_v1.R;
import com.example.pd_mobile_java_v1.model.User;
import com.example.pd_mobile_java_v1.util.AppUtility;
import com.stfalcon.androidmvvmhelper.mvvm.activities.ActivityViewModel;
import com.stfalcon.androidmvvmhelper.mvvm.fragments.BindingFragment;

import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;

public class MainActivityVM extends ActivityViewModel<MainActivity> {

    //view
    private MainActivity activity;
    //data
    private User user;
    //
    private AppBarConfiguration mAppBarConfiguration;

    public MainActivityVM(MainActivity activity) {
        super(activity);
        this.activity = (MainActivity) activity;
        user = new User(AppUtility.getDataFromIntent(activity,"data"));
    }
    @Override
    public void onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        activity.getMenuInflater().inflate(R.menu.main, menu);
    }

    @Override
    public void onStart() {
        //Override me!
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Override me!
        BindingFragment fragment = activity.getFragment();
        fragment.getViewModel().onActivityResult(requestCode,resultCode,data);
    }
    @Override
    public void onResume() {
        //Override me!
    }

    public User getUser() {
        return user;
    }


}
