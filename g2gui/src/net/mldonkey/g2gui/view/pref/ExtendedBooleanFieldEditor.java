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

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.Composite;


/**
 * ExtendedBooleanFieldEditor
 *
 * @author $user$
 * @version $Id: ExtendedBooleanFieldEditor.java,v 1.1 2003/06/30 17:29:54 dek Exp $ 
 *
 */
public class ExtendedBooleanFieldEditor extends BooleanFieldEditor {
	private boolean hasChanged;

	private Composite temp;

	/**
	 * @param name name of the BooleanFieldEditor
	 * @param labelText LabelText, that appears on screen
	 * @param shell where to place this nice thing in?
	 */
	public ExtendedBooleanFieldEditor( String name, String labelText, Composite shell ) {
		super( name, labelText, shell );
		temp = new Composite( shell,SWT.NONE );
			GridData gridData = new GridData();
			gridData.horizontalSpan=2;
			
			gridData.horizontalAlignment=GridData.BEGINNING;
		temp.setLayoutData( gridData );
		temp.setLayout(new GridLayout() );
		
		//Now we tell our parent, that we like to have it the 2 column-way
			( ( GridLayout ) shell.getLayout() ).numColumns=2;	
					
		//now put this whole control inside this nice composite			
			this.getChangeControl( shell ).setParent( temp );		
	}

	/**
	 * @param selected is this BooleanField checked or not
	 */
	public void setSelection(boolean selected){
		getChangeControl( temp ).setSelection( selected );
		
	}

	/**
	 * @param toolTipText The text, that should appear in a nice tooltip-way
	 */
	public void setToolTipText( String toolTipText ) {
		getChangeControl( temp ).setToolTipText( toolTipText );
		
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.BooleanFieldEditor#setEnabled(boolean, org.eclipse.swt.widgets.Composite)
	 */
	public void setEnabled(boolean arg0, Composite arg1) {		
		super.setEnabled(arg0, temp);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.BooleanFieldEditor#valueChanged(boolean, boolean)
	 */
	protected void valueChanged(boolean arg0, boolean arg1) {
		super.valueChanged(arg0, arg1);
		this.hasChanged = true;
		
	}


	
	

	/**
	 * @return
	 */
	public boolean HasChanged() {
		return hasChanged;
	}

	/**
	 * @return the state of this field as String "true" or "false"
	 */
	public String getValue() {
		return Boolean.toString(this.getBooleanValue());		
	}

}

/*
$Log: ExtendedBooleanFieldEditor.java,v $
Revision 1.1  2003/06/30 17:29:54  dek
Saving all the options in General works now (not validated for strings/int/yet)

*/