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

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.util.*;

public class PPPC{

    //initialising components
    private JRadioButton internetCheckBtn, documentCheckBtn;
    private DataClass dataClass;
    private DocumentReaderClass readerClass;
    private PlagiarismCheckerClass plagChecker;
    private JButton checkBtn;
    private JButton selectDocumentsBtn;
    private JButton selectReportPathBtn;
    private JButton selectTypeBtn;
    private JButton okBtn;
    private JPanel folderPanel;
    private JPanel folderGroupPanel;
    private JPanel buttonPanel;
    private JPanel mainPanel;
    private JPanel scrollPanel;
    private JPanel centerPanel;
    private GridBagLayout mainLayout, buttonLayout, folderLayout;
    private GridLayout gridLayout;
    private JScrollPane folderScrollPane;
    private GridBagConstraints constraints;
    private JFileChooser chooser, reportChooser;
    private JFrame frame;
    private int frameWidth;
    private int frameHeight;
    private int paddingLeft;
    private int paddingRight;
    private int screenWidth;
    private int screenHeight;
    private int typeHeight;
    private int typeWidth;
    private int alreadyStarted = 0;
    private ArrayList<JButton> buttons;
    private volatile boolean isAlive=false;

    //constructor for the class
    private PPPC(){
        frame = new JFrame("Purple Pencil Plagiarism Checker");
        getScreenSizeSettings();
    }

    //starts the GUI and instantiates the components
    private void init(){
        frame.setSize(frameWidth, frameHeight);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocation((screenWidth - frameWidth)/2, (screenHeight - frameHeight)/2);

        checkBtn = new JButton("Check For Plagiarism");
        selectDocumentsBtn = new JButton("Select Documents");
        selectReportPathBtn = new JButton("Select Report Folder");
        selectTypeBtn = new JButton("Compare Documents To...");
        okBtn = new JButton("OK");

        internetCheckBtn = new JRadioButton("Internet Websites");
        documentCheckBtn = new JRadioButton("Other Documents");

        folderScrollPane = new JScrollPane();

        folderGroupPanel = new JPanel();
        centerPanel = new JPanel();
        mainPanel = new JPanel();
        folderPanel = new JPanel();
        buttonPanel = new JPanel();
        scrollPanel = new JPanel();

        buttonPanel.setBorder(new EmptyBorder(10, paddingLeft, 10, paddingRight));

        dataClass = new DataClass();
        readerClass = new DocumentReaderClass(dataClass);
        plagChecker = new PlagiarismCheckerClass(dataClass);

        mainLayout = new GridBagLayout();
        buttonLayout = new GridBagLayout();
        folderLayout = new GridBagLayout();
        gridLayout = new GridLayout(0, 1);

        constraints = new GridBagConstraints();

        buttons = new ArrayList<>();

        chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Document Files",
                "doc", "docx", "odt");
        chooser.setFileFilter(filter);
        reportChooser = new JFileChooser();

        dataClass.setCheckType(0);

        build();

