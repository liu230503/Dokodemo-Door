package org.alee.dokodemo.door.aop;

import android.os.Bundle;

import org.alee.dokodemo.door.core.DokodemoDoor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.reflect.Method;

import static org.alee.dokodemo.door.util.Constant.CLASS_NAME_FRAGMENT_ACTIVITY;
import static org.alee.dokodemo.door.util.Constant.MethodName.Launcher.BACK_PRESSED;
import static org.alee.dokodemo.door.util.Constant.MethodName.Launcher.CONSTRUCTOR;
import static org.alee.dokodemo.door.util.Constant.MethodName.Launcher.CREATE;
import static org.alee.dokodemo.door.util.Constant.MethodName.Launcher.DESTROY;
import static org.alee.dokodemo.door.util.Constant.MethodName.Launcher.PAUSE;
import static org.alee.dokodemo.door.util.Constant.MethodName.Launcher.RESUME;
import static org.alee.dokodemo.door.util.Constant.MethodName.Launcher.RESUME_FRAGMENTS;
import static org.alee.dokodemo.door.util.Constant.MethodName.Launcher.SAVE_INSTANCE_STATE;

/**********************************************************
 *
 * @author: MingYu.Liu
 * @date: 2020/4/5
 * @description: 通过 AspectJ 实现AOP思想，此类用于将{@link DokodemoDoor}编织到Activity的生命周期和其他方法中。
 *
 *********************************************************/
@Aspect
public class ActivityWeaver extends BaseWeaver {
    /**
     * 方法名- constructWeaver
     */
    private static final String METHOD_NAME_CONSTRUCT = "constructWeaver()";
    /**
     * 方法名- onCreateWeaver
     */
    private static final String METHOD_NAME_ON_CREATE = "onCreateWeaver()";
    /**
     * 方法名- onResumeFragmentsWeaver
     */
    private static final String METHOD_NAME_ON_RESUME_FRAGMENTS = "onResumeFragmentsWeaver()";
    /**
     * 方法名- onPauseWeaver
     */
    private static final String METHOD_NAME_ON_PAUSE = "onPauseWeaver()";
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
     * 方法名- onBackPressedWeaver
     */
    private static final String METHOD_NAME_ON_BACK_PRESSED = "onBackPressedWeaver()";

    //********************************************Join Points+Pointcuts************************************************

    /**
     * 切入点 -- 被{@link org.alee.dokodemo.door.annotation.Node}注解 标记的类的构造函数执行时
     */
    @Pointcut("execution(" + CLASS_NAME_FRAGMENT_ACTIVITY + "+.new()) && " + METHOD_NAME_NODE)
    public void constructWeaver() {
    }

    /**
     * 切入点 -- 被{@link org.alee.dokodemo.door.annotation.Node}注解 标记的类的onCreate函数执行时
     */
    @Pointcut("execution(* " + CLASS_NAME_FRAGMENT_ACTIVITY + "+.onCreate(..)) && " + METHOD_NAME_NODE)
    public void onCreateWeaver() {
    }

    /**
     * 切入点 -- 被{@link org.alee.dokodemo.door.annotation.Node}注解 标记的类的onResumeFragments函数执行时
     */
    @Pointcut("execution(* " + CLASS_NAME_FRAGMENT_ACTIVITY + "+.onResumeFragments(..)) && " + METHOD_NAME_NODE)
    public void onResumeFragmentsWeaver() {
    }

    /**
     * 切入点 -- 被{@link org.alee.dokodemo.door.annotation.Node}注解 标记的类的onPause函数执行时
     */
    @Pointcut("execution(* " + CLASS_NAME_FRAGMENT_ACTIVITY + "+.onPause(..)) && " + METHOD_NAME_NODE)
    public void onPauseWeaver() {
    }

    /**
     * 切入点 -- 被{@link org.alee.dokodemo.door.annotation.Node}注解 标记的类的onResume函数执行时
     */
    @Pointcut("execution(* " + CLASS_NAME_FRAGMENT_ACTIVITY + "+.onResume(..)) && " + METHOD_NAME_NODE)
    public void onResumeWeaver() {
    }

    /**
     * 切入点 -- 被{@link org.alee.dokodemo.door.annotation.Node}注解 标记的类的onSaveInstanceState函数执行时
     */
    @Pointcut("execution(* " + CLASS_NAME_FRAGMENT_ACTIVITY + "+.onSaveInstanceState(..)) && " + METHOD_NAME_NODE)
    public void onSaveInstanceStateWeaver() {
    }

    /**
     * 切入点 -- 被{@link org.alee.dokodemo.door.annotation.Node}注解 标记的类的onDestroy函数执行时
     */
    @Pointcut("execution(* " + CLASS_NAME_FRAGMENT_ACTIVITY + "+.onDestroy(..)) && " + METHOD_NAME_NODE)
    public void onDestroyWeaver() {
    }

    /**
     * 切入点 -- 被{@link org.alee.dokodemo.door.annotation.Node}注解 标记的类的onBackPressed函数执行时
     */
    @Pointcut("execution(* " + CLASS_NAME_FRAGMENT_ACTIVITY + "+.onBackPressed(..)) && " + METHOD_NAME_NODE)
    public void onBackPressedWeaver() {
    }

    //********************************************Advice************************************************

    @Around(METHOD_NAME_CONSTRUCT)
    public Object constructProcess(ProceedingJoinPoint joinPoint) throws Throwable {
        Method onAttach = getLauncherMethod(CONSTRUCTOR, Object.class);
        onAttach.invoke(getLauncherInstance(), joinPoint.getTarget());
        return joinPoint.proceed();
    }

    @Around(METHOD_NAME_ON_CREATE)
    public Object onCreateProcess(ProceedingJoinPoint joinPoint) throws Throwable {
        Method onCreate = getLauncherMethod(CREATE, Object.class, Bundle.class);
        onCreate.invoke(getLauncherInstance(), joinPoint.getTarget(), joinPoint.getArgs()[0]);
        return joinPoint.proceed();
    }

    @Around(METHOD_NAME_ON_RESUME_FRAGMENTS)
    public Object onResumeFragmentsProcess(ProceedingJoinPoint joinPoint) throws Throwable {
        Method onResumeFragments = getLauncherMethod(RESUME_FRAGMENTS, Object.class);
        onResumeFragments.invoke(getLauncherInstance(), joinPoint.getTarget());
        return joinPoint.proceed();
    }

    @Around(METHOD_NAME_ON_PAUSE)
    public Object onPauseProcess(ProceedingJoinPoint joinPoint) throws Throwable {
        Method onPause = getLauncherMethod(PAUSE, Object.class);
        onPause.invoke(getLauncherInstance(), joinPoint.getTarget());
        return joinPoint.proceed();
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

    @Around(METHOD_NAME_ON_BACK_PRESSED)
    public Object onBackPressedProcess(ProceedingJoinPoint joinPoint) throws Throwable {
        Method onBackPressed = getLauncherMethod(BACK_PRESSED, Object.class);
        return onBackPressed.invoke(getLauncherInstance(), joinPoint.getTarget());
    }
}
