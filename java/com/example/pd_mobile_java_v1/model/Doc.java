package com.example.pd_mobile_java_v1.model;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pd_mobile_java_v1.BR;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class Doc extends BaseObservable implements IListItem{
    private int id;
    private String title;
    private String content;
    private Date update_date;
    private boolean isContentLoaded;
    private boolean isSelected;

    public Doc(int id, String title, Date update_date) {
        setId(id);
        setTitle(title);
        setUpdate_date(update_date);
        setContent("");
        setContentLoaded(false);
        setSelected(false);
    }

    @Override
    @Bindable
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    @Override
    @Bindable
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        notifyPropertyChanged(BR.content);
    }

    @Override
    @Bindable
    public Date getUpdate_date() {
        return update_date;
    }

    @Bindable
    public String getStringDate() {
        Format formatter = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
        return formatter.format(update_date);
    }

    public void setUpdate_date(Date update_date) {
        this.update_date = update_date;
        notifyPropertyChanged(BR.update_date);
    }

    public boolean isContentLoaded() {
        return isContentLoaded;
    }

    public void setContentLoaded(boolean contentLoaded) {
        isContentLoaded = contentLoaded;
        notifyPropertyChanged(BR.update_date);
    }
    @Override
    public void setSelected(boolean selected) {
        isSelected = selected;
        notifyPropertyChanged(BR.selected);
    }
    @Override
    @Bindable
    public boolean isSelected() {
        return this.isSelected;
    }
    @Override
    public String toString() {
        return "Doc{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content=" + content +
                ", update_date=" + update_date +
                ", isContentLoaded="+isContentLoaded +
                '}';
    }


}
