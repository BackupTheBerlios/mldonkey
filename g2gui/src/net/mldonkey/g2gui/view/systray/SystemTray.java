package net.mldonkey.g2gui.view.systray;



import net.mldonkey.g2gui.view.MainWindow;
import net.mldonkey.g2gui.view.resource.G2GuiResources;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Menu;

import com.gc.systray.SystemTrayIconListener;
import com.gc.systray.SystemTrayIconManager;



/**
 * Test class
 */
public class SystemTray implements SystemTrayIconListener, Runnable {


	/**
	 * @author joerg
	 *
	 * To change the template for this generated type comment go to
	 * Window - Preferences - Java - Code Generation - Code and Comments
	 */
	private class ExitAction extends Action {
		public ExitAction() {
			super();
			setText( "Exit" );
			setImageDescriptor(G2GuiResources.getImageDescriptor("X"));
			
			;
		}
		public void run() {
			parent.getMinimizer().forceClose();
			parent.getShell().close();
		}

	}
	private Menu menu;
	private MenuManager popupMenu;
	private MainWindow parent;
	private SystemTrayIconManager mgr;

    /**
     * SystemTrayIconListener implementation
     */
    public void mouseClickedLeftButton(final int x, final int y, SystemTrayIconManager source) {
    System.out.println("left clicked");
    	parent.getShell().getDisplay().asyncExec( new Runnable () {
			public void run() {
				/* check for widget disposed */
				if ( parent.getShell().isDisposed() ) return;
				Menu menu = popupMenu.createContextMenu( parent.getShell() );
				menu.setLocation (x, y);
				menu.setVisible (true);
				
				
			}
		} );	
    	System.out.println("left clicked done");

    }
    public void mouseClickedRightButton(int x, int y, SystemTrayIconManager source) {
        System.out.println("right click at (" + x + ", " + y + ")");
    }
    public void mouseLeftDoubleClicked(int x, int y, SystemTrayIconManager source) {
        System.out.println("left double click at (" + x + ", " +y + ")");
    }
    public void mouseRightDoubleClicked(int x, int y, SystemTrayIconManager source) {
        System.out.println("right double click at (" + x + ", " + y + ")");
    }


    
    /**
	 * @param window
	 */
	public SystemTray(MainWindow window) {
		parent = window;		
		Thread tray = new Thread(this);
		tray.setDaemon(true);
		tray.run();
	}
	
	private class TestAction extends Action {
		public TestAction(int i) {
			super();
			setText( "Hello World - version"+i );
		}
		
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		System.loadLibrary("DesktopIndicator");		
		System.out.println(getClass().getName()+"g2gui.ico");
		int quick = SystemTrayIconManager.loadImage("g2gui.ico");
		if (quick == -1) {
			System.out.println("image icon.ico error");
			return;
		}
		
		mgr = new SystemTrayIconManager(quick, "Daemon?");
		mgr.addSystemTrayIconListener(this);
		mgr.setVisible(true);
		
		
		
		IMenuListener manager = new IMenuListener(  ){
			public void menuAboutToShow(IMenuManager manager) {
				System.out.println("ATTENTIONE A menu comes up");
				manager.add(new TestAction(1));
				manager.add(new TestAction(2));
				manager.add(new TestAction(3));
				manager.add(new Separator());
				manager.add(new TestAction(4));
				manager.add(new Separator());
				manager.add(new ExitAction());

				
			}};
		popupMenu = new MenuManager( "" );
		popupMenu.setRemoveAllWhenShown( true );
		popupMenu.addMenuListener( manager );
		
		
		
		
		
		
		parent.getShell().addDisposeListener(new DisposeListener(){

			public void widgetDisposed(DisposeEvent e) {
				mgr.finalize();				
				
			}});
		
	}
}