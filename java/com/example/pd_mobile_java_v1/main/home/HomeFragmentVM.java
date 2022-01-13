package com.example.pd_mobile_java_v1.main.home;

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
import com.example.pd_mobile_java_v1.dialogs.AddDialog;
import com.example.pd_mobile_java_v1.dialogs.DeleteConfirmDialog;
import com.example.pd_mobile_java_v1.dialogs.IAddElement;
import com.example.pd_mobile_java_v1.dialogs.IDeleteElements;
import com.example.pd_mobile_java_v1.dialogs.IRenameElement;
import com.example.pd_mobile_java_v1.dialogs.RenameDialog;
import com.example.pd_mobile_java_v1.doc.DocActivity;
import com.example.pd_mobile_java_v1.main.IToolbarMain;
import com.example.pd_mobile_java_v1.main.MainActivity;
import com.example.pd_mobile_java_v1.main.MainActivityVM;
import com.example.pd_mobile_java_v1.model.Doc;
import com.example.pd_mobile_java_v1.model.IListItem;
import com.example.pd_mobile_java_v1.model.Topic;
import com.example.pd_mobile_java_v1.model.User;
import com.example.pd_mobile_java_v1.util.IOnBackPressed;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.stfalcon.androidmvvmhelper.mvvm.fragments.FragmentViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Stack;

import androidx.appcompat.widget.Toolbar;
import androidx.databinding.library.baseAdapters.BR;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

