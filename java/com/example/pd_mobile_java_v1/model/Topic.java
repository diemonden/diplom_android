package com.example.pd_mobile_java_v1.model;

import android.annotation.SuppressLint;

import com.example.pd_mobile_java_v1.BR;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;


public class Topic extends BaseObservable implements IListItem{
    private int id;
    private String title;
    private Date update_date;
    private ArrayList<Topic> topics;
    private ArrayList<Doc> docs;
    private boolean isSelected;


    public Topic(int id, String title, Date update_date, JSONArray topics, JSONArray docs) {
        setId(id);
        setTitle(title);
        setUpdate_date(update_date);
        setTopics( new ArrayList<Topic>());
        setDocs(new ArrayList<Doc>());
        setSelected(false);
        //все с парент ид = 1
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            if (topics != null) {
                int len = topics.length();
                for (int i=0;i<len;i++){
                    if(topics.getJSONObject(i).getInt("parent_id") == this.id) {
                        JSONObject topic = topics.getJSONObject(i);
                        this.topics.add(new Topic(
                                topic.getInt("id"),
                                topic.getString("title"),
                                formatter.parse(topic.getString("update_date")),
                                topics,
                                docs
                                ));
                    }
                }
            }
            if (docs != null) {
                int len = docs.length();
                for (int i=0;i<len;i++){
                    if(docs.getJSONObject(i).getInt("parent_id") == this.id) {
                        JSONObject doc = docs.getJSONObject(i);
                        this.docs.add(new Doc(
                                doc.getInt("id"),
                                doc.getString("title"),
                                formatter.parse(doc.getString("update_date"))
                        ));
                    }
                }
            }

        } catch (JSONException | ParseException e){

        }
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

    @Override
    @Bindable
    public Date getUpdate_date() {
        return update_date;
    }

    @Bindable
    public String getStringDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
        return formatter.format(update_date);
    }

    public void setUpdate_date(Date update_date) {
        this.update_date = update_date;
        notifyPropertyChanged(BR.update_date);
    }
    @Bindable
    public ArrayList<Topic> getTopics() {
        return topics;
    }

    public void setTopics(ArrayList<Topic> topics) {
        this.topics = topics;
        notifyPropertyChanged(BR.topics);
    }

    @Bindable
    public ArrayList<Doc> getDocs() {
        return docs;
    }

    public void setDocs(ArrayList<Doc> docs) {
        this.docs = docs;
        notifyPropertyChanged(BR.docs);
    }

    @Override
    @Bindable
    public boolean isSelected() {
        return this.isSelected;
    }
    @Override
    public void setSelected(boolean selected) {
        isSelected = selected;
        notifyPropertyChanged(BR.selected);
    }

    @Override
    public String toString() {
        return "Topic{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", update_date=" + update_date +
                ", topics=" + topics +
                ", docs=" + docs +
                '}';
    }

}
