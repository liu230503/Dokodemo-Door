package org.alee.dokodemo.door.core;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;

import org.alee.dokodemo.door.annotation.EnumLoadModel;
import org.alee.dokodemo.door.annotation.Node;
import org.alee.dokodemo.door.exception.AlreadyExistException;
import org.alee.dokodemo.door.exception.DokodemoDoorException;
import org.alee.dokodemo.door.exception.NotExistException;
import org.alee.dokodemo.door.exception.UnSupportException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static org.alee.dokodemo.door.util.Constant.CLASS_NAME_FRAGMENT;
import static org.alee.dokodemo.door.util.Constant.CLASS_NAME_FRAGMENT_ACTIVITY;
import static org.alee.dokodemo.door.util.Constant.MethodName.Support.GET_CONTAINER_VIEW_ID;
import static org.alee.dokodemo.door.util.Constant.MethodName.Support.ON_INTERRUPT_BACK_PRESSED;

/**********************************************************
 *
 * @author: MingYu.Liu
 * @date: 2020/4/5
 * @description: 基类
 *
 *********************************************************/
abstract class BaseProxy implements IProxy {
    /**
     * key - 返回结果
     */
    protected static final String BUNDLE_KEY_FOR_RESULT = "key_for_result";
    /**
     * key - 返回结果接收者
     */
    protected static final String BUNDLE_KEY_FOR_RESULT_RECEIVE = BUNDLE_KEY_FOR_RESULT + 1;
    /**
     * key - 返回结果请求码
     */
    protected static final String BUNDLE_KEY_FOR_RESULT_REQUEST_CODE = BUNDLE_KEY_FOR_RESULT + 2;

    /**
     * 创建
     *
     * @param object 被代理的对象
     * @return {@link BaseProxy}
     */
    static BaseProxy create(@NonNull Object object) {
        if (object instanceof FragmentActivity) {
            return new ActivityProxy((FragmentActivity) object);
        } else if (object instanceof Fragment) {
            return new FragmentProxy((Fragment) object);
        } else {
            throw new DokodemoDoorException("[" + Node.class.getCanonicalName() + "] " +
                    " 注解只能使用在 [" + CLASS_NAME_FRAGMENT_ACTIVITY + "] 或 [" + CLASS_NAME_FRAGMENT + "] 的子类上");
        }
    }

    /**
     * 节点
     */
    private Object mNodeTarget;
    /**
     * {@link Context}
     */
    protected Context mContext;
    /**
     * 当前节点加载其他节点时使用的容器布局id
     */
    @IdRes
    private int mContainerViewId;
    /**
     * 是否使用粘性堆栈
     */
    private boolean mIsStickyStack;
    /**
     * {@link IFragmentTransaction}
     */
    protected IFragmentTransaction mFragmentTransaction;
    /**
     * {@link FragmentStackManager}
     */
    protected FragmentStackManager mFragmentStackManager;


    BaseProxy(Object nodeTarget) {
        this.mNodeTarget = nodeTarget;
        initNodeValue(mNodeTarget.getClass());
        mFragmentStackManager = new FragmentStackManager();
    }

