package org.alee.dokodemo.door.demo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.alee.dokodemo.door.annotation.Node;
import org.alee.dokodemo.door.core.DokodemoDoor;

/**********************************************************
 *
 * @author: MingYu.Liu
 * @date: 2020/4/5
 * @description: xxxx
 *
 *********************************************************/
@Node
public class Test1Fragment extends Fragment {
    protected View mContentView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mContentView = inflater.inflate(R.layout.fragment_blank, container, false);
        mContentView.findViewById(R.id.btn_add).setOnClickListener(v -> DokodemoDoor.getNodeProxy(Test1Fragment.this).startFragment(new TestFragment()));
        return mContentView;
    }

}
