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

import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.*;
import org.odftoolkit.simple.TextDocument;
import org.odftoolkit.simple.draw.*;
import org.odftoolkit.simple.text.*;
import java.io.*;
import org.apache.poi.hwpf.*;
import org.apache.poi.hwpf.extractor.*;
import org.apache.poi.xwpf.extractor.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import javax.xml.namespace.QName;

//TODO: add powerpoint and excel file support, as well as the ability to read from tables, graphs and wordart / fontwork
class DocumentReaderClass {
    private String filePath;
    private FileInputStream fis;
    private DataClass dataClass;
    private StringBuffer documentContent = new StringBuffer();

    DocumentReaderClass(DataClass dataClass){
        this.dataClass = dataClass;
    }

    void setFilePath(String filePath){
        this.filePath = filePath;
    }

    //extracts the file extension so we can determine how to read the document
    private String getFileExtensionType(String path){
        boolean stop = false;
        char[] string = path.toCharArray();

        //we want the size to be able to stay in the range of the array

        int size = path.length() - 1;
        StringBuilder returnString = new StringBuilder();

        //extracts all characters after the '.'
        while (!stop){
            if (string[size] == '.'){
                stop = true;
            }
            if (!stop)
                returnString.append(string[size]);
            size--;
        }
        //reverses the reversed return string
        return new StringBuilder(returnString.toString()).reverse().toString();
    }

    void readFile(){
        try {

            //reads the file
            fis = new FileInputStream(new File(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //calls the relevant function to read from the relevant file
        switch (getFileExtensionType(filePath)){
            case "docx":
                readDocxFile();
                break;
            case "doc":
                readDocFile();
                break;
            case "odt":
                readOdtFile();
                break;
            default:
                break;
        }
    }

    //Function to read Microsoft .doc files
    private void readDocFile(){
        try {
            WordExtractor extractor;
            HWPFDocument document = new HWPFDocument(fis);
            extractor = new WordExtractor(document);
            documentContent.append(extractor.getText());
            dataClass.setDocumentContent(documentContent.toString());
            documentContent.setLength(0);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //Function to read Microsoft .docx files
    private void readDocxFile(){
        try {
            XWPFDocument doc = new XWPFDocument(fis);
            XWPFWordExtractor extract = new XWPFWordExtractor(doc);
            documentContent.append(extract.getText());

            //gets all textboxes from each paragraph in the document
            for (XWPFParagraph xwpfParagraph : doc.getParagraphs()) {
                printContentsOfTextBox(xwpfParagraph);
            }

            dataClass.setDocumentContent(documentContent.toString());
            documentContent.setLength(0);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    //Function to read Open Document .odt files
    private void readOdtFile(){
        try {
            int i = 0;
            TextDocument document = TextDocument.loadDocument(filePath);

            //gets all paragraphs one by one and breaks the loop when we cannot find more paragraphs
            while (true) {
                try {
                    Paragraph paragraph = document.getParagraphByIndex(i, false);
                    int j = 1;

                    //gets every textbox from each paragraph
                    while (true) {
                        String name = "Shape" + String.valueOf(j);
                        try {

                            Textbox textbox = paragraph.getTextboxByName(name);
                            documentContent.append(textbox.getTextContent());
                            j++;

                        } catch (NullPointerException e) {

                            //We look for 1000 potential textboxes before breaking the loop
                            if (j <= 1000){
                                j++;
                            } else {
                                break;
                            }
                        }
                    }
                    documentContent.append(paragraph.getTextContent());

                    i++;

                //we know we are done when we get here
                } catch (Exception e){
                    dataClass.setDocumentContent(documentContent.toString());
                    documentContent.setLength(0);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //this function allows us to get the text box data from docx file by accessing the XML element of the textbox
    private void printContentsOfTextBox(XWPFParagraph paragraph) {

        //gets all the text boxes using the defined schemas and namespaces
        XmlObject[] textBoxObjects =  paragraph.getCTP().selectPath("declare namespace w='http://schemas.openxmlformats.org/wordprocessingml/2006/main' " +
                "declare namespace wps='http://schemas.microsoft.com/office/word/2010/wordprocessingShape' .//*/wps:txbx/w:txbxContent");

        //goes through each text box, extracts its contents and prints its contents
        for (XmlObject textBoxObject : textBoxObjects) {
            XWPFParagraph embeddedPara;
            try {
                XmlObject[] paraObjects = textBoxObject.
                        selectChildren(
                                new QName("http://schemas.openxmlformats.org/wordprocessingml/2006/main", "p"));

                for (XmlObject paraObject : paraObjects) {
                    embeddedPara = new XWPFParagraph(
                            CTP.Factory.parse(paraObject.xmlText()), paragraph.getBody());
                    //Here you have your paragraph;
                    documentContent.append(embeddedPara.getText());
                }

            } catch (XmlException e) {
                //handle
            }
        }

    }
}
