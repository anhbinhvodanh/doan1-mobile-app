package com.bichan.shop.adapters.product;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bichan.shop.R;
import com.bichan.shop.models.Review;

import java.util.ArrayList;

/**
 * Created by cuong on 5/23/2017.
 */

public class ProductReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Review> lists;
    private Context mContext;

    public ProductReviewAdapter(Context mContext){
        this.mContext = mContext;
        lists = new ArrayList<>();
    }

    public ProductReviewAdapter addItem(Review review){
        if(lists == null)
            lists = new ArrayList<>();
        lists.add(review);
        notifyDataSetChanged();
        return this;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View v = null;

        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_review, null);
        viewHolder = new ProductReviewViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Review review = lists.get(position);
        ProductReviewViewHolder productReviewViewHolder = (ProductReviewViewHolder) holder;
        try{
            productReviewViewHolder.ratingBar.setRating(Integer.parseInt(review.getRating()));
        }catch (NumberFormatException e){
            productReviewViewHolder.ratingBar.setRating(0);
        }
        productReviewViewHolder.tvName.setText(review.getName());
        productReviewViewHolder.tvBuy.setVisibility(review.isBuy()?View.VISIBLE:View.GONE);
        productReviewViewHolder.tvDateAdded.setText(review.getDateAdded());
        productReviewViewHolder.tvText.setText(review.getText());
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    private class ProductReviewViewHolder extends RecyclerView.ViewHolder{
        protected RatingBar ratingBar;
        protected TextView tvName, tvDateAdded, tvBuy, tvText;
        public ProductReviewViewHolder(View itemView) {
            super(itemView);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvDateAdded = (TextView) itemView.findViewById(R.id.tvDateAdded);
            tvBuy = (TextView) itemView.findViewById(R.id.tvBuy);
            tvText = (TextView) itemView.findViewById(R.id.tvText);
            Drawable drawable = ratingBar.getProgressDrawable();
            drawable.setColorFilter(mContext.getResources().getColor(R.color.md_yellow_600), PorterDuff.Mode.SRC_ATOP);
        }
    }
}
