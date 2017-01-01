package me.ArthurRabinovich;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import org.json.JSONArray;
import org.json.JSONObject;

public class GUI extends Frame {

	public GUI(JSONObject config) {
		setSize(350, 130);
		setResizable(true);
		setLocationRelativeTo(null);
		
		Frame f = this;
		
		
		
		Panel panel = new Panel();
		panel.setName("Tournament Seeder");

		panel.add(new Label("(Subdomain-Tournament Name-Tournament Number)"));
		Choice choices = new Choice();
		JSONArray x = ((JSONArray) config.get("Subdomains"));
		for (int i = 0; i < x.length(); i++) {
			choices.add((String) x.get(i));
		}
		panel.add(choices);

		Choice choices2 = new Choice();

		x = ((JSONArray) config.get("Tournament_Names"));
		for (int i = 0; i < x.length(); i++) {
			choices2.add((String) x.get(i));
		}
		panel.add(choices2);

		TextField tb = new TextField(1);
		panel.add(tb);

		Button b = new Button("seed");
		
		b.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (arg0.getID() == ActionEvent.ACTION_PERFORMED) {
					f.setVisible(false);
					Main.run(choices.getSelectedItem() + "-" + choices2.getSelectedItem() + tb.getText(),
							config.getString("Rankings_File"), config.getString("Bans_File"));
					System.exit(0);
					
				}
			}
		});
		addWindowListener(new WindowListener() {

			@Override
			public void windowActivated(WindowEvent arg0) {
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
				System.exit(0);
			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				System.exit(0);
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {

			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
			}

			@Override
			public void windowIconified(WindowEvent arg0) {

			}

			@Override
			public void windowOpened(WindowEvent arg0) {
			}
		});

		panel.add(b);

		this.add(panel);
		panel.setVisible(true);
	}

}
