package com.bichan.shop.adapters.order;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bichan.shop.R;
import com.bichan.shop.models.Order;

import java.util.ArrayList;

/**
 * Created by cuong on 6/4/2017.
 */

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Order> orders;

    private OnItemClickListener onItemClickListener;

    public OrderAdapter(){
        orders = new ArrayList<>();
    }

    public void setOrders(ArrayList<Order> orders){
        this.orders = orders;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public void clear(){
        this.orders = new ArrayList<>();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View v = null;

        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, null);
        viewHolder = new OrderViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Order order = orders.get(position);
        OrderViewHolder orderViewHolder = (OrderViewHolder) holder;
        orderViewHolder.tvOrderId.setText(order.getOrderId());
        orderViewHolder.tvDateAdded.setText(order.getDateAdded());
        orderViewHolder.tvShippingStatus.setText(order.getShippingStatus());
        orderViewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.onClick(order);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    private class OrderViewHolder extends RecyclerView.ViewHolder{
        protected TextView tvOrderId, tvDateAdded, tvShippingStatus;
        protected MaterialRippleLayout layout;
        public OrderViewHolder(View itemView) {
            super(itemView);
            tvOrderId = (TextView) itemView.findViewById(R.id.tvOrderId);
            tvDateAdded = (TextView) itemView.findViewById(R.id.tvDateAdded);
            tvShippingStatus = (TextView) itemView.findViewById(R.id.tvShippingStatus);
            layout = (MaterialRippleLayout) itemView.findViewById(R.id.ripple);
        }
    }

    public interface OnItemClickListener{
        void onClick(Order order);
    }
}
