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
	public void init(IPreferenceStore preferences,Core core);
	
	/**
	 * used to build the tab's content
	 * @param parent the parent Control
	 * @return the top-level control of the tab
	 */
	public Control createContents(Composite parent);
	
	/** 
	 * the tab's name, like "search", "transfer" etc.
	 * @return a String containing the tab's name
	 */
	public String getName();
	
	/**
	 * the tab's icon's name, like "search.png", "transfer.png" etc.
	 * @return a String containing the icon's name, (w/o path)
	 */
	public String getIconFilename();
	
	/**
	 * called when the main Application Window is closing
	 * used to clean up the tab's data
	 */
	public void mainWindowClosing();
	
	/**
	 * called when the "Refresh" button is selected from the Application Window Menu
	 */
	public void forceRefresh();
	
	/**
	 * called when the tab is called to the foreground
	 */
	public void activated();
	
	/**
	 * called when the tab is placed in the background
	 */
	public void deactivated();

}
