package com.vrd.gsaf.adapters;

public class HallModel {

    private String type;
    private String companyLogo;

    public HallModel(String type, String companyLogo) {
        this.type = type;
        this.companyLogo = companyLogo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCompanyLogo() {
        return companyLogo;
    }

    public void setCompanyLogo(String companyLogo) {
        this.companyLogo = companyLogo;
    }
}
