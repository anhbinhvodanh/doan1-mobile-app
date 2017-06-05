package com.bichan.shop.activities.product;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bichan.shop.BaseApp;
import com.bichan.shop.BuildConfig;
import com.bichan.shop.MyApplication;
import com.bichan.shop.Prefs.PrefsUser;
import com.bichan.shop.R;
import com.bichan.shop.activities.cart.CartActivity;
import com.bichan.shop.activities.login.LoginActivity;
import com.bichan.shop.activities.products.ProductsActivity;
import com.bichan.shop.activities.search.SearchActivity;
import com.bichan.shop.adapters.home.CategoryProductAdapter;
import com.bichan.shop.adapters.home.ProductsAdapter;
import com.bichan.shop.adapters.product.ProductOptionAdapter;
import com.bichan.shop.adapters.product.ProductReviewAdapter;
import com.bichan.shop.models.HomeCategory;
import com.bichan.shop.models.Product;
import com.bichan.shop.models.ProductMini;
import com.bichan.shop.models.ProductMiniResponse;
import com.bichan.shop.models.ProductOption;
import com.bichan.shop.models.ProductOptionImage;
import com.bichan.shop.models.ProductOptionResponse;
import com.bichan.shop.models.ProductResponse;
import com.bichan.shop.models.ProductsFilter;
import com.bichan.shop.models.Review;
import com.bichan.shop.models.ReviewResponse;
import com.bichan.shop.models.SubmitResponse;
import com.bichan.shop.networking.NetworkError;
import com.bichan.shop.networking.Service;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.nex3z.notificationbadge.NotificationBadge;

