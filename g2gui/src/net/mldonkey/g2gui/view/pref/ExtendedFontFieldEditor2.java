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

import org.eclipse.jface.preference.FontFieldEditor;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.*;

/**
 * ExtendedFontFieldEditor
 *
 * @author $user$
 * @version $Id: ExtendedFontFieldEditor2.java,v 1.3 2003/07/26 02:30:07 zet Exp $ 
 *
 */
public class ExtendedFontFieldEditor2 extends FontFieldEditor {
	
	private FontDialog fontDialog;
	private Label fontLabel, fontSample;
	private Button fontButton;
	private Font font;
	private boolean hasChanged = false;
	private FontData[] chosenFont;
	
	/**
	 * Creates a new FontEditor
	 * @param name ???
	 * @param labelText What is shown in preferences page
	 * @param sampleText The text, where the font is applyed 
	 * @param composite where the whole thing takes place
	 */
	public ExtendedFontFieldEditor2( String name, String labelText, String sampleText, Composite composite ) {
		init(name, labelText);
		fontDialog = new FontDialog( composite.getShell() );
		this.fontLabel = new Label( composite, SWT.NONE );
			fontLabel.setText( labelText );
		this.fontButton = new Button( composite, SWT.NONE );
			fontButton.setText( "select font" );
			fontButton.addSelectionListener(
					new SelectionListener() {
						public void widgetSelected( SelectionEvent e ) {
							fontDialog.open();
							font = new Font( null, fontDialog.getFontData() );	
							chosenFont = font.getFontData();						
							fontSample.setFont( font );
							fontSample.setSize( fontSample.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );
							hasChanged = true;
						}
						public void widgetDefaultSelected( SelectionEvent e ) {							
						} } );
		this.fontSample = new Label( composite, SWT.SHADOW_NONE );
			fontSample.setText( sampleText );			
			GridData gridData = new GridData();
			gridData.horizontalSpan = 1;
			gridData.grabExcessHorizontalSpace = true;
		fontSample.setLayoutData( gridData );
	}

	/**
	 * Has the font in this Control has been changed?
	 * @return changing status
	 */
	public boolean hasChanged() {
		return hasChanged;
	}
	
	/**
	 * Give me your font ;-)
	 * @return current font
	 */
	public Font getFont() {
		return font;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditor#adjustForNumColumns(int)
	 */
	protected void adjustForNumColumns( int arg0 ) {
		( ( GridData )fontSample.getLayoutData() ).horizontalSpan = ( arg0 - 2 );
		//super.adjustForNumColumns(arg0);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditor#doFillIntoGrid(org.eclipse.swt.widgets.Composite, int)
	 */
	protected void doFillIntoGrid( Composite arg0, int arg1 ) {
		super.doFillIntoGrid(arg0, arg1);

	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditor#doLoad()
	 */
	protected void doLoad() {
		chosenFont = PreferenceConverter.getFontDataArray(
						getPreferenceStore(),
						getPreferenceName());
		
		this.font = new Font(null, chosenFont);
		fontSample.setFont(font);
		fontSample.setSize( fontSample.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );
		super.doLoad();

	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditor#doLoadDefault()
	 */
	protected void doLoadDefault() {
		super.doLoadDefault();

	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditor#doStore()
	 */
	protected void doStore() {
		if (chosenFont != null)
			PreferenceConverter.setValue(
				getPreferenceStore(),
				getPreferenceName(),
				chosenFont);

	}

	/** (non-Javadoc)
	 * @see org.eclipse.jface.preference.FieldEditor#getNumberOfControls()
	 */
	public int getNumberOfControls() {		
		return super.getNumberOfControls();
	}

	/**
	 * a Value, that we can save in a preferenceStore
	 * @return the selected font
	 */
	public String toString() {
		
		return super.toString();
	}

	/**
	 * @param font
	 */
	

}

/*
$Log: ExtendedFontFieldEditor2.java,v $
Revision 1.3  2003/07/26 02:30:07  zet
seems to basically work.. i'm still not sure why we subclass?

Revision 1.2  2003/07/25 03:50:53  zet
damn fontfield.. will continue

Revision 1.9  2003/07/10 19:27:28  dek
some idle-race cleanup

Revision 1.8  2003/07/04 12:06:38  dek
*** empty log message ***

Revision 1.7  2003/07/03 22:56:43  mitch
fixed getFontData() (replaced by getFontList()[0] because of deprecation)

Revision 1.6  2003/07/03 10:23:20  dek
OK, the font-thing finally works

Revision 1.5  2003/07/03 10:14:56  dek
saving font now works

Revision 1.4  2003/07/03 09:42:34  dek
now saving Font as string representation

Revision 1.3  2003/07/03 08:59:06  dek
debugging deleted

Revision 1.2  2003/07/03 08:58:34  dek
how the hell do i store a font on disk...

Revision 1.1  2003/07/03 08:31:06  dek
first sketch of FontFieldeditor

*/