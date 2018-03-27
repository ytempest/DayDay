package com.hui.ffu365;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hui.ffu365.mode.UserLoginResult;
import com.hui.ffu365.util.MD5Util;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by hui on 2016/8/24.
 */
public class UserLoginActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private CheckBox mCheckPasswordCb;
    private EditText mUserPhoneEt,mUserPasswordEt;
    private Button mUserLoginBt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        mCheckPasswordCb = (CheckBox) findViewById(R.id.check_password_cb);
        mUserPhoneEt = (EditText) findViewById(R.id.user_phone_et);
        mUserPasswordEt = (EditText) findViewById(R.id.user_password_et);
        mUserLoginBt = (Button) findViewById(R.id.user_login_bt);

        // 1.完成基本功能   显示和隐藏密码
        // 监听CheckBox状态改变
        mCheckPasswordCb.setOnCheckedChangeListener(this);

        // 2.处理点击提交数据
        mUserLoginBt.setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        // compoundButton 代表的是当前  CheckBox    checked 代表当前是否选中
        if(checked){
            // 显示密码
            mUserPasswordEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }else{
            // 隐藏密码
            mUserPasswordEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }

        // 把光标移动到最后
        Editable etext = mUserPasswordEt.getText();
        Selection.setSelection(etext, etext.length());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.user_login_bt:
                dealUserLogin();
                break;
        }
    }

    private void dealUserLogin() {
        // 1.本地验证
        String userPhone = mUserPhoneEt.getText().toString().trim();
        String password = mUserPasswordEt.getText().toString().trim();

        if(TextUtils.isEmpty(userPhone)){
            Toast.makeText(this,"请输入用户名",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"请输入密码",Toast.LENGTH_LONG).show();
            return;
        }

        // 2.往后台提交数据
        // OKhttp
        // 1.创建一个OkhttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();
        // 2.构建参数的body  MultipartBody.FORM 表单形式
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        // 2.2封装参数
        builder.addFormDataPart("appid", "1");
        builder.addFormDataPart("cell_phone",userPhone); //  添加多个参数
        builder.addFormDataPart("password", MD5Util.strToMd5(password));// MD5 AES

        // 3. 构建一个请求  post 提交里面是参数的builder   url()请求路径
        Request request = new Request.Builder().url("http://v2.ffu365.com/index.php?m=Api&c=Member&a=login")
                .post(builder.build()).build();

        // 4.发送一个请求
        okHttpClient.newCall(request).enqueue(new Callback() {// 请求的回调
            @Override
            public void onFailure(Call call, IOException e) {
                // 失败
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {  // 这个不是运行在主线程中
                // 成功  数据在response里面  获取后台给我们的JSON 字符串
                String result = response.body().string();
                Log.e("TAG", result);
                Gson gson = new Gson();
                UserLoginResult loginResult = gson.fromJson(result, UserLoginResult.class);

                dealLoginResult(loginResult);
            }
        });



    }

    // 3.处理返回的数据
    private void dealLoginResult(UserLoginResult loginResult) {
        // 首先判断有没有成功
        if(loginResult.getErrcode() == 1){
            // 成功处理
            // 1.需要保存登录状态   当前设置为已登录
            SharedPreferences sp =  getSharedPreferences("info",MODE_PRIVATE);
            sp.edit().putBoolean("is_login",true).commit();

            // 2.需要保存用户信息
            UserLoginResult.DataBean userData =  loginResult.getData();
            // SharedPreferences 怎么保存对象   把对象转为JSON String --> SharedPreferences
            Gson gson = new Gson();
            String uesrInfoStr =  gson.toJson(userData);
            // 保存的用户信息为Json格式的字符串
            sp.edit().putString("user_info",uesrInfoStr).commit();

            // 3.关掉这个页面
            finish();
        }else{
            // 登录失败
            Toast.makeText(this,loginResult.getErrmsg(),Toast.LENGTH_LONG).show();
        }
    }
}
