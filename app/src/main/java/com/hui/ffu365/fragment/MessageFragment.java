package com.hui.ffu365.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hui.ffu365.R;

/**
 * Created by hui on 2016/8/22.
 */
public class MessageFragment extends Fragment{

    // 创建View
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_message,null);
        return view;
    }
}
