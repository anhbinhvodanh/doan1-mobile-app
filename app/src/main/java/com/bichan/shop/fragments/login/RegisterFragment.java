package com.bichan.shop.fragments.login;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.bichan.shop.R;
import com.bichan.shop.models.DataRegister;
import com.bichan.shop.models.Social;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    @BindView(R.id.btnRegister)
    Button btnRegister;

    DataRegister dataRegister;

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
