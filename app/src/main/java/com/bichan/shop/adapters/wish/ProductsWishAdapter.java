package com.bichan.shop.adapters.wish;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bichan.shop.BuildConfig;
import com.bichan.shop.R;
import com.bichan.shop.adapters.home.LoadingViewHolder;
import com.bichan.shop.custom.views.ImageViewRatio;
import com.bichan.shop.models.ProductMini;
import com.bichan.shop.models.ProductMiniLoading;
import com.squareup.picasso.Picasso;

import org.fabiomsr.moneytextview.MoneyTextView;

import java.util.ArrayList;

/**
 * Created by cuong on 5/25/2017.
 */

public class ProductsWishAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int VIEW_TYPE_LOADING = 0;
    public static final int VIEW_TYPE_PRODUCT = 1;
    private OnItemProductClickListener onItemProductClickListener;
    private OnRemoveClickListener onRemoveClickListener;
    private OnAddToCartClickListener onAddToCartClickListener;
    private boolean single = false;

    private ArrayList<Object> itemsList;
    private Context mContext;

    public void setOnAddToCartClickListener(OnAddToCartClickListener onAddToCartClickListener){
        this.onAddToCartClickListener = onAddToCartClickListener;
    }

    public void setOnItemProductClickListener(OnItemProductClickListener onItemProductClickListener){
        this.onItemProductClickListener = onItemProductClickListener;
    }

    public void setOnRemoveClickListener(OnRemoveClickListener onRemoveClickListener){
        this.onRemoveClickListener = onRemoveClickListener;
    }

    public ProductsWishAdapter(Context context, ArrayList<Object> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }

    public ProductsWishAdapter(Context context) {
        this.itemsList = new ArrayList<>();
        this.mContext = context;
    }

    public void changeView(){
        single = !single;
        notifyDataSetChanged();
    }

    public ArrayList<Object> getItemsList() {
        return itemsList;
    }

    public void startLoading(){
        for(int i = 0 ; i < 10; i++){
            itemsList.add(null);
        }
        notifyDataSetChanged();
    }

    public boolean isSingle(){
        return single;
    }

    public void stopLoading(){
        for(int i = 0 ; i < 10; i++){
            itemsList.remove(itemsList.size() - 1);
        }
        notifyDataSetChanged();
    }

    public void addProducts(ArrayList<ProductMini> productMinis){
        for(ProductMini productMini : productMinis){
            itemsList.add(productMini);
        }
        notifyDataSetChanged();
    }

    public void addProduct(ProductMini productMini){
        itemsList.add(productMini);
        notifyDataSetChanged();
    }

    public void removeProduct(int positon){
        if(positon >= itemsList.size())
            return;
        itemsList.remove(positon);
        notifyDataSetChanged();
    }

    public void clearAll(){
        this.itemsList.clear();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        RecyclerView.ViewHolder viewHolder = null;
        View v = null;
        switch (i){
            case VIEW_TYPE_LOADING:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(single?R.layout.item_product_loading_line:R.layout.item_product_loading, null);
                viewHolder = new LoadingViewHolder(v);
                break;
            case VIEW_TYPE_PRODUCT:
                v = LayoutInflater.from(viewGroup.getContext()).inflate(single?R.layout.item_product_wish:R.layout.item_product, null);
                viewHolder = new SingleItemRowHolder(v);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int i) {

        switch (holder.getItemViewType()){
            case VIEW_TYPE_LOADING:
                break;
            case VIEW_TYPE_PRODUCT:
                final ProductMini singleItem = (ProductMini) itemsList.get(i);
                SingleItemRowHolder singleItemRowHolder = (SingleItemRowHolder) holder;
                singleItemRowHolder.tvName.setText(singleItem.getName());
                singleItemRowHolder.tvDiscount.setAmount(Float.parseFloat(singleItem.getDiscount()));
                singleItemRowHolder.tvPrice.setAmount(Float.parseFloat(singleItem.getPrice()));
                float discount = Float.parseFloat(singleItem.getDiscount());
                float price = Float.parseFloat(singleItem.getPrice());
                if(discount == price){
                    singleItemRowHolder.tvPrice.setVisibility(View.INVISIBLE);
                    singleItemRowHolder.tvSale.setVisibility(View.INVISIBLE);
                }else {
                    int sale = (int) ( 100 - (discount / price) * 100);
                    singleItemRowHolder.tvSale.setText("-" + sale + "%");
                }

                try{
                    singleItemRowHolder.ratingBar.setRating(Float.parseFloat(singleItem.getRating()));
                }catch (Exception e){
                    singleItemRowHolder.ratingBar.setRating(0);
                }

                Picasso.with(mContext).load(BuildConfig.BASEURL_IMAGES + singleItem.getImage()).into(singleItemRowHolder.image);

                singleItemRowHolder.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(onItemProductClickListener != null){
                            onItemProductClickListener.onClick(singleItem);
                        }
                    }
                });

                singleItemRowHolder.btnRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(onRemoveClickListener != null){
                            onRemoveClickListener.onClick(singleItem, i);
                        }
                    }
                });

                singleItemRowHolder.btnAddToCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(onAddToCartClickListener != null){
                            onAddToCartClickListener.onClick(singleItem);
                        }
                    }
                });

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
        if( o == null || o instanceof ProductMiniLoading){
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
        protected MaterialRippleLayout layout;
        protected AppCompatImageButton btnRemove;
        protected Button btnAddToCart;
        public SingleItemRowHolder(View view) {
            super(view);
            this.tvName = (TextView) view.findViewById(R.id.tvName);
            this.tvDiscount = (MoneyTextView) view.findViewById(R.id.tvDiscount);
            this.tvPrice = (MoneyTextView) view.findViewById(R.id.tvPrice);
            this.tvSale = (TextView) view.findViewById(R.id.tvSale);
            this.image = (ImageViewRatio) view.findViewById(R.id.image);
            this.ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
            this.layout = (MaterialRippleLayout) view.findViewById(R.id.ripple);

            btnAddToCart = (Button) view.findViewById(R.id.btnAddToCart);

            btnRemove = (AppCompatImageButton) view.findViewById(R.id.btnRemove);

            Drawable drawable = ratingBar.getProgressDrawable();
            drawable.setColorFilter(mContext.getResources().getColor(R.color.md_yellow_600), PorterDuff.Mode.SRC_ATOP);

        }

    }

    public interface OnItemProductClickListener{
        void onClick(ProductMini productMini);
    }

    public interface OnRemoveClickListener{
        void onClick(ProductMini productMini, int position);
    }

    public interface OnAddToCartClickListener{
        void onClick(ProductMini productMini);
    }
}
