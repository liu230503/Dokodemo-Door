package org.alee.dokodemo.door.exception;

/**********************************************************
 *
 * @author: MingYu.Liu
 * @date: 2020/4/4
 * @description: xxxx
 *
 *********************************************************/
public class AlreadyExistException extends DokodemoDoorException {
    public AlreadyExistException(String fragmentTag) {
        super(fragmentTag + " 已经被添加至堆栈中！");
    }
}
