package com.bichan.shop.adapters.order;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bichan.shop.R;
import com.bichan.shop.models.OrderProduct;

import org.fabiomsr.moneytextview.MoneyTextView;

import java.util.ArrayList;

/**
 * Created by cuong on 6/4/2017.
 */

public class OrderProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<OrderProduct> orderProducts;

    private OnItemClickListener onItemClickListener;

    public OrderProductAdapter(){
        orderProducts = new ArrayList<>();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }


    public void setOrderProducts(ArrayList<OrderProduct> orderProducts){
        this.orderProducts = orderProducts;
        notifyDataSetChanged();
    }

    public void clear(){
        this.orderProducts = new ArrayList<>();
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View v = null;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_product, null);
        viewHolder = new OrderProductViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final OrderProduct orderProduct = orderProducts.get(position);
        OrderProductViewHolder viewHolder = (OrderProductViewHolder) holder;
        viewHolder.tvName.setText(orderProduct.getName());
        viewHolder.tvQuantity.setText(orderProduct.getQuantity());
        float price = 0;
        float total = 0;

        try {
            price = Float.parseFloat(orderProduct.getPrice());
        }catch (Exception e){
            price = 0;
        }

        try {
            total = Float.parseFloat(orderProduct.getTotal());
        }catch (Exception e){
            total = 0;
        }

        viewHolder.tvPrice.setAmount(price);
        viewHolder.tvTotal.setAmount(total);


        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.onClick(orderProduct);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderProducts.size();
    }

    private class OrderProductViewHolder extends RecyclerView.ViewHolder{
        protected TextView tvName, tvQuantity;
        protected MoneyTextView tvPrice, tvTotal;
        protected MaterialRippleLayout layout;
        public OrderProductViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvQuantity = (TextView) itemView.findViewById(R.id.tvQuantity);
            tvPrice = (MoneyTextView) itemView.findViewById(R.id.tvPrice);
            tvTotal = (MoneyTextView) itemView.findViewById(R.id.tvTotal);
            layout = (MaterialRippleLayout) itemView.findViewById(R.id.ripple);
        }
    }

    public interface OnItemClickListener{
        void onClick(OrderProduct orderProduct);
    }
}
