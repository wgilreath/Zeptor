/*
 * Copyright © 2024 by William F. Gilreath (will@zepton.xyz)
 * All rights reserved.
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 as published
 * by the Free Software Foundation.  
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details. The license is available at the following 
 * link:  https://www.gnu.org/licenses/gpl-3.0.txt.
 *
 */
package xyz.zepton.zeptor.text;

import xyz.zepton.zeptor.editor.Zeptor;
import xyz.zepton.zeptor.editor.EditorOptions;
import xyz.zepton.zeptor.gui.MyFileFilter;
import xyz.zepton.zeptor.listener.MyFileChangedListener;
import xyz.zepton.zeptor.log.Logger;
import xyz.zepton.zeptor.transpiler.ZepT;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;
import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * An internal frame to edit text
 *
 * @author Tan Hong Cheong
 * @version 20050515
 */
public class MyTextPane extends JPanel implements
        CaretListener,
        DocumentListener,
        UndoableEditListener {

    private static final String LINE_COL_FMT = " %d : %d | %s ";
    private static final long serialVersionUID = 978034438378288255L;

    /**
     * The textpane that is displaying the text
     */
    private MyJTextPane textPane;
    /**
     * The file that is being edited
     */
    private File file;
    /**
     * The line label
     */
    private JLabel lineLabel;
    /**
     * indicate wether there is need to save
     */
    private boolean needToSave;

    /**
     * the editor frame
     */
    private Zeptor zeptor;
    /**
     * The vectors to store the file changed listeners
     */
    private ArrayList<MyFileChangedListener> listeners;
    /**
     * the number for the new file -1 for not a new file
     */
    private int no;
    /**
     * the undo manager
     */
    private UndoManager undoManager;
    /**
     * the find dialog
     */
    private FindDialog findDialog;
    /**
     * the find replace dialog
     */
    private FindReplaceDialog replaceDialog;
    /**
     * the title associated with this text pane
     */
    private String title;

    /**
     * Constructor
     *
     * @param p the editor editor frame
     * @param l the my file changed listener
     */
    public MyTextPane(Zeptor p, MyFileChangedListener l) {
        this(p, l, -1);
    }

    /**
     * Constructor
     *
     * @param p the editor editor frame
     * @param l the my file changed listener
     * @param no the no for the new file
     */
    public MyTextPane(Zeptor p, MyFileChangedListener l, int no) {
        file = null;
        zeptor = p;
        init();
        addMyFileChangedListener(l);
        this.no = no;
    }

    /**
     * @return the file being edited
     */
    public File getFile() {
        return file;
    }

    /**
     * set the file being edited
     */
    public void setFile(File f) {
        file = f;
        title = file.toString();
        for (int i = 0; i < listeners.size(); i++) {
            MyFileChangedListener l = listeners.get(i);
            if (l != null) {
                l.fileChanged(file);
            }
        }
    }

    /**
     * initialization
     */
    private void init() {
        listeners = new ArrayList<MyFileChangedListener>();
        undoManager = new UndoManager();
        needToSave = false;
        setLayout(new BorderLayout());

        textPane = new MyJTextPane(zeptor.getEditorOptions());
        JScrollPane scrollPane = new JScrollPane(textPane);

        add(scrollPane, BorderLayout.CENTER);
        textPane.addCaretListener(this);
        textPane.addDocumentListener(this);
        textPane.addUndoableEditListener(this);

        Box south = Box.createHorizontalBox();

        lineLabel = new JLabel(String.format(LINE_COL_FMT, textPane.getLineNo(),
                textPane.getColumnNo(),
                UIManager.getLookAndFeel().getName()
        ));
        south.add(lineLabel);

        add(south, BorderLayout.SOUTH);

        findDialog = new FindDialog(zeptor, textPane);
        replaceDialog = new FindReplaceDialog(zeptor, textPane);

    }

    public final boolean isCompiled() {
        return this.textPane.getCompileFlag();
    }

    public final void setCompileStatus(final boolean flag) {
        this.textPane.setCompileFlag(flag);
    }

    /**
     * Refresh the line number line number is inaccurate after refreshing and
     * reading a file
     */
    public void refreshLineNo() {
        lineLabel.setText(
                String.format(LINE_COL_FMT, textPane.getLineNo(), textPane.getColumnNo(),
                        UIManager.getLookAndFeel().getName()
                ));

        lineLabel.setHorizontalAlignment(JLabel.LEFT);
        this.caretUpdate(null);
    }

    /**
     * close the file
     */
    public void close() {

        if (needToSave) {
            int answer = -1;
            if (file != null) {

                answer = JOptionPane.showConfirmDialog(this,
                        String.format("File: '%s' is not saved!%nSave before exiting?", this.getTitle()),
                        "Save",
                        JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);

//            } else {
//
//                answer = JOptionPane.showConfirmDialog(this,
//                        String.format("File: '%s' is not saved!%nSave before exiting?", this.getTitle()),
//                        "Save",
//                        JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);

            }
            if (answer == JOptionPane.OK_OPTION) {
                save();
            }
        }

        zeptor.removeTextPane(this);
    }//end close()

    /**
     * Goto line
     *
     * @param line the line to go to
     */
    public void gotoLine(int line) {
        textPane.gotoLine(line);
        textPane.requestFocusInWindow();
    }

    /**
     * set the caret position
     *
     * @param pos the caret position
     */
    public void setCaretPosition(int pos) {
        textPane.setCaretPosition(pos);
        textPane.requestFocusInWindow();
    }

    /**
     * highlight a line
     *
     * @param line the line highlight
     */
    public void highlightLine(int line) {
        textPane.highlightLine(line);
    }

    /**
     * highlight portion of the text
     *
     * @param start the start position
     * @param end the end position
     */
    public void highlight(int start, int end) {
        textPane.highlight(start, end);
    }

    /**
     * As defined in CaretListener
     */
    public void caretUpdate(CaretEvent e) {

        lineLabel.setText(
                String.format(LINE_COL_FMT,
                        textPane.getLineNo(),
                        textPane.getColumnNo(),
                        UIManager.getLookAndFeel().getName()));

        lineLabel.setHorizontalAlignment(JLabel.LEFT);
    }

    /**
     * refresh the syntax highlighting
     */
    public void refresh() {
        textPane.refresh();
        refreshLineNo();
        this.caretUpdate(null);
    }

    /**
     * read the file
     *
     * @param f the file to be read
     */
    public void read(File f) throws IOException {
        setFile(f);
        FileReader reader = new FileReader(f);
        textPane.read(reader, null);
        needToSave = false;
        textPane.setCaretPosition(0);
        reader.close();
    }

    /**
     * override the grab focus method so that the textPane will grab focus
     */
    public void grabFocus() {
        textPane.requestFocusInWindow();
    }

    public final String getText() {
        return this.textPane.getText();
    }

    public final void setText(final String text) {
        this.textPane.setText(text);
    }//end setText

    /**
     * Save the file
     */
    public void save() {

        if (file != null) {
            try {
                FileWriter writer = new FileWriter(file, false);
                //do not append
                //do not use this method as it seems to have a bug
                //of adding extra characters
                String s = textPane.getText();
                writer.write(s, 0, s.length());
                needToSave = false;
                writer.flush();
                writer.close();
            } catch (IOException ex) {
                Logger.LOG.logTrap(ex);
                JOptionPane.showConfirmDialog(this, "Error writing to file " + file, "Error", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            saveAs(false);
        }
    }

    /**
     * Save to a file
     */
    public void saveAs(final boolean saveAsFlag) {  //pass boolean for true = save, false = saveAS

        JFileChooser chooser = new JFileChooser();

        chooser.setSize(350, 400);//W x H

        chooser.setDialogTitle("Save ZeptoN Source File");

        chooser.setAcceptAllFileFilterUsed(false);

        MyFileFilter filter = new MyFileFilter("zept", "ZeptoN Source File (.zept)");

        chooser.addChoosableFileFilter(filter);
        chooser.setDialogType(JFileChooser.SAVE_DIALOG);

        chooser.setMultiSelectionEnabled(false);

        try {
            String zepSource = textPane.getText();

            int progIdent = zepSource.indexOf("prog ");

            int openBrace = zepSource.indexOf("{", progIdent);

            String prog = zepSource.substring(progIdent + 5, openBrace);

            prog = prog.trim();

            chooser.setCurrentDirectory(new File(Zeptor.getOutputPath()));
            chooser.setSelectedFile(new File(prog + ZepT.FILE_SOURCE_EXT));
        } catch (Exception ex) {
            Logger.LOG.logTrap(ex);
        }//end try

        int returnVal = -1;

        String buttonText = "";

        if (saveAsFlag) {
            buttonText = "Save As ";
        } else {
            buttonText = "Save ";
        }

        returnVal = chooser.showDialog(this, buttonText);

        if (returnVal == JFileChooser.APPROVE_OPTION) {

            File f = chooser.getSelectedFile();
            StringBuilder b = new StringBuilder(f.toString());
            //check the extension
            int i = b.length() - 1;
            boolean found = false;

            while ((i > 0) && (!found)) {
                if (b.charAt(i) == '.') {
                    found = true;
                } else {
                    i--;
                }
            }
            if (!found) { //no extension, add java
                f = new File(b.toString() + ".zept");
            }
            //check if file exist
            if (!f.exists()) {
                setFile(f);
                save();
                //remove so that can relabel the tabe title
                close();
                zeptor.openFile(file);
            } else {
                int answer = JOptionPane.showConfirmDialog(this,
                        f + " exists! Overwrite?",
                        "Overwrite File",
                        JOptionPane.YES_NO_OPTION);
                if (answer == JOptionPane.OK_OPTION) {
                    setFile(f);
                    save();
                    //remove so that can relabel the tabe title
                    close();
                    zeptor.openFile(file);
                }
            }
        }
    }

    /**
     * Method defines in DoucmentListener interface
     */
    public void changedUpdate(DocumentEvent e) {
    }

    /**
     * Method defines in DoucmentListener interface
     */
    public void insertUpdate(DocumentEvent e) {
        needToSave = true;
    }

    /**
     * Method defines in DoucmentListener interface
     */
    public void removeUpdate(DocumentEvent e) {
        needToSave = true;
    }

    /**
     * Add a file change listener
     *
     * @param listener the listener to be added
     */
    public void addMyFileChangedListener(MyFileChangedListener listener) {
        listeners.add(listener);
    }

    /**
     * set the editor options
     *
     * @param o the editor options
     */
    public void setEditorOptions(EditorOptions o) {
        textPane.setEditorOptions(o);
    }

    /**
     * As defined in the undoable edit listener
     */
    public void undoableEditHappened(UndoableEditEvent e) {
        //remember the edit
        UndoableEdit edit = e.getEdit();
        //brute force method
        String presentation = edit.getPresentationName();
        if (presentation.equals("addition") || presentation.equals("deletion")) {
            undoManager.addEdit(edit);
            zeptor.undoMenuItemSetEnabled(true);
            zeptor.redoMenuItemSetEnabled(true);
        }

    }//end undoableEditHappened

    /**
     * undo
     */
    public void undo() {
        try {
            undoManager.undo();//it seem that it must do both time to undo the changes

            if (!undoManager.canUndo()) {
                zeptor.undoMenuItemSetEnabled(false);
            }
        } catch (CannotUndoException ex) {
            Logger.LOG.logTrap(ex);
        }//end try

    }//end undo

    public void redo() {
        try {
            undoManager.redo();

            if (!undoManager.canRedo()) {
                zeptor.redoMenuItemSetEnabled(false);

            }

        } catch (Exception ex) {
            Logger.LOG.logTrap(ex);
        }//end try

    }//end redo

    /**
     * copy the selected text to clipboard
     */
    public void copy() {
        textPane.copy();
    }

    /**
     * cut the selected text to clipboard
     */
    public void cut() {
        textPane.cut();
    }

    /**
     * clear the selected text
     */
    public void clear() {
        textPane.delete();
    }

    /**
     * select all text
     */
    public void selectAll() {
        textPane.selectAll();
    }

    /**
     * find text
     */
    public void find() {
        findDialog.setVisible(true);
    }

    /**
     * find text
     */
    public void findReplace() {
        replaceDialog.setVisible(true);
    }

    /**
     * gotoLine
     */
    public void gotoLine() {
        GotoDialog d = new GotoDialog(zeptor);
        if (!d.isCancelled()) {
            gotoLine(d.getLineNo());
        }
    }

    /**
     * paste the text from clipboard
     */
    public void paste() {
        textPane.paste();
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the title
     *
     * @param t the title
     */
    public void setTitle(String t) {
        title = t;
    }
}
