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
import android.widget.Toast;

import com.bichan.shop.R;
import com.bichan.shop.RxHelper;
import com.bichan.shop.models.DataRegister;
import com.bichan.shop.models.Social;
import com.blankj.utilcode.util.RegexUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func4;

public class RegisterFragment extends Fragment{
    @BindView(R.id.btnFacebook)
    Button btnFacebook;
    @BindView(R.id.btnGoogle)
    Button btnGoogle;
    @BindView(R.id.edtName)
    EditText edtName;
    @BindView(R.id.edtEmail)
    EditText edtEmail;
    @BindView(R.id.edtPass)
    EditText edtPass;
    @BindView(R.id.edtRePass)
    EditText edtRePass;

    @BindView(R.id.tilName)
    TextInputLayout tilName;
    @BindView(R.id.tilEmail)
    TextInputLayout tilEmail;
    @BindView(R.id.tilPass)
    TextInputLayout tilPass;
    @BindView(R.id.tilRePass)
    TextInputLayout tilRePass;

    @BindView(R.id.btnRegister)
    Button btnRegister;

    DataRegister dataRegister;

    Observable<String> edtNameObservable;
    Observable<String> edtEmailObservable;
    Observable<String> edtPassObservable;
    Observable<String> edtRePassObservable;

    OnSocialClickListener onSocialClickListener;
    public interface OnSocialClickListener{
        void onClick(Social social, Fragment fragment);
    }

    OnRegisterClickListener onRegisterClickListener;
    public interface OnRegisterClickListener{
        void onClick(DataRegister dataRegister);
    }

    public RegisterFragment() {
        dataRegister = new DataRegister();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edtNameObservable = RxHelper.getTextWatcherObservable(edtName);
        edtEmailObservable = RxHelper.getTextWatcherObservable(edtEmail);
        edtPassObservable = RxHelper.getTextWatcherObservable(edtPass);
        edtRePassObservable = RxHelper.getTextWatcherObservable(edtRePass);

        Observable.combineLatest(edtNameObservable, edtEmailObservable, edtPassObservable, edtRePassObservable,
                new Func4<String, String, String, String, Boolean>() {
                    @Override
                    public Boolean call(String s, String s2, String s3, String s4) {
                        // name
                        if(s.trim().equals("")){
                            tilName.setError("Vui lòng nhập tên");
                            return false;
                        }else{
                            tilName.setErrorEnabled(false);
                        }
                        // email
                        if(!RegexUtils.isEmail(s2)){
                            tilEmail.setError("Vui lòng nhập đúng định dạng email");
                            return false;
                        }else{
                            tilEmail.setErrorEnabled(false);
                        }
                        // password
                        String pass = s3.trim();
                        String rePass = s4.trim();
                        if(pass.equals("")){
                            tilPass.setError("Vui lòng nhập mật khẩu");
                            return false;
                        }else{
                            tilPass.setErrorEnabled(false);
                        }
                        if(!pass.equals(rePass)){
                            tilRePass.setError("Mật khẩu không khớp");
                            return false;
                        }else{
                            tilRePass.setErrorEnabled(false);
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
                btnRegister.setEnabled(aBoolean);
            }
        });

        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onSocialClickListener != null){
                    onSocialClickListener.onClick(Social.FACEBOOK, RegisterFragment.this);
                }
            }
        });

        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onSocialClickListener != null){
                    onSocialClickListener.onClick(Social.GOOGLE, RegisterFragment.this);
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

    }

    private void register(){
        if(dataRegister == null)
            dataRegister = new DataRegister();

        // check valuable

        dataRegister.setFirstname(edtName.getText().toString().trim());
        dataRegister.setEmail(edtEmail.getText().toString().trim());
        dataRegister.setPassword(edtPass.getText().toString().trim());

        if(onRegisterClickListener != null){
            onRegisterClickListener.onClick(dataRegister);
        }
    }

    public void receiveDataRegister(DataRegister dataRegister){
        if(dataRegister == null){
            return;
        }

        this.dataRegister = dataRegister;
        setDataRegister(this.dataRegister);
    }

    private void setDataRegister(DataRegister dataRegister){
        if(dataRegister == null)
            return;
        Toast.makeText(getActivity(), "Đã liên kết tài khoản thành công, vui lòng điền các thông tin còn thiếu để hoàn tất quá trình đăng ký", Toast.LENGTH_LONG).show();
        if(!dataRegister.getEmail().equals("")){
            edtEmail.setText(dataRegister.getEmail());
        }

        if(!dataRegister.getFirstname().equals("")){
            edtName.setText(dataRegister.getFirstname());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnSocialClickListener){
            onSocialClickListener = (OnSocialClickListener) context;
            onRegisterClickListener = (OnRegisterClickListener) context;
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        onRegisterClickListener = null;
        onSocialClickListener = null;
    }
}
