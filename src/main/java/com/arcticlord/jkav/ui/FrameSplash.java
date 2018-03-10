package com.arcticlord.jkav.ui;
import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.Timer;

import com.arcticlord.jkav.JKAV;
import com.arcticlord.jkav.utils.ImageLib;


public class FrameSplash extends Frame{

	private static final long serialVersionUID = -5474809069692879635L;
	private MyCanvas canvas;
	private JLabel lblInfo;
	
	public FrameSplash(JKAV c) {
		super(c);
	}

	@Override
	public void init() {
		setMaxSize(500, 400);
		setHeadline("Bitte warten...");
		setLogoEnabled(false);
		
		canvas = new MyCanvas(5); 	
		
		lblInfo = new JLabel("",JLabel.CENTER);
		lblInfo.setForeground(new Color(0,0,0));
		lblInfo.setFont(new Font("Monospaced", Font.PLAIN, 28));
		
		SpringLayout l = new SpringLayout();
		JPanel jl = new JPanel();
		jl.setBackground(new Color(0,150,0));
		jl.setLayout(l);
		jl.add(canvas, BorderLayout.CENTER);	
		l.putConstraint(SpringLayout.HORIZONTAL_CENTER, canvas, 0, SpringLayout.HORIZONTAL_CENTER, jl);
		l.putConstraint(SpringLayout.VERTICAL_CENTER, canvas, 0, SpringLayout.VERTICAL_CENTER, jl);

		
		addComponent(jl, 0, 0, 1, 1, 1, 1);
		addComponent(lblInfo,0,1,1,1,1,0);
	}
	
	@Override
	public void prepare(){
		canvas.startAnimation();
	}
	
	@Override
	public void update(Object... params){
		String strInfoText = "";
		try{
			strInfoText = (String) params[0];
		} catch(Exception e){} 
		lblInfo.setText(strInfoText);
	}
	
	private class MyCanvas extends JPanel implements ActionListener{

		private static final long serialVersionUID = 5128094274563710041L;
		private int trans; 
		private Image img;
		private Timer timer;
		private int imgWidth, imgHeight;

		 public MyCanvas(int runtimeSec){
			img = ImageLib.LOGO_BIG;
			
			imgWidth = img.getWidth(null);
			imgHeight = img.getHeight(null);
			
			trans = 0;
			timer = new Timer(runtimeSec * 10, this);
		 }
		 
		 public void startAnimation(){
			 timer.start();
		 }

		 @Override
		public void paint(Graphics g){
			 super.paint(g);
			Graphics2D gr = (Graphics2D) g;

			gr.setColor(new Color(0,150,0));
			
			Dimension s = getSize();
			gr.fillRect(0, 0, s.width, s.height);

			float val = (float) (Math.pow((float)trans, 2.0f)) / 10000; 
			
			 gr.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, val)); 
			   
			 gr.drawImage(img,0,0,this);
			 gr.dispose();   
		 }

		@Override
		public void actionPerformed(ActionEvent e) {
			if(trans < 100) {
				trans++;
				repaint();
			}
			else timer.stop();
		}
		
		@Override
		public void setBounds(int x, int y, int width, int height){
			super.setBounds(	x - (imgWidth / 2),
								y - (imgHeight / 2),
								imgWidth, imgHeight);
		}
	}

	@Override public void contentChanged() {}
	@Override public void back() {}
	@Override public void open() {}
	@Override public void save() {}
	@Override public void add() {}
	@Override public void edit() {}
	@Override public void word() {}
	@Override public void remove() {}	
}