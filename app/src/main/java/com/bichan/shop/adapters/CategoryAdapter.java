package com.bichan.shop.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bichan.shop.R;
import com.bichan.shop.models.Category;

import java.util.ArrayList;

/**
 * Created by cuong on 5/16/2017.
 */

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Category> categories;
    OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public CategoryAdapter(){
        this.categories = new ArrayList<>();
    }

    public CategoryAdapter addItem(Category category){
        this.categories.add(category);
        notifyDataSetChanged();
        return this;
    }

    public CategoryAdapter addListItem(ArrayList<Category> categories){
        for(Category category : categories){
            this.categories.add(category);
        }
        notifyDataSetChanged();
        return this;
    }

    public void clearData(){
        this.categories.clear();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, null);
        viewHolder = new CategoryViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CategoryViewHolder categoryViewHolder = (CategoryViewHolder) holder;
        Category category = categories.get(position);
        categoryViewHolder.tvName.setText(category.getName());
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    private class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        protected TextView tvName;
        public CategoryViewHolder(View itemView) {
            super(itemView);
            this.tvName = (TextView) itemView.findViewById(R.id.tvName);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(onItemClickListener != null){
                onItemClickListener.onClick(getAdapterPosition());
            }
        }
    }

    public interface OnItemClickListener{
        void onClick(int position);
    }
}
