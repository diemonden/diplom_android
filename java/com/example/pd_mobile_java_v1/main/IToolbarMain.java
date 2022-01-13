package com.example.pd_mobile_java_v1.main;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;
import android.view.View;

import com.example.pd_mobile_java_v1.R;
import com.stfalcon.androidmvvmhelper.mvvm.fragments.BindingFragment;
import com.stfalcon.androidmvvmhelper.mvvm.fragments.FragmentViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public interface IToolbarMain {

    default AppCompatActivity unwrap(Context context) {
        while (!(context instanceof AppCompatActivity) && context instanceof ContextWrapper) {
            context = ((ContextWrapper) context).getBaseContext();
        }
        return (AppCompatActivity) context;
    }
    default BindingFragment getFragment(View view){
        AppCompatActivity activity = unwrap(view.getContext());
        return (BindingFragment) activity.getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment).getChildFragmentManager().getFragments().get(0);
    }

    default void onClose(View view){
        FragmentViewModel viewModel = getFragment(view).getViewModel();
        if ((viewModel instanceof IToolbarMain))
            ((IToolbarMain) viewModel).onClose(view);
        Log.d("CDA", "onClose Called");


    }
    default void onShare(View view){
        Fragment fragment = getFragment(view);
        if ((fragment instanceof IToolbarMain))
            ((IToolbarMain) fragment).onShare(view);
        Log.d("CDA", "onShare Called");
    };
    default void onBookmarks(View view){
        FragmentViewModel viewModel = getFragment(view).getViewModel();
        if ((viewModel instanceof IToolbarMain))
            ((IToolbarMain) viewModel).onBookmarks(view);
        Log.d("CDA", "onAddBookmarks Called");
    };


    default void onRename(View view){
        FragmentViewModel viewModel = getFragment(view).getViewModel();
        if ((viewModel instanceof IToolbarMain))
            ((IToolbarMain) viewModel).onRename(view);
        Log.d("CDA", "onRename Called");
    };
    default void onDelete(View view){
        FragmentViewModel viewModel = getFragment(view).getViewModel();
        if ((viewModel instanceof IToolbarMain))
            ((IToolbarMain) viewModel).onDelete(view);
        Log.d("CDA", "onDelete Called");
    };
    default void onMove(View view){

        Fragment fragment = getFragment(view);
        if ((fragment instanceof IToolbarMain))
            ((IToolbarMain) fragment).onMove(view);
        Log.d("CDA", "onMove Called");
    };

}
