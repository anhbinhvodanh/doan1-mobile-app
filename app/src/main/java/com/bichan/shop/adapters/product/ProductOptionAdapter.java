package com.bichan.shop.adapters.product;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bichan.shop.R;
import com.bichan.shop.models.ProductOption;

import java.util.ArrayList;

/**
 * Created by cuong on 5/20/2017.
 */

public class ProductOptionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_TYPE = 0;
    private static final int TYPE_TYPE_SELECTED = 1;

    private OnProductOptionItemClick onProductOptionItemClick;

    public void setOnProductOptionItemClick(OnProductOptionItemClick onProductOptionItemClick){
        this.onProductOptionItemClick = onProductOptionItemClick;
    }

    private int selectedIndex;
    private Context mContext;
    private ArrayList<ProductOption> lists;

    public ProductOptionAdapter(Context mContext){
        this.mContext = mContext;
        this.lists = new ArrayList<>();
        selectedIndex = -1;
    }


    public ProductOption click(int selectedIndex){
        if(selectedIndex < lists.size()){
            this.selectedIndex = selectedIndex;
            notifyDataSetChanged();
            return lists.get(selectedIndex);
        }
        return null;
    }

    public ProductOptionAdapter addItem(ProductOption productOption){
        this.lists.add(productOption);
        notifyDataSetChanged();
        return this;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View view = null;
        switch (viewType){
            case TYPE_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_option, null);
                viewHolder = new ProductOptionViewHolder(view);
                break;
            case TYPE_TYPE_SELECTED:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_option_selected, null);
                viewHolder = new ProductOptionViewHolder(view);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ProductOption productOption = lists.get(position);
        ProductOptionViewHolder productOptionViewHolder = (ProductOptionViewHolder) holder;
        productOptionViewHolder.tvTitle.setText(productOption.getOptionName());
        productOptionViewHolder.tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedIndex = position;
                notifyDataSetChanged();
                if(onProductOptionItemClick != null){
                    onProductOptionItemClick.onClick(productOption);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(position == selectedIndex){
            return TYPE_TYPE_SELECTED;
        }
        if(position != selectedIndex){
            return TYPE_TYPE;
        }
        return -1;
    }

    private class ProductOptionViewHolder extends RecyclerView.ViewHolder{
        protected TextView tvTitle;
        public ProductOptionViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        }
    }

    public interface OnProductOptionItemClick{
        void onClick(ProductOption productOption);
    }
}
