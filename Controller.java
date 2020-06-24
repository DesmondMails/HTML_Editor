package com.javarush.task.task32.task3209;

import javax.print.Doc;
import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.io.*;

public class Controller {
    private View view;
    private HTMLDocument document;
    private File currentFile;

    public Controller(View view){
        this.view = view;
    }
    public void init(){
        createNewDocument();
    }
    public void exit(){
        System.exit(0);
    }

    public HTMLDocument getDocument() {
        return document;
    }
    public void resetDocument(){
        if (document!=null) {
            document.removeUndoableEditListener(view.getUndoListener());
        }
         document = (HTMLDocument) new HTMLEditorKit().createDefaultDocument();
        document.addUndoableEditListener(view.getUndoListener());
        view.update();

    }
    public void setPlainText(String text){
        resetDocument();
        try {
            StringReader reader = new StringReader(text);
            new HTMLEditorKit().read(reader, document, 0);
        }catch (Exception e){
            ExceptionHandler.log(e);
        }
    }
    public String getPlainText(){
        StringWriter stringWriter = new StringWriter();
        String txt = "";
        try {
            new HTMLEditorKit().write(stringWriter,document,0,document.getLength());
        }catch (Exception e){
            ExceptionHandler.log(e);
        }
        txt = stringWriter.toString();
        return txt;
    }
    public void createNewDocument(){
        view.selectHtmlTab();
        resetDocument();
        view.setTitle("HTML редактор");
        view.resetUndo();
        currentFile = null;

    }
    public void openDocument(){
        view.selectHtmlTab();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new HTMLFileFilter());
        fileChooser.setDialogTitle("Open document");
        int res = fileChooser.showOpenDialog(view);
        if (res == JFileChooser.APPROVE_OPTION){
            currentFile = fileChooser.getSelectedFile();
            resetDocument();
            view.setTitle(currentFile.getName());

            try {
                FileReader reader = new FileReader(currentFile);
                new HTMLEditorKit().read(reader,document,0);
                view.resetUndo();
            }catch (Exception e){
                ExceptionHandler.log(e);
            }
        }
    }
    public void saveDocument(){
        view.selectHtmlTab();
        if (currentFile != null)
        {
            try {
                view.setTitle(currentFile.getName());
                FileWriter writer = new FileWriter(currentFile);
                new HTMLEditorKit().write(writer, document, 0, document.getLength());
                writer.close();
            } catch (Exception e) {
                ExceptionHandler.log(e);
            }
        }
        else saveDocumentAs();

    }
    public void saveDocumentAs(){
        view.selectHtmlTab();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new HTMLFileFilter());
        fileChooser.setDialogTitle("Save file");
        int res = fileChooser.showSaveDialog(view);
        if (res == JFileChooser.APPROVE_OPTION){
            currentFile = fileChooser.getSelectedFile();
            view.setTitle(currentFile.getName());
            try {
                FileWriter writer = new FileWriter(currentFile);
                new HTMLEditorKit().write(writer,document,0,document.getLength());
            }catch (Exception e){
                ExceptionHandler.log(e);
            }
        }

    }
    public static void main(String[] args) {
        View view = new View();
        Controller controller = new Controller(view);
        view.setController(controller);
        view.init();
        controller.init();
    }
}
