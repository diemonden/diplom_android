package com.example.pd_mobile_java_v1.doc;

import android.content.Intent;
import android.os.Bundle;

import com.example.pd_mobile_java_v1.BR;
import com.example.pd_mobile_java_v1.R;
import com.example.pd_mobile_java_v1.databinding.ActivityDocBinding;
import com.example.pd_mobile_java_v1.databinding.ActivityMainBinding;
import com.example.pd_mobile_java_v1.dialogs.IRenameElement;
import com.example.pd_mobile_java_v1.dialogs.SaveConfirmDialog;
import com.example.pd_mobile_java_v1.main.MainActivityVM;
import com.example.pd_mobile_java_v1.util.IOnBackPressed;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.stfalcon.androidmvvmhelper.mvvm.activities.BindingActivity;
import com.stfalcon.androidmvvmhelper.mvvm.fragments.FragmentViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.text.Html.escapeHtml;

public class DocActivity
        extends BindingActivity<ActivityDocBinding, DocActivityVM>
        implements IEditToolbar, IRenameElement
{
    private NavController navController;
    private ImageButton ib_mode;
    public static final boolean MODE_VIEW = true;
    public static final boolean MODE_EDIT = false;
    boolean mode = MODE_VIEW;

    @Override
    public DocActivityVM onCreate() {
        Toolbar toolbar = findViewById(R.id.doc_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        View fragment = findViewById(R.id.nav_host_fragment_doc);

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_doc);
        ib_mode = findViewById(R.id.ib_mode);
        return new DocActivityVM(this);
    }

    public void onSwitchMode(View view) {
        if (mode == MODE_VIEW) {
            mode = MODE_EDIT;
            navController.navigate(R.id.EditFragment, null);
            ib_mode.setImageResource(R.drawable.outline_preview_black_24);
            findViewById(R.id.doc_toolbar_edit).setVisibility(View.VISIBLE);
        } else {
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        if (mode == MODE_EDIT) {
            switchModeBack();
            super.onBackPressed();
        }
        else {
            if(!(getViewModel().generateResult().equals(getViewModel().getContentStr()))) {
                SaveConfirmDialog saveConfirmDialog = new SaveConfirmDialog();
                Bundle args = new Bundle();
                args.putString("action", "onBackPressed");
                saveConfirmDialog.setArguments(args);
                saveConfirmDialog.show(getSupportFragmentManager(), "save_confirm_dialog");
            } else {
                super.onBackPressed();
            }

        }
    }
    private void switchModeBack(){
            mode = MODE_VIEW;
            ib_mode.setImageResource(R.drawable.outline_mode_edit_black_24);
            findViewById(R.id.doc_toolbar_edit).setVisibility(View.GONE);

    }

    @Override
    public int getVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_doc;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public void renameElement(String newTitle) {
        getViewModel().rename(newTitle);
    }
}