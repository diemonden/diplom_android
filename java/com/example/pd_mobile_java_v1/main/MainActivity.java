package com.example.pd_mobile_java_v1.main;

import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.pd_mobile_java_v1.BR;
import com.example.pd_mobile_java_v1.R;
import com.example.pd_mobile_java_v1.StartActivity;
import com.example.pd_mobile_java_v1.databinding.ActivityMainBinding;
import com.example.pd_mobile_java_v1.dialogs.IAddElement;
import com.example.pd_mobile_java_v1.dialogs.IDeleteElements;
import com.example.pd_mobile_java_v1.dialogs.IRenameElement;
import com.example.pd_mobile_java_v1.main.bookmarks.BookmarksFragment;
import com.example.pd_mobile_java_v1.main.home.HomeFragment;
import com.example.pd_mobile_java_v1.main.home.HomeFragmentVM;
import com.example.pd_mobile_java_v1.util.IOnBackPressed;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.stfalcon.androidmvvmhelper.mvvm.activities.BindingActivity;
import com.stfalcon.androidmvvmhelper.mvvm.fragments.BindingFragment;
import com.stfalcon.androidmvvmhelper.mvvm.fragments.FragmentViewModel;

import org.json.JSONObject;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends BindingActivity<ActivityMainBinding, MainActivityVM>
        implements
        NavigationView.OnNavigationItemSelectedListener,
        IAddElement,
        IRenameElement,
        IDeleteElements,
        IToolbarMain
{
    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private NavController navController;
    private FloatingActionButton fab;

    @Override
    public MainActivityVM onCreate() {
        //setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Toolbar toolbar2 = findViewById(R.id.toolbar2);
        //setSupportActionBar(toolbar2);
        fab = findViewById(R.id.fab);
        //setup navigation
        drawer = findViewById(R.id.drawer_layout);
        navigationView =  findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_bookmarks, R.id.nav_shared)
                .setDrawerLayout(drawer)
                .build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);

        return new MainActivityVM(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //final TextView sessionText = context.findViewById(R.id.sessionText);
        int id = item.getItemId();
        RequestQueue queue = Volley.newRequestQueue(this);
        //TextView headerView = findViewById(R.id.selectedMenuItem);
        switch(id){
            case R.id.action_update:
                String url1 = "http://192.168.0.107/pd_mobile/main/getData";
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url1,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Display response string.

                                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.putExtra("data",response.toString());
                                startActivity(intent);
                                finish();
                                overridePendingTransition(0, 0);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("CDA e_upd: ",error.getMessage());

                            }
                        });

                // Add the request to the RequestQueue.
                queue.add(jsonObjectRequest);
                break;
            case R.id.action_logout:
                try {
                    String url ="http://192.168.0.107/pd_mobile/main/logout";
                    // Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    //sessionText.setText("Logout success");
                                    Intent intent = new Intent(MainActivity.this, StartActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                                    startActivity(intent);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    //sessionText.setText("Error: "+error);
                                }
                            });
                    // Add the request to the RequestQueue.
                    queue.add(stringRequest);
                } catch (Exception e) {
                    //sessionText.setText(e.getMessage());
                }
                break;
        }
        return NavigationUI.onNavDestinationSelected(item, Navigation.findNavController(this, R.id.nav_host_fragment))
                || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("CDA", "onBackPressed Called");
        FragmentViewModel viewModel = getFragment().getViewModel();
        if (viewModel instanceof IOnBackPressed)
            ( (IOnBackPressed) viewModel).onBackPressed();
    }

    @Override
    public int getVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    public BindingFragment getFragment(){
        return (BindingFragment) Objects.requireNonNull(getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment))
                .getChildFragmentManager()
                .getFragments()
                .get(0);
    }
    private void onSwitchFromFragment(){
        BindingFragment fragment = getFragment();
        if (fragment instanceof HomeFragment) {
            Log.d("CDA", "From HomeFragment");
            fab.setVisibility(View.INVISIBLE);
            ((HomeFragmentVM) fragment.getViewModel()).onClose(navigationView);
            findViewById(R.id.toolbar).setVisibility(View.VISIBLE);
            findViewById(R.id.toolbar_home).setVisibility(View.GONE);
        }
        if (fragment instanceof BookmarksFragment) {
            Log.d("CDA", "From BookmarksFragment");
            findViewById(R.id.toolbar).setVisibility(View.VISIBLE);
            findViewById(R.id.toolbar_bm).setVisibility(View.GONE);
        }
        /*
        if ((fragment instanceof SharedFragment)) {
            fab.setVisibility(View.INVISIBLE);
            findViewById(R.id.toolbar).setVisibility(View.VISIBLE);
            findViewById(R.id.toolbar2).setVisibility(View.GONE);
        }
         */
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            switch (id) {
                case R.id.nav_home:
                    onSwitchFromFragment();
                    navController.navigate(R.id.nav_home, null);
                    fab.setVisibility(View.VISIBLE);
                    break;
                case R.id.nav_bookmarks:
                    onSwitchFromFragment();
                    navController.navigate(R.id.nav_bookmarks, null);
                    break;
                case R.id.nav_shared:
                    //onSwitchFromFragment();
                    //navController.navigate(R.id.nav_shared, null);
                    break;
                case R.id.logout:
                    try {
                        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                        String url = "http://192.168.0.107/pd_mobile/main/logout";
                        // Request a string response from the provided URL.
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Intent intent = new Intent(MainActivity.this, StartActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        //sessionText.setText("Error: "+error);
                                    }
                                });
                        // Add the request to the RequestQueue.
                        queue.add(stringRequest);
                    } catch (Exception e) {
                        //sessionText.setText(e.getMessage());
                    }
                    break;
            }
            drawer.closeDrawers();
            return true;
    }

    @Override
    public void addElement(String title, boolean type) {
        FragmentViewModel viewModel = getFragment().getViewModel();
        if (viewModel instanceof IAddElement)
             ((IAddElement) viewModel).addElement(title,type);
    }

    @Override
    public void renameElement(String newTitle) {
        FragmentViewModel viewModel = getFragment().getViewModel();
        if (viewModel instanceof IRenameElement)
            ((IRenameElement) viewModel).renameElement(newTitle);
    }

    @Override
    public void cancelRename() {
        FragmentViewModel viewModel = getFragment().getViewModel();
        if (viewModel instanceof IRenameElement)
            ((IRenameElement) viewModel).cancelRename();
    }

    @Override
    public void deleteElements() {
        FragmentViewModel viewModel = getFragment().getViewModel();
        if (viewModel instanceof IDeleteElements)
            ((IDeleteElements) viewModel).deleteElements();
    }
}
