package com.webapp.constants;

public enum KidFriendlyStatus {
    UNKNOWN("Unknown"),
    APPROVED("Approved"),
    REJECTED("Rejected");

    String friendlyStatus;
    private KidFriendlyStatus (String status) {
        this.friendlyStatus = status;
    }

    public String getFriendlyStatus() {
        return friendlyStatus;
    }
}
