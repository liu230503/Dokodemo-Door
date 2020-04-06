package org.alee.dokodemo.door.aop;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.alee.dokodemo.door.core.DokodemoDoor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.reflect.Method;

import static org.alee.dokodemo.door.util.Constant.CLASS_NAME_FRAGMENT;
import static org.alee.dokodemo.door.util.Constant.MethodName.Launcher.ATTACH;
import static org.alee.dokodemo.door.util.Constant.MethodName.Launcher.CONSTRUCTOR;
import static org.alee.dokodemo.door.util.Constant.MethodName.Launcher.CREATE;
import static org.alee.dokodemo.door.util.Constant.MethodName.Launcher.CREATE_VIEW;
import static org.alee.dokodemo.door.util.Constant.MethodName.Launcher.DESTROY;
import static org.alee.dokodemo.door.util.Constant.MethodName.Launcher.DETACH;
import static org.alee.dokodemo.door.util.Constant.MethodName.Launcher.RESUME;
import static org.alee.dokodemo.door.util.Constant.MethodName.Launcher.SAVE_INSTANCE_STATE;
import static org.alee.dokodemo.door.util.Constant.MethodName.Launcher.SET_USER_VISIBLE_HINT;
import static org.alee.dokodemo.door.util.Constant.MethodName.Launcher.VIEW_CREATED;

/**********************************************************
 *
 * @author: MingYu.Liu
 * @date: 2020/4/5
 * @description: 通过 AspectJ 实现AOP思想，此类用于将{@link DokodemoDoor}编织到Fragment的生命周期和其他方法中。
 *
 *********************************************************/
@Aspect
public class FragmentWeaver extends BaseWeaver {
    /**
     * 方法名- constructWeaver
     */
    private static final String METHOD_NAME_CONSTRUCT = "constructWeaver()";
    /**
     * 方法名- onAttachWeaver
     */
    private static final String METHOD_NAME_ON_ATTACH = "onAttachWeaver()";
    /**
     * 方法名- onCreateWeaver
     */
    private static final String METHOD_NAME_ON_CREATE = "onCreateWeaver()";
    /**
     * 方法名- onViewCreatedWeaver
     */
    private static final String METHOD_NAME_ON_VIEW_CREATED = "onViewCreatedWeaver()";
    /**
     * 方法名- onCreateViewWeaver
     */
    private static final String METHOD_NAME_ON_CREATE_VIEW = "onCreateViewWeaver()";
    /**
     * 方法名- onResumeWeaver
     */
    private static final String METHOD_NAME_ON_RESUME = "onResumeWeaver()";
    /**
     * 方法名- onSaveInstanceStateWeaver
     */
    private static final String METHOD_NAME_ON_SAVE_INSTANCE_STATE = "onSaveInstanceStateWeaver()";
    /**
     * 方法名- onDestroyWeaver
     */
    private static final String METHOD_NAME_ON_DESTROY = "onDestroyWeaver()";
    /**
     * 方法名- onDetachWeaver
     */
    private static final String METHOD_NAME_ON_DETACH = "onDetachWeaver()";
    /**
     * 方法名- setUserVisibleHintWeaver
     */
    private static final String METHOD_NAME_SET_USER_VISIBLE_HINT = "setUserVisibleHintWeaver()";

    //********************************************Join Points+Pointcuts************************************************

    /**
     * 切入点 -- 被{@link org.alee.dokodemo.door.annotation.Node}注解 标记的类的构造函数执行时
     */
    @Pointcut("execution(" + CLASS_NAME_FRAGMENT + "+.new()) && " + METHOD_NAME_NODE)
    public void constructWeaver() {
    }

    /**
     * 切入点 -- 被{@link org.alee.dokodemo.door.annotation.Node}注解 标记的类的onAttach函数执行时
     */
    @Pointcut("execution(* " + CLASS_NAME_FRAGMENT + "+.onAttach(..)) && " + METHOD_NAME_NODE)
    public void onAttachWeaver() {
    }

    /**
     * 切入点 -- 被{@link org.alee.dokodemo.door.annotation.Node}注解 标记的类的onCreate函数执行时
     */
    @Pointcut("execution(* " + CLASS_NAME_FRAGMENT + "+.onCreate(..)) && " + METHOD_NAME_NODE)
    public void onCreateWeaver() {
    }

    /**
     * 切入点 -- 被{@link org.alee.dokodemo.door.annotation.Node}注解 标记的类的onViewCreated函数执行时
     */
    @Pointcut("call(* " + CLASS_NAME_FRAGMENT + "+.onViewCreated(..)) && " + METHOD_NAME_NODE)
    public void onViewCreatedWeaver() {
    }

    /**
     * 切入点 -- 被{@link org.alee.dokodemo.door.annotation.Node}注解 标记的类的onCreateView函数执行时
     */
    @Pointcut("execution(* " + CLASS_NAME_FRAGMENT + "+.onCreateView(..)) && " + METHOD_NAME_NODE)
    public void onCreateViewWeaver() {
    }

    /**
     * 切入点 -- 被{@link org.alee.dokodemo.door.annotation.Node}注解 标记的类的onResume函数执行时
     */
    @Pointcut("execution(* " + CLASS_NAME_FRAGMENT + "+.onResume(..)) && " + METHOD_NAME_NODE)
    public void onResumeWeaver() {
    }

