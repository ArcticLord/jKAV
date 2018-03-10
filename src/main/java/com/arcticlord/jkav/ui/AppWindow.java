package com.arcticlord.jkav.ui;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import com.arcticlord.jkav.JKAV;
import com.arcticlord.jkav.utils.Settings;


public class AppWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JPanel bodyPanel;
	private Frame  activeChild;
	private JKAV context;
	private SpringLayout layout;
	
	public AppWindow(JKAV c) {
		context = c;
		this.addWindowListener(new WindowAdapter() {
			// Wird aufgerufen, wenn das Fenster geschlossen wird.
			public void windowClosing(WindowEvent e) {
				context.quit();
			}
		});
	}

	public void initWindow() {
		layout = new SpringLayout();
		this.getContentPane().setLayout(layout);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage("res/logo.gif"));
		this.setTitle("jKAV - java Kunden Auftrags Verwaltung");
		
		Container c = getContentPane();
		c.setBackground(new Color(0,150,0));

		bodyPanel = new JPanel(new GridLayout(1,1));
		bodyPanel.setBackground(new Color(0,150,0));
		
		activeChild = null;
		
		JPanel head = context.getHeadPanel();
		JPanel foot = context.getFootPanel();
		
		c.add(foot);
		c.add(bodyPanel);
		c.add(head);
		
		// Positions		
		layout.putConstraint(SpringLayout.WIDTH, head, 0, SpringLayout.WIDTH, c);
		
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, bodyPanel, 0, SpringLayout.HORIZONTAL_CENTER, c);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, bodyPanel, 0, SpringLayout.VERTICAL_CENTER, c);

		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, foot, 0, SpringLayout.HORIZONTAL_CENTER, c);
		layout.putConstraint(SpringLayout.SOUTH, foot, 0, SpringLayout.SOUTH, c);
//		layout.putConstraint(SpringLayout.WIDTH, foot, 0, SpringLayout.WIDTH, c);
		
		setBounds(10,10, 1024, 768);
		setVisible(true);
		
		c.addComponentListener(new ComponentListener() {  
				@Override
				public void componentHidden(ComponentEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void componentMoved(ComponentEvent arg0) {
					// TODO Auto-generated method stub	
				}

				@Override
				public void componentResized(ComponentEvent arg0) {
					resizeContent();
				}

				@Override
				public void componentShown(ComponentEvent arg0) {
					// TODO Auto-generated method stub
					
				}
		});
		
		addWindowStateListener(new WindowStateListener(){

			@Override
			public void windowStateChanged(WindowEvent arg0) {
				resizeContent();		
			}});
		


	}

	private void resizeContent(){
		if(activeChild != null){
			
			Dimension wnd = getSize();
			Dimension frm = activeChild.getMaxSize();
			Dimension brd = activeChild.getMinBorder();
			
			// Height calculation
			if(wnd.height < (frm.height + (2 * brd.height)) || frm.height < 0)
				wnd.height -= (2 * brd.height);
			else wnd.height = frm.height;
			
			// Width calculation
			if(wnd.width < (frm.width + (2 * brd.width)) || frm.width < 0)
				wnd.width -= (2 * brd.width);
			else wnd.width = frm.width;
			
			bodyPanel.setPreferredSize(wnd);
			bodyPanel.validate();			
		}
	}
	
	public void setContent(Frame child){
		// gesetzen Child Frame merken
		activeChild = child;
		// alte frames entfernen
		bodyPanel.removeAll();
		// größe für den neuen Frame festlegen
		resizeContent();
		// neuen frame hinzufügen (scrollbar)
		if (activeChild.getScrollable()){
			JScrollPane scr = new JScrollPane(	activeChild,
												JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
												JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			scr.setBorder(BorderFactory.createEmptyBorder());
			bodyPanel.add(scr);
		}
		// neuen frame hinzufügen (nicht scrolbar)
		else bodyPanel.add(activeChild);
		// neue Überschrift setzen
		context.getHeadPanel().setHeadlineText(activeChild.getHeadline());
		// Button Panel anpassen
		context.getFootPanel().createButtons(activeChild.getButtonList());
		// Logo enabled/disabled
		context.getHeadPanel().setIconVisible(activeChild.getLogoEnabled());
		// Fensterinhalt erneuern
		validate();
	}
	
	public void showErrorBox(String headline, String text){
		JOptionPane.showMessageDialog(this, text, headline, JOptionPane.ERROR_MESSAGE);
	}
	/**
	 * Creates a tabbed Box for Login and Settings.
	 * @return	Settings Object with all settings in it.
	 */
	public Settings showLoginBox(final Settings settings){
		JTabbedPane tabbedPane = new JTabbedPane();
		
		// Settings Panel
		JPanel settingsPane = new JPanel();
		settingsPane.setLayout(new GridLayout(8,1));		
	    final JTextField edtName = new JTextField();
	    edtName.setText(settings.username);
	    final JTextField edtMySQLDir = new JTextField();
	    edtMySQLDir.setText(settings.mySQLDir);	    
	    final JTextField edtCustomerDir = new JTextField();
	    edtCustomerDir.setText(settings.customerDir);	    
	    JButton btnSave = new JButton("Einstellungen speichern");
	    btnSave.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				settings.username = edtName.getText();
				settings.mySQLDir = edtMySQLDir.getText();
				settings.customerDir = edtCustomerDir.getText();
				settings.storeSettings();
			}});
	    settingsPane.add(new JLabel("Benutzername:"));
	    settingsPane.add(edtName);
	    settingsPane.add(new JLabel("MySQL-Verzeichnis:"));
	    settingsPane.add(edtMySQLDir);
	    settingsPane.add(new JLabel("Kundendaten-Verzeichnis:"));
	    settingsPane.add(edtCustomerDir);
	    settingsPane.add(new JLabel());
	    settingsPane.add(btnSave);
	    
	    // Login Panel
	    JPanel loginPane = new JPanel();
	    loginPane.setLayout(new GridLayout(8,1));		
	    JPasswordField edtPassword = new JPasswordField();	    
	    edtPassword.addAncestorListener( new AncestorListener(){
			@Override
			public void ancestorAdded(AncestorEvent e) {
				JComponent component = e.getComponent();
				component.requestFocusInWindow();
				component.removeAncestorListener( this );
			}
			@Override
			public void ancestorMoved(AncestorEvent arg0) {}
			@Override
			public void ancestorRemoved(AncestorEvent arg0) {}
			});
	    loginPane.add(new JLabel());
	    loginPane.add(new JLabel());
	    loginPane.add(new JLabel("Bitte Datenbank Passwort eingeben!"));
	    loginPane.add(edtPassword);
	    
	    tabbedPane.addTab("Login", null, loginPane, "Login-Tab");
	    tabbedPane.addTab("Einstellungen", null, settingsPane, "Einstellungen-Tab");
	    Object[] message = { tabbedPane };
	    int res = JOptionPane.showOptionDialog(null, message, "Login / Einstellungen", 
	    		JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
	    		null, new Object[] {"Login","Beenden"},"Login");
	    if(res == 0){
	    	settings.password = new String(edtPassword.getPassword());
	    	return settings;
	    }
	    return null;
	}
}