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

import gnu.trove.TIntArrayList;
import net.mldonkey.g2gui.view.pref.PreferenceLoader;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;


/**
 * Graph
 *
 * @version $Id: Graph.java,v 1.18 2003/10/17 15:36:02 zet Exp $
 */
public class Graph implements Runnable {
    public static final short MAX_POINTS = 1600;
    public static final short MAX_GRAPH_TYPES = 2;
    public static final int ONE_HOUR = 60 * 60000;
    private int[] iPoints = new int[ MAX_POINTS ];
    private int insertAt = 0;
    private String graphName;
    private Color graphColor1;
    private Color graphColor2;

    // arrayLists of primatives can't be synch'd..
    private TIntArrayList maxList, avgList;
    
    private int graphType;
    private long sumValue;
    private long hourlySumValue;
    private int amount;
    private int maxValue;
    private int avgValue;
    private int hourlyAmount;
    private int hourlyMaxValue;
    private int hourlyAvgValue;

    public Graph( String name ) {
        this.graphName = name;
        updateDisplay();

        this.maxList = new TIntArrayList( 0 );
        this.avgList = new TIntArrayList( 0 );

        sumValue = hourlySumValue = 0;
        graphType = avgValue = maxValue = 0;
        hourlyMaxValue = 0;
        hourlyAvgValue = 0;

        Display.getDefault(  ).timerExec( ONE_HOUR, this );
    }

    public int getInsertAt(  ) {
        return insertAt;
    }

    public int getPointAt( int i ) {
        return iPoints[ i ];
    }

    public int findMax( int width ) {
        int max = 20;
        int searchPoint = insertAt - 1;

        if ( width > amount ) {
            width = amount;
        }

        for ( int i = 0; i < width; i++ ) {
            if ( searchPoint < 0 ) {
                searchPoint = MAX_POINTS - 1;
            }

            if ( iPoints[ searchPoint ] > max ) {
                max = iPoints[ searchPoint ];
            }

            searchPoint--;
        }

        return max;
    }

    public int getNewestPoint(  ) {
        int newestPoint = insertAt - 1;

        if ( newestPoint < 0 ) {
            newestPoint = MAX_POINTS - 1;
        }

        return iPoints[ newestPoint ];
    }

    public void addPoint( int value ) {
        if ( insertAt > ( MAX_POINTS - 1 ) ) {
            insertAt = 0;
        }

        iPoints[ insertAt++ ] = value;

        if ( value > maxValue ) {
            maxValue = value;
        }

        sumValue += value;
        amount++;
        avgValue = (int) ( sumValue / (long) amount );

        if ( value > hourlyMaxValue ) {
            hourlyMaxValue = value;
        }

        hourlySumValue += value;
        hourlyAmount++;
        hourlyAvgValue = (int) ( hourlySumValue / (long) hourlyAmount );
    }

    public int getAmount(  ) {
        return amount;
    }

    public Color getGraphColor1(  ) {
        return graphColor1;
    }

    public Color getGraphColor2(  ) {
        return graphColor2;
    }

    public int getMax(  ) {
        return maxValue;
    }

    public String getName(  ) {
        return graphName;
    }

    public int getAvg(  ) {
        return avgValue;
    }

    public Color getColor1(  ) {
        return graphColor1;
    }

    public Color getColor2(  ) {
        return graphColor2;
    }

    public void run(  ) {
        maxList.add( hourlyMaxValue );
        avgList.add( hourlyAvgValue );

        hourlyMaxValue = hourlyAvgValue = 0;
        hourlySumValue = hourlyAmount = 0;

        Display.getDefault(  ).timerExec( ONE_HOUR, this );
    }

    public void toggleDisplay(  ) {
        graphType++;

        if ( graphType > MAX_GRAPH_TYPES ) {
            graphType = 0;
        }
    }

    public int getGraphType(  ) {
        return graphType;
    }

    public TIntArrayList getMaxList(  ) {
        return maxList;
    }

    public TIntArrayList getAvgList(  ) {
        return avgList;
    }

    public void clearHistory(  ) {
		sumValue = 0;
		graphType = avgValue = maxValue = 0;
		insertAt = 0;
        maxList.clear(  );
        avgList.clear(  );
    }
    
    public void updateDisplay() {
		graphColor1 = PreferenceLoader.loadColour( "graph" + graphName + "Color1" );
		graphColor2 = PreferenceLoader.loadColour( "graph" + graphName + "Color2" );
    }
    
}


/*
$Log: Graph.java,v $
Revision 1.18  2003/10/17 15:36:02  zet
graph colour prefs

Revision 1.17  2003/09/25 16:32:47  zet
clear both graphs (history & current)

Revision 1.16  2003/09/20 22:08:41  zet
basic graph hourly history

Revision 1.13  2003/09/16 01:18:52  zet
min size/check bounds

Revision 1.12  2003/09/14 22:22:55  zet
*** empty log message ***

Revision 1.11  2003/09/13 22:23:55  zet
use int array instead of creating stat point objects

Revision 1.10  2003/08/23 15:21:37  zet
remove @author

Revision 1.9  2003/08/22 21:13:11  lemmster
replace $user$ with $Author: zet $

Revision 1.8  2003/08/09 00:42:18  zet
dispose colors

Revision 1.7  2003/07/26 17:54:14  zet
fix pref's illegal setParent, redo graphs, other

Revision 1.6  2003/07/26 05:42:39  zet
cleanup



*/
