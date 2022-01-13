package com.example.pd_mobile_java_v1.doc;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pd_mobile_java_v1.R;
import com.example.pd_mobile_java_v1.doc.objects.DocFrame;
import com.example.pd_mobile_java_v1.doc.objects.DocHeader;
import com.example.pd_mobile_java_v1.doc.objects.DocImage;
import com.example.pd_mobile_java_v1.doc.objects.DocLink;
import com.example.pd_mobile_java_v1.doc.objects.DocMedia;
import com.example.pd_mobile_java_v1.doc.objects.DocText;
import com.example.pd_mobile_java_v1.doc.objects.IDocItem;
import com.example.pd_mobile_java_v1.util.AppUtility;
import com.stfalcon.androidmvvmhelper.mvvm.activities.ActivityViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

import static android.app.Activity.RESULT_OK;

public class DocActivityVM extends ActivityViewModel<DocActivity> {

    //view
    private DocActivity activity;
    //data
    private JSONObject jDoc = new JSONObject();
    private JSONArray content  = new JSONArray();;
    private ArrayList<IDocItem> docItems = new ArrayList<>();
    private String title;

    public DocActivityVM(DocActivity activity) {
        super(activity);
        this.activity = activity;
        jDoc = AppUtility.getDataFromIntent(activity,"doc");
        try {
            title = jDoc.getString("title");
            //activity.getSupportActionBar().setTitle(title);
            Log.d("CDA",jDoc.getString("content"));
            content = new JSONArray(jDoc.getString("content"));
            for (int i = 0; i < content.length(); i++) {
                JSONObject item = content.getJSONObject(i);
                switch (item.getString("type")) {
                    case "paragraph":
                        this.docItems.add(new DocText(item.getString("value")));
                        break;
                    case "header":
                        this.docItems.add(new DocHeader(item.getString("value")));
                        break;
                    case "img":
                        this.docItems.add(new DocImage(item.getString("src"),
                                item.getString("size")));
                        break;
                    case "link":
                        this.docItems.add(new DocLink(item.getString("value"),
                                item.getString("src")));
                        break;
                    case "media":
                        this.docItems.add(new DocMedia(item.getString("src")));
                        break;
                    case "frame":
                        this.docItems.add(new DocFrame(item.getString("src")));
                        break;
                    default:
                        break;
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public String generateResult(){
        StringBuilder str = new StringBuilder("[");
        String coma = "";
        for (IDocItem item:docItems) {
            if (item instanceof DocText) {
                str.append(coma).append("{\"type\":\"paragraph\",\"value\":")
                        .append(JSONObject.quote(((DocText) item).getText())).append("}");
            }
            else
            if (item instanceof DocHeader) {
                str.append(coma).append("{\"type\":\"header\",\"value\":")
                        .append(JSONObject.quote(((DocHeader) item).getText())).append("}");
            }
            else
            if (item instanceof DocImage) {
                str.append(coma).append("{\"type\":\"img\",\"src\":")
                        .append(JSONObject.quote(((DocImage) item).getSrc())).append(",\"size\":")
                        .append(JSONObject.quote(((DocImage) item).getSize())).append("}");
            }
            else
            if (item instanceof DocLink) {
                str.append(coma).append("{\"type\":\"link\",\"value\":")
                        .append(JSONObject.quote(((DocLink) item).getText())).append(",\"src\":")
                        .append(JSONObject.quote(((DocLink) item).getLink())).append("}");

            }
            else
            if (item instanceof DocFrame) {
                str.append(coma).append("{\"type\":\"frame\",\"src\":")
                        .append(JSONObject.quote(((DocFrame) item).getSrc())).append("}");

            }
            else {
                str.append(coma).append("{\"type\":\"media\",\"src\":")
                        .append(JSONObject.quote(((DocMedia) item).getSrc())).append("}");
            }
            coma = ",";
        }
        str.append("]");
        return str.toString();
    }
    @Bindable
    public String getTitle() {
        return title;
    }

    public String getContentStr() {
        return content.toString();
    }

    public ArrayList<IDocItem> getDocItems() {
        return docItems;
    }

    public void save(String action){
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("id", jDoc.getInt("id"));
            jsonRequest.put("upd_content", generateResult());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(activity);
        String url = "http://192.168.0.107/pd_mobile/main/save";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                jsonRequest,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Display response string.
                        try {
                            if (response.getBoolean("res")) {
                                try {
                                    jDoc.put("content",generateResult());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Intent intent = new Intent();
                                intent.putExtra("upd_content", jDoc.toString());
                                activity.setResult(Activity.RESULT_OK, intent);
                                if(action.equals("onBackPressed")) {
                                    activity.finish();
                                } else {
                                    try {
                                        content = new JSONArray(generateResult());
                                    } catch (JSONException e) {
                                        Log.d("CDA ed1",e.toString());
                                    }
                                }
                            }else {
                                Toast toast = Toast.makeText(activity.getApplicationContext(),
                                        "Ошибка сохранения. Проверьте соединение", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        } catch (JSONException e) {
                            Log.d("CDA ed2",e.toString());
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

    }
    public void dontSave(String action){
        Intent intent = new Intent();
        activity.setResult(Activity.RESULT_CANCELED, intent);
        if(action.equals("onBackPressed")){
            activity.finish();
        }
    }

    public void rename(String newTitle){
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("id", jDoc.getInt("id"));
            jsonRequest.put("new_name", newTitle);
            jsonRequest.put("table", "docs");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue queue = Volley.newRequestQueue(activity);
        String url = "http://192.168.0.107/pd_mobile/main/rename";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                jsonRequest,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Display response string.
                        try {
                            if (response.getBoolean("res")) {
                                DocActivityVM.this.title = newTitle;
                                notifyPropertyChanged(BR.title);
                                Intent intent = new Intent();
                                try {
                                    jDoc.put("title",newTitle);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                intent.putExtra("upd_content", jDoc.toString());
                                activity.setResult(Activity.RESULT_OK, intent);
                            }else {
                                Toast toast = Toast.makeText(activity.getApplicationContext(),
                                        "Ошибка. Документ нельзя переименовать, возможно объект c таким названием уже существует", Toast.LENGTH_SHORT);
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
                    }
                });
        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }
}
