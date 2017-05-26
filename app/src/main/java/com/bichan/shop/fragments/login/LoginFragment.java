package com.bichan.shop.fragments.login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.bichan.shop.R;
import com.bichan.shop.RxHelper;
import com.bichan.shop.models.DataLogin;
import com.bichan.shop.models.Social;
import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.RegexUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func2;


public class LoginFragment extends Fragment {
    @BindView(R.id.btnFacebook)
    Button btnFacebook;
    @BindView(R.id.btnGoogle)
    Button btnGoogle;
    @BindView(R.id.edtEmail)
    EditText edtEmail;
    @BindView(R.id.edtPass)
    EditText edtPass;

    @BindView(R.id.tilEmail)
    TextInputLayout tilEmail;
    @BindView(R.id.tilPass)
    TextInputLayout tilPass;


    @BindView(R.id.btnLogin)
    Button btnLogin;

    Observable<String> edtEmailObservable;
    Observable<String> edtPassObservable;

    OnSocialClickListener onSocialClickListener;
    public interface OnSocialClickListener{
        void onClick(Social social);
    }

    OnLoginClickListener onLoginClickListener;
    public interface OnLoginClickListener{
        void onClick(DataLogin dataLogin);
    }

    public LoginFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        // Inflate the layout for this fragment
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edtEmailObservable = RxHelper.getTextWatcherObservable(edtEmail);
        edtPassObservable = RxHelper.getTextWatcherObservable(edtPass);

        Observable.combineLatest(edtEmailObservable, edtPassObservable,
                new Func2<String, String, Boolean>() {
                    @Override
                    public Boolean call(String s, String s1) {
                        // email
                        if(!RegexUtils.isEmail(s)){
                            tilEmail.setError("Vui lòng nhập đúng định dạng email");
                            return false;
                        }else{
                            tilEmail.setErrorEnabled(false);
                        }
                        // password
                        String pass = s1.trim();
                        if(pass.equals("")){
                            tilPass.setError("Vui lòng nhập mật khẩu");
                            return false;
                        }else{
                            tilPass.setErrorEnabled(false);
                        }
                        if(pass.length() < 6){
                            tilPass.setError("Mật khẩu phải chứa ít nhất 6 ký tự");
                            return false;
                        }else{
                            tilPass.setErrorEnabled(false);
                        }
                        return true;
                    }
                }
        ).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                btnLogin.setEnabled(aBoolean);
            }
        });

        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onSocialClickListener != null){
                    onSocialClickListener.onClick(Social.FACEBOOK);
                }
            }
        });

        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onSocialClickListener != null){
                    onSocialClickListener.onClick(Social.GOOGLE);
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DataLogin dataLogin = new DataLogin();
                dataLogin.setEmail(edtEmail.getText().toString().trim());
                dataLogin.setPassword(EncryptUtils.encryptMD5ToString(
                        edtPass.getText().toString().trim()
                ).toLowerCase());

                if(onLoginClickListener != null){
                    onLoginClickListener.onClick(dataLogin);
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnSocialClickListener){
            onSocialClickListener = (OnSocialClickListener) context;
            onLoginClickListener = (OnLoginClickListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
