/*
 * Created on 19.06.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package net.mldonkey.g2gui.helper;
import java.io.*;
/**
 * @author typhon
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ConfigFile {
	BufferedReader bufferedreader;
	BufferedWriter bufferedwriter;
	private String configfile="login.cfg";
	String data[]=new String[3];
	private int HOST=0;
	private int PORT=1;
	private int NAME=2;
	/**
	 * 
	 */
	public ConfigFile() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * writes down the config infos in the configfile
	 * @param data
	 */
	public void writeToFile(String[] data){
		this.data=data;
		System.out.println(data[0]);
		try{			
			bufferedwriter=new BufferedWriter(new FileWriter(configfile));
			for(int i=0;i<data.length;i++){
				bufferedwriter.write(data[i]);				
				bufferedwriter.newLine();
			}	
			bufferedwriter.close();					
		}catch(IOException ex){
			ex.printStackTrace();
		}	
	}
	/**
	 *read the login infos from the config file
	 */
	public void readFromFile(){
		data =new String[3];
		int i=0;
		String line;
		try{		
		bufferedreader=new BufferedReader(new FileReader(configfile));
		while((line=bufferedreader.readLine())!=null){			
			data[i]=line;
			i++;
		}
		bufferedreader.close();
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}
	public String getHostname(){
		return data[HOST];
	}
	public String getPort(){
		return data[PORT];
	}	
	public String getUsername(){
		return data[NAME];
	}	
	/**
	 * checks if the file exists
	 * @return boolean  
	 */
	public boolean isAvailable(){
		return new File(configfile).exists();
		}
}