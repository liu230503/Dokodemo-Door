package org.alee.dokodemo.door.demo.base;

import android.view.View;

/**********************************************************
 *
 * @author: MY.Liu
 * @date: 2019/8/9
 * @description: xxxx
 *
 *********************************************************/
public interface OnItemClickListener {
    /**
     * item点击事件回调
     *
     * @param view     item
     * @param position 位置
     */
    void onItemClick(View view, int position);

    /**
     * item长按事件回调
     *
     * @param view     item
     * @param position 位置
     */
    void onItemLongClick(View view, int position);
}
