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


/**
 * StatisticPoint belongs to a Graph its unimportant to which Every Point knows its following point,
 * and the time it was created.
 *
 * @author $Author: lemmster $
 * @version $Id: StatisticPoint.java,v 1.6 2003/08/22 21:13:11 lemmster Exp $
 * 
 */
public class StatisticPoint  {
	
	StatisticPoint nextPoint;
	StatisticPoint prevPoint;
	long createTime;
	int value;
	
	public StatisticPoint(int value) 
	{
		this.value = value;
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

	public int getValue()
	{
		return value;
	}

	public void setPrev(StatisticPoint prevP)
	{
		prevPoint = prevP;
	}
	
	public StatisticPoint getPrev()
	{
		return prevPoint;
	}
}
/*
$Log: StatisticPoint.java,v $
Revision 1.6  2003/08/22 21:13:11  lemmster
replace $user$ with $Author$

Revision 1.5  2003/07/26 05:42:39  zet
cleanup



*/