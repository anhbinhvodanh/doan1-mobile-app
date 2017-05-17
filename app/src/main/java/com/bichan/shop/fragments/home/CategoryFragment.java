package com.bichan.shop.fragments.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bichan.shop.BaseFragment;
import com.bichan.shop.R;
import com.bichan.shop.activities.home.HomeActivity;
import com.bichan.shop.activities.products.ProductsActivity;
import com.bichan.shop.adapters.home.CategoryAdapter;
import com.bichan.shop.models.Category;
import com.bichan.shop.models.CategoryResponse;
import com.bichan.shop.networking.NetworkError;
import com.bichan.shop.networking.Service;

import java.util.ArrayList;
import java.util.Stack;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;
import rx.subscriptions.CompositeSubscription;


public class CategoryFragment extends BaseFragment implements HomeActivity.OnBackPressedListener{
    @Inject
    public Service service;
    @BindView(R.id.rvCategory)
    RecyclerView rvCategory;
    @BindView(R.id.layoutBack)
    LinearLayout layoutBack;
    @BindView(R.id.btnBack)
    AppCompatImageButton btnBack;
    @BindView(R.id.btnMore)
    Button btnMore;
    @BindView(R.id.tvName)
    TextView tvName;
    

    private CompositeSubscription subscriptions;
    private ArrayList<Category> categories;
    private CategoryAdapter adapter;
    private Category categorySelected;

    private HomeActivity homeActivity;

    private Stack<Category> stackCategories = new Stack <>();

    public static CategoryFragment newInstance() {
        CategoryFragment fragment = new CategoryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDeps().inject(this);
        if (getArguments() != null) {

        }
        subscriptions = new CompositeSubscription();
        categories = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();

        getCategoryList();

        adapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                categorySelected = adapter.getCategories().get(position);
                stackCategories.push(categorySelected);
                nextCaterogy(categorySelected.getCategoryId());
            }
        });
    }

    private void init() {
        rvCategory.setHasFixedSize(true);
        adapter = new CategoryAdapter();
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        manager.setSpanCount(1);
        rvCategory.setLayoutManager(manager);
        rvCategory.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvCategory.getContext(),
                manager.getOrientation());
        rvCategory.addItemDecoration(dividerItemDecoration);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doBack();
            }
        });

        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(categorySelected != null){
                    openProducts(categorySelected);
                }
            }
            
        });
    }

    private void openProducts(Category categorySelected) {
        Intent productsIntent = new Intent(getActivity(), ProductsActivity.class);
        productsIntent.putExtra(ProductsActivity.EXTRA_CATEGORY_ID, categorySelected.getCategoryId());
        productsIntent.putExtra(ProductsActivity.EXTRA_CATEGORY_NAME, categorySelected.getName());
        productsIntent.putExtra(ProductsActivity.EXTRA_NAME_SEARCH, "");
        getActivity().startActivity(productsIntent);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void getCategoryList(){
        Subscription subscription = service.getCategory(new Service.GetCategoryCallback() {
            @Override
            public void onSuccess(CategoryResponse categoryResponse) {
                categories = categoryResponse.getCategories();
                nextCaterogy("0");
            }

            @Override
            public void onError(NetworkError networkError) {

            }
        });
        subscriptions.add(subscription);
    }

    private void backCaterogy(final String id){
        final ArrayList<Category> categorieFilter = new ArrayList<>();
        Observable.from(categories).filter(new Func1<Category, Boolean>() {
            @Override
            public Boolean call(Category category) {
                return category.getParentId().equals(id);
            }
        }).subscribe(new Subscriber<Category>() {
            @Override
            public void onCompleted() {
                if(categorieFilter.size() > 0){
                    adapter.clearData();
                    adapter.addListItem(categorieFilter);
                }else{
                    homeActivity.setOnBackPressedListener(null);
                    categorySelected = null;
                }
                setLayoutBackInfo();
            }

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onNext(Category category) {
                categorieFilter.add(category);
            }
        });
    }

    private void nextCaterogy(final String id){
        final ArrayList<Category> categorieFilter = new ArrayList<>();
        Observable.from(categories).filter(new Func1<Category, Boolean>() {
            @Override
            public Boolean call(Category category) {
                return category.getParentId().equals(id);
            }
        }).subscribe(new Subscriber<Category>() {
            @Override
            public void onCompleted() {
                if(categorieFilter.size() > 0){
                    adapter.clearData();
                    adapter.addListItem(categorieFilter);
                    homeActivity.setOnBackPressedListener(CategoryFragment.this);
                }else{
                    stackCategories.pop();
                    if(categorySelected != null){
                        openProducts(categorySelected);
                    }
                }
                setLayoutBackInfo();
            }

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onNext(Category category) {
                categorieFilter.add(category);
            }
        });
    }

    private void setLayoutBackInfo(){
        if(categorySelected == null || categorySelected.getCategoryId().equals("0")){
            layoutBack.setVisibility(View.GONE);
        }else{
            layoutBack.setVisibility(View.VISIBLE);
            tvName.setText(categorySelected.getName());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof HomeActivity){
            homeActivity = (HomeActivity) context;
            homeActivity.setOnBackPressedListener(this);
        }
    }

    @Override
    public void doBack() {
        if(stackCategories.size() > 0){
            categorySelected = stackCategories.pop();
            backCaterogy(categorySelected.getParentId());
        }else{
            homeActivity.setOnBackPressedListener(null);
            categorySelected = null;
            setLayoutBackInfo();
        }
    }
}
