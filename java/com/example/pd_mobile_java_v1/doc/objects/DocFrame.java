package com.example.pd_mobile_java_v1.doc.objects;

import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.pd_mobile_java_v1.BR;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

public class DocFrame extends BaseObservable implements IDocItem {
    public String header;
    public String src;
    private boolean isSelected = false;

    public DocFrame(){
        this.header = "";
        this.src = "";
    }
    public DocFrame(String src) {
        this.header = src;
        this.src = src;
    }

    @Bindable
    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
        notifyPropertyChanged(BR.header);
    }

    @Bindable
    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
        notifyPropertyChanged(BR.src);
    }

    @BindingAdapter({ "app:loadFrameUrl" })
    public static void loadUrl(WebView view, String url) {
        view.setWebViewClient(new WebViewClient());
        view.getSettings().setJavaScriptEnabled(true);
        view.loadUrl(url);
    }


    @Override
    public String toString() {
        return "DocFrame{" +
                "header='" + header + '\'' +
                ", src='" + src + '\'' +
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
