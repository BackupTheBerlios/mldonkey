/*
 * Copyright 2003 g2gui Team
 *
 *
 * This file is part of g2gui.
 *
 * g2gui is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * g2gui is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * g2gui; if not, write to the Free Software Foundation, Inc., 59 Temple Place,
 * Suite 330, Boston, MA 02111-1307 USA
 *
 */
package net.mldonkey.g2gui.view.pref;

import org.eclipse.jface.preference.FileFieldEditor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;

import java.io.File;


/**
 * GCJFileFieldEditor
 * Temp class to circumvent MingW PR/6652
 *
 * @version $Id: GCJFileFieldEditor.java,v 1.2 2003/11/27 20:45:51 zet Exp $
 */
public class GCJFileFieldEditor extends FileFieldEditor {
    private String[] extensions = null;
    private boolean alwaysValid = false;

    public GCJFileFieldEditor(String name, String labelText, boolean enforceAbsolute,
        Composite parent) {
        super(name, labelText, enforceAbsolute, parent);
    }

    public GCJFileFieldEditor(String name, String labelText, boolean enforceAbsolute,
        Composite parent, boolean alwaysValid) {
        this(name, labelText, enforceAbsolute, parent);
        this.alwaysValid = alwaysValid;
    }

    protected String changePressed() {
        String input = getTextControl().getText();

        if (input.equals("") && SWT.getPlatform().equals("win32"))
            input = ".";

        File f = new File(input);

        if (!f.exists())
            f = null;

        File d = getFile(f);

        if (d == null)
            return null;

        return d.getAbsolutePath();
    }

    private File getFile(File startingDirectory) {
        FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);

        if (startingDirectory != null)
            dialog.setFileName(startingDirectory.getPath());

        if (extensions != null)
            dialog.setFilterExtensions(extensions);

        String file = dialog.open();

        if (file != null) {
            file = file.trim();

            if (file.length() > 0)
                return new File(file);
        }

        return null;
    }

    public void setFileExtensions(String[] extensions) {
        this.extensions = extensions;
    }

    public boolean isValid() {
        if (alwaysValid)
            return true;
        else
            return super.isValid();
    }

    protected boolean checkState() {
        if (alwaysValid)
            return true;
        else
            return super.checkState();
    }
}


/*
 * $Log: GCJFileFieldEditor.java,v $
 * Revision 1.2  2003/11/27 20:45:51  zet
 * alwaysValid option
 *
 * Revision 1.1  2003/10/21 03:11:45  zet
 * circumvent gcj bug
 *
 */
