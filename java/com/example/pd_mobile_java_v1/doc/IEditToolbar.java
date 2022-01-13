package com.example.pd_mobile_java_v1.doc;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;
import android.view.View;

import com.example.pd_mobile_java_v1.R;
import com.stfalcon.androidmvvmhelper.mvvm.fragments.BindingFragment;
import com.stfalcon.androidmvvmhelper.mvvm.fragments.FragmentViewModel;

import androidx.appcompat.app.AppCompatActivity;

public interface IEditToolbar {

    default AppCompatActivity unwrap(Context context) {
        while (!(context instanceof AppCompatActivity) && context instanceof ContextWrapper) {
            context = ((ContextWrapper) context).getBaseContext();
        }
        return (AppCompatActivity) context;
    }

    default BindingFragment getFragment(View view){
        AppCompatActivity activity = unwrap(view.getContext());
        return (BindingFragment) activity.getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_doc).getChildFragmentManager().getFragments().get(0);
    }

    default void onUp(View view){
        FragmentViewModel viewModel = getFragment(view).getViewModel();
        if ((viewModel instanceof IEditToolbar))
            ((IEditToolbar) viewModel).onUp(view);
        Log.d("CDA", "onUp Called");
    }

    default void onDown(View view){
        FragmentViewModel viewModel = getFragment(view).getViewModel();
        if ((viewModel instanceof IEditToolbar))
            ((IEditToolbar) viewModel).onDown(view);
        Log.d("CDA", "onDown Called");
    }

    default void onDelete(View view){
        FragmentViewModel viewModel = getFragment(view).getViewModel();
        if ((viewModel instanceof IEditToolbar))
            ((IEditToolbar) viewModel).onDelete(view);
        Log.d("CDA", "onSave Called");
    }

    default void onSave(View view){
        FragmentViewModel viewModel = getFragment(view).getViewModel();
        if ((viewModel instanceof IEditToolbar))
            ((IEditToolbar) viewModel).onSave(view);
        Log.d("CDA", "onSave Called");
    }

    default void onRename(View view){
        FragmentViewModel viewModel = getFragment(view).getViewModel();
        if ((viewModel instanceof IEditToolbar))
            ((IEditToolbar) viewModel).onRename(view);
        Log.d("CDA", "onRename Called");
    }

    default void onAddText(View view){
        FragmentViewModel viewModel = getFragment(view).getViewModel();
        if ((viewModel instanceof IEditToolbar))
            ((IEditToolbar) viewModel).onAddText(view);
        Log.d("CDA", "onAddText Called");
    }
    default void  onAddHeader(View view){
        FragmentViewModel viewModel = getFragment(view).getViewModel();
        if ((viewModel instanceof IEditToolbar))
            ((IEditToolbar) viewModel).onAddHeader(view);
        Log.d("CDA", "onAddHeader Called");
    }
    default void  onAddLink(View view){
        FragmentViewModel viewModel = getFragment(view).getViewModel();
        if ((viewModel instanceof IEditToolbar))
            ((IEditToolbar) viewModel).onAddLink(view);
        Log.d("CDA", "onAddLink Called");
    }
    default void  onAddImage(View view){
        FragmentViewModel viewModel = getFragment(view).getViewModel();
        if ((viewModel instanceof IEditToolbar))
            ((IEditToolbar) viewModel).onAddImage(view);
        Log.d("CDA", "onAddImage Called");
    }

    default void  onAddFrame(View view){
        FragmentViewModel viewModel = getFragment(view).getViewModel();
        if ((viewModel instanceof IEditToolbar))
            ((IEditToolbar) viewModel).onAddFrame(view);
        Log.d("CDA", "onAddFrame Called");
    }

    default void  onAddMedia(View view){
        FragmentViewModel viewModel = getFragment(view).getViewModel();
        if ((viewModel instanceof IEditToolbar))
            ((IEditToolbar) viewModel).onAddMedia(view);
        Log.d("CDA", "onAddMedia Called");
    }
}
