package com.company.appr.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Lob;
import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.chile.core.annotations.NamePattern;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@NamePattern("%s|name")
@Table(name = "APPR_DOCUMENT")
@Entity(name = "appr$Document")
public class Document extends StandardEntity {
    private static final long serialVersionUID = 4492677352328727442L;

    @Column(name = "NAME")
    protected String name;

    @Lob
    @Column(name = "DESCRIPTION")
    protected String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "APPROVAL_ID")
    protected Approval approval;

    public void setApproval(Approval approval) {
        this.approval = approval;
    }

    public Approval getApproval() {
        return approval;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }


}