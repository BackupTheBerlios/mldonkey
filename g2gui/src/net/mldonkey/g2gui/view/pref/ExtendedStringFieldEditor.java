/*
 * Copyright 2003
 * G2GUI Team
 * 
 * 
 * This file is part of G2GUI.
 *
 * G2GUI is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * G2GUI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with G2GUI; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 */
package net.mldonkey.g2gui.view.pref;

import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;


/**
 * ExtendedStringFieldEditor
 *
 * @author $user$
 * @version $Id: ExtendedStringFieldEditor.java,v 1.2 2003/07/10 19:27:28 dek Exp $ 
 *
 */
public class ExtendedStringFieldEditor extends StringFieldEditor implements IValueEditor {
	private boolean hasChanged = false;
	private String defaultValue;

	/**
	 * Creates a new ExtendedStringFieldEditor with the given values
	 * @param name the name of the Edit-Field (don't know why, superclass wants it)
	 * @param Description The Text, that appears on the left side of the TextInputField
	 * @param parent were this Field shoudl be created
	 */
	public ExtendedStringFieldEditor( String name, String Description, Composite parent ) {
		super( name, Description, parent );
	}	
	
	/** (non-Javadoc)
	 * @see net.mldonkey.g2gui.view.pref.IValueEditor#getValue()
	 */
	public String getValue() {		
		return getStringValue();
	}
	/** (non-Javadoc)
	 * @see net.mldonkey.g2gui.view.pref.IValueEditor#hasChanged()
	 */
	public boolean hasChanged() {		
		return hasChanged;
	}
	
	/** (non-Javadoc)
	 * @see org.eclipse.jface.preference.StringFieldEditor#valueChanged()
	 */
	protected void valueChanged() {
		this.hasChanged = true;		
		super.valueChanged();
	}
	
	
	/** (non-Javadoc)
	 * @see org.eclipse.jface.preference.StringFieldEditor#setStringValue(java.lang.String)
	 */
	public void setStringValue( String arg0 ) {		
		super.setStringValue( arg0 );		
		if ( defaultValue == null ) defaultValue = arg0;
		this.hasChanged = false;
	}

	/**
	 * Adds a ToolTipText to the whole Control
	 * @param ToolTip the ToolTip-Text to add
	 */
	public void setToolTipText( String ToolTip ) {
		this.getLabelControl().setToolTipText( ToolTip );
		this.getTextControl().setToolTipText( ToolTip );
	}
	
	/** (non-Javadoc)
	 * @see net.mldonkey.g2gui.view.pref.IValueEditor#resetChangedStatus()
	 */
	public void resetChangedStatus() {
		hasChanged = false;		
	}
	/** (non-Javadoc)
	 * @see net.mldonkey.g2gui.view.pref.IValueEditor#restoreDefault()
	 */
	public void restoreDefault() {
		super.setStringValue( defaultValue );		
		resetChangedStatus();
	}
	

}

/*
$Log: ExtendedStringFieldEditor.java,v $
Revision 1.2  2003/07/10 19:27:28  dek
some idle-race cleanup

Revision 1.1  2003/07/08 16:59:23  dek
now the booleanValues are checkBoxes

*/