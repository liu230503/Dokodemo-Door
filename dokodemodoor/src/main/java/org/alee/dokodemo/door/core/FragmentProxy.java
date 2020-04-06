package org.alee.dokodemo.door.core;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import org.alee.dokodemo.door.annotation.Animation;
import org.alee.dokodemo.door.annotation.EnumLoadModel;
import org.alee.dokodemo.door.annotation.LoadModel;
import org.alee.dokodemo.door.exception.UnSupportException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

import static org.alee.dokodemo.door.util.Constant.MethodName.Support.GET_FRAGMENT_TAG;
import static org.alee.dokodemo.door.util.Constant.MethodName.Support.GET_NODE_ANIMATIONS;
import static org.alee.dokodemo.door.util.Constant.MethodName.Support.ON_FRAGMENT_RESULT;
import static org.alee.dokodemo.door.util.Constant.MethodName.Support.ON_LAZYLOAD_VIEW_CREATED;
import static org.alee.dokodemo.door.util.Constant.MethodName.Support.ON_NODE_BACKPRESSED;

/**********************************************************
 *
 * @author: MingYu.Liu
 * @date: 2020/4/5
 * @description: {@link Fragment} 代理类
 *
 *********************************************************/
public class FragmentProxy extends BaseProxy {

    private static final String BUNDLE_KEY_FRAGMENT_TAG = "/bundle/key/fragment/tag";
    private static final String BUNDLE_KEY_FRAGMENT_STATUS_HIDE = "/bundle/key/fragment/status/hide";
    private static final String BUNDLE_KEY_FRAGMENT_LAZYLOAD_ABLE = "/bundle/key/fragment/lazyLoad/able";
    private static final String BUNDLE_KEY_FRAGMENT_LAZYLOAD_INVOKE = "/bundle/key/fragment/lazyLoad/invoke";
    private static final String BUNDLE_KEY_FRAGMENT_VIEW_INIT = "/bundle/key/fragment/view/init";
    private static final String BUNDLE_KEY_FRAGMENT_ANIMATION = "/bundle/key/fragment/animation";
    /**
     * 宿主{@link Activity}
     */
    private Activity mHostActivity;

    /**
     * 被代理的{@link Fragment}
     */
    private Fragment mFragment;
    /**
     * 其父层{@link IFragmentTransaction}
     */
    private IFragmentTransaction mParentFragmentTransaction;
    /**
     * 被代理的{@link Fragment} 的tag
     */
    private String mFragmentTag;
    /**
     * 保存的Fragment 状态
     */
    private Bundle mSavedFragmentState;
    /**
     * 返回数据
     */
    private Bundle mForResultTarget;
    /**
     * {@link Animation#enterAnim()}
     */
    @AnimRes
    int mEnterAnim;
    /**
     * {@link Animation#exitAnim()}
     */
    @AnimRes
    int mExitAnim;
    /**
     * {@link Animation#popEnterAnim()}
     */
    @AnimRes
    int mPopEnterAnim;
    /**
     * {@link Animation#popExitAnim()}
     */
    @AnimRes
    int mPopExitAnim;
    /**
     * 加载模式
     */
    private EnumLoadModel mLoadModel = EnumLoadModel.NORMAL_LOAD;
    /**
     * 标识是否已经初始化view
     */
    private boolean mHasInitView;
    /**
     * 标识是否已经调用懒加载函数
     */
    private boolean mHasInvokeLazyLoad;

    FragmentProxy(@NonNull Fragment fragment) {
        super(fragment);
        this.mFragment = fragment;
        Class<? extends Fragment> clazz = mFragment.getClass();
        initLoadModel(clazz);
        initFragmentAnimators(clazz);
        initCustomFragmentTag(clazz);
    }

