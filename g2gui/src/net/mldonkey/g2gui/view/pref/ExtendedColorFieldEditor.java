/*
 * Created on Jul 24, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package net.mldonkey.g2gui.view.pref;

import org.eclipse.swt.widgets.*;
import org.eclipse.jface.preference.ColorFieldEditor;

/**
 * ExtendedColorFieldEditor
 *
 * @author $Author: lemmster $
 * @version $Id: ExtendedColorFieldEditor.java,v 1.2 2003/08/22 21:10:57 lemmster Exp $
 */
public class ExtendedColorFieldEditor extends ColorFieldEditor {
	
	/**
	 * Creates a new ColorFieldEditor
	 * @param name ???
	 * @param labelText What is shown in preferences page
	 * @param composite where the whole thing takes place
	 */
	public ExtendedColorFieldEditor( String name, String labelText, Composite composite ) {
		super(name, labelText, composite);
	}
	
	public Button getChangeControl(Composite parent) {
		return super.getChangeControl(parent);
	}
}
