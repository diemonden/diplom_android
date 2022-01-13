package com.example.pd_mobile_java_v1.main.shared;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedFragmentVM extends ViewModel {

    private MutableLiveData<String> mText;

    public SharedFragmentVM() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}