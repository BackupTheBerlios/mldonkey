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
 * Graph is a List of StatisticPoints only knowing the first and the last Point, a Graph also has got a Color. And 
 * knows when it was started
 * 
 * @author $Author: lemmster $
 * @version $Id: Graph.java,v 1.9 2003/08/22 21:13:11 lemmster Exp $
 */
public class Graph {
	
	StatisticPoint firstPoint;
	StatisticPoint lastPoint;

	int amount, maxValue, avgValue;
	long createTime, totalPoints, sumValue;
	
	static final short MAX_POINTS = 1600;

	String graphName;
	Color graphColor1, graphColor2;
	
	public Graph(String name, Color color1, Color color2)
	{
		graphName = name;
		graphColor1 = color1;
		graphColor2 = color2;
		
		sumValue = totalPoints = avgValue = maxValue = 0;
		// System.out.println("Graph created");
		createTime = System.currentTimeMillis();
	}
	
	public void addPoint(int value)
	{
		//System.out.println("pointAdded");
		if (firstPoint == null)
		{
			firstPoint = new StatisticPoint(value);
			lastPoint = firstPoint;
			
		}
		else
		{
			StatisticPoint nextPoint = new StatisticPoint(value);
			nextPoint.setPrev(lastPoint);
			lastPoint.setNext(nextPoint);
			lastPoint = nextPoint;
						
			if (amount >= Graph.MAX_POINTS) {
				StatisticPoint tmpPoint = firstPoint;
				firstPoint = firstPoint.getNext();
				firstPoint.setPrev(null);
				tmpPoint.setNext(null);
				tmpPoint.setPrev(null);
			}
			
		}
		
		if (value > maxValue) maxValue = value;
		sumValue += value;
		amount++;
		avgValue = (int) (sumValue/(long)amount);
		
	}
	
	public int getAmount()
	{
		return amount;
	}
	public StatisticPoint getLast()
	{
		return lastPoint;
	}
	public Color getGraphColor1()
	{
		return graphColor1;
	}
	public Color getGraphColor2()
	{
		return graphColor2;
	}
	public int getMax() 
	{
		return maxValue;
	}
	public String getName() 
	{
		return graphName;
	}
	public int getAvg() 
	{
		return avgValue;
	}

	public void dispose() {
		graphColor1.dispose();
		graphColor2.dispose();
	}

}
/*
$Log: Graph.java,v $
Revision 1.9  2003/08/22 21:13:11  lemmster
replace $user$ with $Author$

Revision 1.8  2003/08/09 00:42:18  zet
dispose colors

Revision 1.7  2003/07/26 17:54:14  zet
fix pref's illegal setParent, redo graphs, other

Revision 1.6  2003/07/26 05:42:39  zet
cleanup



*/