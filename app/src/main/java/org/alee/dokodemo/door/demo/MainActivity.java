package org.alee.dokodemo.door.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;

import org.alee.dokodemo.door.annotation.Node;
import org.alee.dokodemo.door.core.DokodemoDoor;

/**********************************************************
 *
 * @author: MingYu.Liu
 * @date: 2020/4/5
 * @description: xxxx
 *
 *********************************************************/
@Node(containerViewId = R.id.fl_content)
public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DokodemoDoor.getNodeProxy(this).startFragment(new Test1Fragment());
    }
}