import org.fabiomsr.moneytextview.MoneyTextView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class ProductDetailActivity extends BaseApp implements AppBarLayout.OnOffsetChangedListener, BaseSliderView.OnSliderClickListener {
    public static final String EXTRA_PRODUCT_ID = "EXTRA_PRODUCT_ID";
    public static final String EXTRA_OPTION_ID = "EXTRA_OPTION_ID";

    @Inject
    public Service service;

    @BindView(R.id.appbar)
    AppBarLayout appBar;
    @BindView(R.id.btnWish)
    FloatingActionButton btnWish;

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.btnBack)
    AppCompatImageButton btnBack;

    @BindView(R.id.btnCart)
    AppCompatImageButton btnCart;
    @BindView(R.id.cartBadge)
    NotificationBadge cartBadge;
    @BindView(R.id.btnSearch)
    AppCompatImageButton btnSearch;
    @BindView(R.id.slider)
    SliderLayout sliderLayout;
    @BindView(R.id.tvDescriptionShort)
    TextView tvDescriptionShort;
    @BindView(R.id.tvDescription)
    TextView tvDescription;
    @BindView(R.id.btnDescriptionMore)
    Button btnDescriptionMore;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvDiscount)
    MoneyTextView tvDiscount;
    @BindView(R.id.tvPrice)
    MoneyTextView tvPrice;
    @BindView(R.id.tvSale)
    TextView tvSale;
    @BindView(R.id.ratingBar)
    RatingBar ratingBar;

    @BindView(R.id.btnAddToCart)
    Button btnAddToCart;

    @BindView(R.id.rvProductOption)
    RecyclerView rvProductOption;

    @BindView(R.id.rvProductReview)
    RecyclerView rvProductReview;

    @BindView(R.id.rvCategoryProduct)
    RecyclerView rvCategoryProduct;
    @BindView(R.id.btnNewReview)
    Button btnNewReview;
    @BindView(R.id.btnReviewMore)
    Button btnReviewMore;

    MyApplication mApp;

    private ProductOptionAdapter productOptionAdapter;
    StaggeredGridLayoutManager manager;

    private ProductReviewAdapter productReviewAdapter;
    StaggeredGridLayoutManager manager2;

    private MaterialDialog dialogLoading;

    private CompositeSubscription subscriptions;
    private String productId;
    private String optionId;

    private Product product;
    private ProductOption productOption;

    private ArrayList<HomeCategory> homeCategories;
    private CategoryProductAdapter adapter;

    private ProductsFilter productsFilter;

    private boolean[] loadingFinish;

    private boolean addToCart;
    private boolean addToWish;

    private boolean wish = false;

    private void init(){
        wish = false;
        addToCart = false;
        addToWish = false;
        mApp = ((MyApplication)getApplicationContext());
        appBar.addOnOffsetChangedListener(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null) {
            productId = bundle.getString(EXTRA_PRODUCT_ID);
            optionId = bundle.getString(EXTRA_OPTION_ID);
        }else{
            finish();
        }
        subscriptions = new CompositeSubscription();
        dialogLoading = new MaterialDialog.Builder(this)
                .customView(R.layout.layout_dialog_loading, false)
                .canceledOnTouchOutside(true)
                .build();
        dialogLoading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        loadingFinish = new boolean[2];
        for(int i = 0 ; i < loadingFinish.length; i++){
            loadingFinish[i] = false;
        }

        productsFilter = new ProductsFilter(10);
    }

    private void initView(){
        btnWish.setVisibility(View.GONE);
        Drawable drawable = ratingBar.getProgressDrawable();
        drawable.setColorFilter(getResources().getColor(R.color.md_yellow_600), PorterDuff.Mode.SRC_ATOP);

        sliderLayout.setPresetTransformer(SliderLayout.Transformer.DepthPage);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(4000);

        rvProductOption.setHasFixedSize(true);
        rvProductOption.setNestedScrollingEnabled(false);
        productOptionAdapter = new ProductOptionAdapter(this);
        manager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        manager.setSpanCount(1);
        rvProductOption.setLayoutManager(manager);
        rvProductOption.setAdapter(productOptionAdapter);

        rvProductReview.setHasFixedSize(true);
        rvProductReview.setNestedScrollingEnabled(false);
        productReviewAdapter = new ProductReviewAdapter(this);
        manager2 = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        manager2.setSpanCount(1);
        rvProductReview.setLayoutManager(manager2);
        rvProductReview.setAdapter(productReviewAdapter);

        homeCategories = new ArrayList<>();
        rvCategoryProduct.setHasFixedSize(true);
        adapter = new CategoryProductAdapter(this, homeCategories);
        rvCategoryProduct.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvCategoryProduct.setAdapter(adapter);
        rvCategoryProduct.setNestedScrollingEnabled(false);


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent productsIntent = new Intent(ProductDetailActivity.this, SearchActivity.class);
                startActivity(productsIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCartCheck();
            }
        });

        btnWish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToWishCheck();
            }
        });


        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Intent = new Intent(ProductDetailActivity.this, CartActivity.class);
                startActivity(Intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        productOptionAdapter.setOnProductOptionItemClick(new ProductOptionAdapter.OnProductOptionItemClick() {
            @Override
            public void onClick(ProductOption productOption) {
                chooseOption(productOption);
            }
        });

        btnDescriptionMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(product != null){
                   openActivityDescription("Mô tả sản phẩm", product.getDescription());
                }
            }
        });

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

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnNewReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProductAddReviewActivity(productId);
            }
        });

        btnReviewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProductReviewActivity(productId);
            }
        });
    }

    private void addToCartCheck() {
        if(!mApp.hasToken()){
            addToCart = true;
            Intent i = new Intent(this, LoginActivity.class);
            startActivityForResult(i, 1);
        }else{
            addToCart(productOption);
        }
    }

    private void addToWishCheck(){
        if(!mApp.hasToken()){
            addToWish = true;
            Intent i = new Intent(this, LoginActivity.class);
            startActivityForResult(i, 1);
        }else{
            if(wish){
                deleteWish();
            }else{
                addToWish();
            }
        }
    }


    private void addToCart(ProductOption productOption){
        dialogLoading.show();
        String token = mApp.getUserToken();
        Subscription subscription = service.addCart(
                token,
                productOption.getProductOptionId(),
                Integer.toString(1),
                new Service.AddCartCallback() {
                    @Override
                    public void onSuccess(SubmitResponse submitResponse) {
                        dialogLoading.dismiss();
                        if(submitResponse.isStatus()){
                            PrefsUser.updateCartNum(true);
                            updateBadge();
                        }else{
                            Toast.makeText(getApplicationContext(), "Sản phẩm vượt quá số lượng cho phép.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(NetworkError networkError) {

                    }
                });
        subscriptions.add(subscription);
    }

    private void addToWish(){
        dialogLoading.show();
        String token = mApp.getUserToken();
        Subscription subscription = service.addWish(token, product.getProductId(),
                new Service.AddWishCallback() {
                    @Override
                    public void onSuccess(SubmitResponse submitResponse) {
                        dialogLoading.dismiss();
                        wish = submitResponse.isStatus();
                        if(submitResponse.isStatus()){
                            PrefsUser.updateWishNum(true);
                        }
                        updateBadge();
                    }

                    @Override
                    public void onError(NetworkError networkError) {

                    }
                });

    }

    private void deleteWish(){
        dialogLoading.show();
        String token = mApp.getUserToken();
        Subscription subscription = service.deleteWish(token, product.getProductId(),
                new Service.DeleteWishCallback() {
                    @Override
                    public void onSuccess(SubmitResponse submitResponse) {
                        dialogLoading.dismiss();
                        wish = !submitResponse.isStatus();
                        if(submitResponse.isStatus()){
                            PrefsUser.updateWishNum(false);
                        }
                        updateBadge();
                    }

                    @Override
                    public void onError(NetworkError networkError) {

                    }
                });

    }


    private void openProductReviewActivity(String productId){
        Intent productReviewIntent = new Intent(this, ProductReviewActivity.class);
        productReviewIntent.putExtra(ProductReviewActivity.EXTRA_PRODUCT_ID, productId);
        startActivityForResult(productReviewIntent, 1);
    }

    private void openProductAddReviewActivity(String productId){
        Intent productAddReviewIntent = new Intent(this, ProductAddReviewActivity.class);
        productAddReviewIntent.putExtra(ProductAddReviewActivity.EXTRA_PRODUCT_ID, productId);
        startActivityForResult(productAddReviewIntent, 1);
    }

    private void openProductsActivity(String categoryId, String name, String nameSearch){
        Intent productsIntent = new Intent(this, ProductsActivity.class);
        productsIntent.putExtra(ProductsActivity.EXTRA_CATEGORY_ID, categoryId);
        productsIntent.putExtra(ProductsActivity.EXTRA_CATEGORY_NAME, name);
        productsIntent.putExtra(ProductsActivity.EXTRA_NAME_SEARCH, "");
        startActivity(productsIntent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void openProductDetailActivity(String productId){
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra(ProductDetailActivity.EXTRA_PRODUCT_ID, productId);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }


    private void openActivityDescription(String title, String data){
        Intent productDescriptionIntent = new Intent(ProductDetailActivity.this, ProductDescriptionActivity.class);
        productDescriptionIntent.putExtra(ProductDescriptionActivity.EXTRA_DATA, data);
        productDescriptionIntent.putExtra(ProductDescriptionActivity.EXTRA_TITLE, title);
        startActivity(productDescriptionIntent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDeps().inject(this);
        setContentView(R.layout.activity_product_detail);
        ButterKnife.bind(this);
        init();
        initView();
        getProductDetail();
    }

    private void checkWish(){
        dialogLoading.show();
        if(mApp.hasToken()){
            Subscription subscription = service.checkWished(mApp.getUserToken(), product.getProductId(),
                    new Service.CheckWishedCallback() {
                        @Override
                        public void onSuccess(SubmitResponse submitResponse) {
                            dialogLoading.dismiss();
                            wish = submitResponse.isStatus();
                            btnWish.setVisibility(View.VISIBLE);
                            updateBadge();
                        }

                        @Override
                        public void onError(NetworkError networkError) {

                        }
                    });
            subscriptions.add(subscription);
        }
    }


    private void getProductDetail(){
        dialogLoading.show();
        Subscription productSubscription = service.getProduct(productId, new Service.GetProductCallback() {
            @Override
            public void onSuccess(ProductResponse productResponse) {
                loadingFinish[0] = true;
                if(loadingFinish[1]){
                    dialogLoading.dismiss();
                }
                product = productResponse.getProduct();
                if(product != null){
                    setDataProduct();
                    getProductsSame();
                    if(mApp.hasToken()){
                        checkWish();
                    }
                }else{
                    // null handler
                }
            }

            @Override
            public void onError(NetworkError networkError) {

            }
        });

        subscriptions.add(productSubscription);

        Subscription productOptionSubscription = service.getProductOption(productId, new Service.GetProductOptionCallback() {
            @Override
            public void onSuccess(ProductOptionResponse productOptionResponse) {
                loadingFinish[1] = true;
                if(loadingFinish[0]){
                    dialogLoading.dismiss();
                }
                setDataProductOption(productOptionResponse.getProductOptions());
            }

            @Override
            public void onError(NetworkError networkError) {

            }
        });

        subscriptions.add(productOptionSubscription);

        getProductReview();

    }

    private void getProductReview(){

        Subscription productReviewSubscription = service.getProductReview(productId, new Service.GetProductReviewCallback() {
            @Override
            public void onSuccess(ReviewResponse reviewResponse) {
                setDataProductReview(reviewResponse.getReviews());
            }

            @Override
            public void onError(NetworkError networkError) {

            }
        });
        subscriptions.add(productReviewSubscription);
    }

    private void getProductsSame(){
        productsFilter.setCategoryId(product.getCategoryId());
        Subscription subscription = service.getProducts(productsFilter, new Service.GetProductsCallback() {
            @Override
            public void onSuccess(ProductMiniResponse productMiniResponse) {
                setDataProductSame(productMiniResponse.getProductMinis());
            }

            @Override
            public void onError(NetworkError networkError) {

            }
        });
        subscriptions.add(subscription);
    }

    private void setDataProductSame(ArrayList<ProductMini> productMinis){
        HomeCategory homeCategory = new HomeCategory();
        homeCategory.setCategoryId(product.getCategoryId());
        homeCategory.setName("Sản phẩm tương tự");
        homeCategory.setProductMinis(productMinis);
        homeCategories.add(homeCategory);
        adapter.notifyDataSetChanged();
    }

    private void setDataProductReview(ArrayList<Review> reviews){
        productReviewAdapter.clear();
        for(int i = 0 ; i < 2; i++){
            if(reviews.size() == i)
                break;
            productReviewAdapter.addItem(reviews.get(i));
        }
    }


    private void setDataProduct(){
        tvDescriptionShort.setText(Html.fromHtml(product.getDescriptionShort()));
        tvDescription.setText(Html.fromHtml(product.getDescription()));
        tvName.setText(product.getName());
        tvTitle.setText(product.getName());
        try{
            ratingBar.setRating(Float.parseFloat(product.getRating()));
        }catch (Exception e){
            ratingBar.setRating(0);
        }
    }

    private void setDataProductOption(ArrayList<ProductOption> productOptions){
        if(productOptions == null){
            // null
            return;
        }
        int index = 0;
        for(ProductOption p : productOptions){
            productOptionAdapter.addItem(p);
            if(p.getProductOptionId().equals(optionId)){
                try {
                    productOption = productOptionAdapter.click(index);
                    chooseOption(productOption);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            index++;
        }
        try {
            productOption = productOptionAdapter.click(0);
            chooseOption(productOption);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void chooseOption(ProductOption productOption){
        if(productOption == null)
            return;
        float discount = Float.parseFloat(productOption.getDiscount());
        float price = Float.parseFloat(productOption.getPrice());
        tvDiscount.setAmount(discount);
        tvPrice.setAmount(price);
        if(discount == price){
            tvPrice.setVisibility(View.INVISIBLE);
            tvSale.setVisibility(View.INVISIBLE);
        }else {
            int sale = (int) ( 100 - (discount / price) * 100);
            tvSale.setText("-" + sale + "%");
        }

        DefaultSliderView defaultSliderView = null;
        sliderLayout.removeAllSliders();
        for(ProductOptionImage image: productOption.getImages()){
            defaultSliderView = new DefaultSliderView(this);
            defaultSliderView.image(BuildConfig.BASEURL_IMAGES + image.getImage())
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setScaleType(BaseSliderView.ScaleType.CenterInside)
                    .setOnSliderClickListener(this);
            sliderLayout.addSlider(defaultSliderView);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                boolean added = data.getBooleanExtra(ProductAddReviewActivity.EXTRA_ADDED_REVIEW, false);
                if(added){
                    getProductReview();
                }
            }
            if (resultCode == LoginActivity.LOGIN_SUCCESSFULL) {
                if(addToCart){
                    addToCart(productOption);
                    addToCart = false;
                }
                if(addToWish){
                    if(wish){
                        deleteWish();
                    }else{
                        addToWish();
                    }
                    addToWish = false;
                }
            }
        }
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset < -200){
            tvTitle.setTextColor(ContextCompat.getColor(this,R.color.md_white_1000));
            btnBack.setColorFilter(ContextCompat.getColor(this,R.color.md_white_1000));
            btnCart.setColorFilter(ContextCompat.getColor(this,R.color.md_white_1000));
            btnSearch.setColorFilter(ContextCompat.getColor(this,R.color.md_white_1000));

        }else{
            tvTitle.setTextColor(ContextCompat.getColor(this,R.color.md_black_1000));
            btnCart.setColorFilter(ContextCompat.getColor(this,R.color.md_black_1000));
            btnBack.setColorFilter(ContextCompat.getColor(this,R.color.md_black_1000));
            btnSearch.setColorFilter(ContextCompat.getColor(this,R.color.md_black_1000));
        }
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscriptions != null) {
            subscriptions.unsubscribe();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateBadge();
        if(btnWish.getVisibility() == View.GONE && mApp.hasToken() && product != null){
            checkWish();
        }
    }

    private void updateBadge(){
        int cartNum = PrefsUser.getCartNum();
        int wishNum = PrefsUser.getWishNum();
        cartBadge.setNumber(cartNum);
        btnWish.setImageResource(wish?R.drawable.ic_favorite_black_24dp:R.drawable.ic_favorite_border_black_24dp);
    }
}
