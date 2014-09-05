package ui;

import javax.swing.JPanel;
import javax.swing.JTextField;

public class Panel extends JPanel {
	private JTextField txtHello;

	/**
	 * Create the panel.
	 */
	public Panel() {
		setLayout(null);
		
		txtHello = new JTextField();
		txtHello.setBounds(185, 110, 86, 20);
		txtHello.setText("Hello");
		add(txtHello);
		txtHello.setColumns(10);

	}

}
