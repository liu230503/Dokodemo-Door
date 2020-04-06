package org.alee.dokodemo.door.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**********************************************************
 *
 * @author: MingYu.Liu
 * @date: 2020/4/4
 * @description: Fragment的 加载模式
 *
 *********************************************************/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoadModel {

    /**
     * Fragment 的加载模式 默认为普通加载
     *
     * @return 加载模式
     */
    EnumLoadModel loadModel() default EnumLoadModel.NORMAL_LOAD;
}
