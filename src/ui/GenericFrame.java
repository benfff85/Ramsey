package ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.Window.Type;

import ramsey.CayleyGraph;
import ramsey.Logger;
import ramsey.Timer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.AbstractAction;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;

public class GenericFrame extends JFrame {
	
	private JLabel l_count;
	private Logger logger;
	private JPanel contentPane;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GenericFrame frame = new GenericFrame(null,null,null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GenericFrame(CayleyGraph cayleyGraph, Logger logger, Timer timer) {
		this.logger = logger;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton printButton = new JButton("Print");
		printButton.addActionListener(new ActionListener() {
		         public void actionPerformed(ActionEvent e) {
		            System.out.println("XXX");
		         }          
		});
		printButton.setBounds(5, 234, 424, 23);
		contentPane.add(printButton);
		
	    l_count = new JLabel("");
		l_count.setText(String.valueOf(logger.getAnalyzedGraphCount()));
		l_count.setBounds(5, 114, 219, 120);
		contentPane.add(l_count);
	}
	
	public void refreshData(){
		l_count.setText("Count: " + String.valueOf(logger.getAnalyzedGraphCount()));
	}


}
