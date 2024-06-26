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

package xyz.zepton.zeptor.gui;

import javax.swing.*;
import java.awt.*;

@SuppressWarnings("serial")
public class AboutDialog extends JFrame {

    private final static String RELEASE_DATA   = "3.2.2";
    private final static String VERSION_DATA   = "4.16";
    private final static String COPYRIGHT_DATA = "June 2024";

    private final static String TEXTAREA_TITLE     = "";
    private final static int    TEXTAREA_COLUMNS   = 55;
    private final static int    TEXTAREA_FONT_SIZE = 15;
    private final static String TEXTAREA_FONT_NAME = "Monospaced";

    private final static String APP_NAME  = "    Zeptor - ZeptoN Source Code Transpiler Editor (ZEPtoN + ediTOR)                        \n\r";
    private final static String COPYRIGHT = "    Copyright \u00A9 " + COPYRIGHT_DATA + " by William F. Gilreath. All Rights Reserved. \n\r\n\r";
    private final static String LICENSE   = "    License: GNU General Public License (GPL) v 3.0.                         \n\r";
    private final static String VERSION   = "    Zeptor Version " + VERSION_DATA + " Release " + RELEASE_DATA + "                                        \n\r";

    private final static String BLANK_LINE = "                                                                             \n\r";
    private final JTextArea textArea = new JTextArea();
    private final JFrame jframe;

    private final static String JDK_INFO = String.format("    Platform - %s %s %s %s %s",
            System.getProperty("java.vendor"),
            System.getProperty("java.runtime.version"),
            System.getProperty("os.name"),
            System.getProperty("os.version"),
            System.getProperty("os.arch")
    );

    public AboutDialog() {

        this.jframe = new JFrame("");

        textArea.setColumns(AboutDialog.TEXTAREA_COLUMNS);
        textArea.setLineWrap(false);
        textArea.setWrapStyleWord(false);
        textArea.setFont(new Font(AboutDialog.TEXTAREA_FONT_NAME, Font.BOLD, AboutDialog.TEXTAREA_FONT_SIZE));
        textArea.setBackground(Color.WHITE);
        textArea.setForeground(Color.DARK_GRAY);

        textArea.append(AboutDialog.BLANK_LINE);
        textArea.append(AboutDialog.BLANK_LINE);

        textArea.append(AboutDialog.APP_NAME);
        textArea.append(AboutDialog.BLANK_LINE);
        textArea.append(AboutDialog.VERSION);

        textArea.append(AboutDialog.JDK_INFO);

        textArea.append(AboutDialog.BLANK_LINE);
        textArea.append(AboutDialog.BLANK_LINE);

        textArea.append(AboutDialog.COPYRIGHT);
        textArea.append(AboutDialog.LICENSE);

        textArea.append(AboutDialog.BLANK_LINE);
        textArea.append(AboutDialog.BLANK_LINE);

        textArea.setSize(textArea.getPreferredSize().width, 1);

        this.jframe.pack();

    }//end AboutDialog

    @Override
    public void setVisible(final boolean isVisible) {
        if (isVisible) {
            JOptionPane.showMessageDialog(this.jframe, this.textArea, AboutDialog.TEXTAREA_TITLE, JOptionPane.PLAIN_MESSAGE, null);
        }//end if

    }//end setVisible

}//end AboutDialog
