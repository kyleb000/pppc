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


public class PlagiarismCheckerClass {

    private DataClass dataClass;
    private Thread thread1, thread2, thread3, thread4;

    PlagiarismCheckerClass(DataClass dataClass){
        this.dataClass = dataClass;
    }

    void checkForPlagiarism(){
        switch (dataClass.getCheckType()){
            case 0:
                System.out.println(dataClass.getDocumentContent());
                break;
            case 1:
                System.out.println(dataClass.getDocumentContent());
                break;
            default:
                System.out.println(dataClass.getCheckType());
                break;
        }
    }
}
