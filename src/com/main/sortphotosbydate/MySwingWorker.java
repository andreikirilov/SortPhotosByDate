package com.main.sortphotosbydate;

import javax.swing.*;
import java.io.PrintWriter;
import java.io.StringWriter;

public class MySwingWorker extends SwingWorker<Void, Void> {

    @Override
    public Void doInBackground() {
        try {
            SortPhotosByDate.startSortPhotoByDate(Interface.jTextField.getText());
        } catch (Exception e) {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            Interface.jTextArea.append(errors.toString());
        }
        return null;
    }
}