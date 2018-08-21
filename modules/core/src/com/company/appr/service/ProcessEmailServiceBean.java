package com.company.appr.service;

import com.google.common.base.Strings;
import com.haulmont.bpm.entity.ProcActor;
import com.haulmont.bpm.entity.ProcInstance;
import com.haulmont.chile.core.model.MetaClass;
import com.haulmont.cuba.core.EntityManager;
import com.haulmont.cuba.core.Persistence;
import com.haulmont.cuba.core.Transaction;
import com.haulmont.cuba.core.app.EmailService;
import com.haulmont.cuba.core.entity.Entity;
import com.haulmont.cuba.core.global.*;
import com.haulmont.cuba.security.entity.User;
import groovy.lang.Binding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service(ProcessEmailService.NAME)
public class ProcessEmailServiceBean implements ProcessEmailService {

    private static Logger log = LoggerFactory.getLogger(ProcessEmailServiceBean.class);

    @Inject
    private EmailService emailService;

    @Inject
    private Scripting scripting;

    @Inject
    private GlobalConfig globalConfig;

    @Inject
    private Persistence persistence;

    @Inject
    private Metadata metadata;

    private static final String TEMPLATES_BASE_PATH = "com/company/appr/core/process/template/";

    @Override
    public void sendEmails(UUID bpmProcInstanceId, String procRoleCode, String emailTemplateName) {
        ProcInstance procInstance;
        try (Transaction tx = persistence.getTransaction()) {
            EntityManager em = persistence.getEntityManager();
            procInstance = em.find(ProcInstance.class, bpmProcInstanceId,"procInstance-send-email" );
            tx.commit();
        }

        if (procInstance == null) {
            log.error("ProcInstance {} not found", bpmProcInstanceId);
            return;
        }

        List<User> users = procInstance.getProcActors().stream()
                .filter(procActor -> procRoleCode.equals(procActor.getProcRole().getCode()))
                .map(ProcActor::getUser)
                .collect(Collectors.toList());

        String editorLink = createEditorLink(procInstance.getEntityName(), procInstance.getEntity().getEntityId());

        for (User user : users) {
            String emailAddress = user.getEmail();
            if (Strings.isNullOrEmpty(emailAddress)) {
                log.warn("User {} doesn't have an email address", user);
                continue;
            }
            Binding binding = new Binding();
            binding.setVariable("user", user);
            binding.setVariable("editorLink", editorLink);
            binding.setVariable("entity", findRelatedEntity(bpmProcInstanceId));
            scripting.runGroovyScript(TEMPLATES_BASE_PATH + emailTemplateName, binding);
            String emailTitle = binding.getVariable("title").toString();
            String emailBody = binding.getVariable("body").toString();
            emailService.sendEmailAsync(new EmailInfo(emailAddress, emailTitle, emailBody, "text/html;charset=UTF-8"));
        }
    }

    private String createEditorLink(String entityName, UUID entityId) {
        return globalConfig.getWebAppUrl() + "/open?" + "screen=" + entityName + ".edit" +
                "&item=" + entityName + "-" + entityId;
    }

    private Entity findRelatedEntity(UUID bpmProcInstanceId) {
        try (Transaction tx = persistence.getTransaction()) {
            EntityManager em = persistence.getEntityManager();
            ProcInstance procInstance = em.find(ProcInstance.class, bpmProcInstanceId, View.LOCAL);
            if (procInstance == null) {
                throw new RuntimeException("ProcInstance not found " + bpmProcInstanceId);
            }
            String entityName = procInstance.getEntityName();
            Object entityId = procInstance.getEntity().getObjectEntityId();

            Class<Entity> clazz = metadata.getClassNN(entityName).getJavaClass();
            Entity result = em.find(clazz, entityId, View.LOCAL);

            tx.commit();
            return result;
        }
    }

}