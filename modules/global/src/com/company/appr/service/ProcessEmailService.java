package com.company.appr.service;


import java.util.UUID;

public interface ProcessEmailService {
    String NAME = "appr_ProcessEmailService";

    /**
     * Method sends process related emails.
     *
     * @param bpmProcInstanceId - identifier of the ProcInstance
     * @param procRoleCode - all actors with the given process code will receive an email
     * @param emailTemplateName - a groovy script that is used for email title and dody generating
     */
    void sendEmails(UUID bpmProcInstanceId, String procRoleCode, String emailTemplateName);
}