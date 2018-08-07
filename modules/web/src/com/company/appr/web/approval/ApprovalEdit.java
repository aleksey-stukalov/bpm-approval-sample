package com.company.appr.web.approval;

import com.company.appr.entity.Approval;
import com.haulmont.bali.util.ParamsMap;
import com.haulmont.bpm.entity.ProcActor;
import com.haulmont.bpm.entity.ProcInstance;
import com.haulmont.bpm.gui.action.StartProcessAction;
import com.haulmont.bpm.gui.procactions.ProcActionsFrame;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.*;
import com.haulmont.cuba.gui.components.actions.AddAction;
import com.haulmont.cuba.gui.data.Datasource;
import com.haulmont.cuba.security.entity.User;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;
import javax.inject.Named;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ApprovalEdit extends AbstractEditor<Approval> {

    @Inject
    private UserSession userSession;

    @Inject
    private ProcActionsFrame procActionsFrame;

    @Inject
    private Datasource<Approval> approvalDs;

    @Named("fieldGroup.type")
    private LookupField typeField;

    @Named("fieldGroup.dueDate")
    private DateField dueDateField;

    @Named("approversTable.add")
    private AddAction approversTableAdd;

    @Inject
    private Table<User> approversTable;

    @Inject
    private LinkButton displayProcessJournalBtn;

    private static final String APPROVAL_PROCESS_CODE = "approval_process";
    private static final String INITIATOR_ROLE_CODE = "initiator";
    private static final String APPROVER_ROLE_CODE = "approver";

    @Override
    protected void initNewItem(Approval item) {
        super.initNewItem(item);
        item.setInitiator(userSession.getCurrentOrSubstitutedUser());
    }

    @Override
    public void ready() {
        super.ready();
        approversTableAdd.setWindowId("approver-user.lookup");
        initProcActionsFrame();
        initUiComponentsAccess();
    }

    /**
     * When there is an active process some components must be disabled
     */
    private void initUiComponentsAccess() {
        boolean processIsActive = procActionsFrame.getProcInstance() != null &&
                procActionsFrame.getProcInstance().getActive();
        typeField.setEditable(!processIsActive);
        dueDateField.setEditable(!processIsActive);
        approversTable.getActions().forEach(action -> action.setEnabled(!processIsActive));
        displayProcessJournalBtn.setEnabled(processIsActive);
    }

    private void initProcActionsFrame() {
        procActionsFrame.initializer()
                .standard()
                .setTaskInfoEnabled(false)
                .setStartProcessActionProcessVariablesSupplier(() -> {
                    //returns a list of process variables that will be passed on process start
                    Map<String, Object> processVariables = new HashMap<>();
                    processVariables.put("dueDate", getItem().getDueDate() != null ?
                            toISO8601UTC(getItem().getDueDate()) :
                            null);
                    processVariables.put("approvalType", getItem().getType().getId());
                    return processVariables;
                })
                .setBeforeStartProcessPredicate(() -> {
                    //custom predicate validates that approvers are selected and fills the collections of ProcessActor
                    //when the process is started
                    if (!validateProcessActors()) {
                        showNotification(getMessage("selectApprovers"), NotificationType.WARNING);
                        return false;
                    }
                    if (commit()) {
                        createProcActors();
                        return true;
                    } else {
                        return false;
                    }
                })
                //custom listener in addition to the standard behavior refreshes the main datasource, because the process
                //automatically updates the "processState" field and initialize fields visibility because some UI
                //components mus be hidden after the process is started
                .setAfterStartProcessListener(() -> {
                    showNotification(messages.getMessage(ProcActionsFrame.class, "processStarted"));
                    initProcActionsFrame();
                    initUiComponentsAccess();
                    approvalDs.refresh();
                })
                //custom listener in addition to the standard behavior refreshes the main datasource, because the process
                //automatically updates the "processState" field
                .setAfterCompleteTaskListener(() -> {
                    showNotification(messages.getMessage(ProcActionsFrame.class, "taskCompleted"));
                    initProcActionsFrame();
                    approvalDs.refresh();
                })
                .init(APPROVAL_PROCESS_CODE, getItem());

        StartProcessAction startProcessAction = procActionsFrame.getStartProcessAction();
        if (startProcessAction != null) {
            startProcessAction.setCaption(getMessage("startApproval"));
        }
    }

    private String toISO8601UTC(Date date) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        df.setTimeZone(tz);
        return df.format(date);
    }

    private boolean validateProcessActors() {
        return getItem().getApprovers() != null && !getItem().getApprovers().isEmpty();
    }

    private void createProcActors() {
        ProcInstance procInstance = procActionsFrame.getProcInstance();

        Set<ProcActor> procActors = new ProcActorsBuilder(procInstance)
                .addActor(INITIATOR_ROLE_CODE, getItem().getInitiator())
                .addActors(APPROVER_ROLE_CODE, getItem().getApprovers())
                .build();

        procInstance.setProcActors(procActors);
    }

    public void displayProcessJournal() {
        openWindow("process-journal",
                WindowManager.OpenType.DIALOG,
                ParamsMap.of("procInstance", procActionsFrame.getProcInstance()));
    }
}