package org.alee.dokodemo.door.annotation;

import android.support.annotation.IdRes;


import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**********************************************************
 *
 * @author: MingYu.Liu
 * @date: 2020/4/4
 * @description: 标记一个节点
 *
 *********************************************************/
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Node {
    /**
     * 可选参数，此处需要传入fragment在添加时候占用的控件的id，作为fragment在添加的时候代替的位置。
     * 如果是0的话，fragment将不会代替任何位置.默认值是0.
     *
     * @return 装在fragment 的layout id
     */
    @IdRes
    int containerViewId() default 0;

    /**
     * 如果值为true: 当被操纵的类(Activity/Fragment)中的栈底fragment和栈顶fragment是同一个元素并且该fragment被关闭的时候，
     * 这个寄主类也会被关闭，并且栈顶的fragment不会执行退出转场动画.
     * 如果值为false: 当寄主类内部的最后一个fragment出栈的时候，寄主类不会做任何动作.并且所有的转场动画都是正常执行的。
     *
     * @return true / false
     */
    boolean stickyStack() default false;
}
