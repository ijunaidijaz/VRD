package com.vrd.gsaf.home.homeTab.documents;

public class DocumentModel {

    private String documentLink;
    private String documentName;

    public DocumentModel(String documentLink, String documentName) {
        this.documentLink = documentLink;
        this.documentName = documentName;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocumentLink() {
        return documentLink;
    }

    public void setDocumentLink(String documentLink) {
        this.documentLink = documentLink;
    }
}
