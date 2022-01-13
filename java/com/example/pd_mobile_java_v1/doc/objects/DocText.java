package com.example.pd_mobile_java_v1.doc.objects;

import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.pd_mobile_java_v1.BR;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

public class DocText extends BaseObservable implements IDocItem{
    public String text;
    private boolean isSelected = false;

    public DocText(){
        this.text = "";
    }
    public DocText(String text) {
        this.text = text;
    }

    @Bindable
    public String getText() {
        return text;
    }
    @Bindable
    public Spanned getHtmlText() {
        return Html.fromHtml(text);
    }

    public void setText(String text) {
        this.text = text;
        notifyPropertyChanged(BR.text);
    }

    @BindingAdapter({"app:textLinks"})
    public static void setupLinks(TextView view, DocText docText) {
        view.setText(docText.getHtmlText());
        view.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public String toString() {
        return "DocText{" +
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
