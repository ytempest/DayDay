package com.hui.ffu365.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hui.ffu365.DetailLinkActivity;
import com.hui.ffu365.R;
import com.hui.ffu365.adapter.HomeInfoListAdapter;
import com.hui.ffu365.mode.HomeDataResult;
import com.hui.ffu365.ui.ImplantListView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by hui on 2016/8/22.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {
    private ImageView mAdbannerIv, mRecommendedCompanyIv;
    private View mRootView;
    private Context mContext;
    private Handler mHandler = new Handler();
    private ListView mNewsLv;

    private HomeDataResult mHomeDataResult;

    // 创建View
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_home, null);
        mContext = getActivity();// 给Context 赋值，很多地方都需要使用Context
        return mRootView;
    }


    // 处理逻辑
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // findViewById  你界面在哪里就用谁找(.)findViewById
        mAdbannerIv = (ImageView) mRootView.findViewById(R.id.adbanner_iv);
        mRecommendedCompanyIv = (ImageView) mRootView.findViewById(R.id.recommended_company);
        mNewsLv = (ListView) mRootView.findViewById(R.id.industry_information_lv);

        // 设置图片点击时间
        mAdbannerIv.setOnClickListener(this);

        // 请求后台数据
        requestHomeData();
    }

    /**
     * 请求后台数据
     */
    private void requestHomeData() {
        // OKhttp
        // 1.创建一个OkhttpClient对象
        OkHttpClient okHttpClient = new OkHttpClient();

        // 2.构建参数的body  MultipartBody.FORM 表单形式
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        // 2.2封装参数
        builder.addFormDataPart("appid", "1");
        // builder.addFormDataPart("");  添加多个参数

        // 3. 构建一个请求  post 提交里面是参数的builder   url()请求路径
        Request request = new Request.Builder().url("http://v2.ffu365.com/index.php?m=Api&c=Index&a=home")
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

                // Gson解析成可操作的对象
                Gson gson = new Gson();
                mHomeDataResult  = gson.fromJson(result, HomeDataResult.class);

                showHomeData(mHomeDataResult.getData());
            }
        });
    }

    /**
     * 显示首页数据
     */
    private void showHomeData(final HomeDataResult.DataBean data) {
        //  runOnUiThread(); 在activity里面
        mHandler.post(new Runnable() {// 执行在主线程中
            @Override
            public void run() {
                //------------------显示首页图片------------------
                // 从后台返回的数据中获取广告位的图片路径
                String adBannerImage = data.getAd_list().get(0).getImage();
                // SmartImageView   Glide
                Glide.with(mContext)// with(Context context)
                        .load(adBannerImage)// load(网络的图片路径)
                        .into(mAdbannerIv);// 设置给谁
                // 可以一直点的是链表的方式，一般会出现在Builder模式里面  Dialog
                Glide.with(mContext).load(data.getCompany_list().get(0).getImage()).into(mRecommendedCompanyIv);
                // 但是如果直接使用 android:scaleType="fitXY" 会导致图片变形,不使用界面不美观

                //---------------------显示列表--------------------
                mNewsLv.setAdapter(new HomeInfoListAdapter(mContext,data.getNews_list()));
                // ScrollView + ListView 嵌套  一般就是ListView显示不全

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.adbanner_iv:
                Intent intent = new Intent(mContext, DetailLinkActivity.class);
                // 获取广告位的链接
                String bannerUrl = mHomeDataResult.getData().getAd_list().get(0).getLink();
                // 把链接传递给下一个activity
                intent.putExtra(DetailLinkActivity.URL_KEY,bannerUrl);
                startActivity(intent);
                break;
        }
    }
}
