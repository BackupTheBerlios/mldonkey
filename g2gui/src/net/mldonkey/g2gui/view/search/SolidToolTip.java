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

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

/**
 * SolidToolTip
 *
 * @version $Id: SolidToolTip.java,v 1.1 2003/11/29 13:03:54 lemmster Exp $ 
 *
 */
public class SolidToolTip extends ToolTip {
	private StyledText details;
	private ToolTipHandler handler;
	/**
	 * @param aParent
	 */
	public SolidToolTip( Composite aParent, ToolTipHandler handler ) {
		super(aParent);
		this.handler = handler;
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
		shell.addDisposeListener( new DisposeListener() {
			public void widgetDisposed( DisposeEvent e ) {
				handler.setLocked( false );
			}
		} );
		
		this.title = createCLabel( shell );
		this.title.setImage( aResult.getToolTipImage() );
		this.title.setText( aResult.getName() );
		
		createSeparator( shell );

		this.details = new StyledText( shell, SWT.NONE );
		this.details.setForeground(foreGround);
		this.details.setBackground(backGround);
		this.details.setLayoutData(new GridData(GridData.FILL_HORIZONTAL |
				GridData.VERTICAL_ALIGN_CENTER));
		this.details.setEditable( false );
		this.details.setText( aResult.getToolTipString() );
		
		if ( aResult.getNames().length > 1 ) {
			createSeparator( shell );
			this.altNames = new MyList(shell, SWT.H_SCROLL | SWT.V_SCROLL);
		 	( (MyList) altNames ).add( aResult.getSortedNames() );
		}

		setupShell( e );
		handler.setLocked( true );
	}
	
	/**
	 * class MyList 
	 */
	private class MyList extends List {
		public MyList(Composite parent, int style) {
			super(parent, style);
			GridData gridData = new GridData();
			gridData.heightHint = 50;
			gridData.widthHint = parent.getBounds().width;
			this.setLayoutData(gridData);
			this.setForeground(foreGround);
			this.setBackground(backGround);
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
Revision 1.1  2003/11/29 13:03:54  lemmster
ToolTip complete reworked (to be continued)

*/