package com.example.pd_mobile_java_v1.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pd_mobile_java_v1.BR;
import com.example.pd_mobile_java_v1.R;
import com.example.pd_mobile_java_v1.doc.objects.DocFrame;
import com.example.pd_mobile_java_v1.doc.objects.DocHeader;
import com.example.pd_mobile_java_v1.doc.objects.DocImage;
import com.example.pd_mobile_java_v1.doc.objects.DocLink;
import com.example.pd_mobile_java_v1.doc.objects.DocMedia;
import com.example.pd_mobile_java_v1.doc.objects.DocText;
import com.example.pd_mobile_java_v1.doc.objects.IDocItem;

import java.io.InputStream;
import java.util.AbstractList;
import java.util.ArrayList;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerDocViewAdapter extends RecyclerView.Adapter<RecyclerDocViewAdapter.DocHolder> {
    private AbstractList<IDocItem> items = new ArrayList<>();

    public RecyclerDocViewAdapter(AbstractList<IDocItem> items) {
        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof DocText) {
            return R.layout.doc_item_text;
        }
        else
        if (items.get(position) instanceof DocHeader) {
            return R.layout.doc_item_header;
        }
        else
        if (items.get(position) instanceof DocImage) {
            return R.layout.doc_item_image;
        }
        else
        if (items.get(position) instanceof DocLink) {
            return R.layout.doc_item_link;
        }
        else
        if (items.get(position) instanceof DocFrame) {
            return R.layout.doc_item_frame;
        }
        else {
            return R.layout.doc_item_media;
        }
    }
    @Override
    public RecyclerDocViewAdapter.DocHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new DocHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerDocViewAdapter.DocHolder holder, int position) {

        final IDocItem item = items.get(position);
        int variableId = 0;
        if (items.get(position) instanceof DocText) {
            variableId = BR.doc_view_text;
        }
        else
        if (items.get(position) instanceof DocHeader) {
            variableId = BR.doc_view_header;
        }
        else
        if (items.get(position) instanceof DocImage) {
            variableId = BR.doc_view_image;
        }
        else
        if (items.get(position) instanceof DocLink) {
            variableId = BR.doc_view_link;
        }
        else
        if (items.get(position) instanceof DocFrame) {
            variableId = BR.doc_view_frame;
        }
        else {
            variableId = BR.doc_view_media;
        }
        holder.getBinding().setVariable(variableId, item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class DocHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;

        public DocHolder(View v) {
            super(v);
            binding = DataBindingUtil.bind(v);
        }

        public ViewDataBinding getBinding() {
            return binding;
        }
    }


}




