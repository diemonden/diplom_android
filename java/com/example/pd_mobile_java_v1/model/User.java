package com.example.pd_mobile_java_v1.model;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.example.pd_mobile_java_v1.BR;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableInt;

public class User extends BaseObservable {
    private int id;
    private String username;
    private String email;
    private String registredDate;
    private int status;
    private Topic root;
    private ArrayList<Bookmark> bookmarks;
    private String lastError;
    private JSONArray topics;
    private JSONArray docs;
    @SuppressLint("SimpleDateFormat")
    final private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public User(JSONObject response) {
        try {
            //
            JSONObject user = response.getJSONObject("user");
            topics = response.getJSONArray("topics");
            docs = response.getJSONArray("docs");
            JSONArray bookmarks = response.getJSONArray("bookmarks");
            //
            id = user.getInt("id");
            setUsername(user.getString("username"));
            setEmail(user.getString("email"));
            setRegistredDate(user.getString("registred_date"));
            setStatus(user.getInt("status"));
            setRoot(new Topic(1, "Главная",formatter.parse("1999-01-01 00:00:00"), topics, docs));
            this.bookmarks = new ArrayList<>();
            int len = bookmarks.length();
            for (int i=0;i<len;i++){
                JSONObject bookmark = bookmarks.getJSONObject(i);
                this.bookmarks.add(new Bookmark(
                        bookmark.getInt("bm_id"),
                        bookmark.getInt("id"),
                        docs
                ));
            }
            notifyPropertyChanged(BR.bookmarks);
        }
        catch (JSONException| ParseException e){
            lastError = "JSONException: "+e.toString();
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
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        notifyPropertyChanged(BR.username);
    }
    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }
    @Bindable
    public String getRegistredDate() {
        return registredDate;
    }

    public void setRegistredDate(String registredDate) {
        this.registredDate = registredDate;
        notifyPropertyChanged(BR.registredDate);
    }

    @Bindable
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
        notifyPropertyChanged(BR.status);
    }

    @Bindable
    public Topic getRoot() {
        return root;
    }

    public void setRoot(Topic root) {
        this.root = root;
        notifyPropertyChanged(BR.root);
    }
    @Bindable
    public ArrayList<Bookmark> getBookmarks() {
        return bookmarks;
    }

    public void setBookmarks(ArrayList<Bookmark> bookmarks) {
        this.bookmarks = (ArrayList<Bookmark>) bookmarks.clone();
        notifyPropertyChanged(BR.bookmarks);
    }
    public void addBookmarks(Doc doc, int id) {
        bookmarks.add(new Bookmark(id,doc.getId(),docs));
        notifyPropertyChanged(BR.doc);
    }
    public void removeBookmarks(Bookmark bookmark) {
        bookmarks.remove(bookmark);
        notifyPropertyChanged(BR.bookmark);
    }
    @Bindable
    public String getLastError() {
        return lastError;
    }
    public void setLastError(String lastError) {
        this.lastError = lastError;
        notifyPropertyChanged(BR.lastError);
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", registred_date='" + registredDate + '\'' +
                ", status=" + status +
                ", root=" + root +
                '}';
    }
}
