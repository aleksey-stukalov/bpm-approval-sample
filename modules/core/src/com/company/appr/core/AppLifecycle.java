package com.company.appr.core;

import com.company.appr.core.process.ProcModelDeployer;
import com.haulmont.cuba.core.sys.AppContext;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component("appr_AppLifecycle")
public class AppLifecycle implements AppContext.Listener {

    @Inject
    private ProcModelDeployer procModelDeployer;

    public AppLifecycle() {
        AppContext.addListener(this);
    }

    @Override
    public void applicationStarted() {
        procModelDeployer.deployAllModels();
    }

    @Override
    public void applicationStopped() {

    }
}
