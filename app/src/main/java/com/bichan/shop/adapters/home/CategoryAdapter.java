package com.bichan.shop.adapters.home;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bichan.shop.R;
import com.bichan.shop.models.Category;

import java.util.ArrayList;

/**
 * Created by cuong on 5/16/2017.
 */

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int VIEW_TYPE_LOADING = 0;
    public static final int VIEW_TYPE_CATEGORY = 1;
    private ArrayList<Object> objects;
    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public ArrayList<Object> getObjects() {
        return objects;
    }

    public void setObjects(ArrayList<Object> objects) {
        this.objects = objects;
    }

    public void startLoading(){
        for(int i = 0 ; i < 10; i++){
            objects.add(null);
        }
        notifyDataSetChanged();
    }


    public void stopLoading(){
        for(int i = 0 ; i < 10; i++){
            objects.remove(objects.size() - 1);
        }
        notifyDataSetChanged();
    }

    public CategoryAdapter(){
        this.objects = new ArrayList<>();
    }

    public CategoryAdapter addItem(Category category){
        this.objects.add(category);
        notifyDataSetChanged();
        return this;
    }

    public CategoryAdapter addListItem(ArrayList<Category> categories){
        for(Category category : categories){
            this.objects.add(category);
        }
        notifyDataSetChanged();
        return this;
    }

    public void clearData(){
        this.objects.clear();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View v = null;
        switch (viewType){
            case VIEW_TYPE_LOADING:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_loading, null);
                viewHolder = new LoadingViewHolder(v);
                break;
            case VIEW_TYPE_CATEGORY:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, null);
                viewHolder = new CategoryViewHolder(v);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case VIEW_TYPE_CATEGORY:
                CategoryViewHolder categoryViewHolder = (CategoryViewHolder) holder;
                Category category = (Category)objects.get(position);
                categoryViewHolder.tvName.setText(category.getName());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    private class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        protected TextView tvName;
        protected MaterialRippleLayout layout;
        public CategoryViewHolder(View itemView) {
            super(itemView);
            this.tvName = (TextView) itemView.findViewById(R.id.tvName);
            this.layout = (MaterialRippleLayout) itemView.findViewById(R.id.ripple);
            layout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(onItemClickListener != null){
                onItemClickListener.onClick(getAdapterPosition());
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        Object o = objects.get(position);
        if( o == null){
            return VIEW_TYPE_LOADING;
        }
        if(o instanceof Category){
            return VIEW_TYPE_CATEGORY;
        }
        return -1;
    }

    public interface OnItemClickListener{
        void onClick(int position);
    }
}
