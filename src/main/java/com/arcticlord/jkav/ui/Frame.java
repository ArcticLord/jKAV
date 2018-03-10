package com.arcticlord.jkav.ui;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.arcticlord.jkav.JKAV;
import com.arcticlord.jkav.db.DBController;
import com.arcticlord.jkav.db.MySQLControl;
import com.toedter.calendar.JDateChooser;


public abstract class Frame extends JPanel {

	private static final long serialVersionUID = 1L;

	private JKAV app;
	private GridBagLayout layout;
	private Dimension maxSize;
	private Dimension minBorder;  // frame is centered : height - (top,bottom), width - (left,right) 
	private String headline;
	private int buttonList;
	private boolean islogoEnabled;
	private boolean isScrollable;

	
	public interface IRefreshParent{
		public void refreshParent();
	}
	
	public Frame(JKAV a) {
		app = a;
		layout = new GridBagLayout();
		setLayout( layout );
		setBackground(new Color(0,150,0));
		setMaxSize(-1,-1);
		setMinBorder(50,100);
		setHeadline("Überschrift");
		setLogoEnabled(true);
		setScrollable(false);
		buttonList = 0;
		init();
	}

	protected void addComponent( Component c, int x, int y, 
								 int width, int height, double weightx, double weighty ){
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = x; gbc.gridy = y;
		gbc.gridwidth = width; gbc.gridheight = height;
		gbc.weightx = weightx; gbc.weighty = weighty;
		layout.setConstraints( c, gbc );
		add( c );
	}
	
	protected void setMaxSize(int w, int h){
		maxSize = new Dimension(w,h);
	}
	public Dimension getMaxSize(){
		return maxSize;
	}
	// frame ist centered so top=bottom and left=right
	protected void setMinBorder(int t, int l){
		minBorder = new Dimension(t, l);
	}
	public Dimension getMinBorder(){
		return minBorder;
	}
	protected void setHeadline(String h){
		headline = h;
	}
	public String getHeadline(){
		return headline;
	}
	protected void setButtonList(int l){
		buttonList = l;
	}	
	public int getButtonList(){
		return buttonList;
	}
	protected void setLogoEnabled(boolean e){
		islogoEnabled = e;
	}	
	public boolean getLogoEnabled(){
		return islogoEnabled;
	}
	protected void setScrollable(boolean s){
		isScrollable = s;
	}	
	public boolean getScrollable(){
		return isScrollable;
	}
	protected void switchToFrame(int frame, Object... param){
		app.switchToFrame(frame, param);
	} 
	protected DBController getDBConnection(){
		return app.getDB();
	}
	protected MySQLControl getDBControl(){
		return app.getDBControl();
	}
	protected void enableButtons(int btns){
		app.getFootPanel().enableButtons(btns);
	}
	protected void disableButtons(int btns){
		app.getFootPanel().disableButtons(btns);
	}
	protected void quit(){
		app.quit();
	}
	protected void showMessageBox(String headline, String text){
		JOptionPane.showMessageDialog(this, text, headline, JOptionPane.INFORMATION_MESSAGE);
	}
	protected void showErrorBox(String headline, String text){
		JOptionPane.showMessageDialog(this, text, headline, JOptionPane.ERROR_MESSAGE);
	}
	protected boolean showYesNoBox(String headline, String text){
		int n = JOptionPane.showConfirmDialog(this, text, headline, JOptionPane.YES_NO_OPTION);
		if(n == 0) return true;
		return false;
	}
	
	abstract public void init();					// Einmalig jeder Frame beim Programmstart
	abstract public void prepare();					// Jedesmal wenn auf der Frame aufgerufen wird
	abstract public void contentChanged();			// Jedesmal wenn etwas auf dem Frame verändert wurde (Bsp: ins Namensfeld getippt)	
	abstract public void update(Object... params);	// TODO: Noch nicht ganz klar: Zur Laufzeit Daten nachliefern
	
	// Buttons
	abstract public void back();
	abstract public void open();
	abstract public void save();
	abstract public void add();
	abstract public void edit();
	abstract public void word();
	abstract public void remove();
	
	protected JLabel createGap(){
		JLabel gap = new JLabel();
		gap.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		return gap;
	}
	protected JButton createIconButton(String text, Icon icon){
		JButton btn = new JButton(text);
		btn.setIcon(icon);
		btn.setHorizontalAlignment(SwingConstants.LEFT);
		btn.setIconTextGap(25);
		return btn;
	}
	protected JLabel createLabel(String text){
		JLabel label = new JLabel(text);
		label.setForeground(new Color(0,0,0));
		return label;
	}
	protected JTextField createTextField(){
		JTextField textfield = new JTextField();
		textfield.getDocument().addDocumentListener(new MyDocumentListener());
		return textfield;
	}
	protected JTextField createNumberTextField(){
		DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
		symbols.setDecimalSeparator('.');
		JTextField numberfield = new JFormattedTextField(new DecimalFormat("#.##",symbols));
		numberfield.getDocument().addDocumentListener(new MyDocumentListener());
		return numberfield;
	}	
	protected JTextArea createTextArea(int lines){
		JTextArea textarea = new JTextArea();
		textarea.setLineWrap(true);
		textarea.setWrapStyleWord(true);
		textarea.setRows(lines);

		textarea.getDocument().addDocumentListener(new MyDocumentListener());
		return textarea;
	}
	protected JComboBox<String> createComboBox(){
		JComboBox<String> combo = new JComboBox<String>();
	    combo.addItemListener( new ItemListener() {
	        public void itemStateChanged( ItemEvent e ) {
	        	//JComboBox selectedChoice = (JComboBox)e.getSource();
	          	//System.out.println(selectedChoice.getSelectedItem());
	          	contentChanged();
	        }
	    });
		return combo;
	}

	protected void addComboBoxItems (JComboBox<String> cBox, String[] items){
		for(String item : items)
			cBox.addItem(item);
	}

	protected JDateChooser createDateChooser(){
		JDateChooser jdc = new JDateChooser();
		jdc.addPropertyChangeListener(new PropertyChangeListener(){
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				if(arg0.getPropertyName().matches("date"))
					contentChanged();
			}
		});
		return jdc;
	}
	
	
	private class MyDocumentListener implements DocumentListener {

		@Override
		public void changedUpdate(DocumentEvent arg0) {
			contentChanged();
		}

		@Override
		public void insertUpdate(DocumentEvent arg0) {
			contentChanged();
		}

		@Override
		public void removeUpdate(DocumentEvent arg0) {
			contentChanged();
		}
	}
}