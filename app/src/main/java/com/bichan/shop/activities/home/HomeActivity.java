package com.bichan.shop.activities.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bichan.shop.BaseApp;
import com.bichan.shop.R;
import com.bichan.shop.activities.search.SearchActivity;
import com.bichan.shop.fragments.home.AccountFragment;
import com.bichan.shop.fragments.home.CartFragment;
import com.bichan.shop.fragments.home.CategoryFragment;
import com.bichan.shop.fragments.home.HomeCategoryListFragment;
import com.bichan.shop.networking.Service;
import com.nex3z.notificationbadge.NotificationBadge;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends BaseApp implements OnTabSelectListener, OnTabReselectListener{
    @Inject
    public Service service;

    @BindView(R.id.bottomBar)
    BottomBar bottomBar;
    @BindView(R.id.favoriteBadge)
    NotificationBadge favoriteBadge;
    @BindView(R.id.btnSearch)
    MaterialRippleLayout btnSearch;

    private HomeCategoryListFragment homeCategoryListFragment;
    private CategoryFragment categoryFragment;
    private CartFragment cartFragment;
    private AccountFragment accountFragment;
    BottomBarTab cartTab;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDeps().inject(this);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        init();
        initView();
    }

    private void initView(){
        bottomBar.setOnTabReselectListener(this);
        bottomBar.setOnTabSelectListener(this);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent productsIntent = new Intent(HomeActivity.this, SearchActivity.class);
                startActivity(productsIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        cartTab = bottomBar.getTabWithId(R.id.tab_cart);
        cartTab.setBadgeCount(5);
        favoriteBadge.setNumber(3);
    }

    private void init(){
        homeCategoryListFragment = HomeCategoryListFragment.newInstance();
        categoryFragment = CategoryFragment.newInstance();
    }

    @Override
    public void onTabSelected(@IdRes int tabId) {
        switch (tabId){
            case R.id.tab_home:
                showHomeCategoryListFragment();
                break;
            case R.id.tab_category:
                showCategoryFragment();
                break;
            case R.id.tab_cart:
                break;
            case R.id.tab_account:
                break;
        }
    }


    private void showHomeCategoryListFragment(){
        FragmentTransaction ft = getSupportFragmentManager()
                .beginTransaction();

        if (homeCategoryListFragment.isAdded()) {
            ft.show(homeCategoryListFragment);
        } else {
            ft.add(R.id.placeFragment, homeCategoryListFragment, "HomeCaterogyList");
        }

        if (categoryFragment.isAdded()) { ft.hide(categoryFragment); }

        ft.commit();
    }

    private void showCategoryFragment(){
        FragmentTransaction ft = getSupportFragmentManager()
                .beginTransaction();

        if (categoryFragment.isAdded()) {
            ft.show(categoryFragment);
        } else {
            ft.add(R.id.placeFragment, categoryFragment, "Caterogy");
        }

        if (homeCategoryListFragment.isAdded()) { ft.hide(homeCategoryListFragment); }

        ft.commit();
    }

    @Override
    public void onTabReSelected(@IdRes int tabId) {

    }

    @Override
    public void onBackPressed() {
        if (onBackPressedListener != null && bottomBar.getCurrentTabId() == R.id.tab_category)
            onBackPressedListener.doBack();
        else
            super.onBackPressed();
    }

    protected OnBackPressedListener onBackPressedListener;


    public interface OnBackPressedListener {
        void doBack();
    }

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }
}