public class HomeFragmentVM extends FragmentViewModel<HomeFragment>
        implements
        IToolbarMain,
        IRenameElement,
        IAddElement,
        IDeleteElements,
        IOnBackPressed
{
    //data
    public User user;
    //view
    private Fragment fragment;
    private FragmentManager fragmentManager;
    private MainActivity activity;
    private MainActivityVM mainActivityVM;
    private Context context;
    private TextView counter;
    private TextView err;
    //recycler
    private RecyclerMainAdapter<Topic> topicRecyclerHomeAdapter;
    private RecyclerMainAdapter<Doc> docRecyclerHomeAdapter;
    private ConcatAdapter concatAdapter;
    public final RecyclerConfiguration recyclerConfiguration = new RecyclerConfiguration();
    //lists
    //has access to data
    private ArrayList<Topic> topics;
    private ArrayList<Doc> docs;
    private int openedId;
    private int openedId_current;
    //only for view
    private ArrayList<Topic> current_topics  = new ArrayList<>();
    private ArrayList<Doc> current_docs = new ArrayList<>();
    private Topic current_topic;
    //topic stack
    private Stack<Topic> topicStack = new Stack<>();
    //selected items for deleting or move
    private ArrayList<IListItem> selectedElements = new ArrayList<>();

    public HomeFragmentVM(final HomeFragment fragment) {
        //setup view
        super(fragment);
        this.fragment = fragment;
        View root = fragment.getView();
        assert root != null;
        fragmentManager = fragment.getParentFragmentManager();
        activity = (MainActivity) fragment.getActivity();
        assert activity != null;
        mainActivityVM = activity.getViewModel();
        context = fragment.getContext();
        err = root.findViewById(R.id.err1);
        counter = activity.findViewById(R.id.count_text);
        //setup data
        user = mainActivityVM.getUser();
        setupCurrentTopic(user.getRoot());
        //stack
        topicStack.push(user.getRoot());

        //init RecyclerView (list)
        initRecycler();
        //start dialog
        FloatingActionButton fab = activity.findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            AddDialog addDialog = new AddDialog();
            addDialog.show(activity.getSupportFragmentManager(), "add_dialog");
        });
    }

    /**
        IOnBackPressed implementation
     */
    @Override
    public boolean onBackPressed(){
        if(topicStack.size() == 1){
            return false;
        } else {
            topicStack.pop();
            Topic item = topicStack.peek();
            Log.d("CDA", item.toString());
            changeCurrentTopic(topicStack.peek());
            return true;
        }
    }

    private void setupCurrentTopic(Topic item){
        //current topic to get parent id
        current_topic = item;
        //has access to data
        topics = current_topic.getTopics();
        docs = current_topic.getDocs();
        //only for Recycler, clearing when changedCurrentTopic
        current_topics.clear(); current_topics.addAll(current_topic.getTopics());
        current_docs.clear(); current_docs.addAll(current_topic.getDocs());
    }

    private void changeCurrentTopic(Topic item){
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        toolbar.setTitle(item.getTitle());
        setupCurrentTopic(item);
        topicRecyclerHomeAdapter.notifyDataSetChanged();
        docRecyclerHomeAdapter.notifyDataSetChanged();
    }

    private void startDocActivity(Doc item){
        Gson gson = new Gson();
        String json = gson.toJson(item);
        openedId = docs.indexOf(item);
        openedId_current = current_docs.indexOf(item);
        Log.d("CDA 0 hf",json);
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
            docs.set(openedId, doc);
            current_docs.set(openedId_current, doc);
            docRecyclerHomeAdapter.notifyItemChanged(openedId);
        }
         catch (Exception e){
             Log.d("CDA e2",e.toString());
         }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setBasedToolbar(){
        activity.findViewById(R.id.toolbar).setVisibility(View.VISIBLE);
        activity.findViewById(R.id.toolbar_home).setVisibility(View.GONE);
    }
    private void setListToolbar(){
        activity.findViewById(R.id.toolbar).setVisibility(View.GONE);
        activity.findViewById(R.id.toolbar_home).setVisibility(View.VISIBLE);
    }
    private void setRenameButton(){
        if (selectedElements.size() == 1){
            activity.findViewById(R.id.ib_rename).setVisibility(View.VISIBLE);
        } else {
            activity.findViewById(R.id.ib_rename).setVisibility(View.GONE);
        }
    }
    /**
        ON LONG ITEM CLICK
     */
    private void selectItem(IListItem item){
        if (selectedElements.isEmpty())
            setListToolbar();
        selectedElements.add(item);
        item.setSelected(true);
        counter.setText(Integer.toString(selectedElements.size()));
        setRenameButton();
    }
    /**
        ON CLICK ON SELECTED ITEM
     */
    private void unselectItem(IListItem item){
        selectedElements.remove(item);
        item.setSelected(false);
        counter.setText(Integer.toString(selectedElements.size()));
        if(selectedElements.isEmpty())
            setBasedToolbar();
        setRenameButton();
    }
    private void initRecycler() {
        try {
            topicRecyclerHomeAdapter = new RecyclerMainAdapter<>(R.layout.list_item_topic, BR.topic,current_topics);
            docRecyclerHomeAdapter = new RecyclerMainAdapter<>(R.layout.list_item_doc,BR.doc,current_docs);
            topicRecyclerHomeAdapter.setOnItemClickListener(new RecyclerMainAdapter.OnItemClickListener<Topic>() {
                @Override
                public void onItemClick(int position, Topic item) {
                    if(!item.isSelected()) {
                        if (selectedElements.isEmpty()) {
                            changeCurrentTopic(item);
                            fragmentManager.beginTransaction().addToBackStack("null").commit();
                            topicStack.add(item);
                        }
                        //if !selectedElements.isEmpty()
                        else {
                            selectItem(item);
                        }
                    } else {
                        unselectItem(item);
                    }
                }
            });
            topicRecyclerHomeAdapter.setOnItemLongClickListener(new RecyclerMainAdapter.OnItemLongClickListener<Topic>() {
                @Override
                public void onItemLongClick(int position, Topic item) {
                    try {
                        selectItem(item);
                    }catch (Exception e){
                        err.setText(e.toString());
                    }

                }
            });

            docRecyclerHomeAdapter.setOnItemClickListener(new RecyclerMainAdapter.OnItemClickListener<Doc>() {
                @Override
                public void onItemClick(int position, Doc item) {
                        if(!item.isSelected()) {
                            if (selectedElements.isEmpty()) {
                                if (item.isContentLoaded()) {
                                    startDocActivity(item);
                                } else {
                                    JSONObject jsonRequest = new JSONObject();
                                    try {
                                        jsonRequest.put("id", item.getId());

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
                                                        item.setContent(response.getString("content"));
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                    item.setContentLoaded(true);
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
            docRecyclerHomeAdapter.setOnItemLongClickListener(new RecyclerMainAdapter.OnItemLongClickListener<Doc>() {
                @Override
                public void onItemLongClick(int position, Doc item) {
                    try {
                        selectItem(item);
                    }catch (Exception e){
                        err.setText(e.toString());
                    }
                }
            });
            concatAdapter = new ConcatAdapter(topicRecyclerHomeAdapter,docRecyclerHomeAdapter);
            recyclerConfiguration.setLayoutManager(new LinearLayoutManager(context));
            recyclerConfiguration.setItemAnimator(new DefaultItemAnimator());
            recyclerConfiguration.setAdapter(concatAdapter);

        }
        catch (Exception e)
        {
            err.setText(e.toString());
        }
    }

    /**
     IAddElements implementation
     */
    @Override
    public void addElement(String title, boolean type) {
        boolean isTopic = (type == AddDialog.TOPIC_SELECTED);
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("title", title);
            jsonRequest.put("parent_id", current_topic.getId());
            jsonRequest.put("table", (isTopic)?"topics":"docs");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://192.168.0.107/pd_mobile/main/add";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                jsonRequest,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Display response string.
                        try {
                            if (response.getBoolean("success")) {
                                int id = response.getInt("id");
                                if (isTopic) {
                                    addTopic(title,id);
                                } else {
                                    addDoc(title,id);
                                }
                            }else {
                                Toast toast = Toast.makeText(activity.getApplicationContext(),
                                        "Ошибка. "+((isTopic)?"Тема":"Документ")+" с таким названием уже существует в данной теме!", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        err.setText("Error on add element: " + error.getMessage());
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);

    }

    private void addTopic(String title, int id){
        Topic newTopic = new Topic(id, title, new Date(), new JSONArray(), new JSONArray());
        current_topics.add(newTopic);
        topics.add(newTopic);
        topicRecyclerHomeAdapter.notifyItemInserted(topics.size() - 1);

    }
    private void addDoc(String title, int id){
        Doc newDoc = new Doc(id,title, new Date());
        current_docs.add(newDoc);
        docs.add(newDoc);
        docRecyclerHomeAdapter.notifyItemInserted(docs.size() - 1);
    }
    /**
     IRenameElement implementation
    */
    @Override
    public void renameElement(String newTitle) {
        IListItem item = selectedElements.get(0);
        boolean isTopic = item instanceof Topic;
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("id", item.getId());
            jsonRequest.put("new_name", newTitle);
            jsonRequest.put("table", (isTopic)?"topics":"docs");
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
                                item.setTitle(newTitle);
                                item.setSelected(false);
                            }else {
                                Toast toast = Toast.makeText(activity.getApplicationContext(),
                                        "Ошибка. "+((isTopic)?"Тему":"Документ")+" нельзя переименовать, возможно объект c таким названием уже существует", Toast.LENGTH_SHORT);
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
     IDeleteElements implementation
    */
    @Override
    public void deleteElements() {
        RequestQueue queue = Volley.newRequestQueue(context);
        for (IListItem item : selectedElements) {
            boolean isTopic = item instanceof Topic;
            JSONObject jsonRequest = new JSONObject();
            try {
                jsonRequest.put("id", item.getId());
                jsonRequest.put("table", (isTopic)?"topics":"docs");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String url = "http://192.168.0.107/pd_mobile/main/delete";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                    jsonRequest,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // Display response string.
                            try {
                                if (response.getBoolean("res")) {
                                    if ((isTopic)) {
                                        int id = topics.indexOf(item);
                                        topics.remove(item);
                                        current_topics.remove(item);
                                        topicRecyclerHomeAdapter.notifyItemRemoved(id);
                                    } else {
                                        int id = docs.indexOf(item);
                                        docs.remove(item);
                                        current_docs.remove(item);
                                        docRecyclerHomeAdapter.notifyItemRemoved(id);
                                    }
                                }else {
                                    Toast toast = Toast.makeText(activity.getApplicationContext(),
                                            "Ошибка. "+((isTopic)?"Тема":"Документ")+" не существует! Возможно объект уже был удалён с другого устройства. Обновите страницу", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            err.setText("Error on delete element: " + error.getMessage());
                        }
                    });

            // Add the request to the RequestQueue.
            queue.add(jsonObjectRequest);
        }
        closeSelected();
    }
    /**
    IToolbarMain implementation
    */
    @Override
    public void onClose(View view){
        Log.d("CDA", "onClose in VM Called");
        for (IListItem item: selectedElements) {
            item.setSelected(false);
        }
        closeSelected();
    }
    @Override
    public void onDelete(View view){
        DeleteConfirmDialog deleteDialog = new DeleteConfirmDialog();
        Bundle args = new Bundle();
        args.putString("elements", buildSelectedTitlesString());
        args.putString("count", Integer.toString(selectedElements.size()));
        deleteDialog.setArguments(args);
        deleteDialog.show(activity.getSupportFragmentManager(), "delete_dialog");
    }

    private String buildSelectedTitlesString(){
        StringBuilder titles = new StringBuilder();
        String coma = "";
        for (IListItem item:selectedElements) {
            titles.append(coma+item.getTitle());
            coma = ", ";
        }
        return titles.toString();
    }
    @Override
    public void onRename(View view){
        RenameDialog renameDialog = new RenameDialog();
        Bundle args = new Bundle();
        args.putString("oldTitle",selectedElements.get(0).getTitle());
        renameDialog.setArguments(args);
        renameDialog.show(activity.getSupportFragmentManager(), "rename_dialog");
    }
    @Override
    public void onBookmarks(View view){

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://192.168.0.107/pd_mobile/main/save_bookmark";
        for (IListItem item : selectedElements) {
            if (item instanceof Doc) {
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
                                try {
                                    int id = response.getInt("res");
                                    user.addBookmarks((Doc) item, id);
                                    item.setSelected(false);
                                } catch (JSONException e) {
                                    Log.d("CDA add-bm-je2", e.toString());
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                item.setSelected(false);
                                Log.d("CDA add-bm-e", error.toString());
                            }
                        });
                    // Add the request to the RequestQueue.
                    queue.add(jsonObjectRequest);
                }
        }
        Toast toast = Toast.makeText(activity.getApplicationContext(),
                "Выбранные документы добавлены в закладки", Toast.LENGTH_SHORT);
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
}