    private void initCustomFragmentTag(Class<? extends Fragment> clazz) {
        try {
            Method method = clazz.getMethod(GET_FRAGMENT_TAG);
            Object value = method.invoke(mFragment);
            if (!(value instanceof String)) {
                throw new UnSupportException("[" + clazz.getCanonicalName() + "] 中的 [" + GET_FRAGMENT_TAG + "] 函数的返回值必须为 [" + String.class.getCanonicalName() + "] 类型！");
            }
            this.mFragmentTag = (String) value;
        } catch (NoSuchMethodException ignore) {
            printLog("在[" + clazz.getCanonicalName() + "] 中当前容器id为0，且未找到" + GET_FRAGMENT_TAG + "函数");
        } catch (IllegalAccessException ignored) {
            printLog("在[" + clazz.getCanonicalName() + "] 中当前容器id为0，且未找到" + GET_FRAGMENT_TAG + "函数");
        } catch (InvocationTargetException ignored) {
            printLog("在[" + clazz.getCanonicalName() + "] 中当前容器id为0，且未找到" + GET_FRAGMENT_TAG + "函数");
        }
        this.mFragmentTag = TextUtils.isEmpty(mFragmentTag) ? mFragment.getClass().getSimpleName() + "__" + UUID.randomUUID().toString().substring(0, 8) : mFragmentTag;
    }

    private void initFragmentAnimators(Class<? extends Fragment> clazz) {
        Animation animation = clazz.getAnnotation(Animation.class);
        if (null != animation) {
            this.mEnterAnim = animation.enterAnim();
            this.mExitAnim = animation.exitAnim();
            this.mPopEnterAnim = animation.popEnterAnim();
            this.mPopExitAnim = animation.popExitAnim();
        }
        try {
            Method method = clazz.getMethod(GET_NODE_ANIMATIONS);
            Object values = method.invoke(mFragment);
            if (!(values instanceof int[])) {
                throw new UnSupportException("[" + clazz.getCanonicalName() + "] 中的 [" + GET_NODE_ANIMATIONS + "] 函数的返回值必须为 [ int[] ] 类型！");
            }
            int[] animators = (int[]) values;
            if (null == animators || 4 > animators.length) {
                throw new UnSupportException("[" + clazz.getCanonicalName() + "] 中的 [" + GET_NODE_ANIMATIONS + "] 函数的返回值长度不能小于4！");
            }
            this.mEnterAnim = animators[0];
            this.mExitAnim = animators[1];
            this.mPopEnterAnim = animators[2];
            this.mPopExitAnim = animators[3];
        } catch (NoSuchMethodException ignore) {
            printLog("在 [" + clazz.getCanonicalName() + "] 未找到 [" + GET_FRAGMENT_TAG + "] 函数");
        } catch (IllegalAccessException ignore) {
            printLog("在 [" + clazz.getCanonicalName() + "] 未找到 [" + GET_FRAGMENT_TAG + "] 函数");
        } catch (InvocationTargetException ignore) {
            printLog("在 [" + clazz.getCanonicalName() + "] 未找到 [" + GET_FRAGMENT_TAG + "] 函数");
        }
    }

    private void initLoadModel(Class<? extends Fragment> clazz) {
        LoadModel loadModel = clazz.getAnnotation(LoadModel.class);
        if (null == loadModel) {
            return;
        }
        this.mLoadModel = loadModel.loadModel();
    }


    @Override
    void onSaveInstanceState(Bundle outState) {
        outState.putString(BUNDLE_KEY_FRAGMENT_TAG, mFragmentTag);
        outState.putInt(BUNDLE_KEY_FRAGMENT_ANIMATION + 1, mEnterAnim);
        outState.putInt(BUNDLE_KEY_FRAGMENT_ANIMATION + 2, mExitAnim);
        outState.putInt(BUNDLE_KEY_FRAGMENT_ANIMATION + 3, mPopEnterAnim);
        outState.putInt(BUNDLE_KEY_FRAGMENT_ANIMATION + 4, mPopExitAnim);
        outState.putBoolean(BUNDLE_KEY_FRAGMENT_STATUS_HIDE, mFragment.isHidden());
        outState.putInt(BUNDLE_KEY_FRAGMENT_LAZYLOAD_ABLE, mLoadModel.getIndex());
        outState.putBoolean(BUNDLE_KEY_FRAGMENT_LAZYLOAD_INVOKE, mHasInvokeLazyLoad);
        outState.putBoolean(BUNDLE_KEY_FRAGMENT_VIEW_INIT, mHasInitView);
        outState.putParcelable(BUNDLE_KEY_FOR_RESULT, mForResultTarget);
        mFragmentStackManager.saveInstanceState(outState);
    }

