package com.example.pd_mobile_java_v1.doc.objects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.example.pd_mobile_java_v1.BR;
import com.example.pd_mobile_java_v1.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

public class DocImage extends BaseObservable implements IDocItem {
    public String src;
    public String size;
    private boolean isSelected = false;

    public DocImage() {
        this.src = "";
        this.size = "sm100";
    }
    public DocImage(String src, String size) {
        this.src = src;
        this.size = size;
    }

    @Bindable
    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
        notifyPropertyChanged(BR.src);
    }

    @Bindable
    public String getSize() {
        //TODO: можно сюда запихнуть возвращение размера в зависимости от строки (или проигнорить вообще, на телефоне ж сидим)
        return size;
    }

    public void setSize(String size) {
        this.size = size;
        notifyPropertyChanged(BR.size);
    }

    @BindingAdapter({ "app:loadImageFromUrl" })
    public static void loadImage(ImageView view, String url) {
        if (!url.equals("")) {
            Picasso.get().load(url).into(view, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                }
                @Override
                public void onError(Exception e) {
                    view.setImageResource(R.drawable.hqdefault);
                }
            });
        } else {
            view.setImageResource(R.drawable.hqdefault);
        }
    }
    @Override
    public String toString() {
        return "DocImage{" +
                "src='" + src + '\'' +
                ", size='" + size + '\'' +
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
