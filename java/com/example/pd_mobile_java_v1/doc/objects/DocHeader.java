package com.example.pd_mobile_java_v1.doc.objects;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.pd_mobile_java_v1.BR;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

public class DocHeader extends BaseObservable implements IDocItem {
    public String text;
    private boolean isSelected = false;

    public DocHeader() {
        this.text = "";
    }
    public DocHeader(String text) {
        this.text = text;
    }

    @Bindable
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        notifyPropertyChanged(BR.text);
    }
    @Override
    public String toString() {
        return "DocHeader{" +
                "text='" + text + '\'' +
                '}';
    }

    @Override
    @Bindable
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public void setSelected(boolean selected) {
        isSelected = selected;
        notifyPropertyChanged(BR.selected);
    }
}
