package org.alee.dokodemo.door.template;

/**********************************************************
 *
 * @author: MingYu.Liu
 * @date: 2020/4/4
 * @description: Logger
 *
 *********************************************************/
public interface ILogger {
    /**
     * 设置log 功能是否可用
     *
     * @param enable 是否可以输出log
     */
    void logEnable(boolean enable);

    /**
     * 打印debug级别LOG
     *
     * @param tag     标签
     * @param message 内容
     */
    void debug(String tag, String message);

    /**
     * 打印info级别LOG
     *
     * @param tag     标签
     * @param message 内容
     */
    void info(String tag, String message);

    /**
     * 打印verbose级别LOG
     *
     * @param tag     标签
     * @param message 内容
     */
    void verbose(String tag, String message);

    /**
     * 打印warning级别LOG
     *
     * @param tag     标签
     * @param message 内容
     */
    void warning(String tag, String message);

    /**
     * 打印error级别LOG
     *
     * @param tag     标签
     * @param message 内容
     */
    void error(String tag, String message);

}
