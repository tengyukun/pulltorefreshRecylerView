package com.tyk.pulltorefresh.recyclerview;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class UserInfoActivity extends AppCompatActivity {

    @ViewInject(R.id.tb_userinfo_title)
    private Toolbar tb_userinfo_title;

    @ViewInject(R.id.til_userinfo_name)
    private TextInputLayout til_userinfo_name;

    @ViewInject(R.id.til_userinfo_password)
    private TextInputLayout til_userinfo_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        x.view().inject(this);

        init();
    }

    private void init() {

        initView();
        initData();

    }

    private void initData() {

        tb_userinfo_title.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void initView() {

        initToolbar();

        initEditText();

    }

    private void initEditText() {
        EditText name = til_userinfo_name.getEditText();

        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.toString().length()>10){
                    til_userinfo_name.setError("用户名长度不能超过10");
                }else{
                    til_userinfo_name.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        EditText password = til_userinfo_password.getEditText();

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.toString().length()>10){
                    til_userinfo_password.setError("密码长度不能超过10");
                }else{
                    til_userinfo_password.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void initToolbar() {

        tb_userinfo_title.setTitle("个人信息");
        setSupportActionBar(tb_userinfo_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
