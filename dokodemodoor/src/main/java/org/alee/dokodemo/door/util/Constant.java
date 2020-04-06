package org.alee.dokodemo.door.util;

import android.view.View;

import org.alee.dokodemo.door.annotation.Animation;

/**********************************************************
 *
 * @author: MingYu.Liu
 * @date: 2020/4/4
 * @description: 常量类
 *
 *********************************************************/
public class Constant {
    /**
     * 任意门 node 注解 全路径+类名
     */
    public static final String DOKODEMO_DOOR_ANNOTATION_NODE = "org.alee.dokodemo.door.annotation.Node";
    /**
     * v7 FragmentActivity  全路径+类名
     */
    public static final String CLASS_NAME_FRAGMENT_ACTIVITY = "android.support.v4.app.FragmentActivity";

    /**
     * v7 Fragment  全路径+类名
     */
    public static final String CLASS_NAME_FRAGMENT = "android.support.v4.app.Fragment";


    public static class MethodName {

        public static class Support {
            /**
             * 此方法用于获取容器id
             * 此方法必须使用空参数定义，返回值必须为Integer类型
             * 例如：public Integer getContainerViewId（）
             */
            public static final String GET_CONTAINER_VIEW_ID = "getContainerViewId";
            /**
             * 此方法用作标识是否阻断onBackPressed()向子节点继续传递
             * 此方法必须使用空参数定义，返回值必须为Boolean类型
             * 例如：public Boolean onInterruptBackPressed（）
             */
            public static final String ON_INTERRUPT_BACK_PRESSED = "onInterceptBackPressed";
            /**
             * 此方法用于自定义Fragment标签
             * 此方法必须使用空参数定义,返回值必须为String 类型
             * 例如：public String getFragmentTag()
             */
            public static final String GET_FRAGMENT_TAG = "getFragmentTag";

            /**
             * 此方法用于返回Fragment转场动画
             * 此方法必须使用空参数定义,返回值必须为int[]类型,数组大小不能小于4
             * [0] :{@link Animation#enterAnim()}
             * [1] :{@link Animation#exitAnim()}
             * [2] :{@link Animation#popEnterAnim()}
             * [3] :{@link Animation#popExitAnim()}
             * <p>
             * 例如：public int[] getNodeAnimations()
             */
            public static final String GET_NODE_ANIMATIONS = "getNodeAnimations";

            /**
             * 此方法用于懒加载创建view
             * 此方法必须使用Bundle定义参数,返回值不做限制
             * 例如：public void onLazyLoadViewCreated(Bundle savedInstanceState)
             */
            public static final String ON_LAZYLOAD_VIEW_CREATED = "onLazyLoadViewCreated";

            /**
             * 此方法用作标识是否阻断onBackPressed()向父节点继续传递
             * 此方法必须使用空参数定义，返回值必须为Boolean类型
             * 例如：public Boolean onNodeBackPressed()
             */
            public static final String ON_NODE_BACKPRESSED = "onNodeBackPressed";

            /**
             * 此方法用于接受一个返回结果
             * 此方法必须使用int,int,Bundle定义参数,返回值不做限制
             * 例如:public void onFragmentResult(int requestCode,int resultCode,Bundle args)
             */
            public static final String ON_FRAGMENT_RESULT = "onFragmentResult";

        }

        public static class Launcher {
            /**
             * DokodemoDoor 私有方法名 - onConstructor
             */
            public static final String CONSTRUCTOR = "onConstructor";

            /**
             * DokodemoDoor 私有方法名 - onCreate
             */
            public static final String CREATE = "onCreate";

            /**
             * DokodemoDoor 私有方法名 - onResumeFragments
             */
            public static final String RESUME_FRAGMENTS = "onResumeFragments";

            /**
             * DokodemoDoor 私有方法名 - onPause
             */
            public static final String PAUSE = "onPause";

            /**
             * DokodemoDoor 私有方法名 - onResume
             */
            public static final String RESUME = "onResume";

            /**
             * DokodemoDoor 私有方法名 - onSaveInstanceState
             */
            public static final String SAVE_INSTANCE_STATE = "onSaveInstanceState";

            /**
             * DokodemoDoor 私有方法名 - onDestroy
             */
            public static final String DESTROY = "onDestroy";

            /**
             * DokodemoDoor 私有方法名 - onBackPressed
             */
            public static final String BACK_PRESSED = "onBackPressed";

            /**
             * DokodemoDoor 私有方法名 - onAttach
             */
            public static final String ATTACH = "onAttach";

            /**
             * DokodemoDoor 私有方法名 - onCreateView
             */
            public static final String CREATE_VIEW = "onCreateView";

            /**
             * DokodemoDoor 私有方法名 - onViewCreated
             */
            public static final String VIEW_CREATED = "onViewCreated";

            /**
             * DokodemoDoor 私有方法名 - onDetach
             */
            public static final String DETACH = "onDetach";
            /**
             * DokodemoDoor 私有方法名 - setUserVisibleHint
             */
            public static final String SET_USER_VISIBLE_HINT = "setUserVisibleHint";


        }
    }
}
