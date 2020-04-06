package org.alee.dokodemo.door.demo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
public class TestFragment extends Fragment {
    protected View mContentView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        TestFragment newTest = new TestFragment();
        mContentView = inflater.inflate(R.layout.fragment_test, container, false);
        ((TextView) mContentView.findViewById(R.id.tv)).setText(this.toString());
        DokodemoDoor.getNodeProxy(DokodemoDoor.getNodeProxy(this).getHost()).printStack();
        mContentView.findViewById(R.id.btn_add).setOnClickListener(v -> DokodemoDoor.getNodeProxy(TestFragment.this)
                .startFragment(newTest));
        mContentView.findViewById(R.id.btn_show).setOnClickListener(v -> DokodemoDoor.getNodeProxy(TestFragment.this)
                .showFragment(DokodemoDoor.getNodeProxy(newTest).getFragmentTag()));
        mContentView.findViewById(R.id.btn_hide).setOnClickListener(v -> DokodemoDoor.getNodeProxy(TestFragment.this)
                .hideFragment(newTest));
        mContentView.findViewById(R.id.btn_close).setOnClickListener(v -> DokodemoDoor.getNodeProxy(TestFragment.this)
                .close(newTest));
        return mContentView;
    }
}
