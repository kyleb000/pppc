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

class DocumentReaderClass {
    private String filePath;
    private FileInputStream fis;

    DocumentReaderClass(){

    }

    void setFilePath(String filePath){
        this.filePath = filePath;
    }

    private String getFileExtensionType(String path){
        boolean stop = false;
        char[] string = path.toCharArray();
        int size = path.length() - 1;
        StringBuilder returnString = new StringBuilder();
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
            fis = new FileInputStream(new File(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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

    private void readDocFile(){
        try {
            WordExtractor extractor;
            HWPFDocument document = new HWPFDocument(fis);
            extractor = new WordExtractor(document);
            System.out.println(extractor.getText());
            System.out.println();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void readDocxFile(){
        try {
            XWPFDocument doc = new XWPFDocument(fis);
            XWPFWordExtractor extract = new XWPFWordExtractor(doc);
            System.out.println(extract.getText());
            for (XWPFParagraph xwpfParagraph : doc.getParagraphs()) {
                printContentsOfTextBox(xwpfParagraph);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void readOdtFile(){
        try {
            int i = 0;
            TextDocument document = TextDocument.loadDocument(filePath);

            while (true) {
                try {
                    Paragraph paragraph = document.getParagraphByIndex(i, false);
                    int j = 1;
                    while (true) {
                        String name = "Shape" + String.valueOf(j);
                        try {

                            Textbox textbox = paragraph.getTextboxByName(name);
                            System.out.println(textbox.getTextContent());
                            j++;

                        } catch (NullPointerException e) {
                            if (j <= 1000){
                                j++;
                            } else {
                                break;
                            }
                        }
                    }
                    System.out.println(paragraph.getTextContent());

                    i++;
                } catch (Exception e){
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printContentsOfTextBox(XWPFParagraph paragraph) {

        XmlObject[] textBoxObjects =  paragraph.getCTP().selectPath("declare namespace w='http://schemas.openxmlformats.org/wordprocessingml/2006/main' " +
                "declare namespace wps='http://schemas.microsoft.com/office/word/2010/wordprocessingShape' .//*/wps:txbx/w:txbxContent");

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
                    System.out.println(embeddedPara.getText());
                }

            } catch (XmlException e) {
                //handle
            }
        }

    }
}
