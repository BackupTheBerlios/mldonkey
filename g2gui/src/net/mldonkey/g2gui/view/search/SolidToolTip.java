/*
 * Copyright 2003
 * G2Gui Team
 * 
 * 
 * This file is part of G2Gui.
 *
 * G2Gui is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * G2Gui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with G2Gui; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 */
package net.mldonkey.g2gui.view.search;

import net.mldonkey.g2gui.model.ResultInfo;
import net.mldonkey.g2gui.view.helper.WidgetFactory;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

/**
 * SolidToolTip
 *
 * @version $Id: SolidToolTip.java,v 1.4 2003/12/04 08:47:29 lemmy Exp $ 
 *
 */
public class SolidToolTip extends ToolTip {
	private StyledText details;
	
	/**
	 * @param aParent
	 */
	public SolidToolTip( Composite aParent ) {
		super(aParent);
	}

	/*
	 * (non-Javadoc)
	 * @see net.mldonkey.g2gui.view.search.ToolTip#create(org.eclipse.swt.widgets.Event)
	 */
	public void create( Event e ) {
		ResultInfo aResult = getResult( e );
		if ( aResult == null ) return;
		
		shell = new Shell(parent.getShell());
		shell.setText("result details");
		shell.setLayout(WidgetFactory.createGridLayout(1, 2, 2, 0, 0, false));
		shell.setBackground(backGround);
		
		// title field
		this.title = createCLabel( shell );
		this.title.setImage( aResult.getToolTipImage() );
		this.title.setText( aResult.getName() );
		
		createSeparator( shell );

		// detail field
		this.details = new StyledText( shell, SWT.NONE );
		this.details.setForeground(foreGround);
		this.details.setBackground(backGround);
		this.details.setLayoutData(new GridData(GridData.FILL_HORIZONTAL |
				GridData.VERTICAL_ALIGN_CENTER));
		this.details.setEditable( false );
		this.details.setText( aResult.getToolTipString() );
		Menu popupMenu = new Menu( this.details );
		MenuItem menuItem = new MenuItem( popupMenu, SWT.PUSH );
		menuItem.setText( G2GuiResources.getString( "MISC_COPY" ) );
		menuItem.setImage( G2GuiResources.getImage( "copy" ) );
		menuItem.addListener( SWT.Selection,
				new Listener() {
					public void handleEvent( Event event ) {
						details.copy();
					}
				} );

		menuItem = new MenuItem( popupMenu, SWT.PUSH );
		menuItem.setText( G2GuiResources.getString( "MISC_SELECT_ALL" ) );
		menuItem.setImage( G2GuiResources.getImage( "plus" ) );
		menuItem.addListener( SWT.Selection,
				new Listener() {
					public void handleEvent( Event event ) {
						details.selectAll();
					}
				} );
		this.details.setMenu( popupMenu );
		
		// alt names field
		if ( aResult.getNames().length > 1 ) {
			createSeparator( shell );
			this.altNames = new MyList(shell, SWT.H_SCROLL | SWT.V_SCROLL);
		 	( (MyList) altNames ).add( aResult.getSortedNames() );
		}

		setupShell( e );
		this.shell.forceFocus();
	}
	
	/**
	 * class MyList 
	 */
	private class MyList extends List {
		private List self;
		private GridData gridData;
		public MyList(Composite parent, int style) {
			super(parent, style);
			self = this;
			gridData = new GridData( GridData.FILL_HORIZONTAL );
			gridData.heightHint = 23;
			this.setLayoutData(gridData);
			this.setForeground(foreGround);
			this.setBackground(backGround);
			
			Menu popupMenu = new Menu( this );
			MenuItem menuItem = new MenuItem( popupMenu, SWT.PUSH );
			menuItem.setText( G2GuiResources.getString( "MISC_COPY" ) );
			menuItem.setImage( G2GuiResources.getImage( "copy" ) );
			menuItem.addListener( SWT.Selection,
					new Listener() {
						public void handleEvent( Event event ) {
							int i = self.getSelectionIndex();
							if ( i != -1 ) {
								Clipboard clipboard = new Clipboard( self.getDisplay() );
								clipboard.setContents( new Object[] { self.getItem( i ) },
									new Transfer[] { TextTransfer.getInstance() } );
								clipboard.dispose();						
							}
						}
					} );
			this.setMenu( popupMenu );
		}
		
		public void add( String[] strings ) {
			for ( int i = 0; i < strings.length; i++ ) {
				this.add( strings[ i ] );
			}
		}
		
		public void dispose() {
			if ( !this.isDisposed() )
				super.dispose();
		}
		
		protected void checkSubclass() {
		}
	}	
}

/*
$Log: SolidToolTip.java,v $
Revision 1.4  2003/12/04 08:47:29  lemmy
replaced "lemmstercvs01" and "lemmster" with "lemmy"

Revision 1.3  2003/12/01 08:45:37  lemmy
cosmetic

Revision 1.2  2003/11/30 09:31:26  lemmy
ToolTip complete reworked (complete)

Revision 1.1  2003/11/29 13:03:54  lemmy
ToolTip complete reworked (to be continued)

*/