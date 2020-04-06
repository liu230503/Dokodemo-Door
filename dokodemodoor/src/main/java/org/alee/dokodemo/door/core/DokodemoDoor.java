package org.alee.dokodemo.door.core;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.alee.dokodemo.door.annotation.Node;
import org.alee.dokodemo.door.exception.DokodemoDoorException;
import org.alee.dokodemo.door.template.ILogger;
import org.alee.dokodemo.door.util.Logger;

import static org.alee.dokodemo.door.util.Constant.CLASS_NAME_FRAGMENT;
import static org.alee.dokodemo.door.util.Constant.CLASS_NAME_FRAGMENT_ACTIVITY;

/**********************************************************
 *
 * @author: MingYu.Liu
 * @date: 2020/4/5
 * @description: xxxx
 *
 *********************************************************/
public final class DokodemoDoor {
    /**
     * 用于{@link IProxy#setResult(int, Bundle)}成功
     */
    public static final int RESULT_OK = 0x1010;
    /**
     * 用于{@link IProxy#setResult(int, Bundle)} 取消
     */
    public static final int RESULT_CANCELED = 0x1020;
    /**
     * {@link ILogger}
     */
    static ILogger sLogger = new Logger();
    /**
     * 标识是否使用debug 模式
     */
    static boolean sUseDebugModel = true;
    /**
     * 节点代理集合
     */
    private SparseArray<IProxy> mNodeList;

    /**
     * 静态内部类，持有外部类
     */
    private static class DokodemoDoorHolder {
        private static DokodemoDoor INSTANCE = new DokodemoDoor();
    }

    /**
     * 获取单例对象
     *
     * @return {@link DokodemoDoor}
     */
    private static DokodemoDoor getInstance() {
        return DokodemoDoorHolder.INSTANCE;
    }

    private DokodemoDoor() {
        mNodeList = new SparseArray<>();
    }

    public static void setLogger(ILogger logger) {
        sLogger = logger;
    }

    public static void setUseDebugModel(boolean useDebugModel) {
        sUseDebugModel = useDebugModel;
    }

    public static IProxy getNodeProxy(Object node) {
        if (!(node instanceof FragmentActivity) && !(node instanceof Fragment)) {
            throw new DokodemoDoorException(
                    "任意门 只支持处理 [" + CLASS_NAME_FRAGMENT_ACTIVITY + "] or [" + CLASS_NAME_FRAGMENT + "] 的子类");
        }
        Class<?> clazz = node.getClass();
        Node annotation = clazz.getAnnotation(Node.class);
        if (null == annotation) {
            throw new DokodemoDoorException("请检查 [" + clazz.getCanonicalName() + "] 是否使用了 [" + Node.class.getCanonicalName() + "] 注解");
        }
        int code = System.identityHashCode(node);
        IProxy proxy = getInstance().mNodeList.get(code);
        if (null == proxy) {
            throw new DokodemoDoorException("未知异常，[" + clazz.getCanonicalName() + "] 未添加到任意门中，请检查你的代码或联系作者！");
        }
        return proxy;
    }

    private BaseProxy createNodeProxy(Object node) {
        int code = System.identityHashCode(node);
        IProxy proxy = mNodeList.get(code);
        if (null == proxy) {
            proxy = BaseProxy.create(node);
            mNodeList.put(code, proxy);
            printLog("添加 [" + node + "] 的代理对象到缓存中！");
            return (BaseProxy) proxy;
        }
        return (BaseProxy) proxy;
    }

    private void printLog(String message) {
        if (DokodemoDoor.sUseDebugModel) {
            DokodemoDoor.sLogger.error(this.getClass().getSimpleName(), message);
        }
    }

    private boolean removeNodeProxy(Object node) {
        int code = System.identityHashCode(node);
        if (0 > mNodeList.indexOfKey(code)) {
            return false;
        }
        mNodeList.remove(code);
        printLog("从缓存中移除 [" + node + "] 的代理对象！");
        return true;
    }

    /**
     * 在{@link FragmentActivity}\{@link Fragment}的构造方法执行时被调用，将目标节点添加到缓存中
     *
     * @param object 目标节点
     */
    private void onConstructor(Object object) {
        createNodeProxy(object);
    }

