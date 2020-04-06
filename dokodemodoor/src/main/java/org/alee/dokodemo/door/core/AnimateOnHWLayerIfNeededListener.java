package org.alee.dokodemo.door.core;

import android.view.View;
import android.view.animation.Animation;

import static org.alee.dokodemo.door.core.BaseProxy.shouldRunOnHWLayer;

/**********************************************************
 *
 * @author: MingYu.Liu
 * @date: 2020/4/5
 * @description: xxxx
 *
 *********************************************************/
class AnimateOnHWLayerIfNeededListener implements Animation.AnimationListener {
    private boolean mShouldRunOnHWLayer = false;
    private View mView;

    AnimateOnHWLayerIfNeededListener(final View v, Animation anim) {
        if (null == v || null == anim) {
            return;
        }
        mView = v;
    }

    @Override
    public void onAnimationStart(Animation animation) {
        mShouldRunOnHWLayer = shouldRunOnHWLayer(mView, animation);
        if (mShouldRunOnHWLayer) {
            mView.post(new Runnable() {
                @Override
                public void run() {
                    mView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                }
            });
        }
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (mShouldRunOnHWLayer) {
            mView.post(new Runnable() {
                @Override
                public void run() {
                    mView.setLayerType(View.LAYER_TYPE_NONE, null);
                }
            });
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
