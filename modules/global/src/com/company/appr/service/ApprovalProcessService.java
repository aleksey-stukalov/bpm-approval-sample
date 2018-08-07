package com.company.appr.service;


import com.haulmont.cuba.core.entity.Entity;

import java.util.UUID;

public interface ApprovalProcessService {
    String NAME = "appr_ApprovalProcessService";

    boolean isApprovalTaskCompleted(int nrOfInstances, int nrOfCompletedInstances, String approvalTypeStr);
}