    /**
     * 在{@link Fragment#onAttach(Context)}函数执行时注入
     *
     * @param object  节点
     * @param context {@link Context}
     */
    private void onAttach(Object object, Context context) {
        printLog(" [" + object + "]  onAttach！");
        createNodeProxy(object).onAttach(context);
    }

    /**
     * 在{@link FragmentActivity#onCreate}\{@link Fragment#onCreate(Bundle)}函数执行时注入
     *
     * @param object             节点
     * @param savedInstanceState {@link Bundle}
     */
    private void onCreate(Object object, Bundle savedInstanceState) {
        printLog(" [" + object + "]  onCreate！");
        createNodeProxy(object).onCreate(savedInstanceState);
    }

    /**
     * 在{{@link Fragment#onCreateView(LayoutInflater, ViewGroup, Bundle)}函数执行时注入
     *
     * @param object             节点
     * @param savedInstanceState {@link Bundle}
     */
    private Object onCreateView(Object object, LayoutInflater inflater, @Nullable ViewGroup container,
                                @Nullable Bundle savedInstanceState, @Nullable View view) {
        printLog(" [" + object + "]  onCreateView！");
        return createNodeProxy(object).onCreateView(inflater, container, savedInstanceState, view);
    }

    /**
     * 在{{@link Fragment#onViewCreated(View, Bundle)}函数执行时注入
     *
     * @param object             节点
     * @param view               {@link View}
     * @param savedInstanceState {@link Bundle}
     */
    private void onViewCreated(Object object, View view, @Nullable Bundle savedInstanceState) {
        printLog(" [" + object + "]  onViewCreated！");
        createNodeProxy(object).onViewCreated(view, savedInstanceState);
    }

    /**
     * 在{@link FragmentActivity#onResume()}\{@link Fragment#onResume()} 函数执行时注入
     *
     * @param object 节点
     */
    private void onResume(Object object) {
        printLog(" [" + object + "]  onResume！");
        createNodeProxy(object).onResume();
    }

    /**
     * 在{@link FragmentActivity#onResumeFragments()}  函数执行时注入
     *
     * @param object 节点
     */
    private void onResumeFragments(Object object) {
        printLog(" [" + object + "]  onResumeFragments！");
        createNodeProxy(object).onResumeFragments();
    }

    /**
     * 在{@link FragmentActivity#onPause()} \{@link Fragment#onPause()}  函数执行时注入
     *
     * @param object 节点
     */
    private void onPause(Object object) {
        printLog(" [" + object + "]  onPause！");
        createNodeProxy(object).onPause();
    }

    /**
     * 在{@link FragmentActivity#onSaveInstanceState} \{@link Fragment#onSaveInstanceState(Bundle)}   函数执行时注入
     *
     * @param object   节点
     * @param outState {@link Bundle}
     */
    private void onSaveInstanceState(Object object, Bundle outState) {
        printLog(" [" + object + "]  onSaveInstanceState！");
        createNodeProxy(object).onSaveInstanceState(outState);
    }

    /**
     * 在{@link FragmentActivity#onDestroy()}  \{@link Fragment#onDestroy()} 函数执行时注入
     *
     * @param object 节点
     */
    private void onDestroy(Object object) {
        printLog(" [" + object + "]  onDestroy！");
        createNodeProxy(object).onDestroy();
        removeNodeProxy(object);
    }

    /**
     * 在{@link Fragment#onDetach()}   函数执行时注入
     *
     * @param object 节点
     */
    private void onDetach(Object object) {
        printLog(" [" + object + "]  onDetach！");
        createNodeProxy(object).onDetach();
        removeNodeProxy(object);
    }

    /**
     * 在{@link FragmentActivity#onBackPressed()}    函数执行时注入
     *
     * @param object 节点
     */
    private void onBackPressed(Object object) {
        printLog(" [" + object + "]  onBackPressed！");
        createNodeProxy(object).handleBackPressed();
    }

    /**
     * 在{@link Fragment#onDetach()}   函数执行时注入
     *
     * @param object          节点
     * @param isVisibleToUser 是否显示
     */
    private void setUserVisibleHint(Object object, boolean isVisibleToUser) {
        printLog(" [" + object + "]  setUserVisibleHint！");
        createNodeProxy(object).setUserVisibleHint(isVisibleToUser);
    }
}
