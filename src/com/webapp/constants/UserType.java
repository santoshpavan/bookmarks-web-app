package com.webapp.constants;

public enum UserType {
    USER("user"),
    EDITOR("editor"),
    CHIEF_EDITOR("chiefeditor");

    String userType;
    private UserType(String type) {
        this.userType = type;
    }

    public String getUserType() {
        return userType;
    }
}
