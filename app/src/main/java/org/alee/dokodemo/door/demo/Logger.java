package org.alee.dokodemo.door.demo;

import org.alee.dokodemo.door.template.ILogger;
import org.lmy.open.utillibrary.LogUtil;

/**********************************************************
 *
 * @author: MingYu.Liu
 * @date: 2020/4/5
 * @description: xxxx
 *
 *********************************************************/
public class Logger implements ILogger {
    @Override
    public void logEnable(boolean enable) {

    }

    @Override
    public void debug(String tag, String message) {
        LogUtil.d(tag, message);
    }

    @Override
    public void info(String tag, String message) {
        LogUtil.i(tag, message);
    }

    @Override
    public void verbose(String tag, String message) {
        LogUtil.w(tag, message);
    }

    @Override
    public void warning(String tag, String message) {
        LogUtil.w(tag, message);
    }

    @Override
    public void error(String tag, String message) {
        LogUtil.e(tag, message);
    }
}
