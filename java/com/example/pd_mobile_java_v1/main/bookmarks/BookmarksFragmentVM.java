package com.example.pd_mobile_java_v1.main.bookmarks;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pd_mobile_java_v1.R;
import com.example.pd_mobile_java_v1.adapters.RecyclerConfiguration;
import com.example.pd_mobile_java_v1.adapters.RecyclerMainAdapter;
import com.example.pd_mobile_java_v1.dialogs.IRenameElement;
import com.example.pd_mobile_java_v1.dialogs.RenameDialog;
import com.example.pd_mobile_java_v1.doc.DocActivity;
import com.example.pd_mobile_java_v1.main.IToolbarMain;
import com.example.pd_mobile_java_v1.main.MainActivity;
import com.example.pd_mobile_java_v1.main.MainActivityVM;
import com.example.pd_mobile_java_v1.model.Bookmark;
import com.example.pd_mobile_java_v1.model.Doc;
import com.example.pd_mobile_java_v1.model.IListItem;
import com.example.pd_mobile_java_v1.model.Topic;
import com.example.pd_mobile_java_v1.model.User;
import com.google.gson.Gson;
import com.stfalcon.androidmvvmhelper.mvvm.fragments.FragmentViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.databinding.library.baseAdapters.BR;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

public class BookmarksFragmentVM extends FragmentViewModel<BookmarksFragment>
implements IRenameElement,
        IToolbarMain,
        IOnBackPressed {
    //view
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private MainActivity activity;
    private MainActivityVM mainActivityVM;
    private Context context;
    private TextView err;
    private TextView counter;
    //data
    public User user;
    private ArrayList<Bookmark> bookmarks;
    private int openedId;
    private ArrayList<Bookmark> selectedElements = new ArrayList<>();
    //recycler
    private RecyclerMainAdapter<Bookmark> bookmarkRecyclerAdapter;
    public RecyclerConfiguration recyclerConfiguration = new RecyclerConfiguration();

    public BookmarksFragmentVM(BookmarksFragment fragment) {
        super(fragment);
        this.fragment = fragment;
        View root = fragment.getView();
        assert root != null;
        err = root.findViewById(R.id.err_bm);
        fragmentManager = fragment.getParentFragmentManager();
        activity = (MainActivity) fragment.getActivity();
        assert activity != null;
        mainActivityVM = activity.getViewModel();
        context = fragment.getContext();
        counter = activity.findViewById(R.id.count_text_bm);
        //setup data
        user = mainActivityVM.getUser();
        bookmarks = user.getBookmarks();
        //setup list
        initRecycler();
        Log.d("CDA", "Bookmarks VM");
    }

    private void startDocActivity(Bookmark item){
        Gson gson = new Gson();
        String json = gson.toJson(item.getDoc());
        openedId = bookmarks.indexOf(item);
        Log.d("CDA 0 bf",json);
        Intent intent = new Intent(context, DocActivity.class);
        intent.putExtra("doc", json);
        try {
            activity.startActivityForResult(intent,1);
        } catch (Exception e) {
            err.setText("Error on start DocActivity: " + e.toString());
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            String result = data.getStringExtra("upd_content");
            Log.d("CDA 1 hf",result);
            Gson gson = new Gson();
            Doc doc = gson.fromJson(result, Doc.class);
            Log.d("CDA 2 hf",doc.toString());
            bookmarks.get(openedId).setDoc(doc);
            bookmarkRecyclerAdapter.notifyItemChanged(openedId);
        }
        catch (Exception e){
            Log.d("CDA e2",e.toString());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setBasedToolbar(){
        activity.findViewById(R.id.toolbar).setVisibility(View.VISIBLE);
        activity.findViewById(R.id.toolbar_bm).setVisibility(View.GONE);
    }
    private void setListToolbar(){
        activity.findViewById(R.id.toolbar).setVisibility(View.GONE);
        activity.findViewById(R.id.toolbar_bm).setVisibility(View.VISIBLE);
    }
    private void setGotoButton(){
        if (selectedElements.size() == 1){
            activity.findViewById(R.id.ib_goto).setVisibility(View.VISIBLE);
        } else {
            activity.findViewById(R.id.ib_goto).setVisibility(View.GONE);
        }
    }
    private void setRenameButton(){
        if (selectedElements.size() == 1){
            activity.findViewById(R.id.ib_rename_bm).setVisibility(View.VISIBLE);
        } else {
            activity.findViewById(R.id.ib_rename_bm).setVisibility(View.GONE);
        }
    }
    /**
     ON LONG ITEM CLICK
     */
    private void selectItem(Bookmark item){
        if (selectedElements.isEmpty())
            setListToolbar();
        selectedElements.add(item);
        item.setSelected(true);
        counter.setText(Integer.toString(selectedElements.size()));
        setGotoButton();
        setRenameButton();
    }

    /**
     ON CLICK ON SELECTED ITEM
     */
    private void unselectItem(Bookmark item){
        selectedElements.remove(item);
        item.setSelected(false);
        counter.setText(Integer.toString(selectedElements.size()));
        if(selectedElements.isEmpty())
            setBasedToolbar();
        setGotoButton();
        setRenameButton();
    }

    private void initRecycler() {
        try {
            bookmarkRecyclerAdapter = new RecyclerMainAdapter<>(R.layout.list_item_bookmark, BR.bookmark, bookmarks);
            bookmarkRecyclerAdapter.setOnItemClickListener(new RecyclerMainAdapter.OnItemClickListener<Bookmark>() {
                @Override
                public void onItemClick(int position, Bookmark item) {
                    if(!item.isSelected()) {
                        if (selectedElements.isEmpty()) {
                            if (item.getDoc().isContentLoaded()) {
                                startDocActivity(item);
                            } else {
                                JSONObject jsonRequest = new JSONObject();
                                try {
                                    jsonRequest.put("id", item.getDoc().getId());

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                RequestQueue queue = Volley.newRequestQueue(context);
                                String url = "http://192.168.0.107/pd_mobile/main/get_cur_doc";
                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                                        jsonRequest,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                // Display response string.
                                                try {
                                                    item.getDoc().setContent(response.getString("content"));
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                item.getDoc().setContentLoaded(true);
                                                startDocActivity(item);
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                err.setText("Error on get current doc content: " + error.getMessage());
                                            }
                                        });

                                // Add the request to the RequestQueue.
                                queue.add(jsonObjectRequest);
                            }
                        }
                        //if !selectedElements.isEmpty()
                        else {
                            selectItem(item);
                        }
                    }
                    //if item.isSelected()
                    else {
                        unselectItem(item);
                    }
                }
            });

            bookmarkRecyclerAdapter.setOnItemLongClickListener(new RecyclerMainAdapter.OnItemLongClickListener<Bookmark>() {
                @Override
                public void onItemLongClick(int position, Bookmark item) {
                    try {
                        selectItem(item);
                    }catch (Exception e){
                        err.setText(e.toString());
                    }
                }
            });
            recyclerConfiguration.setLayoutManager(new LinearLayoutManager(this.fragment.getContext()));
            recyclerConfiguration.setItemAnimator(new DefaultItemAnimator());
            recyclerConfiguration.setAdapter(bookmarkRecyclerAdapter);
        }
        catch (Exception e)
        {
            err.setText(e.toString());
        }
    }
    /**
     IRenameElement implementation
     */
    @Override
    public void renameElement(String newTitle) {
        Bookmark item = selectedElements.get(0);
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("id", item.getContentId());
            jsonRequest.put("new_name", newTitle);
            jsonRequest.put("table", "docs");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://192.168.0.107/pd_mobile/main/rename";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                jsonRequest,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Display response string.
                        try {
                            if (response.getBoolean("res")) {
                                item.getDoc().setTitle(newTitle);
                                item.setSelected(false);
                            }else {
                                Toast toast = Toast.makeText(activity.getApplicationContext(),
                                        "Ошибка. Документ нельзя переименовать, возможно объект c таким названием уже существует", Toast.LENGTH_SHORT);
                                toast.show();
                                item.setSelected(false);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
        closeSelected();
    }

    @Override
    public void cancelRename() {
        Log.d("CDA", "cancelRename();");
        selectedElements.get(0).setSelected(false);
        closeSelected();
    }
    private void closeSelected(){
        selectedElements.clear();
        counter.setText(Integer.toString(selectedElements.size()));
        setBasedToolbar();
    }
    /**
     IToolbarMain implementation
     */
    @Override
    public void onClose(View view){
        Log.d("CDA", "onClose in VM Called");
        for (Bookmark item: selectedElements) {
            item.setSelected(false);
        }
        closeSelected();
    }
    @Override
    public void onRename(View view){
        RenameDialog renameDialog = new RenameDialog();
        Bundle args = new Bundle();
        args.putString("oldTitle",selectedElements.get(0).getDoc().getTitle());
        renameDialog.setArguments(args);
        renameDialog.show(activity.getSupportFragmentManager(), "rename_dialog");
    }
    @Override
    public void onBookmarks(View view){

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://192.168.0.107/pd_mobile/main/delete_bookmark";

        for (Bookmark item: selectedElements)
        {
            JSONObject jsonRequest = new JSONObject();
            try {
                jsonRequest.put("id", item.getId());
            } catch (JSONException e) {
                Log.d("CDA add-bm-je1", e.toString());
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                    jsonRequest,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // Display response string.
                            try{
                                Log.d("CDA add-bm-je2", response.toString());
                                if(response.getBoolean("res")) {
                                    int id = bookmarks.indexOf(item);
                                    bookmarks.remove(item);
                                    bookmarkRecyclerAdapter.notifyItemRemoved(id);
                                } else {
                                }
                            } catch (JSONException e) {
                                Log.d("CDA add-bm-je2", e.toString());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("CDA add-bm-e", error.toString());
                        }
                    });
            // Add the request to the RequestQueue.
            queue.add(jsonObjectRequest);
        }
        Toast toast = Toast.makeText(activity.getApplicationContext(),
                "Выбранные элементы удалены из закладок", Toast.LENGTH_SHORT);
        toast.show();
        closeSelected();
    }
    @Override
    public void onMove(View view){
        //сложна
    }
    @Override
    public void onShare(View view){
        //тут нет функционала даже на сервере
    }
                
    @Override
    public boolean onBackPressed() {
        return false;
    }
}
