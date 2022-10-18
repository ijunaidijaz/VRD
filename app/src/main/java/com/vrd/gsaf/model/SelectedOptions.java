package com.vrd.gsaf.model;

public class SelectedOptions {
    public String type;
    private String value;
    private String autoId = null;

    public SelectedOptions(String value, String type) {
        this.value = value;
        this.type = type;
    }

    public String getAutoId() {
        return autoId;
    }

    public void setAutoId(String autoId) {
        this.autoId = autoId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
