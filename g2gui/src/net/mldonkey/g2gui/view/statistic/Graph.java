/*
 * Created on 07.07.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package net.mldonkey.g2gui.view.statistic;

import org.eclipse.swt.graphics.Color;

/**
 * @author achim
 *
 * Graph is a List of StatisticPoints only knowing the first and the last Point, a Graph also has got a Color. And 
 * knows when it was started
 * 
 */
public class Graph {
	
	StatisticPoint firstPoint;
	StatisticPoint lastPoint;
	int amount;
	long createTime;
	
	Color graphColor;
	
	public Graph(Color color)
	{
		graphColor = color;
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
			
		}
		amount++;
		//System.out.println(lastPoint.getTime());		
	}
	
	public int getAmount()
	{

		return amount;
		
	}
	public StatisticPoint getLast()
	{
		return lastPoint;
	}
	
	public Color getGraphColor()
	{
		return graphColor;
	}
	
	
	

}
