package com.weather.risk.mfi.myfarminfo.home;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ExternalStorageGPS {
	static boolean mExternalStorageAvailable = false;
	static boolean mExternalStorageWriteable = false;

	public static boolean status(){
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			// We can read and write the media
			mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			// We can only read the media
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
		} else {
			// Something else is wrong. It may be one of many other states, but all we need
			//  to know is we can neither read nor write
			mExternalStorageAvailable = mExternalStorageWriteable = false;
		}
		//System.out.println("----------storageavailbe"+mExternalStorageAvailable);
		//System.out.println("----------StorageWriteable"+mExternalStorageWriteable);
		return mExternalStorageAvailable;
	}
	public static boolean chk_mount(){

		//System.out.println("-----Data DIRECTORY----"+Environment.getDataDirectory());
		//System.out.println("-----Root DIRECTORY----"+Environment.getRootDirectory());
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
		//------Saving file to data Directory--------//
	}
    /*
    for reading the file read_file("FileName") with out extension
    */
    
    public static void write_file(String filename,boolean append,String data){    	
    	if(ExternalStorageGPS.status()){
    		if(ExternalStorageGPS.chk_mount()){
    	File root = Environment.getExternalStorageDirectory();
    	    	    	
    	try{
    	if (root.canWrite()) 
    	 {
    		//MyService.Write_Progress=true;
    		 File myfile= new File(root+"/"+filename+".txt");
    		 
    		 if(!myfile.exists()){
    			 myfile.createNewFile();
    		 }
    		 
    		 FileWriter myFileWriter = new FileWriter(myfile,append);
    		    		
    		 BufferedWriter out = new BufferedWriter(myFileWriter); 
             out.write(data); 
             //System.out.println("Write inside "+filename);
             out.close();     
             myFileWriter.close();       
             //MyService.Write_Progress=false;
    	 }    	 
    	 }catch (IOException e) {  
    		 ExternalStorageGPS.write_file("Log_3", true, e.toString()+"****** in writing error****\n\r");
    		 e.printStackTrace();
		} 
    		}
    	}
    	
    }
    public static void write_log_file(String filename,boolean append,String data){    	
    	if(ExternalStorageGPS.status()){
    		if(ExternalStorageGPS.chk_mount()){
    	File root = Environment.getExternalStorageDirectory();
    	    	    	
    	try{
    	if (root.canWrite()) 
    	 {
    		//MyService.Write_Progress=true;
    		File logfile =  new File(root+"/"+"LogFiles");
    		if(!logfile.exists()){
    			logfile.mkdir();
    		}
    		 File myfile= new File(logfile+"/"+filename+".txt");
    		 
    		 if(!myfile.exists()){
    			 myfile.createNewFile();
    		 }
    		 
    		 FileWriter myFileWriter = new FileWriter(myfile,append);
    		    		
    		 BufferedWriter out = new BufferedWriter(myFileWriter); 
             out.write(data); 
             out.close();     
             myFileWriter.close();       
             //MyService.Write_Progress=false;
    	 }    	 
    	 }catch (IOException e) {  
    		//ExternalStorageGPS.write_file("Log_3", true, e.toString()+"****** in writing error****\n\r");
    		 e.printStackTrace();
		} 
    		}
    	}
    	
    }
    
    /*
     * for reading the file read_file("FileName") with out extension*?
    */
    
    public static String read_file(String filename){
    	File root = Environment.getExternalStorageDirectory();
    	 StringBuffer strContent = new StringBuffer("");
    	 try{
    	 if(root.canRead())
  	   {  		  
             FileReader myFileReader = new FileReader(root+"/"+filename+".txt");
             BufferedReader Readfile = new BufferedReader(myFileReader);
             
             int ch;
             while( (ch = Readfile.read()) != -1){
 				strContent.append((char)ch); 			
             }
             Readfile.close();
             myFileReader.close();
  	 }
    	 
	} catch (IOException e) {
		ExternalStorageGPS.write_file("Log_3", true, e.toString()+"******in reading error****\n\r");
		e.printStackTrace();
	} 	
	
	return strContent.toString();
    }

}