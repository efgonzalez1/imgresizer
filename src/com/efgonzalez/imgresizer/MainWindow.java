package com.efgonzalez.imgresizer;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.io.*;
import java.util.prefs.*;

import javax.swing.*;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class MainWindow extends javax.swing.JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel toolbar;
	private JTextArea text;
	private JButton btnRun;
	private JLabel widthLabel;
	private JLabel outputLabel;
	private JProgressBar pbar;
	private JTextField output;
	private JLabel heightLabel;
	private JTextField heightVal;
	private JTextField widthVal;

	// Preference keys for this package
	private static final String WIDTH = "widthVal";
	private static final String HEIGHT = "heightVal";
	Preferences prefs = Preferences
			.systemNodeForPackage(com.efgonzalez.imgresizer.MainWindow.class);

	// ////////////////////////////////////////////////////////////////////

	/**
	 * Auto-generated main method to display this JFrame
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainWindow inst = new MainWindow();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}

	public MainWindow() {
		super();
		initGUI();
	}

	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			{
				toolbar = new JPanel();
				toolbar.setLayout(null);
				getContentPane().add(toolbar, BorderLayout.NORTH);
				toolbar.setBackground(new java.awt.Color(174, 174, 174));
				toolbar.setPreferredSize(new java.awt.Dimension(400, 58));
				{
					widthLabel = new JLabel();
					toolbar.add(widthLabel);
					widthLabel.setText("Width: ");
					widthLabel.setBounds(8, 5, 46, 15);
					widthLabel.setFont(new java.awt.Font("Dialog", 1, 10));
				}
				{
					widthVal = new JTextField();
					toolbar.add(widthVal);
					widthVal.setText(prefs.getInt(WIDTH, 550) + "");
					widthVal.setBounds(59, 2, 60, 22);
				}
				{
					heightLabel = new JLabel();
					toolbar.add(heightLabel);
					heightLabel.setText("Height: ");
					heightLabel.setBounds(126, 5, 53, 15);
					heightLabel.setFont(new java.awt.Font("Dialog", 1, 10));
				}
				{
					heightVal = new JTextField();
					toolbar.add(heightVal);
					heightVal.setText(prefs.getInt(HEIGHT, 550) + "");
					heightVal.setBounds(184, 2, 60, 22);
				}
				{
					btnRun = new JButton();
					toolbar.add(btnRun);
					btnRun.setText("Resize");
					btnRun.setBounds(323, 0, 86, 26);
					btnRun.setFont(new java.awt.Font("Dialog", 0, 10));
					btnRun.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							btnRunActionPerformed(evt);
						}
					});
				}
				{
					outputLabel = new JLabel();
					toolbar.add(outputLabel);
					outputLabel.setText("Output: ");
					outputLabel.setBounds(3, 32, 60, 15);
					outputLabel.setFont(new java.awt.Font("Dialog", 1, 10));
				}
				{
					output = new JTextField();
					toolbar.add(output);
					output.setText(new File("").getCanonicalPath()
							+ File.separator + "resized_out");
					output.setBounds(60, 29, 349, 22);
				}
			}
			{
				text = new JTextArea();
				getContentPane().add(text, BorderLayout.CENTER);
				{
					pbar = new JProgressBar(0, 100);
					pbar.setStringPainted(true);
					getContentPane().add(pbar, BorderLayout.SOUTH);
				}
				text.setPreferredSize(new java.awt.Dimension(431, 220));
			}
			pack();
			this.setSize(434, 300);
		} catch (Exception e) {
			// add your error handling code here
			e.printStackTrace();
		}

		new FileDrop(System.out, text, new FileDrop.Listener() {
			public void filesDropped(java.io.File[] files) {
				for (int i = 0; i < files.length; i++) {
					try {
						if (files[i].isDirectory())
							text.append(files[i].getCanonicalPath() + "\n");
						if (files[i].getCanonicalPath().toLowerCase()
								.endsWith(".jpeg")
								| files[i].getCanonicalPath().toLowerCase()
										.endsWith(".jpg"))
							text.append(files[i].getCanonicalPath() + "\n");
						if (files[i].getCanonicalPath().toLowerCase()
								.endsWith(".png"))
							text.append(files[i].getCanonicalPath() + "\n");
					} // end try
					catch (java.io.IOException e) {
					}
				}
				// end for: through each dropped file
			} // end filesDropped
		}); // end FileDrop.Listener

		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			public void run() {
				prefs.putInt(WIDTH, Integer.parseInt(widthVal.getText()));
				prefs.putInt(HEIGHT, Integer.parseInt(heightVal.getText()));
			}
		}));
	}

	private void btnRunActionPerformed(ActionEvent evt) {
		if (text.getText().equals(""))
			return;
		Resizer r = new Resizer(getTextField(), getWidthField(), getHeightField(), getOutput());

		r.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if ("progress".equals(evt.getPropertyName())) {
					pbar.setValue((Integer) evt.getNewValue());
				}
			}
		});
		pbar.setValue(0);
		r.execute();
	}

	public String getTextField() {
		return text.getText();
	}

	public int getWidthField() {
		return Integer.parseInt(widthVal.getText());
	}

	public int getHeightField() {
		return Integer.parseInt(heightVal.getText());
	}

	public String getOutput() {
		return output.getText();
	}
}
