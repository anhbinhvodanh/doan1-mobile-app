package com.bichan.shop.adapters.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bichan.shop.R;
import com.bichan.shop.models.HomeCategory;
import com.bichan.shop.models.ProductMini;
import com.bichan.shop.models.ProductMiniLoading;

import java.util.ArrayList;

/**
 * Created by cuong on 5/16/2017.
 */

public class CategoryProductAdapter extends RecyclerView.Adapter<CategoryProductAdapter.ItemRowHolder> {

    private ArrayList<HomeCategory> dataList;
    private Context mContext;
    private OnItemClickListener onItemClickListener;
    private ProductsAdapter.OnItemProductClickListener onItemProductClickListener;

    public CategoryProductAdapter(Context context, ArrayList<HomeCategory> dataList) {
        this.dataList = dataList;
        this.mContext = context;
    }

    public void setOnItemProductClickListener(ProductsAdapter.OnItemProductClickListener onItemProductClickListener){
        this.onItemProductClickListener = onItemProductClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public void startLoading(){
        if(dataList != null){
            HomeCategory homeCategory = null;
            for (int i = 0 ; i < 5; i++){
                homeCategory = new HomeCategory();
                homeCategory.setName("");
                for (int j = 0; j < 5;j++){
                    homeCategory.getProductMinis().add(new ProductMiniLoading());
                }
                dataList.add(homeCategory);
            }
        }
        notifyDataSetChanged();
    }

    public void endLoading(){
        if(dataList != null){
            dataList.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_category_product, null);
        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(ItemRowHolder itemRowHolder, final int i) {
        final HomeCategory homeCategory = dataList.get(i);
        final String sectionName = homeCategory.getName();

        ArrayList singleSectionItems = homeCategory.getProductMinis();

        itemRowHolder.itemTitle.setText(sectionName);

        ProductsAdapter itemListDataAdapter = new ProductsAdapter(mContext, singleSectionItems);

        itemRowHolder.recycler_view_list.setHasFixedSize(true);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        manager.setSpanCount(1);
        itemRowHolder.recycler_view_list.setLayoutManager(manager);
        itemRowHolder.recycler_view_list.setAdapter(itemListDataAdapter);


        itemListDataAdapter.setOnItemProductClickListener(new ProductsAdapter.OnItemProductClickListener() {
            @Override
            public void onClick(ProductMini productMini) {
                if(onItemProductClickListener != null){
                    onItemProductClickListener.onClick(productMini);
                }
            }
        });

        itemRowHolder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null && homeCategory.getCategoryId() != null){
                    onItemClickListener.onClick(homeCategory);
                }
            }
        });


    }

    public interface OnItemClickListener{
        void onClick(HomeCategory homeCategory);
    }



    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        protected TextView itemTitle;
        protected RecyclerView recycler_view_list;
        protected Button btnMore;

        public ItemRowHolder(View view) {
            super(view);
            this.itemTitle = (TextView) view.findViewById(R.id.itemTitle);
            this.recycler_view_list = (RecyclerView) view.findViewById(R.id.rvCategoryProductList);
            this.btnMore= (Button) view.findViewById(R.id.btnMore);
        }

    }

}
