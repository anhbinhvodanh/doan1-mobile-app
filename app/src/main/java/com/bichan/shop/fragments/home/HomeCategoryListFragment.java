package com.bichan.shop.fragments.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bichan.shop.BaseFragment;
import com.bichan.shop.R;
import com.bichan.shop.adapters.CategoryProductAdapter;
import com.bichan.shop.models.HomeCategory;
import com.bichan.shop.models.HomeCategoryResponse;
import com.bichan.shop.networking.NetworkError;
import com.bichan.shop.networking.Service;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class HomeCategoryListFragment extends BaseFragment {
    @Inject
    public Service service;

    @BindView(R.id.rvCategoryProduct)
    RecyclerView rvCategoryProduct;

    private CompositeSubscription subscriptions;
    private ArrayList<HomeCategory> homeCategories;
    private CategoryProductAdapter adapter;
    public static HomeCategoryListFragment newInstance() {
        HomeCategoryListFragment fragment = new HomeCategoryListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDeps().inject(this);
        if (getArguments() != null) {

        }
        subscriptions = new CompositeSubscription();
        homeCategories = new ArrayList<>();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_category_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
        getHomeCategoryList();
    }

    void init(){
        rvCategoryProduct.setHasFixedSize(true);
        adapter = new CategoryProductAdapter(getActivity(), homeCategories);
        rvCategoryProduct.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rvCategoryProduct.setAdapter(adapter);
    }

    private void getHomeCategoryList(){
        adapter.startLoading();

        Subscription subscription = service.getHomeCategory(new Service.GetHomeCategoryCallback() {
            @Override
            public void onSuccess(HomeCategoryResponse homeCategoryResponse) {
                setDataCategoryProduct(homeCategoryResponse.getHomeCategories());
            }

            @Override
            public void onError(NetworkError networkError) {

            }
        });

        subscriptions.add(subscription);
    }

    private void setDataCategoryProduct(ArrayList<HomeCategory> homeCategoriesGet){
        adapter.endLoading();
        for(HomeCategory homeCategory: homeCategoriesGet){
            homeCategories.add(homeCategory);
        }
        adapter.notifyDataSetChanged();
    }
}
