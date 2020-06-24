package com.javarush.task.task32.task3209;

import com.javarush.task.task32.task3209.listeners.FrameListener;
import com.javarush.task.task32.task3209.listeners.TabbedPaneChangeListener;
import com.javarush.task.task32.task3209.listeners.UndoListener;

import javax.swing.*;
import javax.swing.text.html.HTMLDocument;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class View extends JFrame implements ActionListener {
    private Controller controller;
    private JTabbedPane tabbedPane = new JTabbedPane();
    private JTextPane htmlTextPane = new JTextPane();
    private JEditorPane plainTextPane = new JEditorPane();
    private UndoManager undoManager = new UndoManager();
    private UndoListener undoListener = new UndoListener(undoManager);


    public View(){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            ExceptionHandler.log(e);
        }
    }
    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        String command = actionEvent.getActionCommand();

        switch (command){
            case "Новый" : controller.createNewDocument();
                break;
            case "Открыть" : controller.openDocument();
                break;
            case "Сохранить" : controller.saveDocument();
                break;
            case "Сохранить как..." : controller.saveDocumentAs();
                break;
            case "Выход" : controller.exit();
                break;
            case "О программе" : showAbout();
                break;
        }
    }
    public void init(){
        initGui();
        FrameListener frameListener = new FrameListener(this);
        addWindowListener(frameListener);
        setVisible(true);
    }
    public  void exit(){
        controller.exit();
    }

    public void initMenuBar(){
        JMenuBar menuBar = new JMenuBar();

        MenuHelper.initFileMenu(this,menuBar);
        MenuHelper.initEditMenu(this,menuBar);
        MenuHelper.initStyleMenu(this,menuBar);
        MenuHelper.initAlignMenu(this,menuBar);
        MenuHelper.initColorMenu(this,menuBar);
        MenuHelper.initFontMenu(this,menuBar);
        MenuHelper.initHelpMenu(this,menuBar);

        getContentPane().add(menuBar,BorderLayout.NORTH);


    }
    public void selectedTabChanged(){
       int numberOfTab = tabbedPane.getSelectedIndex();

       if (numberOfTab == 0){
           String str = plainTextPane.getText();
           controller.setPlainText(str);
       }else if (numberOfTab == 1){
           String str = controller.getPlainText();
           plainTextPane.setText(str);
       }
       
       resetUndo();
    }
    public void initEditor(){
        htmlTextPane.setContentType("text/html");

        JScrollPane scrollPane_HTML = new JScrollPane(htmlTextPane);
        tabbedPane.add("HTML",scrollPane_HTML);
        JScrollPane scrollPane_Text = new JScrollPane(plainTextPane);
        tabbedPane.add("Текст",scrollPane_Text);

        tabbedPane.setPreferredSize(new Dimension(800,600));

        TabbedPaneChangeListener listener = new TabbedPaneChangeListener(this);
        tabbedPane.addChangeListener(listener);

        getContentPane().add(tabbedPane, BorderLayout.CENTER);

    }
    public void initGui(){
        initMenuBar();
        initEditor();
        pack();

    }

    public boolean canUndo(){
        return undoManager.canUndo();
    }
    public boolean canRedo(){
        return undoManager.canRedo();
    }

    public void undo(){
        try {
            undoManager.undo();
        }catch (Exception e){
            ExceptionHandler.log(e);
        }
    }
    public void redo(){
        try {
            undoManager.redo();
        }catch (Exception e){
            ExceptionHandler.log(e);
        }

    }

    public void resetUndo(){
        undoManager.discardAllEdits();
    }

    public UndoListener getUndoListener() {
        return undoListener;
    }
    public boolean isHtmlTabSelected(){
        return tabbedPane.getSelectedIndex() == 0;
    }

    public void selectHtmlTab(){
        tabbedPane.setSelectedIndex(0);
        resetUndo();

    }
    public void update(){
        htmlTextPane.setDocument(controller.getDocument());
    }
    public void showAbout(){
        String message = "Here is information about app";
        JOptionPane optionPane = new JOptionPane();
        JOptionPane.showMessageDialog(optionPane, message, "About", JOptionPane.INFORMATION_MESSAGE);
    }
}
