package com.example.pd_mobile_java_v1.doc.edit;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.pd_mobile_java_v1.adapters.RecyclerConfiguration;
import com.example.pd_mobile_java_v1.adapters.RecyclerDocEditAdapter;
import com.example.pd_mobile_java_v1.dialogs.RenameDialog;
import com.example.pd_mobile_java_v1.dialogs.SaveConfirmDialog;
import com.example.pd_mobile_java_v1.doc.DocActivity;
import com.example.pd_mobile_java_v1.doc.DocActivityVM;
import com.example.pd_mobile_java_v1.doc.IEditToolbar;
import com.example.pd_mobile_java_v1.doc.objects.DocFrame;
import com.example.pd_mobile_java_v1.doc.objects.DocHeader;
import com.example.pd_mobile_java_v1.doc.objects.DocImage;
import com.example.pd_mobile_java_v1.doc.objects.DocLink;
import com.example.pd_mobile_java_v1.doc.objects.DocMedia;
import com.example.pd_mobile_java_v1.doc.objects.DocText;
import com.example.pd_mobile_java_v1.doc.objects.IDocItem;
import com.stfalcon.androidmvvmhelper.mvvm.fragments.FragmentViewModel;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

public class EditFragmentVM extends FragmentViewModel<EditFragment>
        implements IEditToolbar

{
    //view
    private Fragment fragment;
    private DocActivity activity;
    private DocActivityVM docActivityVM;
    private Context context;
    //data
    private ArrayList<IDocItem> docItems;
    //recycler
    private RecyclerDocEditAdapter docEditAdapter;
    public final RecyclerConfiguration recyclerConfiguration = new RecyclerConfiguration();
    //selected items for deleting or move
    private IDocItem prevSelected;
    private IDocItem selected;

    public EditFragmentVM(EditFragment fragment) {
        super(fragment);
        //setup view
        context = fragment.getContext();
        activity =  (DocActivity) fragment.getActivity();
        docActivityVM = (DocActivityVM) activity.getViewModel();
        View root = fragment.getView();
        assert root != null;
        //setup data
        docItems = docActivityVM.getDocItems();
        //
        initRecycler();
    }
    private void initRecycler(){
        docEditAdapter = new RecyclerDocEditAdapter(docItems);
        docEditAdapter.setOnItemClickListener(new RecyclerDocEditAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, IDocItem item) {
                try {
                    selected = item;
                    selected.setSelected(true);
                    if(prevSelected != null) {
                        Log.d("CDA 1",prevSelected.toString());
                        Log.d("CDA 2",selected.toString());
                        prevSelected.setSelected(false);
                    }
                    prevSelected = selected;
                } catch (Exception e){
                    Log.d("CDA e1",e.toString());
                }
            }
        });
        recyclerConfiguration.setLayoutManager(new LinearLayoutManager(context));
        recyclerConfiguration.setItemAnimator(new DefaultItemAnimator());
        recyclerConfiguration.setAdapter(docEditAdapter);
    }

    @Override
    public void onUp(View view) {
        int id = docItems.indexOf(selected);
        if (id > 0){
            IDocItem temp = docItems.get(id - 1);
            docItems.set(id - 1,docItems.get(id));
            docItems.set(id,temp);
            docEditAdapter.notifyItemMoved(id, id-1);
        }
    }

    @Override
    public void onDown(View view) {
        int id = docItems.indexOf(selected);
        if(id < docItems.size() - 1){
            IDocItem temp = docItems.get(id + 1);
            docItems.set(id + 1,docItems.get(id));
            docItems.set(id,temp);
            docEditAdapter.notifyItemMoved(id, id+1);
        }
    }

    @Override
    public void onSave(View view) {
        try {
            if(!(docActivityVM.generateResult().equals(docActivityVM.getContentStr()))) {
                SaveConfirmDialog saveConfirmDialog = new SaveConfirmDialog();
                Bundle args = new Bundle();
                args.putString("action", "edit");
                saveConfirmDialog.setArguments(args);
                saveConfirmDialog.show(activity.getSupportFragmentManager(), "save_confirm_dialog");
            }
        } catch (Exception e){
            Log.d("CDA",e.getMessage());
        }

    }

    @Override
    public void onRename(View view) {
        RenameDialog renameDialog = new RenameDialog();
        Bundle args = new Bundle();
        args.putString("oldTitle",docActivityVM.getTitle());
        renameDialog.setArguments(args);
        renameDialog.show(activity.getSupportFragmentManager(), "rename_dialog");
    }

    @Override
    public void onDelete(View view) {
        int id = docItems.indexOf(selected);
        docItems.remove(selected);
        docEditAdapter.notifyItemRemoved(id);
    }

    @Override
    public void onAddText(View view){
        docItems.add(new DocText());
        docEditAdapter.notifyItemInserted(docItems.size() -1);
    }

    @Override
    public void onAddHeader(View view) {
        docItems.add(new DocHeader());
        docEditAdapter.notifyItemInserted(docItems.size() -1);
    }

    @Override
    public void onAddLink(View view) {
        docItems.add(new DocLink());
        docEditAdapter.notifyItemInserted(docItems.size() -1);
    }

    @Override
    public void onAddImage(View view) {
        docItems.add(new DocImage());
        docEditAdapter.notifyItemInserted(docItems.size() -1);
    }

    @Override
    public void onAddFrame(View view) {
        docItems.add(new DocFrame());
        docEditAdapter.notifyItemInserted(docItems.size() -1);
    }

    @Override
    public void onAddMedia(View view) {
        docItems.add(new DocMedia());
        docEditAdapter.notifyItemInserted(docItems.size() -1);
    }
}
