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

import net.mldonkey.g2gui.model.NetworkInfo;
import net.mldonkey.g2gui.model.ResultInfo;
import net.mldonkey.g2gui.model.Tag;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * ResultTableLabelProvider
 *
 * @author $user$
 * @version $Id: ResultTableLabelProvider.java,v 1.2 2003/07/24 16:20:10 lemmstercvs01 Exp $ 
 *
 */
public class ResultTableLabelProvider implements ITableLabelProvider {

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#
	 * getColumnImage(java.lang.Object, int)
	 */
	public Image getColumnImage( Object arg0, int arg1 ) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#
	 * getColumnText(java.lang.Object, int)
	 */
	public String getColumnText( Object arg0, int arg1 ) {
		/*
		 * Network id
		 * Name
		 * MD4
		 * Size
		 * Format
		 * Type
		 * Metadata
		 * Comment
		 * downloaded
		 */
		ResultInfo resultInfo = ( ResultInfo ) arg0;
		
		if ( arg1 == 0 ) // network id
			return ( ( NetworkInfo ) resultInfo.getNetwork() ).getNetworkName();
		else if ( arg1 == 1 ) // name
			return resultInfo.getNames()[ 0 ];
		else if ( arg1 == 2 ) // size
			return resultInfo.getStringSize();
		else if ( arg1 == 3 ) // format
			return resultInfo.getFormat();
		else if ( arg1 == 4 ) // type
			return resultInfo.getType();
		else if ( arg1 == 5 ) { //metadata
			Tag aTag = resultInfo.getTags()[ 0 ];
			if ( aTag.getValue() > 20 )
				return "high";
			else if ( aTag.getValue() > 10 )
				return "normal";
			else
				return "low";
		}	
		return "";
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#
	 * addListener(org.eclipse.jface.viewers.ILabelProviderListener)
	 */
	public void addListener( ILabelProviderListener arg0 ) { }

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
	 */
	public void dispose() { }

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#
	 * isLabelProperty(java.lang.Object, java.lang.String)
	 */
	public boolean isLabelProperty( Object arg0, String arg1 ) { 
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#
	 * removeListener(org.eclipse.jface.viewers.ILabelProviderListener)
	 */
	public void removeListener( ILabelProviderListener arg0 ) { }
}

/*
$Log: ResultTableLabelProvider.java,v $
Revision 1.2  2003/07/24 16:20:10  lemmstercvs01
lots of changes

Revision 1.1  2003/07/23 16:56:28  lemmstercvs01
initial commit

*/