package org.alee.dokodemo.door.demo;

import android.app.Application;
import android.content.Context;

import org.alee.dokodemo.door.core.DokodemoDoor;
import org.lmy.open.utillibrary.UtilApplication;

/**********************************************************
 *
 * @author: MingYu.Liu
 * @date: 2020/4/5
 * @description: xxxx
 *
 *********************************************************/
public class App extends UtilApplication {

    @Override
    protected void initForMainProcess(Context context) {
        super.initForMainProcess(context);
        DokodemoDoor.setUseDebugModel(true);
        DokodemoDoor.setLogger(new Logger());
    }

    @Override
    public Class getAbnormalRestartActivity() {
        return null;
    }
}
