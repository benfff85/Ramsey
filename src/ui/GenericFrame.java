package ui;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import ramsey.CayleyGraph;
import ramsey.Controller;
import ramsey.Logger;
import ramsey.Timer;

import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;

public class GenericFrame extends JFrame {
	
	private JLabel l_count;
	private JLabel l_clique;
	private Logger logger;
	private CayleyGraph cayleyGraph;
	private JPanel contentPane;
	private Controller controller;
	private boolean continueRunningInd;


	/**
	 * Create the frame.
	 */
	public GenericFrame(CayleyGraph cayleyGraph, Logger logger, Timer timer, final Controller controller) {
		this.logger = logger;
		this.cayleyGraph = cayleyGraph;
		this.controller = controller;
		continueRunningInd = true;
		
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
		l_count.setText("Count: " + String.valueOf(logger.getAnalyzedGraphCount()));
		l_count.setBounds(5, 114, 219, 23);
		contentPane.add(l_count);
		
	    l_clique = new JLabel("");
	    l_clique.setText("Clique: ");
	    l_clique.setBounds(5, 90, 219, 23);
		contentPane.add(l_clique);
		
		JButton playButton = new JButton("");
		playButton.setIcon(new ImageIcon(GenericFrame.class.getResource("/com/sun/javafx/webkit/prism/resources/mediaPlayDisabled.png")));
		playButton.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	 controller.runOneIteration();
	         }          
		});
		playButton.setBounds(39, 200, 33, 23);
		contentPane.add(playButton);
		

	}
	
	public void refreshData(){
		l_count.setText("Count: " + String.valueOf(logger.getAnalyzedGraphCount()));
	    l_clique.setText("Clique: " + cayleyGraph.getClique().printClique());
	}
	
	public boolean getContinueRunningInd(){
		return continueRunningInd;
	}
}
