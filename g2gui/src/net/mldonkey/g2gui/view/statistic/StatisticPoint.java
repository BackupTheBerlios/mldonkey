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
 *A Statistic Point belongs to a Graph its unimportant to which Every Point knows its following point,
 *and the time it was created.
 */
public class StatisticPoint {
	
	StatisticPoint nextPoint;
	long createTime;
	double value;
	
	public StatisticPoint(double value_) {
		
			value = value_;
			createTime = System.currentTimeMillis();
			
		}	

	
	public long getTime()
	{
		return createTime;
	}
	
	public void setNext(StatisticPoint nextP)
	{
		nextPoint = nextP;
		
	}
	
	public StatisticPoint getNext()
		{
			return nextPoint;
		
		}
	public double getValue()
	{
		return 
		value;
		
	}
	

}