    /**
     * 初始化{@link Node} 注解的属性
     *
     * @param clazz 节点的类属性
     */
    @SuppressLint("ResourceType")
    private void initNodeValue(Class<?> clazz) {
        Node node = clazz.getAnnotation(Node.class);
        if (null == node) {
            throw new DokodemoDoorException("请检查 [" + clazz.getCanonicalName() + "] 是否使用了 [" + Node.class.getCanonicalName() + "] 注解");
        }
        this.mIsStickyStack = node.stickyStack();
        this.mContainerViewId = node.containerViewId();
        if (0 >= mContainerViewId) {
            try {
                Method containerViewId = clazz.getMethod(GET_CONTAINER_VIEW_ID);
                Object value = containerViewId.invoke(mNodeTarget);
                if (!(value instanceof Integer)) {
                    throw new UnSupportException("[" + clazz.getCanonicalName() + "] 中的 [" + GET_CONTAINER_VIEW_ID + "] 函数的返回值必须为 [" + Integer.class.getCanonicalName() + "] 类型！");
                }
                this.mContainerViewId = (Integer) value;
            } catch (NoSuchMethodException ignored) {
                printLog("在[" + clazz.getCanonicalName() + "] 中当前容器id为0，且未找到" + GET_CONTAINER_VIEW_ID + "函数");
            } catch (IllegalAccessException ignored) {
                printLog("在[" + clazz.getCanonicalName() + "] 中当前容器id为0，且未找到" + GET_CONTAINER_VIEW_ID + "函数");
            } catch (InvocationTargetException ignored) {
                printLog("在[" + clazz.getCanonicalName() + "] 中当前容器id为0，且未找到" + GET_CONTAINER_VIEW_ID + "函数");
            }
        }
    }

    protected void printLog(String message) {
        if (DokodemoDoor.sUseDebugModel) {
            DokodemoDoor.sLogger.error(this.getClass().getSimpleName(), message);
        }
    }

    @SuppressLint("ResourceType")
    @NonNull
    @Override
    public Object getHost() {
        if (mNodeTarget instanceof Fragment) {
            Fragment fragment = (Fragment) mNodeTarget;
            Fragment parent = fragment.getParentFragment();
            while (null != parent) {
                IProxy proxy = DokodemoDoor.getNodeProxy(parent);
                String[] stack = ((BaseProxy) proxy).mFragmentStackManager.getFragmentsWithoutStack();
                for (String tag : stack) {
                    if (tag.equals(getFragmentTag())) {
                        return parent;
                    }
                }
                if (0 < proxy.getContainerViewId()) {
                    break;
                }
                parent = parent.getParentFragment();
            }
            if (null == parent) {
                Object host = fragment.getHost();
                return null == host ? mNodeTarget : host;
            }
            return parent;
        } else {
            return mNodeTarget;
        }
    }

    /**
     * 当{@link Fragment} 首次被添加到容器中时调用
     *
     * @param context {@link Context}
     */
    void onAttach(Context context) {

    }

    /**
     * 当{@link android.app.Activity#onCreate(Bundle)}/{@link Fragment#onCreate(Bundle)} 执行时调用
     *
     * @param savedInstanceState 保存上次页面状态的数据
     */
    void onCreate(Bundle savedInstanceState) {
        if (null != savedInstanceState) {
            this.mFragmentStackManager = FragmentStackManager.restoreStack(savedInstanceState);
        }
    }

