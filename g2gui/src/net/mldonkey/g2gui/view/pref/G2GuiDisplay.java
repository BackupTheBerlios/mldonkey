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
 * (  at your option  ) any later version.
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
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.swt.widgets.Composite;
/**
 * G2Gui_Display
 *
 *
 * @version $Id: G2GuiDisplay.java,v 1.3 2003/10/12 16:27:01 lemmster Exp $
 */
public class G2GuiDisplay extends PreferencePage {
	/**
	 * DOCUMENT ME!
	 * 
	 * @param string The name of the OptionsPage
	 * @param i Style: SWT.XXXX
	 */
	public G2GuiDisplay( String string, int i ) {
		super( string, i );
	}

	/* (   non-Javadoc   )
	 * @see org.eclipse.jface.preference.FieldEditorPreferencePage#createFieldEditors()
	 */
	protected void createFieldEditors() {
		Composite composite = getFieldEditorParent();
		
		setupEditor( 
			new ExtendedColorFieldEditor( 
				"consoleBackground",
				G2GuiResources.getString( "PREF_DISPLAY_CONSOLE_OBK" ),
				composite ) );

		setupEditor( 
			new ExtendedColorFieldEditor( 
				"consoleForeground",
				G2GuiResources.getString( "PREF_DISPLAY_CONSOLE_OFG" ),
				composite ) );

		setupEditor( 
			new ExtendedColorFieldEditor( 
				"consoleHighlight",
				G2GuiResources.getString( "PREF_DISPLAY_CONSOLE_HL" ),
				composite ) );

		setupEditor( 
			new ExtendedColorFieldEditor( 
				"consoleInputBackground",
				G2GuiResources.getString( "PREF_DISPLAY_CONSOLE_IBK" ),
				composite ) );

		setupEditor( 
			new ExtendedColorFieldEditor( 
				"consoleInputForeground",
				G2GuiResources.getString( "PREF_DISPLAY_CONSOLE_IFG" ),
				composite ) );

		setupEditor( 
			new ExtendedFontFieldEditor2( 
				"consoleFontData",
				G2GuiResources.getString( "PREF_DISPLAY_CONSOLE_FONT" ),
				G2GuiResources.getString( "PREF_DISPLAY_SAMPLE" ),
				composite ) );
				
		setupEditor( 
			new BooleanFieldEditor( 
				"flatInterface",
				G2GuiResources.getString( "PREF_DISPLAY_FLAT_INTERFACE" ),
				composite ) );		
		
		setupEditor( 
			new BooleanFieldEditor( 
				"useGradient",
				G2GuiResources.getString( "PREF_DISPLAY_USE_GRADIENT" ),
				composite ) );			
				
	}
}
/*
$Log: G2GuiDisplay.java,v $
Revision 1.3  2003/10/12 16:27:01  lemmster
minor changes

Revision 1.2  2003/10/08 01:12:16  zet
useGradient preference

Revision 1.1  2003/10/01 20:56:27  lemmster
add class hierarchy

Revision 1.29  2003/09/18 10:23:48  lemmster
checkstyle

Revision 1.28  2003/09/15 22:06:19  zet
split preferences

Revision 1.27  2003/09/14 09:01:15  lemmster
show nodes on request

Revision 1.26  2003/09/10 16:55:32  dek
chunks are hidden in simple-mode, so is the corresponding option..

Revision 1.25  2003/08/29 20:24:42  dek
icon for preferences, and simple frame (group)

Revision 1.24  2003/08/29 18:30:29  dek
removed name, added shadow to mldonkey-options for previewing

Revision 1.23  2003/08/29 17:33:20  zet
remove headerbar

Revision 1.22  2003/08/29 17:13:29  dek
all content is now within a group, do you like it, or should i revert changes?

Revision 1.21  2003/08/29 14:59:34  dek
cleaning up, jFaced the whole thing even more, removed not needed methods & calls..
now the whole thing is kind of small ;-)

Revision 1.20  2003/08/28 18:27:32  zet
configurable flat interface

Revision 1.19  2003/08/26 22:44:03  zet
basic filtering

Revision 1.18  2003/08/23 15:21:37  zet
remove @author

Revision 1.17  2003/08/22 23:25:15  zet
downloadtabletreeviewer: new update methods

Revision 1.16  2003/08/22 21:10:57  lemmster
replace $user$ with $Author: lemmster $

Revision 1.15  2003/08/19 22:02:15  zet
localise

Revision 1.14  2003/08/19 13:16:10  dek
advanced-Mode introduced

Revision 1.13  2003/08/18 14:51:58  dek
some more jface-work

Revision 1.12  2003/08/18 00:24:44  zet
cleanup

Revision 1.11  2003/08/17 23:13:42  zet
centralize resources, move images

Revision 1.10  2003/08/17 16:57:55  zet
*** empty log message ***

Revision 1.9  2003/08/14 12:59:47  zet
fix typo

Revision 1.8  2003/08/14 12:57:03  zet
fix nullpointer in clientInfo, add icons to tables

Revision 1.7  2003/08/11 11:27:46  lemmstercvs01
display only connected servers added

Revision 1.6  2003/08/08 21:11:15  zet
set defaults in central location



*/