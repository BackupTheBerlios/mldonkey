/*
 * Copyright 2003
 * g2gui Team
 * 
 * 
 * This file is part of g2gui.
 *
 * g2gui is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * g2gui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with g2gui; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 */
package net.mldonkey.g2gui.helper;

import net.mldonkey.g2gui.view.G2Gui;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * ProgramExecutor
 *
 * @author $user$
 * @version $Id: ProgramExecutor.java,v 1.1 2004/03/25 18:30:37 psy Exp $ 
 *
 */
public class ProgramExecutor implements Runnable {
 	String[] cmdline;
 	
 	public ProgramExecutor(String[] string) {
 		cmdline = string;
 		
		Thread previewer = new Thread(this);
		//previewer.setDaemon(true);
		previewer.start();

 	}
 	
    public void run() {
   		try {
   			String outline,errline;
   			Process p = Runtime.getRuntime().exec(cmdline);
   			if (G2Gui.debug) {
        			BufferedReader input = new BufferedReader (new InputStreamReader(p.getInputStream()));
        			BufferedReader error = new BufferedReader (new InputStreamReader(p.getErrorStream()));
        			
        			while ( (outline = input.readLine()) != null | (errline = error.readLine()) != null) {
        				if (outline != null) System.out.println(outline);
        				if (errline != null) System.out.println(errline);
        			}
        			input.close();
   			} 
   		} 
   		catch (Exception err) {
   			err.printStackTrace();
   		}
    }
 	
}
/*
$Log: ProgramExecutor.java,v $
Revision 1.1  2004/03/25 18:30:37  psy
introduced http-preview

*/