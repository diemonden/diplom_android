package com.example.pd_mobile_java_v1.doc.objects;

import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.example.pd_mobile_java_v1.BR;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

public class DocLink extends BaseObservable implements IDocItem {
    public String text;
    public String link;
    private boolean isSelected = false;

    public DocLink() {
        this.text = "";
        this.link = "";
    }
    public DocLink(String text, String link) {
        this.text = text;
        this.link = link;
    }
    @Bindable
    public String getText(){
        return Html.fromHtml("<a href= '"+link+"'>"+text+"</a>").toString();
    }

    @Bindable
    public Spanned getLinkedText(){
        return Html.fromHtml("<a href= '"+link+"'>"+text+"</a>");
    }

    public void setText(String text) {
        this.text = text;
        notifyPropertyChanged(BR.text);
    }

    @Bindable
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
        notifyPropertyChanged(BR.link);
    }

    @BindingAdapter({"app:setLink"})
    public static void setupLinks(TextView view, DocLink docLink) {
        view.setText(docLink.getLinkedText());
        view.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public String toString() {
        return "DocLink{" +
                "text='" + text + '\'' +
                ", link='" + link + '\'' +
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
