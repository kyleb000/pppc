/*
 * This file is part of Purple Pencil Plagiarism Checker.

 Purple Pencil Plagiarism Checker is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 Purple Pencil Plagiarism Checker is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Purple Pencil Plagiarism Checker.  If not, see <http://www.gnu.org/licenses/>.
 */


/*
 * @author Kyle Barry
 * @version 1.0
 */

//Copyright (c) Kyle Barry 2017

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
