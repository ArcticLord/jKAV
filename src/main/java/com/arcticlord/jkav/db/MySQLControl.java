package com.arcticlord.jkav.db;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import com.arcticlord.jkav.utils.Settings;



public class MySQLControl{
	
	public static final int DB_ONLINE 		 = 0;
	public static final int DB_ACCESS_DENIED = 1;
	public static final int DB_OFFLINE		 = 2;
	public static final int DB_IO_ERROR		 = 3;
	public static final int DB_UNKNOWN_ERROR = 4;
	
	private boolean started;
	
	private Settings settings;
	
	public MySQLControl(Settings settings){
		this.settings = settings;
		started = false;
	}
	
	
	public int getState(){
		String[] cmd ={	"cmd", "/c", "mysqladmin",
					 	"-u", settings.username,
					 	"--password=" + settings.password,
					 	"ping" };
		

		CmdReturnInfo ret = executeShellCommand(cmd,null,true);

		// if Command Execution failed
		if (ret == null) return DB_IO_ERROR;
		
		System.out.println("state->"+ret.toString());
		
		// returnCode 0 - mysqld is running
		// returnCode 1 - mysqld is not running
		if(ret.returnCode == 1) return DB_OFFLINE;
		if(ret.returnCode == 0){
			if(ret.outputLog.contains("mysqld is alive")) return DB_ONLINE;
			if(ret.outputLog.contains("Access denied")) return DB_ACCESS_DENIED;
		}
		return DB_UNKNOWN_ERROR;
	}
	
	public void startDB(){
		String[] cmd = {"cmd", "/c", "mysqld --console"};
		System.out.println("start->"+
				executeShellCommand(cmd, "mysqld: ready for connections", true));
		started = true;
	}	
	
	public void stopDB(){
		String[] cmd = {"cmd", "/c", "mysqladmin",
			"-u", settings.username,
			 	"--password=" + settings.password,
			 	"shutdown"};
		if(started == true){
			System.out.println("stop->"+executeShellCommand(cmd, null,true));
			started = false;
		}
	}
	
	
	public boolean dumpDB(String filename){
		String[] cmd = {"cmd", "/c", "mysqldump", "jkav",
				"-u", settings.username,
 			 	"--password=" + settings.password,
 			 	">", filename};
		CmdReturnInfo i = executeShellCommand(cmd, null,true);
		System.out.println(i.toString());
		if(i.returnCode == 0) return true;
		return false;
	}
	
	public boolean restoreDB(String filename){
		String[] cmd = {"cmd", "/c", "mysql", "jkav",
				"-u", settings.username,
 			 	"--password=" + settings.password,
 			 	"<", filename};
		CmdReturnInfo i = executeShellCommand(cmd, null,true);
		System.out.println(i.toString());
		if(i.returnCode == 0) return true;
		return false;
	}
    
	private class CmdReturnInfo{
		public int returnCode = 0;
		public String outputLog = "";
		
		public CmdReturnInfo(int c, String o){
			returnCode = c;
			outputLog = o;
		}
		public String toString(){
			return "CmdReturnInfo\nReturnCode: " + returnCode +
					"\nOutput: " + outputLog;
			}
	}
	
	//http://www.rgagnon.com/javadetails/java-0014.html
	// sehr interessant zu dem thema
	/**
	 * 
	 * @param cmd
	 * @param keyphrase		expected Keyphrase to stop waiting
	 * @param sqlDir		run this command in sql Directory?
	 * @return	The output Information (return code, output Text) of CommandExecution<br>
	 * 			null if Command Execution failed 
	 */
	private CmdReturnInfo executeShellCommand(	final String[] cmd,
												final String keyphrase,
												boolean sqlDir) {
		String output = "";
		int code = 0;
		boolean keyphraseFound = false;
		
		try {
			ProcessBuilder builder = new ProcessBuilder(cmd);
			if(sqlDir)
				builder.directory(new File(settings.mySQLDir));

			builder.redirectErrorStream(true);
			Process pr = builder.start();

			BufferedReader i = new BufferedReader(new InputStreamReader(pr.getInputStream(),"US-ASCII"));
           
            String line = "";
            StringBuilder str = new StringBuilder();

            while((line = i.readLine()) != null){
            	str.append(line + "\n");
            	
            	if(keyphrase != null){
            		if (line.contains(keyphrase)){
            			keyphraseFound = true;
            			break;
            		}
            	}
            }
            
            if(!keyphraseFound)
            	code = pr.waitFor();
            
            // letzes NewLine Zeichen entfernen
            if(str.length() > 0)
            str.deleteCharAt(str.length() - 1);
        
            output = str.toString();
            i.close();
            pr.destroy();
			
		} catch (Exception e){ e.printStackTrace(); return null; }

		return new CmdReturnInfo(code, output);
	}
}