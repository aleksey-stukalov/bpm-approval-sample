package com.company.appr.web.approval;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.haulmont.bpm.entity.ProcActor;
import com.haulmont.bpm.entity.ProcInstance;
import com.haulmont.bpm.entity.ProcRole;
import com.haulmont.bpm.service.BpmEntitiesService;
import com.haulmont.cuba.core.global.AppBeans;
import com.haulmont.cuba.core.global.Metadata;
import com.haulmont.cuba.core.global.View;
import com.haulmont.cuba.security.entity.User;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ProcActorsBuilder {

    private ProcInstance procInstance;

    private Multimap<String, User> actorsMap = ArrayListMultimap.create();

    public ProcActorsBuilder(ProcInstance procInstance) {
        this.procInstance = procInstance;
    }

    public ProcActorsBuilder addActor(String procRoleCode, User user) {
        actorsMap.put(procRoleCode, user);
        return this;
    }

    public ProcActorsBuilder addActors(String procRoleCode, Collection<User> users) {
        for (User user : users) {
            actorsMap.put(procRoleCode, user);
        }
        return this;
    }

    public Set<ProcActor> build() {
        Set<ProcActor> procActors = new HashSet<>();
        BpmEntitiesService bpmEntitiesService = AppBeans.get(BpmEntitiesService.class);
        Metadata metadata = AppBeans.get(Metadata.class);
        for (String procRoleCode : actorsMap.keySet()) {
            ProcRole procRole = bpmEntitiesService.findProcRole(procInstance.getProcDefinition().getCode(), procRoleCode, View.MINIMAL);
            for (User user : actorsMap.get(procRoleCode)) {
                ProcActor procActor = metadata.create(ProcActor.class);
                procActor.setUser(user);
                procActor.setProcRole(procRole);
                procActor.setProcInstance(procInstance);
                procActors.add(procActor);
            }
        }
        return procActors;
    }

    public Set<ProcActor> buildAndAssign() {
        Set<ProcActor> procActors = build();
        procInstance.setProcActors(procActors);
        return procActors;
    }
}
