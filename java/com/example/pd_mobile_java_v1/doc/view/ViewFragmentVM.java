package com.example.pd_mobile_java_v1.doc.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.example.pd_mobile_java_v1.R;
import com.example.pd_mobile_java_v1.adapters.RecyclerConfiguration;
import com.example.pd_mobile_java_v1.adapters.RecyclerDocViewAdapter;
import com.example.pd_mobile_java_v1.doc.DocActivity;
import com.example.pd_mobile_java_v1.doc.DocActivityVM;
import com.example.pd_mobile_java_v1.doc.objects.IDocItem;
import com.stfalcon.androidmvvmhelper.mvvm.fragments.FragmentViewModel;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

public class ViewFragmentVM extends FragmentViewModel<ViewFragment> {
    //view
    private Fragment fragment;
    private TextView textview_first;
    private DocActivity activity;
    private DocActivityVM docActivityVM;
    private Context context;
    //data
    private ArrayList<IDocItem> docItems;
    //recycler
    private RecyclerDocViewAdapter docViewAdapter;
    public final RecyclerConfiguration recyclerConfiguration = new RecyclerConfiguration();
    //-methods
    public ViewFragmentVM(ViewFragment fragment) {
        super(fragment);
        //setup view
        this.fragment = fragment;
        context = fragment.getContext();
        activity =  (DocActivity) fragment.getActivity();
        docActivityVM = (DocActivityVM) activity.getViewModel();
        View root = fragment.getView();
        assert root != null;
        textview_first = root.findViewById(R.id.textview_first);
        //setup data
        docItems = docActivityVM.getDocItems();
        initRecycler();
    }
    private void initRecycler(){
        docViewAdapter = new RecyclerDocViewAdapter(docItems);
        recyclerConfiguration.setLayoutManager(new LinearLayoutManager(context));
        recyclerConfiguration.setItemAnimator(new DefaultItemAnimator());
        recyclerConfiguration.setAdapter(docViewAdapter);
    }
}