    @Override
    void onDestroy() {
        initLazyLoadStatus();
    }

    @Override
    public boolean isResumed() {
        return mFragment.isResumed();
    }

    @Override
    public void onBackPressed() {
        boolean isInterrupt;
        Class<?> clazz = mFragment.getClass();
        try {
            Method onBackPressed = clazz.getMethod(ON_NODE_BACKPRESSED);
            Object value = onBackPressed.invoke(mFragment);
            if (!(value instanceof Boolean)) {
                throw new UnSupportException("[" + clazz.getCanonicalName() + "] 中的 [" + ON_NODE_BACKPRESSED + "] 函数的返回值必须为 [" + Boolean.class.getCanonicalName() + "] 类型！");
            }
            isInterrupt = (Boolean) value;
        } catch (NoSuchMethodException ignore) {
            isInterrupt = false;
            printLog("在[" + clazz.getCanonicalName() + "] 中未找到 [" + ON_NODE_BACKPRESSED + "] 函数");
        } catch (InvocationTargetException ignore) {
            isInterrupt = false;
            printLog("在[" + clazz.getCanonicalName() + "] 中未找到 [" + ON_NODE_BACKPRESSED + "] 函数");
        } catch (IllegalAccessException ignore) {
            isInterrupt = false;
            printLog("在[" + clazz.getCanonicalName() + "] 中未找到 [" + ON_NODE_BACKPRESSED + "] 函数");
        }
        if (isInterrupt) {
            printLog(mFragment.getClass().getCanonicalName() + "[" + ON_NODE_BACKPRESSED + "] 函数已经调用");
            return;
        }
        if (DokodemoDoor.getNodeProxy(getHost()).getFragmentTagStack().contains(getFragmentTag())) {
            super.onBackPressed();
        } else {
            printLog(mFragment.getClass().getCanonicalName() + "[" + ON_NODE_BACKPRESSED + "] 函数已经调用");
            DokodemoDoor.getNodeProxy(getHost()).onBackPressed();
        }
    }

    @SuppressLint("ResourceType")
    @Override
    public void startFragment(@NonNull Fragment fragment) {
        if (0 < getContainerViewId()) {
            super.startFragment(fragment);
            return;
        }
        DokodemoDoor.getNodeProxy(getHost()).startFragment(fragment);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        printLog("liumy=== "+mFragment+" isVisibleToUser:"+isVisibleToUser);
        invokeOnLazyLoadViewCreated();
    }

    @SuppressLint("ResourceType")
    @Override
    public void close() {
        if (mExitAnim != 0 && !mFragment.isHidden()) {
            boolean isParentBond = DokodemoDoor.getNodeProxy(getHost()).isStickyStack();
            int parentStackSize = DokodemoDoor.getNodeProxy(getHost()).getFragmentTagStack().size();
            if (!isParentBond || 0 < parentStackSize) {
                android.view.animation.Animation animation = AnimationUtils.loadAnimation(mHostActivity, mExitAnim);
                if (null != animation) {
                    View view = mFragment.getView();
                    if (null != view) {
                        setHWLayerAnimListenerIfAlpha(view, animation);
                        view.startAnimation(animation);
                    }
                }
            }
        }
        mFragmentStackManager.clear();
        mFragmentTransaction.removeAll();
        DokodemoDoor.getNodeProxy(getHost()).close(mFragment);
        android.view.animation.Animation animation = null;
        if (0 < mPopEnterAnim) {
            animation = AnimationUtils.loadAnimation(mContext, mPopEnterAnim);
        }
        ((BaseProxy) DokodemoDoor.getNodeProxy(getHost())).startPopFragment(animation);
    }

