/**
 * This intreface should be implemented by a class that want to manage
 * the icon events.
 * 
 * Source from "http://members.lycos.co.uk/gciubotaru/systray/"
 * adapted to fit g2gui's needs by Dek in March 2004
 *
 * @author <a href="mail:gciubotaru@yahoo.com">George Ciubotaru</a>
 * @version 1.0
 * 
 */
package com.gc.systray;
public interface SystemTrayIconListener
{
    /**
     * This method will be called when the icon is left clicked
     *
     * @param pos the mouse position on the screen when mouse click
     * @param source the SystemTrayIconManager instance
     */
    public void mouseClickedLeftButton(int x, int y, SystemTrayIconManager source);

    /**
     * This method will be called when the icon is right clicked
     *
     * @param pos the mouse position on the screen when mouse click
     * @param source the SystemTrayIconManager instance
     */
    public void mouseClickedRightButton(int x, int y, SystemTrayIconManager source);

    /**
     * This method will be called when the icon is double right clicked
     *
     * @param pos the mouse position on the screen when mouse click
     * @param source the SystemTrayIconManager instance
     */
    public void mouseRightDoubleClicked(int x, int y, SystemTrayIconManager source);

    /**
     * This method will be called when the icon is double left clicked
     *
     * @param pos the mouse position on the screen when mouse click
     * @param source the SystemTrayIconManager instance
     */
    public void mouseLeftDoubleClicked(int x, int y, SystemTrayIconManager source);
}
