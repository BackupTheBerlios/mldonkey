/*
 * Created on 07.07.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package net.mldonkey.g2gui.view.statistic;

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
	long createTime;
	
	int GraphColor[] = new int[3];
	
	public Graph(int[] color)
	{
		System.out.println("Graph created");
		createTime = System.currentTimeMillis();
	}
	
	public void addPoint(double value)
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
			lastPoint.setNext(nextPoint);
			lastPoint = nextPoint;
			
		}
		//System.out.println(lastPoint.getTime());		
	}
	
	public int getAmount()
	{
		StatisticPoint currentPoint = firstPoint;
		int i = 1;
		while (currentPoint.getNext() != null)
		{
			currentPoint = currentPoint.getNext();
			i++;
			
		}
		return i;
		
	}
	public StatisticPoint getLast()
	{
		return lastPoint;
	}
	public int[] getGraphColor()
	{
		return GraphColor;
	}
	
	
	

}
