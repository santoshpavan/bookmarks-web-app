package com.webapp.constants;

public enum BookGenreType {
    ART("Art"),
    BIOGRAPHY("Biography"),
    CHILDREN("Children"),
    FICTION("Fiction"),
    HISTORY("History"),
    MYSTERY("Mystery"),
    PHILOSOPHY("Philosophy"),
    RELIGION("Religion"),
    SELF_HELP("Self Help"),
    TECHNICAL("Technical");

    private String typeName;
    private BookGenreType(String name) {
        this.typeName = name;
    }

    public String getString() {
        return typeName;
    }
}
