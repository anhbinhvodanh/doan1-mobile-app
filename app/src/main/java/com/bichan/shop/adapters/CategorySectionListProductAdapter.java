package com.bichan.shop.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bichan.shop.BuildConfig;
import com.bichan.shop.R;
import com.bichan.shop.custom.views.ImageViewRatio;
import com.bichan.shop.models.ProductMiniLoading;
import com.bichan.shop.models.ProductMini;
import com.squareup.picasso.Picasso;

import org.fabiomsr.moneytextview.MoneyTextView;

import java.util.ArrayList;

/**
 * Created by cuong on 5/16/2017.
 */

public class CategorySectionListProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int VIEW_TYPE_LOADING = 0;
    public static final int VIEW_TYPE_PRODUCT = 1;

    private ArrayList<Object> itemsList;
    private Context mContext;

    public CategorySectionListProductAdapter(Context context, ArrayList<Object> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder viewHolder = null;
        View v = null;
        switch (i){
            case VIEW_TYPE_LOADING:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_product_loading, null);
                viewHolder = new LoadingViewHolder(v);
                break;
            case VIEW_TYPE_PRODUCT:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_product, null);
                viewHolder = new SingleItemRowHolder(v);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {

        switch (holder.getItemViewType()){
            case VIEW_TYPE_LOADING:
                break;
            case VIEW_TYPE_PRODUCT:
                ProductMini singleItem = (ProductMini) itemsList.get(i);
                SingleItemRowHolder singleItemRowHolder = (SingleItemRowHolder) holder;
                singleItemRowHolder.tvName.setText(singleItem.getName());
                singleItemRowHolder.tvDiscount.setAmount(Float.parseFloat(singleItem.getDiscount()));
                singleItemRowHolder.tvPrice.setAmount(Float.parseFloat(singleItem.getPrice()));
                int sale = (int) ( 100 - Float.parseFloat(singleItem.getDiscount()) / (Float.parseFloat(singleItem.getPrice())) * 100);
                singleItemRowHolder.tvSale.setText("-" + sale + "%");
                singleItemRowHolder.ratingBar.setRating(2);
                Picasso.with(mContext).load(BuildConfig.BASEURL_IMAGES + singleItem.getImage()).into(singleItemRowHolder.image);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return (null != itemsList ? itemsList.size() : 0);
    }

    @Override
    public int getItemViewType(int position) {
        Object o = itemsList.get(position);
        if(o instanceof ProductMiniLoading){
            return VIEW_TYPE_LOADING;
        }
        if(o instanceof ProductMini){
            return VIEW_TYPE_PRODUCT;
        }
        return -1;
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView tvName,  tvSale;
        protected MoneyTextView tvDiscount, tvPrice;
        protected ImageViewRatio image;
        protected RatingBar ratingBar;

        public SingleItemRowHolder(View view) {
            super(view);
            this.tvName = (TextView) view.findViewById(R.id.tvName);
            this.tvDiscount = (MoneyTextView) view.findViewById(R.id.tvDiscount);
            this.tvPrice = (MoneyTextView) view.findViewById(R.id.tvPrice);
            this.tvSale = (TextView) view.findViewById(R.id.tvSale);
            this.image = (ImageViewRatio) view.findViewById(R.id.image);
            this.ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);

            Drawable drawable = ratingBar.getProgressDrawable();
            drawable.setColorFilter(mContext.getResources().getColor(R.color.md_yellow_600), PorterDuff.Mode.SRC_ATOP);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), tvName.getText(), Toast.LENGTH_SHORT).show();
                }
            });


        }

    }



}
