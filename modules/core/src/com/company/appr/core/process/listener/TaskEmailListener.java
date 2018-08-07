package com.company.appr.core.process.listener;

import com.company.appr.service.ProcessEmailService;
import com.haulmont.cuba.core.global.AppBeans;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.TaskListener;

import java.util.UUID;

/**
 * An alternative way of sending emails (task listener) - is not used in the current project
 */
@Deprecated
public class TaskEmailListener implements TaskListener {

    protected Expression templateName;

    protected Expression procRoleCode;


    @Override
    public void notify(DelegateTask delegateTask) {
        String templateNameValue = (String) templateName.getValue(delegateTask);
        String procRoleCodeValue = (String) procRoleCode.getValue(delegateTask);
        UUID bpmProcInstanceId = (UUID) delegateTask.getVariable("bpmProcInstanceId");

        AppBeans.get(ProcessEmailService.class).sendEmails(bpmProcInstanceId, procRoleCodeValue, templateNameValue);

    }
}