    @Override
    public void closeWithoutTransaction() {
        mFragmentStackManager.clear();
        mFragmentTransaction.removeAll();
        DokodemoDoor.getNodeProxy(getHost()).close(mFragment);
        ((BaseProxy) DokodemoDoor.getNodeProxy(getHost())).startPopFragment(null);
    }

    @Override
    public void setFragmentTag(@NonNull String tag) {
        if (TextUtils.isEmpty(tag)) {
            throw new UnSupportException("[" + mFragment.getClass().getCanonicalName() + "] setFragmentTag中传入的Tag 不能为空");
        }
        tag.intern();
        mFragmentTag = tag.intern();
    }

    @Override
    public String getFragmentTag() {
        if (null == mFragment) {
            return mFragmentTag;
        }
        if (TextUtils.isEmpty(mFragment.getTag())) {
            return mFragmentTag;
        }
        return mFragment.getTag();
    }

    @Override
    public EnumLoadModel getLoadModel() {
        return mLoadModel;
    }

    @Override
    public void setResult(int resultCode, Bundle bundle) {
        if (null == mForResultTarget) {
            throw new UnSupportException("[" + mFragment.getClass().getCanonicalName() + "] 不是由 startFragmentForResult 函数启动的!");
        }
        int requestCode = mForResultTarget.getInt(BUNDLE_KEY_FOR_RESULT_REQUEST_CODE);
        String receiveTargetTag = mForResultTarget.getString(BUNDLE_KEY_FOR_RESULT_RECEIVE);
        Object host = DokodemoDoor.getNodeProxy(getHost()).findFragmentByTag(receiveTargetTag);
        if (null == host) {
            Fragment startFragment = mFragment.getParentFragment();
            while (null != startFragment) {
                int containerViewId = DokodemoDoor.getNodeProxy(startFragment).getContainerViewId();
                if (0 < containerViewId) {
                    break;
                }
                startFragment = startFragment.getParentFragment();
            }
            host = startFragment == null ? mHostActivity : startFragment;
        }
        Class<?> clazz = host.getClass();
        try {
            Method method = clazz.getMethod(ON_FRAGMENT_RESULT, int.class, int.class, Bundle.class);
            method.invoke(host, requestCode, resultCode, bundle);
        } catch (NoSuchMethodException ignored) {
            printLog("[" + host.getClass().getCanonicalName() + "] 中未找到 [" + ON_FRAGMENT_RESULT + "] 函数");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(Context context) {
        this.mHostActivity = (Activity) context;
        this.mContext = context;
    }

    @Override
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mSavedFragmentState = savedInstanceState;
        if (null == mFragmentTransaction) {
            mFragmentTransaction = new MyFragmentTransaction(DokodemoDoor.getNodeProxy(mHostActivity), mFragment.getChildFragmentManager());
        }
        if (null == mParentFragmentTransaction) {
            mParentFragmentTransaction = new MyFragmentTransaction(DokodemoDoor.getNodeProxy(mHostActivity), mFragment.getFragmentManager());
        }
        initResultParams(savedInstanceState);
        restoreAttributes(savedInstanceState);
    }

    @Override
    public void onResume() {
        mParentFragmentTransaction.commit();
        mFragmentTransaction.commit();
    }

    private void restoreAttributes(Bundle savedInstanceState) {
        if (null == savedInstanceState) {
            return;
        }
        this.mLoadModel = EnumLoadModel.getLoadModelByIndex(savedInstanceState.getInt(BUNDLE_KEY_FRAGMENT_LAZYLOAD_ABLE));
        this.mHasInitView = savedInstanceState.getBoolean(BUNDLE_KEY_FRAGMENT_VIEW_INIT);
        this.mHasInvokeLazyLoad = savedInstanceState.getBoolean(BUNDLE_KEY_FRAGMENT_LAZYLOAD_INVOKE);
        this.mFragmentTag = savedInstanceState.getString(BUNDLE_KEY_FRAGMENT_TAG);
        this.mEnterAnim = savedInstanceState.getInt(BUNDLE_KEY_FRAGMENT_ANIMATION + 1);
        this.mExitAnim = savedInstanceState.getInt(BUNDLE_KEY_FRAGMENT_ANIMATION + 2);
        this.mPopEnterAnim = savedInstanceState.getInt(BUNDLE_KEY_FRAGMENT_ANIMATION + 3);
        this.mPopExitAnim = savedInstanceState.getInt(BUNDLE_KEY_FRAGMENT_ANIMATION + 4);
        restoreHiddenState(savedInstanceState);
    }

    /**
     * 恢复隐藏状态
     *
     * @param savedInstanceState {@link Bundle}
     */
    private void restoreHiddenState(Bundle savedInstanceState) {
        boolean isHidden = savedInstanceState.getBoolean(BUNDLE_KEY_FRAGMENT_STATUS_HIDE);
        if (isHidden) {
            mParentFragmentTransaction.hide(getFragmentTag()).commit();
        } else {
            mParentFragmentTransaction.show(getFragmentTag()).commit();
        }
    }

    private void initResultParams(Bundle savedInstanceState) {
        Bundle bundle = null == savedInstanceState ? mFragment.getArguments() : savedInstanceState;
        if (null == bundle) {
            bundle = new Bundle();
        }
        this.mForResultTarget = bundle.getParcelable(BUNDLE_KEY_FOR_RESULT);
    }

    @Override
    View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                      @Nullable Bundle savedInstanceState, @Nullable View view) {
        this.mHasInitView = true;
        initLazyLoadStatus();
        printLog("liumy=== onCreateView:" + mFragment);
        return super.onCreateView(inflater, container, savedInstanceState, view);
    }

