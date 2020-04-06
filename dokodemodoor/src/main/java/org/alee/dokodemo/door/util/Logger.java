package org.alee.dokodemo.door.util;

import android.util.Log;

import org.alee.dokodemo.door.template.ILogger;

/**********************************************************
 *
 * @author: MingYu.Liu
 * @date: 2020/4/4
 * @description: 默认的Log 工具
 *
 *********************************************************/
public class Logger implements ILogger {
    /**
     * 是否可以打印log
     */
    private boolean mLogEnable = true;

    @Override
    public void logEnable(boolean enable) {
        mLogEnable = enable;
    }

    @Override
    public void debug(String tag, String message) {
        if (!mLogEnable) {
            return;
        }
        Log.d(tag, message);
    }

    @Override
    public void info(String tag, String message) {
        if (!mLogEnable) {
            return;
        }
        Log.i(tag, message);
    }

    @Override
    public void verbose(String tag, String message) {
        if (!mLogEnable) {
            return;
        }
        Log.v(tag, message);
    }

    @Override
    public void warning(String tag, String message) {
        if (!mLogEnable) {
            return;
        }
        Log.w(tag, message);
    }

    @Override
    public void error(String tag, String message) {
        if (!mLogEnable) {
            return;
        }
        Log.e(tag, message);
    }
}
