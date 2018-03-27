package com.hui.ffu365;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by hui on 2016/8/22.
 * 介绍：这是一个欢迎页
 */
public class WelcomeActivity extends Activity{
    private ImageView mWelcomeIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        mWelcomeIv = (ImageView) findViewById(R.id.welcome_iv);

        //1. 停顿几秒，然后进到主页面， 如果说你刚开始就打开主界面，黑一块 主界面加载很多资源
        //2. 为了更好的能够让主页面加载

        // 停顿5秒，再跳转页面
        // 1. sleep()  2.handler 发送延迟消息     3.Timer类    4.用动画   0.7-1.0

       /*new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);// 主页面里面是不允许耗时操作 2.3
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // 进入主页面
            }
        }).start();*/

        // 4.用动画
        // 4.1AlphaAnimation    mWelcomeIv 谁要执行动画  "alpha" 需要的动画效果  从  0.7f  到 1.0f
        ObjectAnimator animator  = ObjectAnimator.ofFloat(mWelcomeIv,"alpha",0.7f,1.0f);
        // 4.2设置动画执行时间
        animator.setDuration(500);
        // 4.3开启动画
        animator.start();

        // 4.4 等动画执行完毕需要调到主页面,添加动画执行监听
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                // 动画执行完毕
                // 进入主页面
                Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
                startActivity(intent);

                // 在主页面按返回又回到了Welcome  你一进主页面就应该从Task 里面移除
                finish();
            }
        });
    }
}
