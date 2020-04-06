package org.alee.dokodemo.door.annotation;


import androidx.annotation.AnimRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**********************************************************
 *
 * @author: MingYu.Liu
 * @date: 2020/4/4
 * @description: fragment 转场动画注解
 *
 *********************************************************/
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Animation {
    /**
     * 打开的动画，主要是对于新加进来的Fragment执行动画
     *
     * @return 动画资源id
     */
    @AnimRes
    int enterAnim() default 0;

    /**
     * 退出动画，主要是对于关闭Fragment执行动画。需要注意的是当调用replace的时候，前一个Fragment也会执行退出动画。
     *
     * @return 动画资源id
     */
    @AnimRes
    int exitAnim() default 0;

    /**
     * 当调用addToBackStack()方法后，当前的Fragment退出返回上一个Fragment，上一个Fragment就是popEnter，这时候它就会执行这个动画。这个动画对于新创建的Fragment无效。
     *
     * @return 动画资源id
     */
    @AnimRes
    int popEnterAnim() default 0;

    /**
     * 当前Fragment在back stack中，并且出栈的时候会执行popExit动画。
     *
     * @return 动画资源id
     */
    @AnimRes
    int popExitAnim() default 0;


}
