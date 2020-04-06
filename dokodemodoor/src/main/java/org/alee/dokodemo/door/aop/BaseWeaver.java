package org.alee.dokodemo.door.aop;

import org.alee.dokodemo.door.core.DokodemoDoor;
import org.aspectj.lang.annotation.Pointcut;

import java.lang.reflect.Method;

import static org.alee.dokodemo.door.util.Constant.DOKODEMO_DOOR_ANNOTATION_NODE;

/**********************************************************
 *
 * @author: MingYu.Liu
 * @date: 2020/4/4
 * @description: 编织者基类
 *
 *********************************************************/
public class BaseWeaver {
    /**
     * 方法名---获取自身实例
     */
    private static final String METHOD_NAME_SINGLETON = "getInstance";

    /**
     * 任意门 node 注解 全路径+类名
     */
    protected static final String METHOD_NAME_NODE = "annotatedWithNode()";


    /**
     * 切入点--- 被{@link org.alee.dokodemo.door.annotation.Node}注解标记的所有类
     */
    @Pointcut("@target(" + DOKODEMO_DOOR_ANNOTATION_NODE + ")")
    public void annotatedWithNode() {
    }

    /**
     * 通过反射获取{@link DokodemoDoor}实例
     *
     * @return {@link DokodemoDoor}实例
     * @throws {@link Exception}
     */
    DokodemoDoor getLauncherInstance() throws Exception {
        Class<?> clazz = Class.forName(DokodemoDoor.class.getName());
        Method getInstance = clazz.getDeclaredMethod(METHOD_NAME_SINGLETON);
        getInstance.setAccessible(true);
        return (DokodemoDoor) getInstance.invoke(null);
    }


    /**
     * 通过反射获取{@link DokodemoDoor} 的方法对象
     *
     * @param methodName 函数名
     * @param params     参数列表
     * @return {@link Method}
     * @throws {@link Exception}
     */
    Method getLauncherMethod(String methodName, Class<?>... params) throws Exception {
        DokodemoDoor rigger = getLauncherInstance();
        Class<? extends DokodemoDoor> clazz = rigger.getClass();
        Method method = clazz.getDeclaredMethod(methodName, params);
        method.setAccessible(true);
        return method;
    }
}
