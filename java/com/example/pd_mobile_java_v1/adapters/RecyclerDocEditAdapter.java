package com.example.pd_mobile_java_v1.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.pd_mobile_java_v1.BR;
import com.example.pd_mobile_java_v1.R;
import com.example.pd_mobile_java_v1.doc.objects.DocFrame;
import com.example.pd_mobile_java_v1.doc.objects.DocHeader;
import com.example.pd_mobile_java_v1.doc.objects.DocImage;
import com.example.pd_mobile_java_v1.doc.objects.DocLink;
import com.example.pd_mobile_java_v1.doc.objects.DocText;
import com.example.pd_mobile_java_v1.doc.objects.IDocItem;
import com.example.pd_mobile_java_v1.util.AppUtility;

import java.util.AbstractList;
import java.util.ArrayList;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.pd_mobile_java_v1.util.AppUtility.getAllChildren;

public class RecyclerDocEditAdapter extends RecyclerView.Adapter<RecyclerDocEditAdapter.DocHolder> {
    private AbstractList<IDocItem> items = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public RecyclerDocEditAdapter(AbstractList<IDocItem> items) {
        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof DocText) {
            return R.layout.edit_item_text;
        }
        else
        if (items.get(position) instanceof DocHeader) {
            return R.layout.edit_item_header;
        }
        else
        if (items.get(position) instanceof DocImage) {
            return R.layout.edit_item_image;
        }
        else
        if (items.get(position) instanceof DocLink) {
            return R.layout.edit_item_link;
        }
        else
        if (items.get(position) instanceof DocFrame) {
            return R.layout.edit_item_frame;
        }
        else {
            return R.layout.edit_item_media;
        }
    }
    @Override
    public RecyclerDocEditAdapter.DocHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new DocHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerDocEditAdapter.DocHolder holder, int position) {

        final IDocItem item = items.get(position);

        ArrayList<View> children = AppUtility.getAllChildren(holder.getBinding().getRoot());
        for (View view:children) {
            if(view instanceof EditText) {
                view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        if (onItemClickListener != null)
                            onItemClickListener.onItemClick(position, item);
                    }
                });
            }
        }

        int variableId = 0;
        if (items.get(position) instanceof DocText) {
            variableId = BR.doc_edit_text;
        }
        else
        if (items.get(position) instanceof DocHeader) {
            variableId = BR.doc_edit_header;
        }
        else
        if (items.get(position) instanceof DocImage) {
            variableId = BR.doc_edit_image;
        }
        else
        if (items.get(position) instanceof DocLink) {
            variableId = BR.doc_edit_link;
        }
        else
        if (items.get(position) instanceof DocFrame) {
            variableId = BR.doc_edit_frame;
        }
        else {
            variableId = BR.doc_edit_media;
        }
        holder.getBinding().setVariable(variableId, item);
    }

    public void focus(int position){

    }
    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, IDocItem item);
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




