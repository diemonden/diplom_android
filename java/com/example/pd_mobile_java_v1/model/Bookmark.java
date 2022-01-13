package com.example.pd_mobile_java_v1.model;

import android.annotation.SuppressLint;

import com.example.pd_mobile_java_v1.BR;
import com.example.pd_mobile_java_v1.util.ISelectable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class Bookmark extends BaseObservable implements ISelectable {
    private int id;
    private int contentId;
    private Doc doc;
    private boolean isSelected = false;

    public Bookmark(int id, int content_id, JSONArray docs) {
        setId(id);
        setContentId(content_id);
        try {
            for (int i = 0; i < docs.length(); i++){
                if (docs.getJSONObject(i).getInt("id") == content_id){
                    JSONObject jDoc = docs.getJSONObject(i);
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    setDoc(new Doc(
                            jDoc.getInt("id"),
                            jDoc.getString("title"),
                            formatter.parse(jDoc.getString("update_date"))
                    ));
                    break;
                }
            }
        }catch (JSONException | ParseException e){
            e.printStackTrace();
        }

    }
    @Bindable
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
        notifyPropertyChanged(BR.contentId);
    }

    @Bindable
    public Doc getDoc() {
        return doc;
    }

    public void setDoc(Doc doc) {
        this.doc = doc;
        notifyPropertyChanged(BR.doc);
    }

    @Override
    public String toString() {
        return "Bookmarks{" +
                "id=" + id +
                ", doc=" + doc +
                ", isSelected=" + isSelected +
                '}';
    }


    @Bindable
    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public void setSelected(boolean selected) {
        isSelected = selected;
        notifyPropertyChanged(BR.selected);
    }
}
