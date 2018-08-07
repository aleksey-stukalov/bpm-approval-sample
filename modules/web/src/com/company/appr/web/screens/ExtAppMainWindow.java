package com.company.appr.web.screens;

import com.company.appr.entity.Approval;
import com.haulmont.bali.util.ParamsMap;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.core.global.LoadContext;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.gui.WindowManager;
import com.haulmont.cuba.gui.components.AbstractMainWindow;
import com.haulmont.cuba.gui.components.Embedded;
import com.haulmont.cuba.gui.components.Label;
import com.haulmont.cuba.gui.components.mainwindow.FtsField;
import com.haulmont.cuba.gui.components.mainwindow.SideMenu;
import com.haulmont.cuba.security.global.UserSession;

import javax.inject.Inject;
import java.util.Map;

public class ExtAppMainWindow extends AbstractMainWindow {
    @Inject
    private FtsField ftsField;

    @Inject
    private Embedded logoImage;

    @Inject
    private SideMenu sideMenu;

    @Inject
    private Label requireActionCountLabel;

    @Inject
    private Label allApprovalsCountLabel;

    @Inject
    private DataManager dataManager;

    @Inject
    private UserSession userSession;

    @Inject
    private Metadata metadata;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        sideMenu.requestFocus();
        initLayoutAnalyzerContextMenu(logoImage);
        initLogoImage(logoImage);
        initFtsField(ftsField);
        refreshApprovalsCountLabels();
    }

    public void refreshApprovalsCountLabels() {
        requireActionCountLabel.setValue(getRequireActionApprovalsCount());
        allApprovalsCountLabel.setValue(getAllAvailableApprovalsCount());
    }

    private long getRequireActionApprovalsCount() {
        LoadContext<Approval> ctx = LoadContext.create(Approval.class)
                .setQuery(LoadContext.createQuery("select e from appr$Approval e " +
                        "join bpm$ProcInstance pi on pi.entity.entityId = e.id " +
                        "join pi.procTasks pt " +
                        "where pt.procActor.user.id = :userId and pt.endDate is null")
                        .setParameter("userId", userSession.getCurrentOrSubstitutedUser()));
        return dataManager.getCount(ctx);
    }

    private long getAllAvailableApprovalsCount() {
        LoadContext<Approval> ctx = LoadContext.create(Approval.class)
                .setQuery(LoadContext.createQuery("select distinct e from appr$Approval e " +
                        "left join e.approvers a " +
                        "where e.initiator.id = :userId " +
                        "or a.id = :userId")
                        .setParameter("userId", userSession.getCurrentOrSubstitutedUser()));
        return dataManager.getCount(ctx);
    }

    public void openRequireActionApprovals() {
        openWindow("appr$Approval.browse", WindowManager.OpenType.NEW_TAB, ParamsMap.of("filter", "APPROVAL_REQUIRE_PROCESS_ACTION"));
    }

    public void openAllApprovals() {
        openWindow("appr$Approval.browse", WindowManager.OpenType.NEW_TAB);
    }

    public void createApproval() {
        openEditor(metadata.create(Approval.class), WindowManager.OpenType.NEW_TAB);
    }

}