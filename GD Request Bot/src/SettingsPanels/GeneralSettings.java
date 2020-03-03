package SettingsPanels;

import Main.Defaults;
import Main.Main;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class GeneralSettings {
	public static JPanel createPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setDoubleBuffered(true);
		panel.setBounds(0, 0, 415, 622);
		panel.setBackground(Defaults.SUB_MAIN);
		InputStream is = null;
		try {
			is = new FileInputStream(System.getenv("APPDATA") + "\\GDBoard\\version.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		assert is != null;
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		JLabel label = null;
		try {
			label = new JLabel("GDBoard version: " + br.readLine().replaceAll("version=", ""));
		} catch (IOException e) {
			e.printStackTrace();
		}
		assert label != null;
		label.setForeground(Defaults.FOREGROUND2);
		label.setFont(new Font("bahnschrift", Font.PLAIN, 14));
		label.setBounds(25,20,label.getPreferredSize().width+5,label.getPreferredSize().height+5);
		panel.add(label);
		return panel;
		
	}
}