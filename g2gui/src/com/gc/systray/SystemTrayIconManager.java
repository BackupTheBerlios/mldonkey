package com.gc.systray;

import java.util.LinkedList;

import net.mldonkey.g2gui.view.G2Gui;



/**
 * This is the manager class. It may bee seen as a wrapper for the native code.
 * But also provides support for the rigth-click popup throught a JPoupupMenu.
 * 
 * Source from "http://members.lycos.co.uk/gciubotaru/systray/"
 * adapted to fit g2gui's needs by Dek in March 2004
 *
 * @author <a href="mail:gciubotaru@yahoo.com">George Ciubotaru</a>
 * @version 1.0
 * 
 */


public class SystemTrayIconManager {

    /**
     * Defines the mouse left click
     */
    public static final int mouseLeftClick = 0;
    /**
     * Defines the mouse right click
     */
    public static final int mouseRightClick = 1;
    /**
     * Defines the mouse double left click
     */
    public static final int mouseLeftDoubleClick = 2;
    /**
     * Defines the mouse double right click
     */    
    public static final int mouseRightDoubleClick = 6;
    /**
     * Defines mouse-movement on Tray-Icon
     */    
    public static final int mouseMove = 3;

    private int image;
    private String tooltip;
    private LinkedList listeners = new LinkedList();
    private int handler = 0;

    private boolean mouseOnPopup = false;

    private static String dllName = new String("DesktopIndicator");
	public static boolean libLoaded;

    static {		
    	// try to load the library
		try {
			System.loadLibrary(dllName);
		} 
		catch (ExceptionInInitializerError e){
			/*sometimes seen in gcj-environment*/
			libLoaded = false;
		}catch (UnsatisfiedLinkError e) {
			// thrown when the library is not found or can't be loaded
			libLoaded = false;
			e.printStackTrace();			
		} catch (SecurityException e) {
			// thrown because of security reasons, check policies
			libLoaded = false;
			
		}		
		libLoaded=true;
		
		if (G2Gui.debug) System.out.println(dllName+".dll"+" loaded: "+libLoaded);
    }


    private final int DISTANCE = 1000; // a positive value


    /**
     * The class doesn't have a default constructor because the need for the icon.
     *
     * @param image an int that represent the icon resource
     * @param tooltip the tooltip string
     */
    public SystemTrayIconManager(int image, String tooltip) {
    	
        this.image = image;
        this.tooltip = tooltip;
    }

    /**
     * Add sys tray icon listener
     *
     * @param obj the listener to be added
     */
    public void addSystemTrayIconListener(SystemTrayIconListener obj) {
        listeners.add(obj);
    }

    /**
     * Remove sys tray icon listener
     *
     * @param obj the listener to be removed
     */
    public void removeSystemTrayIconListener(SystemTrayIconListener obj) {
        listeners.remove(obj);
    }




    /**
     * Internal use - will be called from the native code
     */
    private void fireClicked(int buttonType, int x, int y) {
        //System.out.println("" + buttonType + ", " + x + ", " + y);
        for (int i = 0; i < listeners.size(); i++) {
            SystemTrayIconListener listener =
                (SystemTrayIconListener)listeners.get(i);
            /**
             * depending on button type call listener's
             *  mouseClickLeftButton
             *  ...
             */          
            switch (buttonType) {
                case (mouseLeftClick):
                    listener.mouseClickedLeftButton(x,y, this);
                    break;
                case (mouseRightClick):
                    listener.mouseClickedRightButton(x,y, this);
                    break;
                case (mouseLeftDoubleClick):
                    listener.mouseLeftDoubleClicked(x,y, this);
                    break;
                case (mouseRightDoubleClick):
                    listener.mouseRightDoubleClicked(x,y, this);
                    break;
            }
        }
    }

    /**
     * This static methos is used to load an icon in the
     * native code and returns an identifier of this image
     *
     * @param filename the file that contains the icon (in ico file usualy)
     * @return -1 in case of error
     */
    public static int loadImage(String filename) {
        try	{
                return nativeLoadImage( filename );
        }
        catch( UnsatisfiedLinkError x )	{
        	System.out.println(x.getMessage());
               return -1;
       }
    }

    /**
     * Load an image form resource
     *
     * @param resourceNo rsource number
     * @return -1 in case of error
     */
    public static int loadImageFromResource(int resourceNo) {
        try	{
                return nativeLoadImageFromResource( resourceNo );
        }
        catch ( UnsatisfiedLinkError x ) {
                return -1;
        }
    }

    /**
     * Free the memore ocupied by the icon image
     *
     * @param image the image ot me removed
     */
    public static void freeImage(int image) {
        try {
                nativeFreeImage( image );
        }
        catch( UnsatisfiedLinkError x )	{}
    }

    /**
     * Hide the icon sys tray
     *
     * @deprecated replace by the new setVisible(false)
     */
    public void hide() {
        try	{
                nativeHide();
        }
        catch( UnsatisfiedLinkError x )	{}
    }

    /**
     * Show the icon sys tray
     *
     * @deprecated replace by the new setVisible(true)
     */
    public void show() {
        try	{
                nativeEnable( image, tooltip );
        }
        catch( UnsatisfiedLinkError x )	{}
    }

    /**
     * Change the icon and tooltip
     *
     * @param image the new image
     * @param tooltip thenew tooltip
     */
    public void update(int image, String tooltip) {
        this.image = image;
        this.tooltip = tooltip;

        try	{
                nativeEnable( image, tooltip );
        }
        catch( UnsatisfiedLinkError x )	{}
    }

    /**
     * The new method used to show or hide the icon
     *
     * @param status true - show icon
     */
    public void setVisible(boolean status) {
        if (status)
            show();
        else
            hide();
    }

    /**
     * finaalize method
     */
    public void finalize() {
        nativeDisable();
    }

    /**
     * native methods
     */
    private synchronized native void nativeDisable() throws UnsatisfiedLinkError;
    private synchronized native void nativeEnable( int image, String tooltip ) throws UnsatisfiedLinkError;
    private synchronized static native void nativeFreeImage( int image ) throws UnsatisfiedLinkError;
    private synchronized static native int nativeLoadImage( String filename ) throws UnsatisfiedLinkError;
    private synchronized static native int nativeLoadImageFromResource( int inResource ) throws UnsatisfiedLinkError;
    private synchronized native void nativeHide() throws UnsatisfiedLinkError;
    private synchronized native void nativeMoveToFront(String title) throws UnsatisfiedLinkError;
   
}
