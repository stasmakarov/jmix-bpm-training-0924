package com.company.jmixbpmtraining.entity;

import io.jmix.core.metamodel.datatype.EnumClass;

import org.springframework.lang.Nullable;


public enum TransferDirection implements EnumClass<String> {

    DEBIT("D"),
    CREDIT("C");

    private final String id;

    TransferDirection(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static TransferDirection fromId(String id) {
        for (TransferDirection at : TransferDirection.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}