package com.example.pd_mobile_java_v1.doc.objects;

import android.webkit.WebView;

import com.example.pd_mobile_java_v1.BR;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

public class DocMedia extends BaseObservable implements IDocItem {
    public String src;
    private boolean isSelected = false;

    public DocMedia() {
        this.src = "";
    }
    public DocMedia(String src) {
        this.src = src;
    }

    @Bindable
    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
        notifyPropertyChanged(BR.src);
    }

    @BindingAdapter({ "app:loadMediaUrl" })
    public static void loadUrl(WebView view, String url) {
        String summary = "<!Doctype html><html><head><meta charset=utf-8></head><body>" + url + "</body></html>";
        view.getSettings().setJavaScriptEnabled(true);
        view.loadData(summary, "text/html; charset=utf-8", "utf-8");
    }
    @Override
    public String toString() {
        return "DocMedia{" +
                "src='" + src + '\'' +
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
