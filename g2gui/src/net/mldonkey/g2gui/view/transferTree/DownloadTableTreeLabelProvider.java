/*
 * Copyright 2003
 * g2gui Team
 * 
 * 
 * This file is part of g2gui.
 *
 * g2gui is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * g2gui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with g2gui; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 */
 
package net.mldonkey.g2gui.view.transferTree;

import java.text.DecimalFormat;

import net.mldonkey.g2gui.model.ClientInfo;
import net.mldonkey.g2gui.model.FileInfo;
import net.mldonkey.g2gui.model.enum.EnumClientMode;
import net.mldonkey.g2gui.model.enum.EnumFileState;
import net.mldonkey.g2gui.model.enum.EnumState;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

/**
 * DownloadTableTreeLabelProvider
 *
 * @version $Id: DownloadTableTreeLabelProvider.java,v 1.19 2003/08/31 01:46:33 zet Exp $ 
 *
 */
public class DownloadTableTreeLabelProvider implements ITableLabelProvider, IColorProvider {
	
	protected Color unAvailableFileColor = new Color(null, 255,0,0);
	protected Color availableFileColor = new Color(null, 255,165,0);
	protected Color downloadedFileColor = new Color(null, 0,0,255);
	protected Color queuedFileColor = new Color(null, 192,192,192);
	protected Color pausedFileColor = new Color(null, 255, 0, 0);
	protected Color rateAbove20Color = new Color(null, 35, 214, 0);
	protected Color rateAbove10Color = new Color(null, 30, 170, 2);
	protected Color rateAbove0Color = new Color(null, 24, 142, 4);
	protected DecimalFormat df = new DecimalFormat( "0.0" );
	protected DecimalFormat dfp = new DecimalFormat( "0" );
	
	protected CustomTableTreeViewer tableTreeViewer;
	
	public Color getBackground (Object arg0) {
			return null;
	}
	
	public Image getColumnImage(Object object, int column) {
		return null;
	}
	
	public String getColumnText(Object object, int column) {
		return "";
	}
	
	public Color getForeground(Object arg0) {
		if (arg0 instanceof FileInfo) {
			FileInfo fileInfo = (FileInfo) arg0;
			if (fileInfo.getState().getState() == EnumFileState.QUEUED)
				return queuedFileColor;
			if (fileInfo.getState().getState() == EnumFileState.PAUSED)
				return pausedFileColor;
			if (fileInfo.getState().getState() == EnumFileState.DOWNLOADED)
				return downloadedFileColor;
			if (fileInfo.getRate() > 20f) 
				return rateAbove20Color;
			if (fileInfo.getRate() > 10f) 
				return rateAbove10Color;
			if (fileInfo.getRate() > 0f) 
				return rateAbove0Color;	
			return availableFileColor;
		} else if (arg0 instanceof TreeClientInfo) {
			ClientInfo clientInfo = ((TreeClientInfo) arg0).getClientInfo();
			if (clientInfo.getState().getState() == EnumState.CONNECTED_DOWNLOADING)
				return rateAbove0Color;
			else
				return null;
		}
		return null;
	}
	

	public String getFileETA (FileInfo fileInfo) {
		
		if ( fileInfo.getState().getState() == EnumFileState.QUEUED
			|| fileInfo.getState().getState() == EnumFileState.PAUSED )
			return "";
		return fileInfo.getStringETA();
	}

	public String getClientConnection(ClientInfo clientInfo) {
		if ( clientInfo.getClientKind().getClientMode() == EnumClientMode.FIREWALLED ) 
			return G2GuiResources.getString( "TT_Firewalled" ).toLowerCase();			
		else
			return G2GuiResources.getString( "TT_Direct" ).toLowerCase();	
	}
	
	public String getClientActivity( ClientInfo clientInfo) {
		if ( clientInfo.getState().getState() == EnumState.CONNECTED_DOWNLOADING )
			return G2GuiResources.getString( "TT_Transferring" ).toLowerCase();
		else 
			return G2GuiResources.getString( "TT_Rank" ).toLowerCase() + ": " + clientInfo.getState().getRank() ;
	}	

	public void addListener(ILabelProviderListener arg0) {
	}

	public void dispose() {
		unAvailableFileColor.dispose();
		availableFileColor.dispose();
		queuedFileColor.dispose();
		pausedFileColor.dispose();
		downloadedFileColor.dispose();
		rateAbove20Color.dispose();
		rateAbove10Color.dispose();
		rateAbove0Color.dispose();

	}

	public boolean isLabelProperty(Object arg0, String arg1) {
		return true;
	}
	public void removeListener(ILabelProviderListener arg0) {
	}
	public void setTableTreeViewer(CustomTableTreeViewer v) {
		tableTreeViewer = v;
	}
	
	

}

/*
$Log: DownloadTableTreeLabelProvider.java,v $
Revision 1.19  2003/08/31 01:46:33  zet
localise

Revision 1.18  2003/08/23 19:44:12  zet
split transfer table to basic/advanced modes

Revision 1.17  2003/08/23 15:21:37  zet
remove @author

Revision 1.16  2003/08/23 01:04:03  zet
*** empty log message ***

Revision 1.15  2003/08/22 23:25:15  zet
downloadtabletreeviewer: new update methods

Revision 1.14  2003/08/22 21:16:36  lemmster
replace $user$ with $Author: zet $

Revision 1.13  2003/08/20 14:58:43  zet
sources clientinfo viewer

Revision 1.12  2003/08/17 23:13:42  zet
centralize resources, move images

Revision 1.11  2003/08/17 19:16:36  vnc
cosmetics: added "%" sign

Revision 1.10  2003/08/16 20:05:19  zet
*** empty log message ***

Revision 1.9  2003/08/16 13:48:24  vnc
dispose() the two new added colors

Revision 1.8  2003/08/16 13:26:42  vnc
switched client transferring/ranking colors

Revision 1.7  2003/08/16 13:07:42  vnc
more file colors based on availability

Revision 1.6  2003/08/15 22:05:58  zet
*** empty log message ***

Revision 1.5  2003/08/14 12:57:03  zet
fix nullpointer in clientInfo, add icons to tables

Revision 1.4  2003/08/11 00:30:10  zet
show queued files

Revision 1.3  2003/08/08 02:46:31  zet
header bar, clientinfodetails, redo tabletreeviewer

Revision 1.2  2003/08/06 17:14:17  zet
2 new columns

Revision 1.1  2003/08/04 19:22:08  zet
trial tabletreeviewer


*/
