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

import java.util.ResourceBundle;

import net.mldonkey.g2gui.model.NetworkInfo;
import net.mldonkey.g2gui.model.ResultInfo;
import net.mldonkey.g2gui.model.Tag;
import net.mldonkey.g2gui.view.MainTab;

import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

/**
 * ResultTableLabelProvider
 *
 * @author $user$
 * @version $Id: ResultTableLabelProvider.java,v 1.8 2003/08/14 12:57:03 zet Exp $ 
 *
 */
public class ResultTableLabelProvider implements ITableLabelProvider, IColorProvider {
	
	private ResourceBundle bundle = ResourceBundle.getBundle( "g2gui" );
	private Color alreadyDownloadedColor = new Color(null, 41, 174, 57);
	

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#
	 * getColumnImage(java.lang.Object, int)
	 */
	public Image getColumnImage( Object arg0, int arg1 ) {
		if (arg0 instanceof ResultInfo && arg1 == 0) {
			ResultInfo resultInfo = ( ResultInfo ) arg0;
			return MainTab.getNetworkImage( resultInfo.getNetwork().getNetworkType() );
		}
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
		
		if ( arg0 instanceof ResultInfo ) {
		ResultInfo resultInfo = ( ResultInfo ) arg0;
		
			switch ( arg1 ) {
						
				case 0: // network id
					return " " + ( ( NetworkInfo ) resultInfo.getNetwork() ).getNetworkName();
				case 1: // name
					return ""+ resultInfo.getNames()[ 0 ];
				case 2: // size
					return "" + resultInfo.getStringSize();
				case 3: // format
					return "" + resultInfo.getFormat();
				case 4: // type
					return "" + resultInfo.getType();
				case 5: // metadata
				/* check if we have Tags at all (Fix for strange SWT-exception when searching)*/
					Tag[] tags = resultInfo.getTags();
					if (tags != null){					
						Tag aTag = resultInfo.getTags()[ 0 ];
						if ( aTag.getValue() > 20 )
							return "" + bundle.getString( "RTLP_HIGH" );
						else if ( aTag.getValue() > 10 )
							return "" + bundle.getString( "RTLP_NORMAL" );
						else
							return "" + bundle.getString( "RTLP_LOW" );
					}
					else return "";
				default: 
					return "";
			
			}	
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
	public void dispose() {
		alreadyDownloadedColor.dispose();
	}

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
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IColorProvider#getBackground(java.lang.Object)
	 */
	public Color getBackground( Object element ) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IColorProvider#getForeground(java.lang.Object)
	 */
	public Color getForeground( Object element ) {
		if ( element instanceof ResultInfo ) {
			ResultInfo resultInfo = ( ResultInfo ) element;
			if ( !resultInfo.getHistory() )
				return alreadyDownloadedColor;
		}
		return null;
	}
}

/*
$Log: ResultTableLabelProvider.java,v $
Revision 1.8  2003/08/14 12:57:03  zet
fix nullpointer in clientInfo, add icons to tables

Revision 1.7  2003/08/14 12:44:44  dek
searching works now without errors

Revision 1.6  2003/08/12 07:26:03  lemmstercvs01
checkstyle applied

Revision 1.5  2003/08/08 23:33:49  zet
dispose color

Revision 1.4  2003/07/31 04:11:00  zet
searchresult changes

Revision 1.3  2003/07/27 18:45:47  lemmstercvs01
lots of changes

Revision 1.2  2003/07/24 16:20:10  lemmstercvs01
lots of changes

Revision 1.1  2003/07/23 16:56:28  lemmstercvs01
initial commit

*/