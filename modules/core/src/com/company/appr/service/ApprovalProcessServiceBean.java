package com.company.appr.service;

import com.company.appr.entity.Approval;
import com.company.appr.entity.ApprovalType;
import com.haulmont.bpm.entity.ProcInstance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.View;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.UUID;

@Service(ApprovalProcessService.NAME)
public class ApprovalProcessServiceBean implements ApprovalProcessService {

    @Inject
    private Persistence persistence;

    @Inject
    private Metadata metadata;

    @Override
    public boolean isApprovalTaskCompleted(int nrOfInstances, int nrOfCompletedInstances, String approvalTypeStr) {
        ApprovalType approvalType = ApprovalType.fromId(approvalTypeStr);
        if (approvalType == null) throw new RuntimeException("Unknown approval type " + approvalTypeStr);
        switch (approvalType) {
            case ALL:
                return nrOfCompletedInstances == nrOfInstances;
            case ANYONE:
                return nrOfCompletedInstances >= 1;
            case HALF_PLUS_1:
                return nrOfCompletedInstances / nrOfInstances > 0.5;
            default:
                throw new RuntimeException("Unknown approval type " + approvalTypeStr);
        }
    }
}