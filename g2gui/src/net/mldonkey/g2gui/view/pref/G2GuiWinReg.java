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

import java.io.FileOutputStream;
import java.io.PrintStream;

import net.mldonkey.g2gui.helper.RegExp;
import net.mldonkey.g2gui.view.helper.WidgetFactory;
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
 * @version $Id: G2GuiWinReg.java,v 1.9 2004/03/26 20:24:46 dek Exp $
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
        composite.setLayout(WidgetFactory.createGridLayout(2, 5, 5, 5, 5, false));

        registerLinks = new RegisterLink[ 4 ];

        registerLinks[ 0 ] = new RegisterLink("ed2k://","ed2k", composite);
        registerLinks[ 1 ] = new RegisterLink("magnet://","magnet", composite);
        registerLinks[ 2 ] = new RegisterLink("sig2dat://","sig2dat", composite);
        registerLinks[ 3 ] = new RegisterLink(".torrent",".torrent", composite);
        

        Button button = new Button(composite, SWT.NONE);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        button.setLayoutData(gd);
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
        
        currentDir = System.getProperty("user.dir") + System.getProperty("file.separator");

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
                    registerType(p, registerLinks[ i ].getName(), exeFile, prefFile);

                    break;

                case RegisterLink.UNREGISTER:
                    unregisterType(p, registerLinks[ i ].getName());

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
        String[] cmd = new String[ 3 ];

        cmd[ 0 ] = "regedit.exe";
        cmd[ 1 ] = "/s";
        cmd[ 2 ] = regFile;

        Runtime rt = Runtime.getRuntime();

        try {
            rt.exec(cmd);
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
        p.println("@=\"\\\"" + exeFile + "\\\" \\\"-c\\\" \\\"" + prefFile + "\\\" \\\"-l\\\" \\\"%1\\\"\"");
    }
    
    /**
     * 
     * @param p
     * @param name is kind of ".xyz"
     * @param exeFile
     * @param prefFile
     */
    private void registerExtension(PrintStream p, String name, String exeFile, String prefFile){
    	
    	p.println("[HKEY_CLASSES_ROOT\\" + name + "]");
    	p.println("[HKEY_CLASSES_ROOT\\" + name + "\\shell]");
    	p.println("[HKEY_CLASSES_ROOT\\" + name + "\\DefaultIcon]");
    	p.println("@=\"\\\"" + exeFile + "\"");
    	
    	p.println("[HKEY_CLASSES_ROOT\\" + name + "\\shell\\open]");
    	p.println("[HKEY_CLASSES_ROOT\\" + name + "\\shell\\open\\command]");
    	p.println("@=\"\\\"" + exeFile + "\\\" \\\"-c\\\" \\\"" + prefFile + "\\\" \\\"-l\\\" \\\"%1\\\"\"");
    	
    	
    }
    /*
    
    [HKEY_CLASSES_ROOT\.torrent\shell\open\command]
    @="\"C:\\Gnu\\msys\\1.0\\home\\g2gui-test\\g2gui\\g2gui.exe\" \"%1\""
    [HKEY_CLASSES_ROOT\.torrent\DefaultIcon]
    @="C:\\Programme\\Kazaa Lite\\dksigtool.exe"
    */

    /**
     * @param p
     * @param name
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
		private String caption;
		private String name;

        public RegisterLink(String caption, String name, Composite parent) {
        	this.caption = caption;
            this.name = name;
            selection = NO_CHANGE;
            createContents(parent);
        }

        /**
         * @param parent
         */
        protected void createContents(Composite parent) {
            Group group = new Group(parent, SWT.SHADOW_ETCHED_IN);
            group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            group.setLayout(WidgetFactory.createGridLayout(1, 5, 5, 5, 5, false));
            group.setText(caption);
            createButton(group, G2GuiResources.getString("BTN_NO_CHANGE"), NO_CHANGE);
            createButton(group, G2GuiResources.getString("BTN_REGISTER"), REGISTER);
            createButton(group, G2GuiResources.getString("BTN_UNREGISTER"), UNREGISTER);
           
        }

        /**
         * @param group
         * @param text
         * @param select
         * @param type
         */
        private void createButton(Group group, String text, final int type) {
            Button button = new Button(group, SWT.RADIO);
            button.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            button.setText(text);
            button.setSelection(type == NO_CHANGE);
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
        public String getName() {
            return name;
        }
    }
}


/*
$Log: G2GuiWinReg.java,v $
Revision 1.9  2004/03/26 20:24:46  dek
.torrent is added to registry

Revision 1.8  2003/12/04 08:47:27  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.7  2003/11/23 17:58:03  lemmy
removed dead/unused code

Revision 1.6  2003/11/22 02:24:29  zet
widgetfactory & save sash postions/states between sessions

Revision 1.5  2003/11/20 14:05:15  lemmy
link need the "-l" prefix by now

Revision 1.4  2003/11/09 17:11:45  zet
minor

Revision 1.3  2003/11/09 16:44:13  zet
silent

Revision 1.2  2003/11/06 15:26:25  zet
use System instead of io.File

Revision 1.1  2003/11/06 03:27:29  zet
initial

*/
