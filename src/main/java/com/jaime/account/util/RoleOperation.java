package com.jaime.account.util;

public enum RoleOperation {
    GRANT, REMOVE;

    public String getActionNameCapitalized() {
        return this.name().substring(0, 1).toUpperCase() + this.name().substring(1).toLowerCase();
    }
}
