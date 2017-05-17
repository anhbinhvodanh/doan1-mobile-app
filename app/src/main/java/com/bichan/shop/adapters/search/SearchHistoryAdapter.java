package com.bichan.shop.adapters.search;

import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bichan.shop.R;

import java.util.ArrayList;

/**
 * Created by cuong on 5/17/2017.
 */

public class SearchHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<String> strings;
    OnItemNameClickListener onItemNameClickListener;
    OnItemSearchClickListener onItemSearchClickListener;

    public void setOnItemNameClickListener(OnItemNameClickListener onItemNameClickListener){
        this.onItemNameClickListener = onItemNameClickListener;
    }

    public void setOnItemSearchClickListener(OnItemSearchClickListener onItemSearchClickListener){
        this.onItemSearchClickListener = onItemSearchClickListener;
    }

    public SearchHistoryAdapter(){
        this.strings = new ArrayList<>();
    }


    public ArrayList<String> getStrings() {
        return strings;
    }


    public void addItem(String s){
        strings.add(s);
        notifyDataSetChanged();
    }

    public void clearAll(){
        strings.clear();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_history, null);
        viewHolder = new SearchHistoryViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SearchHistoryViewHolder searchHistoryViewHolder = (SearchHistoryViewHolder) holder;
        String s = strings.get(position);
        searchHistoryViewHolder.tvName.setText(s);
    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    private class SearchHistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        protected TextView tvName;
        protected AppCompatImageButton btnSearch;
        protected LinearLayout btnSetData;
        public SearchHistoryViewHolder(View itemView) {
            super(itemView);
            this.tvName = (TextView) itemView.findViewById(R.id.tvName);
            btnSetData = (LinearLayout) itemView.findViewById(R.id.btnSetData);
            btnSetData.setOnClickListener(this);
            btnSearch = (AppCompatImageButton) itemView.findViewById(R.id.btnSearch);
            btnSearch.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnSetData:
                    if(onItemNameClickListener !=null){
                        onItemNameClickListener.onClick(getAdapterPosition());
                    }
                    break;
                case R.id.btnSearch:
                    if(onItemSearchClickListener != null){
                        onItemSearchClickListener.onClick(getAdapterPosition());
                    }
                    break;
            }
        }
    }

    public interface OnItemNameClickListener{
        void onClick(int position);
    }

    public interface OnItemSearchClickListener{
        void onClick(int position);
    }
}