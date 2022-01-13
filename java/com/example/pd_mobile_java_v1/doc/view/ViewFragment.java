package com.example.pd_mobile_java_v1.doc.view;

import com.example.pd_mobile_java_v1.R;
import com.example.pd_mobile_java_v1.databinding.FragmentViewBinding;
import com.stfalcon.androidmvvmhelper.mvvm.fragments.BindingFragment;

import androidx.databinding.library.baseAdapters.BR;

public class ViewFragment extends BindingFragment<ViewFragmentVM, FragmentViewBinding> {
    @Override
    protected ViewFragmentVM onCreateViewModel(FragmentViewBinding fragmentViewBinding) {
        return new ViewFragmentVM(this);
    }

    @Override
    public int getVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_view;
    }


}
