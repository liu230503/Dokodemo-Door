package org.alee.dokodemo.door.exception;

/**********************************************************
 *
 * @author: MingYu.Liu
 * @date: 2020/4/4
 * @description: xxxx
 *
 *********************************************************/
public class NotExistException extends DokodemoDoorException {

    public NotExistException(String fragmentTag) {
        super(fragmentTag + "在堆栈中未找到！");
    }
}