        frame.setVisible(true);
    }

    //gets the size of the default screen and sets the display sizes accordingly
    private void getScreenSizeSettings(){
        GraphicsDevice defaultScreenDevice = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice();

        screenWidth = defaultScreenDevice.getDisplayMode().getWidth();
        screenHeight = defaultScreenDevice.getDisplayMode().getHeight();

        double resRatio = (double)screenWidth / (double)screenHeight;

        if (resRatio < 1.4d && resRatio > 1.2d){
            frameWidth = (int)(screenWidth * 0.7031);
            frameHeight = (int)(screenHeight * 0.5208);
            paddingRight = (int)(defaultScreenDevice.getDisplayMode().getWidth() * 0.0391);
            paddingLeft = (int)(defaultScreenDevice.getDisplayMode().getWidth() * 0.03125);
            typeWidth = (int)(defaultScreenDevice.getDisplayMode().getWidth() * 0.46875);
            typeHeight = (int)(defaultScreenDevice.getDisplayMode().getHeight() * 0.2083);
        } else {
            frameWidth = (int)(screenWidth * 0.50);
            frameHeight = (int)(screenHeight * 0.2315);
            paddingRight = (int)(defaultScreenDevice.getDisplayMode().getWidth() * 0.0391);
            paddingLeft = (int)(defaultScreenDevice.getDisplayMode().getWidth() * 0.03125);
            typeWidth = (int)(defaultScreenDevice.getDisplayMode().getWidth() * 0.15625);
            typeHeight = (int)(defaultScreenDevice.getDisplayMode().getHeight() * 0.0926);
        }
    }

    //gets the position of the buttons, determines if the mouse is over the buttons and toggles them visible or invisible
    //TODO: Optimise usage of memory and CPU resources --DESIRED--
    private Thread buttonPositionGetter = new Thread(new Runnable() {
        @Override
        public void run() {
            //noinspection InfiniteLoopStatement
            while (true) {
                //only runs when we are ready for the thread to resume
                if (isAlive) {
                    if (alreadyStarted == 1) {

                        //increments through the buttons
                        for (JButton button : buttons) {
                            Point p;
                            try {
                                p = button.getLocationOnScreen();
                            } catch (IllegalComponentStateException e) {
                                break;

                            }
                            Point p2 = MouseInfo.getPointerInfo().getLocation();
                            double mouseX = p2.getX();
                            double mouseY = p2.getY();

                            //checks if mouse is within the area of the button in question
                            if (mouseX > p.getX() && mouseX < (p.getX() + button.getWidth()) &&
                                    mouseY > p.getY() && mouseY < (p.getY() + button.getHeight())) {
                                button.setContentAreaFilled(true);
                                button.setOpaque(true);
                                button.setBorderPainted(true);
                                button.setText("Remove");
                                button.setEnabled(true);

                            } else {
                                button.setContentAreaFilled(false);
                                button.setOpaque(false);
                                button.setBorderPainted(false);
                                button.setText(null);
                                button.setEnabled(false);
                            }
                        }
                    }
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    });

    //builds the GUI
    private void build(){
        buttonPositionGetter.start();

        buttonPanel.setLayout(buttonLayout);
        folderGroupPanel.setLayout(new BorderLayout());
        mainPanel.setLayout(mainLayout);
        scrollPanel.setLayout(folderLayout);
        centerPanel.setLayout(new BorderLayout());
        folderPanel.setLayout(gridLayout);

        componentBuilder();

        frame.add(mainPanel);

        buildButtonAction();
    }

    //adds button actions
    private void buildButtonAction(){
        selectDocumentsBtn.addActionListener(e -> {
            int i = chooser.showOpenDialog(checkBtn);
            if (i == JFileChooser.APPROVE_OPTION) {
                ArrayList<String> list = dataClass.getDocumentList();
                list.add(chooser.getSelectedFile().getAbsolutePath());
                dataClass.setDocumentList(list);
                updateFolderPanel();
            }
        });
        selectTypeBtn.addActionListener(e -> {
            JFrame selectFrame = new JFrame("Compare Documents To...");
            selectFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            JPanel selectPanel = new JPanel(new BorderLayout());
            selectPanel.add(internetCheckBtn, BorderLayout.NORTH);
            selectPanel.add(documentCheckBtn, BorderLayout.CENTER);
            selectPanel.add(okBtn, BorderLayout.SOUTH);
            internetCheckBtn.setSelected(true);
            selectFrame.add(selectPanel);
            internetCheckBtn.addActionListener(e1 -> {
                dataClass.setCheckType(0);
                internetCheckBtn.setSelected(true);
                documentCheckBtn.setSelected(false);
            });
            documentCheckBtn.addActionListener(e1 -> {
                dataClass.setCheckType(1);
                internetCheckBtn.setSelected(false);
                documentCheckBtn.setSelected(true);
            });
            okBtn.addActionListener(e1 -> selectFrame.dispose());
            selectFrame.setResizable(false);
            selectFrame.setSize(typeWidth, typeHeight);
            selectFrame.setLocation((screenWidth - typeWidth)/2, (screenHeight - typeHeight)/2);
            selectFrame.setVisible(true);
        });

        selectReportPathBtn.addActionListener(e -> {
            reportChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int i = reportChooser.showOpenDialog(selectReportPathBtn);
            if (i == JFileChooser.APPROVE_OPTION) {
                dataClass.setReportPath(reportChooser.getSelectedFile().getAbsolutePath());
            }
        });

        checkBtn.addActionListener(e -> {
            for (String docs : dataClass.getDocumentList()){
                readerClass.setFilePath(docs);
                readerClass.readFile();
                plagChecker.checkForPlagiarism();
            }
        });
    }

    //removes and adds an action event for each button in the remove button list
    private void buildRemoveButtonAction(){
        for (JButton button : buttons){
            try {
                button.removeActionListener(e -> dummyAction());
            } catch (Exception e){
                button.removeActionListener(e2 -> addButtonAction(button));
            }
            button.addActionListener(e -> addButtonAction(button));
        }
    }

    private void dummyAction(){/*Dummy code so we get no Exceptions thrown*/}

    //builds a document list and adds it to a component
    private void buildDocumentList(){
        alreadyStarted = 0;
        isAlive = false;
        buttons.clear();
        int inc = 0;

        gridLayout = new GridLayout(dataClass.getDocumentList().size(), 0);
        folderPanel.setLayout(gridLayout);
        for (String e : dataClass.getDocumentList()){
            JPanel containerPanel = new JPanel(new GridLayout(1, 2));
            JButton removeBtn = new JButton();
            JTextArea folderArea = new JTextArea(getDocumentNameFromPath(e));
            folderArea.setOpaque(false);
            removeBtn.addActionListener(e1 -> dummyAction());
            removeBtn.setName(String.valueOf(inc));
            buttons.add(removeBtn);
            folderArea.setEditable(false);
            Font font = new Font("Arial", Font.PLAIN, 16);
            folderArea.setMargin( new Insets(10,10,10,10) );
            folderArea.setFont(font);
            containerPanel.add(folderArea,0);
            containerPanel.add(removeBtn, 1);
            folderPanel.add(containerPanel, inc);
            inc++;
        }

    }

    //removes the document that shares the same position as the button
    private void addButtonAction(JButton rButton){
        dataClass.getDocumentList().remove(Integer.parseInt(rButton.getName()));
        updateFolderPanel();
    }

    //this function takes out the document name from the path so that the user does
    //not have to see the entire path
    private String getDocumentNameFromPath(String path){
        boolean stop = false;
        char[] string = path.toCharArray();
        int size = path.length() - 1;
        StringBuilder returnString = new StringBuilder();
        while (!stop){
            if (string[size] == '/' || string[size] == '\\'){
                stop = true;
            }
            if (!stop)
                returnString.append(string[size]);
            size--;
        }
        //reverses the reversed return string
        return new StringBuilder(returnString.toString()).reverse().toString();
    }

    //updates the folder panel to display the new document list
    private void updateFolderPanel(){
        folderPanel.removeAll();
        folderGroupPanel.removeAll();
        scrollPanel.removeAll();
        if (dataClass.getDocumentList().size() >= 1)
            buildDocumentList();

        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridy = constraints.gridx = 0;
        scrollPanel.add(folderPanel,constraints);
        centerPanel.add(scrollPanel, BorderLayout.NORTH);
        folderGroupPanel.add(centerPanel,BorderLayout.WEST);

        scrollPanel.revalidate();
        scrollPanel.repaint();
        folderGroupPanel.revalidate();
        folderGroupPanel.repaint();

        updateMainPanel();
        buildRemoveButtonAction();

    }

    //One function to do the component building of build() and updateMainPanel()
    private void componentBuilder(){
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.gridx = 0;
        constraints.gridy = 0;
        buttonPanel.add(selectDocumentsBtn,constraints);

        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.gridx = 0;
        constraints.gridy = 1;
        buttonPanel.add(selectReportPathBtn,constraints);

        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.gridx = 0;
        constraints.gridy = 2;
        buttonPanel.add(selectTypeBtn,constraints);

        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.gridx = 0;
        constraints.gridy = 3;
        buttonPanel.add(checkBtn,constraints);

        constraints.fill = GridBagConstraints.BOTH;
        folderScrollPane.setViewportView(folderGroupPanel);
        constraints.weightx = 5;
        constraints.weighty = 1;
        constraints.gridx = 0;
        constraints.gridy = 0;
        mainPanel.add(folderScrollPane,constraints);


        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.gridx = 1;
        constraints.gridy = 0;
        mainPanel.add(buttonPanel,constraints);
    }

    //updates the main panel to display the updated folder panel
    private void updateMainPanel(){

        mainPanel.removeAll();

        componentBuilder();

        if (alreadyStarted == 0){
            isAlive = true;
            alreadyStarted = 1;
        }

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    //the main method. It creates a new instance of the class and starts the init() function
    public static void main(String[] args){
        PPPC p = new PPPC();
        p.init();
    }
}
