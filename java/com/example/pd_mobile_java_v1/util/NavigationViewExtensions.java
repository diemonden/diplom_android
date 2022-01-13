package com.example.pd_mobile_java_v1.util;

import android.view.LayoutInflater;

import com.example.pd_mobile_java_v1.databinding.NavHeaderMainBinding;
import com.example.pd_mobile_java_v1.main.MainActivityVM;
import com.google.android.material.navigation.NavigationView;

import androidx.databinding.BindingAdapter;

public class NavigationViewExtensions {

    @BindingAdapter({"bind:model"})
    public static void loadHeader(NavigationView view, MainActivityVM viewModel) {
        NavHeaderMainBinding binding = NavHeaderMainBinding.inflate(LayoutInflater.from(view.getContext()));
        binding.setViewModel(viewModel);
        binding.executePendingBindings();
        view.addHeaderView(binding.getRoot());
    }
}
