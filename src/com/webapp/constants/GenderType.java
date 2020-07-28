package com.webapp.constants;

public enum GenderType {
    MALE(1),
    FEMALE(2),
    TRANSGENDER(3);

    int genderType;
    private GenderType(int gender) {
        this.genderType = gender;
    }

    public int getGenderType() {
        return genderType;
    }

}
