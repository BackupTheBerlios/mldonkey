/*
 * Copyright 2003
 * g2gui Team
 *
 *
 * This file is part of g2gui.
 *
 * g2gui is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * g2gui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with g2gui; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package net.mldonkey.g2gui.view.pref;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

import net.mldonkey.g2gui.helper.RegExp;
import net.mldonkey.g2gui.view.helper.CGridLayout;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;


/**
 * G2GuiWinReg - associate link types with the application in the windows registry
 *
 * @version $Id: G2GuiWinReg.java,v 1.1 2003/11/06 03:27:29 zet Exp $
 *
 */
public class G2GuiWinReg extends PreferencePage {
    RegisterLink[] registerLinks;

    /**
     * @param title
     * @param style
     */
    protected G2GuiWinReg(String title, int style) {
        super(title, style);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
     */
    protected void createFieldEditors() {
        Composite composite = getFieldEditorParent();
        composite.setLayout(CGridLayout.createGL(1, 5, 5, 5, 5, false));

        registerLinks = new RegisterLink[ 3 ];

        registerLinks[ 0 ] = new RegisterLink("ed2k", composite);
        registerLinks[ 1 ] = new RegisterLink("magnet", composite);
        registerLinks[ 2 ] = new RegisterLink("sig2dat", composite);

        Button button = new Button(composite, SWT.NONE);
        button.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        button.setText(G2GuiResources.getString("BTN_UPDATE_REGISTRY"));

        button.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent e) {
                    if (changedLinkPrefs()) {
                        createRegFile();
                    }
                }
            });
    }

    /**
     * @return true if we should update the registry
     */
    private boolean changedLinkPrefs() {
        for (int i = 0; i < registerLinks.length; i++) {
            if (registerLinks[ i ].getSelection() != RegisterLink.NO_CHANGE) {
                return true;
            }
        }

        return false;
    }

    /**
     * Create the registry file
     */
    private void createRegFile() {
        String appName = "g2gui";
        String currentDir;
        FileOutputStream out;
        PrintStream p;
        File thisDir = new File(".");

        try {
            currentDir = thisDir.getCanonicalPath() + System.getProperty("file.separator");
        } catch (Exception e) {
            System.err.println("getCanonicalPath: " + e);
            return;
        }

        try {
            String regFile = currentDir + appName + ".reg";
            String exeFile = currentDir + appName + ".exe";
            String prefFile = currentDir + appName + ".pref";

            exeFile = RegExp.replaceAll(exeFile, "\\\\", "\\\\");
            prefFile = RegExp.replaceAll(prefFile, "\\\\", "\\\\");

            out = new FileOutputStream(regFile);
            p = new PrintStream(out);
            
            p.println("Windows Registry Editor Version 5.00");

            for (int i = 0; i < registerLinks.length; i++) {
                switch (registerLinks[ i ].getSelection()) {
                case RegisterLink.REGISTER:
                    registerType(p, registerLinks[ i ].getText(), exeFile, prefFile);

                    break;

                case RegisterLink.UNREGISTER:
                    unregisterType(p, registerLinks[ i ].getText());

                    break;

                default:
                    break;
                }
            }

            p.close();

            updateRegistry(regFile);
        } catch (Exception e) {
            System.err.println("createRegFile: " + e);
        }
    }

    /**
     * Spawn regedit passing the regfile as the parameter
     * Requires regedit.exe to be in the system path
     * @param regFile
     */
    private void updateRegistry(String regFile) {
        String[] cmd = new String[ 2 ];

        cmd[ 0 ] = "regedit.exe";
        cmd[ 1 ] = regFile;

        Runtime rt = Runtime.getRuntime();

        try {
            Process p = rt.exec(cmd);
        } catch (Exception e) {
            System.err.println("updateRegistry: " + e);
        }
    }

    /**
     * @param p
     * @param name
     * @param exeFile
     * @param prefFile
     */
    private void registerType(PrintStream p, String name, String exeFile, String prefFile) {
        p.println("[HKEY_CLASSES_ROOT\\" + name + "]");
        p.println("@=\"URL: " + name + " Protocol\"");
        p.println("\"URL Protocol\"=\"\"");
        p.println("[HKEY_CLASSES_ROOT\\" + name + "\\shell]");
        p.println("[HKEY_CLASSES_ROOT\\" + name + "\\shell\\open]");
        p.println("[HKEY_CLASSES_ROOT\\" + name + "\\shell\\open\\command]");
        p.println("@=\"\\\"" + exeFile + "\\\" \\\"-c\\\" \\\"" + prefFile + "\\\" \\\"%1\\\"\"");
    }

    /**
     * @param p
     * @param name
     * @param exeFile
     * @param prefFile
     */
    private void unregisterType(PrintStream p, String name) {
        p.println("[-HKEY_CLASSES_ROOT\\" + name + "\\shell\\open\\command]");
        p.println("[-HKEY_CLASSES_ROOT\\" + name + "\\shell\\open]");
        p.println("[-HKEY_CLASSES_ROOT\\" + name + "\\shell]");
        p.println("[-HKEY_CLASSES_ROOT\\" + name + "]");
    }

    private class RegisterLink {
        public static final int NO_CHANGE = 0;
        public static final int REGISTER = 1;
        public static final int UNREGISTER = 2;
        private int selection;
        private String text;

        public RegisterLink(String text, Composite parent) {
            this.text = text;
            selection = NO_CHANGE;
            createContents(parent);
        }

        /**
         * @param parent
         */
        protected void createContents(Composite parent) {
            Group group = new Group(parent, SWT.SHADOW_ETCHED_IN);
            group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            group.setLayout(CGridLayout.createGL(1, 5, 5, 5, 5, false));
            group.setText(text + "://");

            createButton(group, G2GuiResources.getString("BTN_NO_CHANGE"), true, NO_CHANGE);
            createButton(group, G2GuiResources.getString("BTN_REGISTER"), false, REGISTER);
            createButton(group, G2GuiResources.getString("BTN_UNREGISTER"), false, UNREGISTER);
        }

        /**
         * @param group
         * @param text
         * @param select
         * @param type
         */
        private void createButton(Group group, String text, boolean select, final int type) {
            Button button = new Button(group, SWT.RADIO);
            button.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            button.setText(text);
            button.setSelection(select);
            button.addSelectionListener(new SelectionAdapter() {
                    public void widgetSelected(SelectionEvent e) {
                        selection = type;
                    }
                });
        }

        /**
         * @return int (the selection type)
         */
        public int getSelection() {
            return selection;
        }

        /**
         * @return String 
         */
        public String getText() {
            return text;
        }
    }
}


/*
$Log: G2GuiWinReg.java,v $
Revision 1.1  2003/11/06 03:27:29  zet
initial

*/