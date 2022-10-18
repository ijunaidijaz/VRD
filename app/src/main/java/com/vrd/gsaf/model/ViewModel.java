package com.vrd.gsaf.model;

import android.widget.EditText;
import android.widget.TextView;

public class ViewModel {
    private int id;
    private String type;
    private String value;

    private String autoId;
    private EditText editText;
    private String required;
    private TextView title;
    private Boolean isExtra = false;


    public ViewModel(int id, String type, String required, TextView titleTxt) {
        this.id = id;
        this.type = type;
        this.required = required;
        this.title = titleTxt;
    }


    public Boolean getExtra() {
        return isExtra;
    }

    public void setExtra(Boolean extra) {
        isExtra = extra;
    }


    public String getAutoId() {
        return autoId;
    }

    public void setAutoId(String autoId) {
        this.autoId = autoId;
    }

    public TextView getTitle() {
        return title;
    }

    public void setTitle(TextView title) {
        this.title = title;
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public EditText getEditText() {
        return editText;
    }

    public void setEditText(EditText editText) {
        this.editText = editText;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
