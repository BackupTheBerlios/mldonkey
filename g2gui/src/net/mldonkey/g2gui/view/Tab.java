package net.mldonkey.g2gui.view;

import net.mldonkey.g2gui.comm.Core;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * @author mitch
 *
 * the interface every tab has to implement
 */
public interface Tab {

	/**
	 * initializes the Tab logically
	 * @param preferences an object storing
	 * @param core the Core-object
	 */
	void init( IPreferenceStore preferences, Core core );
	
	/**
	 * used to build the tab's content
	 * @param parent the parent Control
	 * @return the top-level control of the tab
	 */
	Control createContents( Composite parent );
	
	/** 
	 * the tab's name, like "search", "transfer" etc.
	 * @return a String containing the tab's name
	 */
	String getName();
	
	/**
	 * the tab's icon's name, like "search.png", "transfer.png" etc.
	 * @return a String containing the icon's name, (w/o path)
	 */
	String getIconFilename();
	
	/**
	 * called when the main Application Window is closing
	 * used to clean up the tab's data
	 */
	void mainWindowClosing();
	
	/**
	 * called when the "Refresh" button is selected from the Application Window Menu
	 */
	void forceRefresh();
	
	/**
	 * called when the tab is called to the foreground
	 */
	void activated();
	
	/**
	 * called when the tab is placed in the background
	 */
	void deactivated();

}
/*
$Log: Tab.java,v $
Revision 1.3  2003/07/02 17:09:28  dek
minor checkstyle

*/