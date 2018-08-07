package com.company.appr.core.process.delegate;

import com.company.appr.service.ProcessEmailService;
import com.haulmont.cuba.core.global.AppBeans;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;

import java.util.UUID;

/**
 * Java delegate that is used for sending email notification from the process.
 */
public class EmailSendingDelegate implements JavaDelegate {

    protected Expression templateName;

    protected Expression procRoleCode;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String templateNameValue = (String) templateName.getValue(execution);
        String procRoleCodeValue = (String) procRoleCode.getValue(execution);
        UUID bpmProcInstanceId = (UUID) execution.getVariable("bpmProcInstanceId");
        AppBeans.get(ProcessEmailService.class).sendEmails(bpmProcInstanceId, procRoleCodeValue, templateNameValue);
    }
}
