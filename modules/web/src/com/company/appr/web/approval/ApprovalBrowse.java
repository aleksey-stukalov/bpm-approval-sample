package com.company.appr.web.approval;

import com.company.appr.entity.Approval;
import com.haulmont.cuba.gui.components.AbstractLookup;
import com.haulmont.cuba.gui.components.GroupTable;

import javax.inject.Inject;
import java.util.Map;

public class ApprovalBrowse extends AbstractLookup {

    @Inject
    private GroupTable<Approval> approvalsTable;

    @Override
    public void init(Map<String, Object> params) {
        super.init(params);
        approvalsTable.addStyleProvider((approval, property) -> {
            if (property == null) {
                //style for row
                if (approval.getProcessState() == null) {
                    return null;
                }
                switch (approval.getProcessState()) {
                    case "Approval": return "approval-approval";
                    case "Correction": return "approval-correction";
                    case "Approved": return "approval-approved";
                }
            }
            return null;
        });
    }
}