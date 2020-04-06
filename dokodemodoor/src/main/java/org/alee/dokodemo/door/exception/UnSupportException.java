package org.alee.dokodemo.door.exception;

/**********************************************************
 *
 * @author: MingYu.Liu
 * @date: 2020/4/4
 * @description: xxxx
 *
 *********************************************************/
public class UnSupportException extends DokodemoDoorException {
    public UnSupportException(String message) {
        super(" [" + message + "] 是不被支持的操作,请检查你的代码");
    }
}
