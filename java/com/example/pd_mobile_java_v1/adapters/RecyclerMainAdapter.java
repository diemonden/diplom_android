package com.example.pd_mobile_java_v1.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.AbstractList;
import java.util.ArrayList;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerMainAdapter<T> extends RecyclerView.Adapter<RecyclerMainAdapter.HomeHolder> {
    private int holderLayout, variableId;
    private AbstractList<T> items = new ArrayList<>();
    private OnItemClickListener<T> onItemClickListener;
    private OnItemLongClickListener<T> onItemLongClickListener;

    public RecyclerMainAdapter(int holderLayout, int variableId, AbstractList<T> items) {
        this.holderLayout = holderLayout;
        this.variableId = variableId;
        this.items = items;
    }

    @Override
    public RecyclerMainAdapter.HomeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(holderLayout, parent, false);
        return new HomeHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerMainAdapter.HomeHolder holder, int position) {

        final T item = items.get(position);
        holder.getBinding().getRoot().setOnClickListener(v -> {
            if (onItemClickListener != null)
                onItemClickListener.onItemClick(position, item);
        });
        holder.getBinding().getRoot().setOnLongClickListener(v ->{
            if (onItemLongClickListener != null)
                onItemLongClickListener.onItemLongClick(position, item);
            return true;
        });
        holder.getBinding().setVariable(variableId, item);


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<T> onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(int position, T item);
    }

    public interface OnItemLongClickListener<T>{
        void onItemLongClick(int position, T item);
    }

    public static class HomeHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding binding;

        public HomeHolder(View v) {
            super(v);
            binding = DataBindingUtil.bind(v);
        }

        public ViewDataBinding getBinding() {
            return binding;
        }
    }
}


