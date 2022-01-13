package com.example.pd_mobile_java_v1.doc.edit;

import com.example.pd_mobile_java_v1.R;
import com.example.pd_mobile_java_v1.databinding.FragmentEditBinding;
import com.example.pd_mobile_java_v1.databinding.FragmentViewBinding;
import com.example.pd_mobile_java_v1.doc.view.ViewFragmentVM;
import com.stfalcon.androidmvvmhelper.mvvm.fragments.BindingFragment;

import androidx.databinding.library.baseAdapters.BR;

public class EditFragment extends BindingFragment<EditFragmentVM, FragmentEditBinding> {
    @Override
    protected EditFragmentVM onCreateViewModel(FragmentEditBinding fragmentEditBinding) {
        return new EditFragmentVM(this);
    }

    @Override
    public int getVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_edit;
    }
}
