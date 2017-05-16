package com.bichan.shop.fragments.home;

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
import com.bichan.shop.adapters.CategoryProductAdapter;
import com.bichan.shop.models.HomeCategory;
import com.bichan.shop.models.HomeCategoryResponse;
import com.bichan.shop.models.HomeSlider;
import com.bichan.shop.models.HomeSliderResponse;
import com.bichan.shop.networking.NetworkError;
import com.bichan.shop.networking.Service;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

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

        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(4000);

        rvCategoryProduct.setHasFixedSize(true);
        adapter = new CategoryProductAdapter(getActivity(), homeCategories);
        rvCategoryProduct.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        rvCategoryProduct.setAdapter(adapter);
        rvCategoryProduct.setNestedScrollingEnabled(false);
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

    }
}
