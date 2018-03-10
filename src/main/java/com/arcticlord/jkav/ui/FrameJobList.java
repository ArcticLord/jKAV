package com.arcticlord.jkav.ui;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JScrollPane;

import com.arcticlord.jkav.JKAV;
import com.arcticlord.jkav.data.JobData;

public class FrameJobList extends Frame {

	private static final long serialVersionUID = 704032377245836805L;

	private PanelJobView jobView;
	private JKAVTable table;
	
	private ArrayList<Object[]> jobsData = null;
	
	private JobData activeJobData = null;
	private int activeCustomerId = -1;
	private String activeCustomerName = "";

	public FrameJobList(JKAV c) {
		super(c);
	}
	
	@Override
	public void init() {
		setHeadline("Auftragsliste");
		setButtonList(FrameFoot.BTN_BACK | FrameFoot.BTN_EDIT  
					| FrameFoot.BTN_ADD | FrameFoot.BTN_REMOVE);
	
		
		jobView = new PanelJobView(getDBConnection(), new IRefreshParent(){
			@Override
			public void refreshParent() {
				prepare();
			}});
		
		// Set JobState Replacements
		int anzStates	= JobData.jobStates.size();
		
		JKAVTable.CellReplacementRule[] rules
			= new JKAVTable.CellReplacementRule[anzStates];
		
		// Es müssten 3 States existieren
		if(anzStates == 3){
			
			rules[0] = new JKAVTable.CellReplacementRule(	JobData.jobStates.getEnumIdOf("pending"),
																"ausstehend",
																new Color(255,255,0));
			rules[1] = new JKAVTable.CellReplacementRule(	JobData.jobStates.getEnumIdOf("accepted"),
																"angenommen",
																new Color(0,255,0));				
			rules[2] = new JKAVTable.CellReplacementRule(	JobData.jobStates.getEnumIdOf("rejected"),
																"abgelehnt",
																new Color(255,0,0));
		}
		// Erstelle Tabellen Element
		JKAVTable.ContentDescription content = new JKAVTable.ContentDescription();
		content.addDescription("ID", true, true);
		content.addDescription("Titel", true, false);
		content.addDescription("Preis", true, false);
		content.addDescription("Status", true, false);
		table = new JKAVTable(content);
		table.setCellReplacement("Status", rules);
		table.setSelectionListener(new ITableSelectionListener(){
			@Override
			public void clicked(int selectedRow) {
				int activeJobId = (Integer)jobsData.get(selectedRow)[0];
				activeJobData = getDBConnection().getJobData(activeJobId);
				jobView.setData(activeJobData, activeCustomerName);
				enableButtons(FrameFoot.BTN_EDIT | FrameFoot.BTN_REMOVE);
				
			}
			@Override
			public void doubleClicked(int selectedRow) {
				// nothing
			}
		});

		JScrollPane ss = new JScrollPane(jobView);
		ss.setPreferredSize(new Dimension(450,550));
		
		addComponent(new JScrollPane(table),	0, 0, 1, 1, 1, 1);
		addComponent(ss,						1, 0, 1, 2, 1, 1);
		addComponent(table.getFilterText(),		0, 1, 1, 1, 1, 0);
	}
	
	@Override
	public void update(Object... params){
		if(params.length > 0){
			try{
				activeCustomerId = (Integer) params[0];
			} catch ( Exception e ){
				e.printStackTrace();
				activeCustomerId = -1;
			}
		}
//		else activeCustomerId = -1;
	}
	
	
	@Override
	public void prepare(){
		table.clearSelection();
		activeJobData = null;
		activeCustomerName = getDBConnection().getCustomerName(activeCustomerId);
		jobsData = getDBConnection().getCustomerJobsList(activeCustomerId);
		setHeadline("Aufträge für "+activeCustomerName);
		table.setData(jobsData);
		jobView.clear();
		disableButtons(	FrameFoot.BTN_EDIT | FrameFoot.BTN_REMOVE);
	}
	
	@Override
	public void back(){
		switchToFrame(JKAV.FRAME_CUSTOMER_LIST);
	}
	@Override
	public void edit(){
		switchToFrame(JKAV.FRAME_JOB_EDIT, activeJobData);
	}
	@Override
	public void add(){
		JobData newJobData = new JobData();
		newJobData.customerId = activeCustomerId;
		newJobData.state = JobData.jobStates.getEnumIdOf("pending");
		switchToFrame(JKAV.FRAME_JOB_EDIT, newJobData);
	}
	@Override
	public void remove(){
		if(showYesNoBox("Löschen","Den ausgewählten Job wirklich löschen?")){
			if(!getDBConnection().removeJobData(activeJobData.jid))
				showErrorBox("Fehler", "Beim Löschen des Jobs ist ein Fehler aufgetreten.");
			else {
				prepare();
				showMessageBox("Erfolg", "Der ausgewählte Job wurde erfolgreich gelöscht.");
			}
		}
	}

	@Override public void contentChanged() {}
	@Override public void open() {}
	@Override public void save() {}
	@Override public void word() {}
}