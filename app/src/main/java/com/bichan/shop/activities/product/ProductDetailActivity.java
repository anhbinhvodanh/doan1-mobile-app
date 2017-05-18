package com.bichan.shop.activities.product;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageButton;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bichan.shop.BaseApp;
import com.bichan.shop.BuildConfig;
import com.bichan.shop.R;
import com.bichan.shop.models.Product;
import com.bichan.shop.models.ProductOption;
import com.bichan.shop.models.ProductOptionImage;
import com.bichan.shop.models.ProductOptionResponse;
import com.bichan.shop.models.ProductResponse;
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
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.btnBack)
    AppCompatImageButton btnBack;
    @BindView(R.id.btnFavorite)
    AppCompatImageButton btnFavorite;
    @BindView(R.id.favoriteBadge)
    NotificationBadge favoriteBadge;
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

    private CompositeSubscription subscriptions;
    private String productId;
    private String optionId;

    private Product product;
    private ArrayList<ProductOption> productOptions;

    private void init(){
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
    }

    private void initView(){
        Drawable drawable = ratingBar.getProgressDrawable();
        drawable.setColorFilter(getResources().getColor(R.color.md_yellow_600), PorterDuff.Mode.SRC_ATOP);

        favoriteBadge.setNumber(3);
        cartBadge.setNumber(5);

        sliderLayout.setPresetTransformer(SliderLayout.Transformer.DepthPage);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(4000);

        btnDescriptionMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(product != null){
                    Intent productDescriptionIntent = new Intent(ProductDetailActivity.this, ProductDescriptionActivity.class);
                    productDescriptionIntent.putExtra(ProductDescriptionActivity.EXTRA_DATA, product.getDescription());
                    startActivity(productDescriptionIntent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
        });
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

    private void getProductDetail(){
        Subscription productSubscription = service.getProduct(productId, new Service.GetProductCallback() {
            @Override
            public void onSuccess(ProductResponse productResponse) {
                product = productResponse.getProduct();
                if(product != null){
                    setDataProduct();
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
                productOptions = productOptionResponse.getProductOptions();
                setDataProductOption();
            }

            @Override
            public void onError(NetworkError networkError) {

            }
        });

        subscriptions.add(productOptionSubscription);

    }

    private void setDataProduct(){
        tvDescriptionShort.setText(Html.fromHtml(product.getDescriptionShort()));
        tvDescription.setText(Html.fromHtml(product.getDescription()));
        tvName.setText(product.getName());
        tvTitle.setText(product.getName());
    }

    private void setDataProductOption(){
        if(productOptions == null){
            // null
            return;
        }
        int index = 0;
        for(ProductOption productOption : productOptions){
            if(productOption.getProductOptionId().equals(optionId)){
                try {
                    chooseOption(index);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            index++;
        }
        try {
            chooseOption(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void chooseOption(int index) throws Exception {
        if(index >= productOptions.size())
            throw new Exception("Out of index");

        ProductOption productOption = productOptions.get(index);

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
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset < -200){
            tvTitle.setTextColor(ContextCompat.getColor(this,R.color.md_white_1000));
            btnBack.setColorFilter(ContextCompat.getColor(this,R.color.md_white_1000));
            btnCart.setColorFilter(ContextCompat.getColor(this,R.color.md_white_1000));
            btnSearch.setColorFilter(ContextCompat.getColor(this,R.color.md_white_1000));
            btnFavorite.setColorFilter(ContextCompat.getColor(this,R.color.md_white_1000));

        }else{
            tvTitle.setTextColor(ContextCompat.getColor(this,R.color.md_black_1000));
            btnCart.setColorFilter(ContextCompat.getColor(this,R.color.md_black_1000));
            btnBack.setColorFilter(ContextCompat.getColor(this,R.color.md_black_1000));
            btnSearch.setColorFilter(ContextCompat.getColor(this,R.color.md_black_1000));
            btnFavorite.setColorFilter(ContextCompat.getColor(this,R.color.md_black_1000));
        }
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }
}
