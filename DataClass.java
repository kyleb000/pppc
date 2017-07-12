package com.purplepencil.pppc;

import java.util.ArrayList;
class DataClass {

    private int checkType;
    private ArrayList<String> documentList = new ArrayList<>();
    private String reportPath;
    private String documentContent;
    private String webContent;
    private String thread1Data, thread2Data, thread3Data, thread4Data;
    private ArrayList<String> webList = new ArrayList<>();

    void setCheckType(int checkType){
        this.checkType = checkType;
    }

    void setDocumentList(ArrayList<String> documentList){
        this.documentList = documentList;
    }

    void setReportPath(String reportPath){
        this.reportPath = reportPath;
    }

    void setDocumentContent(String documentContent){
        this.documentContent = documentContent;
    }

    int getCheckType(){
        return checkType;
    }

    ArrayList<String> getDocumentList(){
        return documentList;
    }

    String getReportPath(){
        return reportPath;
    }

    String getDocumentContent(){
        return documentContent;
    }
}
