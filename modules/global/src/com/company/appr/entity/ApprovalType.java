package com.company.appr.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum ApprovalType implements EnumClass<String> {

    HALF_PLUS_1("approval_half_plus_1"),
    ALL("approval_all"),
    ANYONE("approval_anyone");

    private String id;

    ApprovalType(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static ApprovalType fromId(String id) {
        for (ApprovalType at : ApprovalType.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}