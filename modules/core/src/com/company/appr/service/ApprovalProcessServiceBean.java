package com.company.appr.service;

import com.company.appr.entity.ApprovalType;
import org.springframework.stereotype.Service;

@Service(ApprovalProcessService.NAME)
public class ApprovalProcessServiceBean implements ApprovalProcessService {

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