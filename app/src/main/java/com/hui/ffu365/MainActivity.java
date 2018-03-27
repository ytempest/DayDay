package com.hui.ffu365;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import com.hui.ffu365.adapter.HomePagerAdapter;
import com.hui.ffu365.fragment.CenterFragment;
import com.hui.ffu365.fragment.CollectionFragment;
import com.hui.ffu365.fragment.HomeFragment;
import com.hui.ffu365.fragment.MessageFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;

    private RadioButton mHomeRb,mCollectionRb,mMessageRb,mCenterRb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);// ViewPager + RadioGroup   获取到了首页数据   ListView

        initView();



        // 主页面的解决方案
        // 1.ViewPager + Fragment + RadioButton
        // 2.TabHost + Fragment
        // 3.ViewGroup + Fragment + 动态的去切换  会不断的销毁和创建
        initData();


    }

    /**
     * 初始化界面
     */
    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        mHomeRb = (RadioButton) findViewById(R.id.home_rb);
        mCollectionRb = (RadioButton) findViewById(R.id.collection_rb);
        mMessageRb = (RadioButton) findViewById(R.id.message_rb);
        mCenterRb = (RadioButton) findViewById(R.id.center_rb);

        // 给下面四个RadioButton设置点击事件,是为了保证点击按钮能够切换到相应的页面
        mHomeRb.setOnClickListener(this);
        mCollectionRb.setOnClickListener(this);
        mMessageRb.setOnClickListener(this);
        mCenterRb.setOnClickListener(this);

        // 设置页面滑动的监听   为了保证滑动之后下面相应的RadioButton 改变显示
        mViewPager.setOnPageChangeListener(this);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        // 给ViewPager设置Adapter
        ArrayList<Fragment> fragments = new ArrayList<>();// 1.7里面的写法
        // 往集合里面添加Fragment
        fragments.add(new HomeFragment());
        fragments.add(new CollectionFragment());
        fragments.add(new MessageFragment());
        fragments.add(new CenterFragment());

        // getSupportFragmentManager() 是FragmentActivity里面的
        HomePagerAdapter  adapter = new HomePagerAdapter(getSupportFragmentManager(),fragments);
        mViewPager.setAdapter(adapter);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.home_rb:
                Log.e("TAG","TTT");
                // 把ViewPager切换到第一页
                mViewPager.setCurrentItem(0,false);// false 代表切换的时候不显示滑动效果
                break;
            case R.id.collection_rb:

                // 先判断一下用户有没有登录
                SharedPreferences sp =  getSharedPreferences("info",MODE_PRIVATE);
                boolean isLogin = sp.getBoolean("is_login",false);
                if(isLogin){
                    // 把ViewPager切换到第二页
                    mViewPager.setCurrentItem(1,false);
                }else{
                    // 如果用户没有登录就跳转到登录界面
                    Intent intent = new Intent(this,UserLoginActivity.class);
                    startActivity(intent);

                    // 找到原来的位置，然后把原来的位置恢复
                    onPageSelected(mViewPager.getCurrentItem());
                }
                break;
            case R.id.message_rb:
                // 把ViewPager切换到第三页
                mViewPager.setCurrentItem(2,false);
                break;
            case R.id.center_rb:
                // 把ViewPager切换到第四页
                mViewPager.setCurrentItem(3,false);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        // 切换到相应页面之后调用  postion 当前的位置
        switch(position){
            case 0:
                mHomeRb.setChecked(true);
                break;
            case 1:
                mCollectionRb.setChecked(true);
                break;
            case 2:
                mMessageRb.setChecked(true);
                break;
            case 3:
                mCenterRb.setChecked(true);
                break;
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
