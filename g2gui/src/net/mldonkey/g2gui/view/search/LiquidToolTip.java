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

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * LiquidToolTip
 *
 * @version $Id: LiquidToolTip.java,v 1.1 2003/11/29 13:03:54 lemmster Exp $ 
 *
 */
public class LiquidToolTip extends ToolTip {
	private Label details;
	/**
	 * @param aParent
	 */
	public LiquidToolTip( Composite aParent ) {
		super( aParent );
	}

	/* (non-Javadoc)
	 * @see net.mldonkey.g2gui.view.search.ToolTip#create()
	 */
	public void create( Event e ) {
		ResultInfo aResult = getResult( e );
		if ( aResult == null ) return;
		
		shell = new Shell(parent.getShell(), SWT.ON_TOP);
		shell.setLayout(WidgetFactory.createGridLayout(1, 2, 2, 0, 0, false));
		shell.setBackground(backGround);
		
		this.title = createCLabel( shell );
		this.title.setImage( aResult.getToolTipImage() );
		this.title.setText( aResult.getName() );
		
		createSeparator( shell );

		this.details = createLabel( shell );
		this.details.setText( aResult.getToolTipString() );

		if ( aResult.getNames().length > 1 ) {
			this.altNames = createLabel( shell );
			( (Label) this.altNames ).setText( "bla\nbla\n" );
		}

		createSeparator( shell );

		Label aLabel = (Label) createLabel( shell );
		aLabel.setFont( JFaceResources.getTextFont() );
		aLabel.setText( "Press F2 for focus" );
		
		setupShell( e );
	}

	/**
	 * 
	 * @param aComposite
	 * @return
	 */
	private Label createLabel( Composite aComposite ) {
		Label aLabel = new Label(aComposite, SWT.NONE);
		aLabel.setForeground(foreGround);
		aLabel.setBackground(backGround);
		aLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL |
				GridData.VERTICAL_ALIGN_CENTER));
		return aLabel;
	}
}

/*
$Log: LiquidToolTip.java,v $
Revision 1.1  2003/11/29 13:03:54  lemmster
ToolTip complete reworked (to be continued)

*/