package com.example.pd_mobile_java_v1.main.home;

import com.example.pd_mobile_java_v1.BR;
import com.example.pd_mobile_java_v1.R;
import com.example.pd_mobile_java_v1.databinding.FragmentHomeBinding;
import com.stfalcon.androidmvvmhelper.mvvm.fragments.BindingFragment;


public class HomeFragment extends BindingFragment<HomeFragmentVM, FragmentHomeBinding>{
    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment getInstance() {
        return new HomeFragment();
    }
    @Override
    protected HomeFragmentVM onCreateViewModel(FragmentHomeBinding fragmentHomeBinding) {
        return new HomeFragmentVM(this);
    }

    @Override
    public int getVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

}
