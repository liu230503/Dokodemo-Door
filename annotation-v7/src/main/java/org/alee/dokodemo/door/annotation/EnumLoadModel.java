package org.alee.dokodemo.door.annotation;

/**********************************************************
 *
 * @author: MingYu.Liu
 * @date: 2020/4/4
 * @description: Fragment 加载模式
 *
 *********************************************************/
public enum EnumLoadModel {
    /**
     * 普通加载
     */
    NORMAL_LOAD(0x1000),
    /**
     * 懒加载
     */
    LAZY_LOAD(0x1001);
    /**
     * 索引
     */
    private int mIndex;

    public int getIndex() {
        return mIndex;
    }

    EnumLoadModel(int index) {
        mIndex = index;
    }

    /**
     * 获取加载模式
     *
     * @param index 索引
     * @return 结果
     */
    public static EnumLoadModel getLoadModelByIndex(int index) {
        for (EnumLoadModel model : EnumLoadModel.values()) {
            if (index == model.getIndex()) {
                return model;
            }
        }
        return EnumLoadModel.NORMAL_LOAD;
    }
}
