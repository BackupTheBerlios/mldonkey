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
 *
 * @version $Id: ExtendedColorFieldEditor.java,v 1.6 2003/09/08 19:46:25 lemmster Exp $
 */
public class ExtendedColorFieldEditor extends ColorFieldEditor {
	
	/**
	 * Creates a new ColorFieldEditor
	 * @param name ???
	 * @param labelText What is shown in preferences page
	 * @param composite where the whole thing takes place
	 */
	public ExtendedColorFieldEditor( String name, String labelText, Composite composite ) {
		super( name, labelText, composite );
	}
	/**
	 * @param parent of this control
	 * @return the "Change" button
	 */
	public Button getChangeControl( Composite parent ) {
		return super.getChangeControl( parent );
	}
}
/*
$Log: ExtendedColorFieldEditor.java,v $
Revision 1.6  2003/09/08 19:46:25  lemmster
just repaired the log

Revision 1.5  2003/08/25 13:17:25  dek
checkstyle

Revision 1.4  2003/08/23 15:21:37  zet
remove @author

Revision 1.3  2003/08/22 21:22:58  lemmster

*/