    /**
     * 切入点 -- 被{@link org.alee.dokodemo.door.annotation.Node}注解 标记的类的onSaveInstanceState函数执行时
     */
    @Pointcut("execution(* " + CLASS_NAME_FRAGMENT + "+.onSaveInstanceState(..)) && " + METHOD_NAME_NODE)
    public void onSaveInstanceStateWeaver() {
    }

    /**
     * 切入点 -- 被{@link org.alee.dokodemo.door.annotation.Node}注解 标记的类的onDestroy函数执行时
     */
    @Pointcut("execution(* " + CLASS_NAME_FRAGMENT + "+.onDestroy(..)) && " + METHOD_NAME_NODE)
    public void onDestroyWeaver() {
    }

    /**
     * 切入点 -- 被{@link org.alee.dokodemo.door.annotation.Node}注解 标记的类的onDetach函数执行时
     */
    @Pointcut("execution(* " + CLASS_NAME_FRAGMENT + "+.onDetach(..)) && " + METHOD_NAME_NODE)
    public void onDetachWeaver() {
    }

    /**
     * 切入点 -- 被{@link org.alee.dokodemo.door.annotation.Node}注解 标记的类的setUserVisibleHint函数执行时
     */
    @Pointcut("execution(* " + CLASS_NAME_FRAGMENT + "+.setUserVisibleHint(..)) && " + METHOD_NAME_NODE)
    public void setUserVisibleHintWeaver() {
    }

    //********************************************Advice************************************************

    @Around(METHOD_NAME_CONSTRUCT)
    public Object constructProcess(ProceedingJoinPoint joinPoint) throws Throwable {
        Method onAttach = getLauncherMethod(CONSTRUCTOR, Object.class);
        onAttach.invoke(getLauncherInstance(), joinPoint.getTarget());
        return joinPoint.proceed();
    }

    @Around(METHOD_NAME_ON_ATTACH)
    public Object onAttachProcess(ProceedingJoinPoint joinPoint) throws Throwable {
        Method onAttach = getLauncherMethod(ATTACH, Object.class, Context.class);
        onAttach.invoke(getLauncherInstance(), joinPoint.getTarget(), joinPoint.getArgs()[0]);
        return joinPoint.proceed();
    }

    @Around(METHOD_NAME_ON_CREATE)
    public Object onCreateProcess(ProceedingJoinPoint joinPoint) throws Throwable {
        Method onCreate = getLauncherMethod(CREATE, Object.class, Bundle.class);
        onCreate.invoke(getLauncherInstance(), joinPoint.getTarget(), joinPoint.getArgs()[0]);
        return joinPoint.proceed();
    }

    @Around(METHOD_NAME_ON_CREATE_VIEW)
    public Object onCreateViewProcess(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        Object[] args = joinPoint.getArgs();
        Method onCreate = getLauncherMethod(CREATE_VIEW, Object.class, LayoutInflater.class, ViewGroup.class,
                Bundle.class, View.class);
        Object riggerResult = onCreate.invoke(getLauncherInstance(), joinPoint.getTarget(), args[0], args[1], args[2], result);
        return null == riggerResult ? result : riggerResult;
    }

    @After(METHOD_NAME_ON_VIEW_CREATED)
    public void onViewCreatedProcess(JoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Method onCreate = getLauncherMethod(VIEW_CREATED, Object.class, View.class, Bundle.class);
        onCreate.invoke(getLauncherInstance(), joinPoint.getTarget(), args[0], args[1]);
    }

    @Around(METHOD_NAME_ON_RESUME)
    public Object onResumeProcess(ProceedingJoinPoint joinPoint) throws Throwable {
        Method onPause = getLauncherMethod(RESUME, Object.class);
        onPause.invoke(getLauncherInstance(), joinPoint.getTarget());
        return joinPoint.proceed();
    }

    @Around(METHOD_NAME_ON_SAVE_INSTANCE_STATE)
    public Object onSaveInstanceStateProcess(ProceedingJoinPoint joinPoint) throws Throwable {
        Method onSaveInstanceState = getLauncherMethod(SAVE_INSTANCE_STATE, Object.class, Bundle.class);
        onSaveInstanceState.invoke(getLauncherInstance(), joinPoint.getTarget(), joinPoint.getArgs()[0]);
        return joinPoint.proceed();
    }

    @Around(METHOD_NAME_ON_DESTROY)
    public Object onDestroyProcess(ProceedingJoinPoint joinPoint) throws Throwable {
        Method onDestroy = getLauncherMethod(DESTROY, Object.class);
        onDestroy.invoke(getLauncherInstance(), joinPoint.getTarget());
        return joinPoint.proceed();
    }

    @Around(METHOD_NAME_ON_DETACH)
    public Object onDetachProcess(ProceedingJoinPoint joinPoint) throws Throwable {
        Method onDestroy = getLauncherMethod(DETACH, Object.class);
        onDestroy.invoke(getLauncherInstance(), joinPoint.getTarget());
        return joinPoint.proceed();
    }

    @Around(METHOD_NAME_SET_USER_VISIBLE_HINT)
    public Object setUserVisibleHintProcess(ProceedingJoinPoint joinPoint) throws Throwable {
        Method onDestroy = getLauncherMethod(SET_USER_VISIBLE_HINT, Object.class, boolean.class);
        onDestroy.invoke(getLauncherInstance(), joinPoint.getTarget(), joinPoint.getArgs()[0]);
        return joinPoint.proceed();
    }
}