    /**
     * 当{@link Fragment#onCreateView(LayoutInflater, ViewGroup, Bundle)} 执行时调用
     *
     * @param inflater           {@link LayoutInflater}
     * @param container          {@link ViewGroup}
     * @param savedInstanceState {@link Bundle}
     * @param view               {@link Fragment#onCreateView(LayoutInflater, ViewGroup, Bundle)} 方法的返回值
     * @return {@link View}
     */
    View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState, @Nullable View view) {
        return view;
    }

    /**
     * 在{@link Fragment#onViewCreated(View, Bundle)}执行结束但在任何保存状态恢复到视图之前立即调用。
     *
     * @param view               来自于 {@link #onCreateView(LayoutInflater, ViewGroup, Bundle, View)}
     * @param savedInstanceState {@link Bundle}
     */
    void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    }

    /**
     * 当{@link FragmentActivity#onResumeFragments()}  执行时调用
     */
    void onResumeFragments() {
    }

    /**
     * 当{@link Activity#onResume()} \{@link Fragment#onResume()}  执行时调用
     */
    abstract void onResume();

    /**
     * 当{@link Activity#onPause()}  \{@link Fragment#onPause()}   执行时调用
     */
    void onPause() {
    }

    /**
     * 当{@link Activity#onSaveInstanceState(Bundle)} \{@link Activity#onSaveInstanceState(Bundle, PersistableBundle)} \{@link Fragment#onSaveInstanceState(Bundle)}    执行时调用
     */
    abstract void onSaveInstanceState(Bundle outState);

    /**
     * 当{@link Activity#onDestroy()}  \{@link Fragment#onDestroy()}   执行时调用
     */
    abstract void onDestroy();

    /**
     * 当 {@link Fragment#onDetach()}  执行时调用
     */
    void onDetach() {
    }

    /**
     * 当 {@link Fragment#setUserVisibleHint(boolean)}  执行时调用
     */
    void setUserVisibleHint(boolean isVisibleToUser) {
    }

    /**
     * 处理返回事件
     */
    void handleBackPressed() {
        printLog(mNodeTarget + "handleBackPressed 函数被调用");
        boolean isInterrupt = onInterruptBackPressed();
        if (isInterrupt) {
            onBackPressed();
            return;
        }
        String[] fragmentsWithoutStack = mFragmentStackManager.getFragmentsWithoutStack();
        for (String tag : fragmentsWithoutStack) {
            Fragment fragmentWithoutStack = mFragmentTransaction.find(tag);
            if (null == fragmentWithoutStack) {
                throw new NotExistException(tag);
            }
            ((BaseProxy) DokodemoDoor.getNodeProxy(fragmentWithoutStack)).handleBackPressed();
        }
        String topFragmentTag = mFragmentStackManager.peek();
        if (!TextUtils.isEmpty(topFragmentTag)) {
            callTopFragmentHandleBackPressed(topFragmentTag);
        }
        if (0 == fragmentsWithoutStack.length && TextUtils.isEmpty(topFragmentTag)) {
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        printLog(mNodeTarget + "onBackPressed 函数被调用了");
        String topFragmentTag = mFragmentStackManager.peek();
        if (TextUtils.isEmpty(topFragmentTag)) {
            close();
        } else {
            callTopFragmentHandleBackPressed(topFragmentTag);
        }
    }

    /**
     * 调用上层Fragment的onBackPressed方法。
     *
     * @param topFragmentTag tag
     */
    private void callTopFragmentHandleBackPressed(String topFragmentTag) {
        Fragment topFragment = mFragmentTransaction.find(topFragmentTag);
        if (null == topFragment) {
            throw new NotExistException(topFragmentTag);
        }
        ((BaseProxy) DokodemoDoor.getNodeProxy(topFragment)).handleBackPressed();
    }

    /**
     * 如果当前节点包含
     *
     * @return
     */
    private boolean onInterruptBackPressed() {
        printLog(mNodeTarget + "onInterruptBackPressed 函数被调用");
        Class<?> clazz = mNodeTarget.getClass();
        try {
            Method onBackPressed = clazz.getMethod(ON_INTERRUPT_BACK_PRESSED);
            Object value = onBackPressed.invoke(mNodeTarget);
            if (!(value instanceof Boolean)) {
                throw new UnSupportException("[" + clazz.getCanonicalName() + "] 中的 [" + ON_INTERRUPT_BACK_PRESSED + "] 函数的返回值必须为 [" + Boolean.class.getCanonicalName() + "] 类型！");
            }
            return (Boolean) value;
        } catch (NoSuchMethodException ignored) {
            printLog("在[" + clazz.getCanonicalName() + "] 中未找到 [" + ON_INTERRUPT_BACK_PRESSED + "] 函数");
            return false;
        } catch (IllegalAccessException ignored) {
            printLog("在[" + clazz.getCanonicalName() + "] 中未找到 [" + ON_INTERRUPT_BACK_PRESSED + "] 函数");
            return false;
        } catch (InvocationTargetException ignored) {
            printLog("在[" + clazz.getCanonicalName() + "] 中未找到 [" + ON_INTERRUPT_BACK_PRESSED + "] 函数");
            return false;
        }
    }

    @Override
    public Fragment findFragmentByTag(String tag) {
        if (!mFragmentStackManager.contain(tag)) {
            return null;
        }
        return mFragmentTransaction.find(tag);
    }

    @Override
    public void addFragment(int containerViewId, Fragment... fragments) {
        if (null == fragments || 0 >= fragments.length) {
            printLog("要添加的Fragment为Null!");
            return;
        }
        for (Fragment fragment : fragments) {
            String fragmentTag = DokodemoDoor.getNodeProxy(fragment).getFragmentTag();
            if (mFragmentStackManager.add(fragmentTag, containerViewId)) {
                addFragmentWithAnim(fragment, containerViewId);
                mFragmentTransaction.hide(fragmentTag);
            } else {
                throw new AlreadyExistException(fragmentTag);
            }
            fragment.setUserVisibleHint(false);
        }
        mFragmentTransaction.commit();
    }

    /**
     * 获取已经添加到容器中并且可见的{@link Fragment} 的Tag
     *
     * @param containerViewId 容器id
     * @return 结果
     */
    private String[] getVisibleFragmentTags(@IdRes int containerViewId) {
        List<String> result = new ArrayList<>();
        String[] fragmentTags = mFragmentStackManager.getFragmentTags(containerViewId);
        for (String tag : fragmentTags) {
            Fragment fragment = mFragmentTransaction.find(tag);
            if (null == fragment) {
                continue;
            }
            if (fragment.isHidden()) {
                continue;
            }
            if (null == fragment.getView()) {
                continue;
            }
            if (View.VISIBLE == fragment.getView().getVisibility()) {
                result.add(tag);
            }
        }
        return result.toArray(new String[result.size()]);
    }

    /**
     * 设置动画期间在硬件层上的动画视图。
     * 请注意，调用此方法将用新的动画侦听器替换动画上的任何现有动画侦听器，因为动画不支持多个侦听器。
     * 因此，已经具有侦听器的动画应该 层在其现有侦听器中更改操作，而不是调用此函数。
     *
     * @param v    {@link View}
     * @param anim {@link Animation}
     */
    void setHWLayerAnimListenerIfAlpha(View v, Animation anim) {
        if (v == null || anim == null) {
            return;
        }
        if (shouldRunOnHWLayer(v, anim)) {
            anim.setAnimationListener(new AnimateOnHWLayerIfNeededListener(v, anim));
        }
    }

    static synchronized boolean shouldRunOnHWLayer(View v, Animation anim) {
        return View.LAYER_TYPE_NONE == v.getLayerType()
                && ViewCompat.hasOverlappingRendering(v)
                && modifiesAlpha(anim);
    }

    private static boolean modifiesAlpha(Animation anim) {
        if (anim instanceof AlphaAnimation) {
            return true;
        } else if (anim instanceof AnimationSet) {
            List<Animation> anims = ((AnimationSet) anim).getAnimations();
            for (int i = 0; i < anims.size(); i++) {
                if (anims.get(i) instanceof AlphaAnimation) {
                    return true;
                }
            }
        }
        return false;
    }

    private void addFragmentWithAnim(Fragment fragment, int containerViewId) {
        FragmentProxy proxy = (FragmentProxy) DokodemoDoor.getNodeProxy(fragment);
        mFragmentTransaction.setCustomAnimations(proxy.mEnterAnim, proxy.mPopExitAnim);
        mFragmentTransaction.add(containerViewId, fragment, proxy.getFragmentTag());
    }

    @SuppressLint("ResourceType")
    @Override
    public void startFragment(@NonNull Fragment fragment) {
        String tag = DokodemoDoor.getNodeProxy(fragment).getFragmentTag();
        if (!mFragmentStackManager.push(tag, mContainerViewId)) {
//            throw new AlreadyExistException(tag);
            return;
        }
        if (0 >= getContainerViewId()) {
            throw new UnSupportException("在 [" + mNodeTarget.getClass().getCanonicalName() + "] 中必须提供 ContainerViewId!");
        }
        addFragmentWithAnim(fragment, mContainerViewId);
        mFragmentTransaction.hide(getVisibleFragmentTags(getContainerViewId()));
        mFragmentTransaction.show(tag).commit();
    }

    @Override
    public void startFragmentForResult(@NonNull Fragment fragment, int requestCode) {
        startFragmentForResult(null, fragment, requestCode);
    }

    @Override
    public void startFragmentForResult(Object receive, @NonNull Fragment fragment, int requestCode) {
        Bundle arguments = fragment.getArguments();
        arguments = null == arguments ? new Bundle() : arguments;
        Bundle receiveArgs = new Bundle();
        if (null != receive) {
            receiveArgs.putString(BUNDLE_KEY_FOR_RESULT_RECEIVE, DokodemoDoor.getNodeProxy(receive).getFragmentTag());
        }
        receiveArgs.putInt(BUNDLE_KEY_FOR_RESULT_REQUEST_CODE, requestCode);
        arguments.putParcelable(BUNDLE_KEY_FOR_RESULT, receiveArgs);
        fragment.setArguments(arguments);
        startFragment(fragment);
    }

    @Override
    public void startPopFragment() {
        startPopFragment(null);
    }

    void startPopFragment(Animation animation) {
        String topFragmentTag = mFragmentStackManager.peek();
        mFragmentTransaction.hide(getVisibleFragmentTags(getContainerViewId()));
        if (TextUtils.isEmpty(topFragmentTag)) {
            mFragmentTransaction.commit();
            return;
        }
        Fragment topFragment = mFragmentTransaction.find(topFragmentTag);
        if (null == topFragment) {
            mFragmentTransaction.commit();
            return;
        }
        if (null != animation) {
            View view = topFragment.getView();
            if (null != view) {
                view.startAnimation(animation);
            }
        }
        mFragmentTransaction.setCustomAnimations(0, 0);
        mFragmentTransaction.show(topFragmentTag);
        mFragmentTransaction.commit();
    }

    @Override
    public void showFragment(@NonNull String tag) {
        showFragment(tag, false);
    }

    @Override
    public void showFragment(@NonNull Fragment fragment, int containerViewId) {
        showFragment(fragment, containerViewId, false);
    }

    @Override
    public void showFragment(@NonNull String tag, boolean showRepeatAnim) {
        int containerViewId = mFragmentStackManager.getContainer(tag);
        if (0 >= containerViewId) {
            throw new NotExistException(tag);
        }
        showFragment(mFragmentTransaction.find(tag), containerViewId, showRepeatAnim);
    }

    @Override
    public void showFragment(@NonNull Fragment fragment, int containerViewId, boolean showRepeatAnim) {
        String fragmentTag = DokodemoDoor.getNodeProxy(fragment).getFragmentTag();
        if (mFragmentStackManager.add(fragmentTag, containerViewId)) {
            addFragmentWithAnim(fragment, containerViewId);
        }
        String[] fragmentTags = mFragmentStackManager.getFragmentTags(containerViewId);
        for (String tag : fragmentTags) {
            Fragment hideFrag = mFragmentTransaction.find(tag);
            if (null == hideFrag) {
                continue;
            }
            hideFrag.setUserVisibleHint(false);
        }
        fragment.setUserVisibleHint(true);
        boolean hidden = fragment.isHidden();
        boolean added = fragment.isAdded();
        if (!added || hidden || showRepeatAnim) {
            mFragmentTransaction.hide(getVisibleFragmentTags(containerViewId));
            showFragmentWithAnim(fragment);
        }
        mFragmentTransaction.commit();
    }

    private void showFragmentWithAnim(Fragment fragment) {
        FragmentProxy proxy = (FragmentProxy) DokodemoDoor.getNodeProxy(fragment);
        mFragmentTransaction.setCustomAnimations(proxy.mPopEnterAnim, proxy.mExitAnim);
        mFragmentTransaction.show(proxy.getFragmentTag());
    }

    @Override
    public void hideFragment(@NonNull String tag) {
        if (!mFragmentStackManager.contain(tag)) {
            throw new NotExistException(tag);
        }
        hideFragment(mFragmentTransaction.find(tag));
    }

    @Override
    public void hideFragment(@NonNull Fragment fragment) {
        FragmentProxy proxy = (FragmentProxy) DokodemoDoor.getNodeProxy(fragment);
        String fragmentTAG = proxy.getFragmentTag();
        mFragmentTransaction.setCustomAnimations(proxy.mPopEnterAnim, proxy.mExitAnim);
        mFragmentTransaction.hide(fragmentTAG)
                .commit();
    }

    @Override
    public void replaceFragment(@NonNull Fragment fragment, int containerViewId) {
        String fragmentTag = DokodemoDoor.getNodeProxy(fragment).getFragmentTag();
        addFragmentWithAnim(fragment, containerViewId);
        mFragmentTransaction.remove(mFragmentStackManager.getFragmentTags(containerViewId))
                .show(fragmentTag)
                .commit();
        mFragmentStackManager.remove(containerViewId);
        mFragmentStackManager.add(fragmentTag, containerViewId);
    }

    @Override
    public void close(@NonNull Fragment fragment) {
        String fragmentTag = DokodemoDoor.getNodeProxy(fragment).getFragmentTag();
        if (!mFragmentStackManager.remove(fragmentTag)) {
            throw new NotExistException(fragmentTag);
        }
        if (isStickyStack() && mFragmentStackManager.getFragmentTagStack().empty()) {
            close();
        } else {
            mFragmentTransaction.remove(fragmentTag).commit();
        }
    }

    @Override
    public int getContainerViewId() {
        return mContainerViewId;
    }

    @Override
    public boolean isStickyStack() {
        return mIsStickyStack;
    }

    @Override
    public EnumLoadModel getLoadModel() {
        return EnumLoadModel.NORMAL_LOAD;
    }

    @Override
    public Stack<String> getFragmentTagStack() {
        if (null == mFragmentStackManager || null == mFragmentStackManager.getFragmentTagStack()) {
            return new Stack<>();
        }
        return (Stack<String>) mFragmentStackManager.getFragmentTagStack().clone();
    }

    @Override
    public void printStack() {
        StringBuilder sb = new StringBuilder("printStack \r\n");
        sb.append(getFragmentTagStack());
        Stack<String> stack = mFragmentStackManager.getFragmentTagStack();
        printStack(sb, this, stack, 1);
        printLog(sb.toString());
    }

    private void printStack(StringBuilder sb, BaseProxy proxy, Stack<String> stack, int level) {
        if (null == stack || stack.isEmpty()) {
            return;
        }
        for (int p = stack.size() - 1; p >= 0; p--) {
            String tag = stack.get(p);
            sb.append("\n");
            sb.append("┃");
            if (level != 1) {
                for (int i = 0; i < level; i++) {
                    sb.append(" ").append(" ").append(" ").append(" ");
                }
            }
            for (int i = 0; i < level; i++) {
                sb.append("\t");
            }
            Fragment fragment = proxy.mFragmentTransaction.find(tag);
            BaseProxy childRigger = (BaseProxy) DokodemoDoor.getNodeProxy(fragment);
            Stack<String> childStack = childRigger.getFragmentTagStack();
            if (p > 0 && childStack.isEmpty()) {
                sb.append("┠");
            } else {
                sb.append("┖");
            }
            sb.append("————");
            sb.append(tag);
            printStack(sb, childRigger, childStack, level + 1);
        }
    }


}
