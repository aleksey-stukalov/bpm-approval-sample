package com.company.appr.web.processjournal;

import com.haulmont.bpm.entity.ProcInstance;
import com.haulmont.bpm.entity.ProcTask;
import com.haulmont.cuba.gui.WindowParam;
import com.haulmont.cuba.gui.components.AbstractWindow;
import com.haulmont.cuba.gui.components.Table;
import com.haulmont.cuba.gui.data.CollectionDatasource;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProcessJournal extends AbstractWindow {

    @WindowParam
    private ProcInstance procInstance;

    @Inject
    private CollectionDatasource<ProcTask, UUID> procTasksDs;

    @Inject
    private Table<ProcTask> procTasksTable;

    @Override
    public void ready() {
        Map<String, Object> params = new HashMap<>();
        if (procInstance != null)
            params.put("procInstance", procInstance);
        procTasksDs.refresh(params);

        procTasksTable.addGeneratedColumn("locOutcome", entity -> {
            String locOutcome = entity.getLocOutcome();
            if ("completedAutomatically".equals(locOutcome)) {
                return new Table.PlainTextCell(getMessage("completedAutomatically"));
            } else {
                return new Table.PlainTextCell(locOutcome);
            }
        });
    }
}
