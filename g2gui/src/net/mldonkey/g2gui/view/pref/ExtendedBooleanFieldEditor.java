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

import org.eclipse.jface.preference.BooleanFieldEditor;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;


/**
 * ExtendedBooleanFieldEditor - created to gain access to the Button
 *
 * @version $Id: ExtendedBooleanFieldEditor.java,v 1.9 2003/11/04 22:58:47 zet Exp $
 *
 */
public class ExtendedBooleanFieldEditor extends BooleanFieldEditor {
    public ExtendedBooleanFieldEditor(String name, String labelText, Composite parent) {
        super(name, labelText, parent);
    }

    public Button getChangeControl(Composite parent) {
        return super.getChangeControl(parent);
    }
}


/*
$Log: ExtendedBooleanFieldEditor.java,v $
Revision 1.9  2003/11/04 22:58:47  zet
Make "advanced user" option more noticeable

*/
