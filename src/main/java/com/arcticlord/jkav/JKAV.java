package com.arcticlord.jkav;

import com.arcticlord.jkav.data.CustomerData;
import com.arcticlord.jkav.data.JobData;
import com.arcticlord.jkav.db.DBController;
import com.arcticlord.jkav.db.MySQLControl;
import com.arcticlord.jkav.ui.AppWindow;
import com.arcticlord.jkav.ui.Frame;
import com.arcticlord.jkav.ui.FrameCustomerEdit;
import com.arcticlord.jkav.ui.FrameCustomerList;
import com.arcticlord.jkav.ui.FrameFoot;
import com.arcticlord.jkav.ui.FrameHead;
import com.arcticlord.jkav.ui.FrameJobEdit;
import com.arcticlord.jkav.ui.FrameJobList;
import com.arcticlord.jkav.ui.FrameMain;
import com.arcticlord.jkav.ui.FrameSplash;
import com.arcticlord.jkav.ui.FrameWord;
import com.arcticlord.jkav.utils.FolderControl;
import com.arcticlord.jkav.utils.Settings;


// (J)ava (K)unden (A)uftrags (V)erwaltung

public class JKAV{

	public  static final int FRAME_MAIN =			0;
	public  static final int FRAME_CUSTOMER_LIST =	1;
	public  static final int FRAME_CUSTOMER_EDIT =	2;
	public  static final int FRAME_JOB_LIST =		3;
	public  static final int FRAME_JOB_EDIT =		4;
	public  static final int FRAME_WORD =			5;
	private static final int FRAMES_ANZ =			6;
	
	private AppWindow theAppWindow;
	private FrameFoot footPanel;
	private FrameHead headPanel;
	
	private Frame[] frames;	
	private FrameSplash splash;
	
	private int activeFrame = -1;
	
	private DBController db;
	private MySQLControl dbControl;
	
	private Settings settings;
	
	public JKAV(){	
		theAppWindow = new AppWindow(this);	
		headPanel = new FrameHead();
		footPanel = new FrameFoot(this);

		splash = new FrameSplash(this);
	
		frames = null;
		
		// TODO: framefoot übergeben
		theAppWindow.initWindow();
		
		// Settings laden
		settings = new Settings();
		settings.loadSettings();
		
		settings = theAppWindow.showLoginBox(settings);
		if(settings != null){
			// set Splash Screen
			splash.prepare();
			theAppWindow.setContent(splash);
			// Init the Application
			init();
		}
		else
			quit();
	}
	
	public void switchToFrame(int frame, Object... params){
		if( frame < 0 | frame > FRAMES_ANZ) return;
		// set child frame parameters if exists
		//if(params != null){
		//	if(params.length > 0){
				frames[frame].update(params);
		//	}
	//	}
		// prepare child frame
		frames[frame].prepare();
		// set child frame
		theAppWindow.setContent(frames[frame]);
		// store active child frame id
		activeFrame = frame;
	}
	
	public Frame getActiveFrame(){ return frames[activeFrame]; }
	public DBController getDB(){ return db; }
	public MySQLControl getDBControl(){ return dbControl; }
	public FrameHead getHeadPanel(){ return headPanel; }
	public FrameFoot getFootPanel(){ return footPanel; }	
	
	private void init(){
		// timer
        long t0, t1;
        t0 =  System.currentTimeMillis();
		// timer
        
        boolean dbRunning = false;
        boolean dbExists = false;
  
        
        ///// SCHRITT 1//////
        splash.update("Initialisiere Anwendung");
        
        dbControl = new MySQLControl(settings);
        db = new DBController(settings);
        FolderControl.setMainPath(settings.customerDir);
        
        ///// SCHRITT 2//////
        splash.update("Überprüfe MySQL Status");
        int state = dbControl.getState();
        if( state == MySQLControl.DB_IO_ERROR ){
        	theAppWindow.showErrorBox("Fehler", "MySQL Anwendungsdateien nicht gefunden");
        	quit();
        }
        if( state != MySQLControl.DB_OFFLINE ){
        	dbRunning = true;
        }
        
        ///// SCHRITT 3 (optional) //////
        if( dbRunning == false ){
        	splash.update("Starte MySQL");
			dbControl.startDB();
        }
        
        ///// SCHRITT 4//////
        splash.update("Überprüfe MySQL Status erneut");
        state = dbControl.getState();
        if(state == MySQLControl.DB_ACCESS_DENIED ){
        	theAppWindow.showErrorBox("Fehler", "Ungültige MySQL Zugangsdaten. MySQL Instanz bleibt bestehen.");
        	quit();
        }
        if(state != MySQLControl.DB_ONLINE ){
        	theAppWindow.showErrorBox("Fehler", "Unbekannter MySQL Fehler");
        	quit();
        }
        
        ///// SCHRITT 5//////
        splash.update("Überprüfe Datenbank Schema");
        dbExists = db.checkDatabase();
        if(dbExists){
        	if(!db.checkDatabaseScheme()){
        		theAppWindow.showErrorBox("Fehler", "Ungültiges MySQL Schema eingerichtet");
            	quit();
        	}
        }
 
        ///// SCHRITT 6 (optional) //////
        else {
        	splash.update("Erstelle Datenbank Schema");
        	db.createDatabaseScheme();
        }
        ///// SCHRITT 7//////
        splash.update("Initialisiere Anwendung");
        
    	// load and set Job States and Customer Titles
        CustomerData.titleSelection = db.getTitleEnum();
		JobData.jobStates = db.getJobStateEnum();
        
		// Init GUI Frame Classes
		frames = new Frame[FRAMES_ANZ];
		frames[FRAME_MAIN] = new FrameMain(this);
		frames[FRAME_CUSTOMER_LIST] = new FrameCustomerList(this);
		frames[FRAME_CUSTOMER_EDIT] = new FrameCustomerEdit(this);
		frames[FRAME_JOB_LIST] = new FrameJobList(this);
		frames[FRAME_JOB_EDIT] = new FrameJobEdit(this);
		frames[FRAME_WORD] = new FrameWord(this);
		
		// timer
        do{
            t1 = System.currentTimeMillis();
        }
        while ((t1 - t0) < (5 * 1000));
        // timer
		switchToFrame(FRAME_MAIN);
		
	}
	
	public void quit(){
		if(dbControl != null)
			dbControl.stopDB();
		System.exit(0);
	}
	
	public static void main(String args[]){
		new JKAV();
	}
}