    /**
     * 初始化懒加载状态
     */
    private void initLazyLoadStatus() {
        this.mHasInitView = false;
        this.mHasInvokeLazyLoad = false;
    }

    @Override
    void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mHasInitView = true;
        mSavedFragmentState = savedInstanceState;
        printLog("liumy=== onViewCreated:" + mFragment);
        invokeOnLazyLoadViewCreated();
    }


    /**
     * 调用懒加载创建view函数
     */
    private void invokeOnLazyLoadViewCreated() {
        printLog("liumy=== invokeOnLazyLoadViewCreated:" + mFragment);
        printLog("liumy=== getUserVisibleHint:" + mFragment.getUserVisibleHint());
        if (EnumLoadModel.LAZY_LOAD != mLoadModel) {
            return;
        }
        if (!mHasInitView) {
            return;
        }
        if (!mFragment.getUserVisibleHint()) {
            return;
        }
        if (mHasInvokeLazyLoad) {
            return;
        }
        Method onLazyLoadViewCreated = null;
        try {
            onLazyLoadViewCreated = mFragment.getClass()
                    .getMethod(ON_LAZYLOAD_VIEW_CREATED, Bundle.class);
        } catch (NoSuchMethodException ignore) {
            printLog("在 [" + mFragment.getClass().getCanonicalName() + "] 中未找到 [" + ON_LAZYLOAD_VIEW_CREATED + "] 函数");
        }
        if (null == onLazyLoadViewCreated) {
            throw new UnSupportException("在 [" + mFragment.getClass().getCanonicalName() + "] 中未找到 [" + ON_LAZYLOAD_VIEW_CREATED + "] 函数");
        } else {
            try {
                onLazyLoadViewCreated.invoke(mFragment, mSavedFragmentState);
                mHasInvokeLazyLoad = true;
            } catch (Exception ignore) {
                printLog("在调用 [" + mFragment.getClass().getCanonicalName() + "] 中的[" + ON_LAZYLOAD_VIEW_CREATED + "] 函数时发生异常：" + ignore.getMessage());
            }
        }
    }
}
