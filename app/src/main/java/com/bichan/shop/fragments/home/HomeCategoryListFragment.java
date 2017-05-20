package com.bichan.shop.fragments.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bichan.shop.BaseFragment;
import com.bichan.shop.BuildConfig;
import com.bichan.shop.R;
import com.bichan.shop.activities.product.ProductDetailActivity;
import com.bichan.shop.activities.products.ProductsActivity;
import com.bichan.shop.adapters.home.CategoryProductAdapter;
import com.bichan.shop.adapters.home.ProductsAdapter;
import com.bichan.shop.models.HomeCategory;
import com.bichan.shop.models.HomeCategoryResponse;
import com.bichan.shop.models.HomeSlider;
import com.bichan.shop.models.HomeSliderResponse;
import com.bichan.shop.models.ProductMini;
import com.bichan.shop.networking.NetworkError;
import com.bichan.shop.networking.Service;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.elyeproj.loaderviewlibrary.LoaderImageView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class HomeCategoryListFragment extends BaseFragment implements BaseSliderView.OnSliderClickListener{
    @Inject
    public Service service;

    @BindView(R.id.rvCategoryProduct)
    RecyclerView rvCategoryProduct;
    @BindView(R.id.slider)
    SliderLayout sliderLayout;
    @BindView(R.id.sliderLoading)
    LoaderImageView sliderLoading;
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
        getHomeSlider();
        getHomeCategoryList();
    }

    void init(){

        sliderLayout.setPresetTransformer(SliderLayout.Transformer.DepthPage);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(4000);

        rvCategoryProduct.setHasFixedSize(true);
        adapter = new CategoryProductAdapter(getActivity(), homeCategories);
        rvCategoryProduct.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rvCategoryProduct.setAdapter(adapter);
        rvCategoryProduct.setNestedScrollingEnabled(false);

        adapter.setOnItemClickListener(new CategoryProductAdapter.OnItemClickListener() {
            @Override
            public void onClick(HomeCategory homeCategory) {
                openProductsActivity(homeCategory.getCategoryId(), homeCategory.getName(), "");
            }
        });

        adapter.setOnItemProductClickListener(new ProductsAdapter.OnItemProductClickListener() {
            @Override
            public void onClick(ProductMini productMini) {
                openProductDetailActivity(productMini.getProductId());
            }
        });
    }

    private void openProductsActivity(String categoryId, String name, String nameSearch){
        Intent productsIntent = new Intent(getActivity(), ProductsActivity.class);
        productsIntent.putExtra(ProductsActivity.EXTRA_CATEGORY_ID, categoryId);
        productsIntent.putExtra(ProductsActivity.EXTRA_CATEGORY_NAME, name);
        productsIntent.putExtra(ProductsActivity.EXTRA_NAME_SEARCH, "");
        getActivity().startActivity(productsIntent);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void openProductDetailActivity(String productId){
        Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
        intent.putExtra(ProductDetailActivity.EXTRA_PRODUCT_ID, productId);
        getActivity().startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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

    private void getHomeSlider(){
        Subscription subscription = service.getHomeSlider(new Service.GetHomeSliderCallback() {
            @Override
            public void onSuccess(HomeSliderResponse homeSliderResponse) {
                setDataSlider(homeSliderResponse.getHomeSliders());
            }

            @Override
            public void onError(NetworkError networkError) {

            }
        });

        subscriptions.add(subscription);
    }

    private void setDataSlider(ArrayList<HomeSlider> homeSliders){
        rx.Observable.from(homeSliders).subscribe(new Subscriber<HomeSlider>() {
            @Override
            public void onCompleted() {
                sliderLoading.setVisibility(View.GONE);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HomeSlider homeSlider) {

                DefaultSliderView defaultSliderView = new DefaultSliderView(getActivity());
                defaultSliderView.image(BuildConfig.BASEURL_IMAGES + homeSlider.getImage())
                        .setScaleType(BaseSliderView.ScaleType.Fit)
                        .setOnSliderClickListener(HomeCategoryListFragment.this);
                defaultSliderView.bundle(new Bundle());
                defaultSliderView.getBundle().putString("id",homeSlider.getId());
                defaultSliderView.getBundle().putString("type",homeSlider.getType());
                sliderLayout.addSlider(defaultSliderView);
            }
        });
    }


    private void setDataCategoryProduct(ArrayList<HomeCategory> homeCategoriesGet){
        adapter.endLoading();
        for(HomeCategory homeCategory: homeCategoriesGet){
            homeCategories.add(homeCategory);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        if(slider.getBundle() != null){
            String id = (String) slider.getBundle().get("id");
            String type = (String) slider.getBundle().get("type");
            switch (type){
                case "category":
                    openProductsActivity(id, "", "");
                    break;
                case "product":
                    openProductDetailActivity(id);
                    break;
            }
        }
    }


}
