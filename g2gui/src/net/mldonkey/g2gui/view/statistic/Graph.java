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
package net.mldonkey.g2gui.view.statistic;

import org.eclipse.swt.graphics.Color;

/**
 * Graph is a List of StatisticPoints only knowing the first and the last Point,
 * a Graph also has got a Color. And knows when it was started
 *
 *
 * @version $Id: Graph.java,v 1.15 2003/09/18 11:30:18 lemmster Exp $
 */
public class Graph {
    public static final short MAX_POINTS = 1600;
    private int[] iPoints = new int[ MAX_POINTS ];
    private int insertAt = 0;
    private String graphName;
    private Color graphColor1;
    private Color graphColor2;
    private int amount;
    private int maxValue;
    private int avgValue;
    private long sumValue;

	/**
	 * DOCUMENT ME!
	 * 
	 * @param name DOCUMENT ME!
	 * @param color1 DOCUMENT ME!
	 * @param color2 DOCUMENT ME!
	 */
    public Graph( String name, Color color1, Color color2 ) {
        graphName = name;
        graphColor1 = color1;
        graphColor2 = color2;
        sumValue = 0;
        avgValue = 0;
        maxValue = 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getInsertAt() {
        return insertAt;
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getPointAt( int i ) {
        return iPoints[ i ];
    }

    /**
     * DOCUMENT ME!
     *
     * @param width DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int findMax( int width ) {
        int max = 20;
        int searchPoint = insertAt - 1;
        if ( width > amount )
            width = amount;
        for ( int i = 0; i < width; i++ ) {
            if ( searchPoint < 0 )
                searchPoint = MAX_POINTS - 1;
            if ( iPoints[ searchPoint ] > max )
                max = iPoints[ searchPoint ];
            searchPoint--;
        }
        return max;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNewestPoint() {
        int newestPoint = insertAt - 1;
        if ( newestPoint < 0 )
            newestPoint = MAX_POINTS - 1;
        return iPoints[ newestPoint ];
    }

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     */
    public void addPoint( int value ) {
        if ( insertAt > ( MAX_POINTS - 1 ) )
            insertAt = 0;
        iPoints[ insertAt++ ] = value;
        if ( value > maxValue )
            maxValue = value;
        sumValue += value;
        amount++;
        avgValue = ( int ) ( sumValue / ( long ) amount );
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getAmount() {
        return amount;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Color getGraphColor1() {
        return graphColor1;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Color getGraphColor2() {
        return graphColor2;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getMax() {
        return maxValue;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return graphName;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getAvg() {
        return avgValue;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Color getColor1() {
        return graphColor1;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Color getColor2() {
        return graphColor2;
    }
}

/*
$Log: Graph.java,v $
Revision 1.15  2003/09/18 11:30:18  lemmster
checkstyle

Revision 1.14  2003/09/18 11:28:51  lemmster
checkstyle

Revision 1.13  2003/09/16 01:18:52  zet
min size/check bounds

Revision 1.12  2003/09/14 22:22:55  zet
*** empty log message ***

Revision 1.11  2003/09/13 22:23:55  zet
use int array instead of creating stat point objects

Revision 1.10  2003/08/23 15:21:37  zet
remove @author

Revision 1.9  2003/08/22 21:13:11  lemmster
replace $user$ with $Author: lemmster $

Revision 1.8  2003/08/09 00:42:18  zet
dispose colors

Revision 1.7  2003/07/26 17:54:14  zet
fix pref's illegal setParent, redo graphs, other

Revision 1.6  2003/07/26 05:42:39  zet
cleanup



*/
