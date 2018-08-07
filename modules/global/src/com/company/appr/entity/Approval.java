package com.company.appr.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import com.haulmont.cuba.security.entity.User;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.chile.core.annotations.NamePattern;
import javax.validation.constraints.NotNull;
import com.haulmont.chile.core.annotations.Composition;
import com.haulmont.cuba.core.entity.annotation.OnDelete;
import com.haulmont.cuba.core.global.DeletePolicy;
import javax.persistence.OneToMany;

@NamePattern("%s|name")
@Table(name = "APPR_APPROVAL")
@Entity(name = "appr$Approval")
public class Approval extends StandardEntity implements HasProcessState {
    private static final long serialVersionUID = -3280690846657981285L;

    @NotNull
    @Column(name = "NAME", nullable = false)
    protected String name;

    @Composition
    @OnDelete(DeletePolicy.CASCADE)
    @OneToMany(mappedBy = "approval")
    protected List<Document> documents;

    @NotNull
    @Column(name = "TYPE_", nullable = false)
    protected String type = ApprovalType.ALL.getId();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DUE_DATE")
    protected Date dueDate;

    @Column(name = "PROCESS_STATE")
    protected String processState;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INITIATOR_ID")
    protected User initiator;

    @JoinTable(name = "APPR_APPROVAL_USER_LINK",
        joinColumns = @JoinColumn(name = "APPROVAL_ID"),
        inverseJoinColumns = @JoinColumn(name = "USER_ID"))
    @ManyToMany
    protected List<User> approvers;

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public List<Document> getDocuments() {
        return documents;
    }


    public void setProcessState(String processState) {
        this.processState = processState;
    }

    public String getProcessState() {
        return processState;
    }


    public void setType(ApprovalType type) {
        this.type = type == null ? null : type.getId();
    }

    public ApprovalType getType() {
        return type == null ? null : ApprovalType.fromId(type);
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setInitiator(User initiator) {
        this.initiator = initiator;
    }

    public User getInitiator() {
        return initiator;
    }

    public void setApprovers(List<User> approvers) {
        this.approvers = approvers;
    }

    public List<User> getApprovers() {
        return approvers;
    }


}