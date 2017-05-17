package com.bichan.shop.activities.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ArraySet;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.bichan.shop.MyApplication;
import com.bichan.shop.R;
import com.bichan.shop.activities.products.ProductsActivity;
import com.bichan.shop.adapters.search.SearchHistoryAdapter;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {
    public static final String EXTRA_CATEGORY_ID = "EXTRA_CATEGORY_ID";
    public static final String EXTRA_CATEGORY_NAME = "EXTRA_CATEGORY_NAME";
    public static final String EXTRA_NAME_SEARCH = "EXTRA_NAME_SEARCH";

    @BindView(R.id.btnBack)
    AppCompatImageButton btnBack;
    @BindView(R.id.btnClear)
    AppCompatImageButton btnClear;
    @BindView(R.id.edtSearch)
    EditText edtSearch;
    @BindView(R.id.rvHistory)
    RecyclerView rvHistory;
    Set<String> textSearchHistory;

    private String categoryId;
    private String categoryName;
    private String textSearch;

    private SearchHistoryAdapter searchHistoryAdapter;
    StaggeredGridLayoutManager manager;

    private void init(){
        textSearchHistory = Prefs.getOrderedStringSet(MyApplication.KEY_SEARCH_HISTORY, new ArraySet<String>());
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            categoryId = bundle.getString(EXTRA_CATEGORY_ID);
            categoryName = bundle.getString(EXTRA_CATEGORY_NAME);
            textSearch = bundle.getString(EXTRA_NAME_SEARCH);
        }
        edtSearch.setText(textSearch);
    }

    private void initView(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtSearch.setText("");
            }
        });
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_DONE){
                    textSearch = edtSearch.getText().toString().trim();
                    if(!textSearch.equals("")){
                        // save history
                        saveSearchHistory(textSearch);
                        // open products
                        openProducts(textSearch);
                        return true;
                    }
                }
                return false;
            }
        });

        rvHistory.setHasFixedSize(true);
        searchHistoryAdapter = new SearchHistoryAdapter();
        manager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        manager.setSpanCount(1);
        rvHistory.setLayoutManager(manager);
        rvHistory.setAdapter(searchHistoryAdapter);

        for(String s : textSearchHistory){
            searchHistoryAdapter.addItem(s);
        }

        searchHistoryAdapter.setOnItemNameClickListener(new SearchHistoryAdapter.OnItemNameClickListener() {
            @Override
            public void onClick(int position) {
                edtSearch.setText(searchHistoryAdapter.getStrings().get(position));
            }
        });

        searchHistoryAdapter.setOnItemSearchClickListener(new SearchHistoryAdapter.OnItemSearchClickListener() {
            @Override
            public void onClick(int position) {
                textSearch = searchHistoryAdapter.getStrings().get(position);
                openProducts(textSearch);
            }
        });

    }

    private void openProducts(String textSearch) {
        Intent productsIntent = new Intent(this, ProductsActivity.class);
        productsIntent.putExtra(ProductsActivity.EXTRA_CATEGORY_ID, "");
        productsIntent.putExtra(ProductsActivity.EXTRA_CATEGORY_NAME, "");
        productsIntent.putExtra(ProductsActivity.EXTRA_NAME_SEARCH, textSearch);
        startActivity(productsIntent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void saveSearchHistory(String textSearch) {
        textSearchHistory.add(textSearch);
        Prefs.putOrderedStringSet(MyApplication.KEY_SEARCH_HISTORY, textSearchHistory);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        init();
        initView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
