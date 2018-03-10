package com.arcticlord.jkav.ui;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.arcticlord.jkav.JKAV;
import com.arcticlord.jkav.utils.ImageLib;


public class FrameFoot extends JPanel {

	private static final long serialVersionUID = 5259007394845678269L;
	
	public static final int BTN_BACK =				1;
	public static final int BTN_ADD =				2;
	public static final int BTN_REMOVE =			4;
	public static final int BTN_SAVE =				8;
	public static final int BTN_OPEN =			   16;
	public static final int BTN_EDIT =			   32;
	public static final int BTN_WORD =			   64;
	
	private JButton btnAdd;
	private JButton	btnRemove;
	private JButton	btnBack;
	private JButton	btnSave;
	private JButton	btnOpen;
	private JButton	btnEdit;
	private JButton	btnWord;

	private JKAV context;
	
	public FrameFoot(JKAV c) {
		context = c;
		initFrame();
	}

	protected void initFrame() {	
		this.setLayout(new GridLayout(1, 1));
		
		// Button Appearence
		btnAdd = createButton("Hinzufügen", ImageLib.ADD);
		btnRemove = createButton("Entfernen", ImageLib.REMOVE);
		btnBack = createButton("Zurück", ImageLib.BACK);
		btnSave = createButton("Speichern", ImageLib.SAVE);
		btnOpen = createButton("Öffnen", ImageLib.OPEN);
		btnEdit = createButton("Bearbeiten", ImageLib.EDIT);
		btnWord = createButton("Word export", ImageLib.WORD);
		
		// Button Functionality
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				context.getActiveFrame().back();
			}
		});
		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				context.getActiveFrame().open();
			}
		});
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				context.getActiveFrame().save();
			}
		});
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				context.getActiveFrame().add();
			}
		});
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				context.getActiveFrame().remove();
			}
		});
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				context.getActiveFrame().edit();
			}
		});
		btnWord.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				context.getActiveFrame().word();
			}
		});
	}
	
	private JButton createButton(String text, Icon icon){
		JButton btn = new JButton(text);
		btn.setIcon(icon);
		return btn;
	}
	
	private ArrayList<JButton> getButtonList(int btns){
		ArrayList<JButton> ret = new ArrayList<JButton>();
		btns = 0 | btns;
		
		if ((btns & BTN_BACK) == BTN_BACK)
			ret.add(btnBack);
		if ((btns & BTN_OPEN) == BTN_OPEN)
			ret.add(btnOpen);
		if ((btns & BTN_EDIT) == BTN_EDIT)
			ret.add(btnEdit);
		if ((btns & BTN_ADD) == BTN_ADD)
			ret.add(btnAdd);
		if ((btns & BTN_REMOVE) == BTN_REMOVE)
			ret.add(btnRemove);
		if ((btns & BTN_SAVE) == BTN_SAVE)
			ret.add(btnSave);
		if ((btns & BTN_WORD) == BTN_WORD)
			ret.add(btnWord);
	
		return ret;
	}
	
	public void createButtons(int btns){
		removeAll();
		for(JButton b : getButtonList(btns))
			add(b);
	}
	
	public void disableButtons(int btns){
		for(JButton b : getButtonList(btns))
			b.setEnabled(false);
	}
	public void enableButtons(int btns){
		for(JButton b : getButtonList(btns))
			b.setEnabled(true);
	}
}