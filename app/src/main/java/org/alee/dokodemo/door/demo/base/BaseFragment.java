package org.alee.dokodemo.door.demo.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.alee.dokodemo.door.annotation.Node;
import org.alee.dokodemo.door.core.DokodemoDoor;
import org.lmy.open.utillibrary.LogUtil;
import org.lmy.open.utillibrary.PreferenceUtil;

/**********************************************************
 *
 * @author: MingYu.Liu
 * @date: 2020/4/7
 * @description: Fragment 模板类
 *
 *********************************************************/
@Node
public abstract class BaseFragment extends Fragment implements View.OnClickListener {
    /**
     * Tag
     */
    protected static String TAG;
    /**
     * 根布局
     */
    protected View mView = null;
    /**
     * Context 对象
     */
    protected Context mContext = null;
    /**
     * 数据存储器
     */
    protected PreferenceUtil mSpfUtil = null;
    /**
     * 所有注册的view
     */
    private SparseArray<View> mViews;

    @Override
    public void onAttach(Context context) {
        mContext = context;
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getName();
        mViews = new SparseArray<>();
        mSpfUtil = PreferenceUtil.getInstance();
        initData();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(getLayoutId(), container, false);
        getViews();
        setListeners();
        setViewsValue();
        return mView;
    }



    /**
     * 关联控件值
     */
    protected abstract void setViewsValue();

    /**
     * 设置布局文件
     *
     * @return 布局资源id
     */
    protected abstract int getLayoutId();

    /**
     * 关联控件
     */
    protected abstract void getViews();

    /**
     * 关联控件点击事件
     */
    protected abstract void setListeners();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    @Override
    public void onClick(View view) {
        processClick(view);
    }

    /**
     * 事件回调 点击事件回调
     *
     * @param v view
     */
    protected abstract void processClick(View v);


    /**
     * 根据id查找绑定view
     *
     * @param viewId 控件id
     * @param <E>    类型
     * @return view
     */
    public <E extends View> E findView(int viewId) {
        if (mView != null) {
            E view = (E) mViews.get(viewId);
            if (view == null) {
                view = mView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return view;
        }
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        DokodemoDoor.getNodeProxy(DokodemoDoor.getNodeProxy(this).getHost()).printStack();
    }

    /**
     * 绑定事件
     *
     * @param view view
     * @param <E>  类型
     */
    public <E extends View> void setClick(E view) {
        view.setOnClickListener(this);
    }

    public Boolean onInterruptBackPressed() {
        LogUtil.d("onInterruptBackPressed return false");
        return false;
    }

    public void onLazyLoadViewCreated(Bundle savedInstanceState) {
        LogUtil.d("onLazyLoadViewCreated ");
    }

    public Boolean onNodeBackPressed() {
        LogUtil.d("onNodeBackPressed return false");
        return false;
    }

    public void onFragmentResult(int requestCode, int resultCode, Bundle args) {
        LogUtil.d("onFragmentResult  ");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }
}