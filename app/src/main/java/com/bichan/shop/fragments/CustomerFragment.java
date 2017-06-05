package com.bichan.shop.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bichan.shop.MainActivity;
import com.bichan.shop.MyApplication;
import com.bichan.shop.Prefs.PrefsUser;
import com.bichan.shop.R;
import com.bichan.shop.activities.login.LoginActivity;
import com.bichan.shop.activities.order.OrderActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.carbs.android.avatarimageview.library.AvatarImageView;

import static com.facebook.FacebookSdk.getApplicationContext;

public class CustomerFragment extends Fragment {
    @BindView(R.id.btnFacebook)
    Button btnFacebook;
    @BindView(R.id.btnGoogle)
    Button btnGoogle;

    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.btnLogout)
    Button btnLogout;

    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.tvTelephone)
    TextView tvTelephone;
    @BindView(R.id.tvDateAdded)
    TextView tvDateAdded;

    @BindView(R.id.item_avatar)
    AvatarImageView item_avatar;

    @BindView(R.id.layoutSocial)
    LinearLayout layoutSocial;

    @BindView(R.id.btnOrder)
    MaterialRippleLayout btnOrder;

    MyApplication mApp;



    public CustomerFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApp = ((MyApplication)getApplicationContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkLogin();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OrderActivity.class);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void login(){
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        getActivity().startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void logout(){
        PrefsUser.logout();
        Intent mainIntent = new Intent(getActivity(), MainActivity.class);
        getActivity().startActivity(mainIntent);
        getActivity().finish();
    }

    private void checkLogin(){

        if(mApp.hasToken()){
            //item_avatar.setTextAndColor(PrefsUser.getFirstname(), AvatarImageView.COLORS[2]);
            item_avatar.setImageResource(R.drawable.default_user_icon_profile);
            tvName.setText(PrefsUser.getLastname() + " " + PrefsUser.getFirstname());
            tvDateAdded.setText(PrefsUser.getDate_added());
            tvEmail.setText(PrefsUser.getEmail());
            tvTelephone.setText(PrefsUser.getTelephone());
            btnLogin.setVisibility(View.GONE);
            layoutSocial.setVisibility(View.GONE);
            btnLogout.setVisibility(View.VISIBLE);
            btnOrder.setVisibility(View.VISIBLE);
        }else{
            item_avatar.setImageResource(R.drawable.default_user_icon_profile);
            tvName.setText("Chưa đăng nhập");
            tvDateAdded.setText("Chưa có thông tin");
            tvEmail.setText("Chưa có thông tin");
            tvTelephone.setText("Chưa có thông tin");
            btnLogin.setVisibility(View.VISIBLE);
            layoutSocial.setVisibility(View.GONE);
            btnLogout.setVisibility(View.GONE);
            btnOrder.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        checkLogin();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
