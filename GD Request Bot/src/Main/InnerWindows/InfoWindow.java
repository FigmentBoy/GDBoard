package Main.InnerWindows;

import Main.*;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

public class InfoWindow {

	private static int height = 110;
	private static int width = 400;
	private static JPanel panel = new JPanel();
	private static JPanel descPanel = new JPanel();
	private static JPanel fullPanel = new JPanel(null);
	private static JLabel likes = new JLabel();
	private static JLabel downloads = new JLabel();
	private static JLabel length = new JLabel();
	private static JLabel password = new JLabel();

	private static JTextPane description = new JTextPane();
	private static JPanel window = new InnerWindow("Information", Settings.getInfoWLoc().x, Settings.getInfoWLoc().y, width, height, "\uE946", false).createPanel();

	public static void createPanel() {

		panel.setPreferredSize(new Dimension(width, height));
		panel.setBounds(0, 0, 160, height);
		panel.setBackground(Defaults.MAIN);

		descPanel.setPreferredSize(new Dimension(240, height));
		descPanel.setBounds(160, 0, 240, height);
		descPanel.setBackground(Defaults.SUB_MAIN);
		descPanel.setLayout(null);
		descPanel.setOpaque(true);

		panel.setLayout(null);
		likes = createLabel("LIKES: NA", 10, width/2);
		downloads = createLabel("DOWNLOADS: NA", 32, width/2);
		length = createLabel("LENGTH: NA", 54, width/2);
		password = createLabel("PASSWORD: NA", 76, width/2);

		description.setText("N/A");
		StyledDocument doc = description.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		description.setFont(Defaults.MAIN_FONT.deriveFont(14f));
		description.setOpaque(false);
		description.setEditable(false);
		description.setForeground(Defaults.FOREGROUND);
		description.setBackground(new Color(0,0,0,0));
		description.setBounds(5,5,230, height-10);

		panel.add(likes);
		panel.add(downloads);
		panel.add(length);
		panel.add(password);
		descPanel.add(description);
		fullPanel.setBounds(1,31,400,110);
		fullPanel.add(panel);
		fullPanel.add(descPanel);
		window.add(fullPanel);
		((InnerWindow)window).refreshListener();
		Overlay.addToFrame(window);
		refreshInfo();
	}
	public static JPanel getInfoWindow(){
		return fullPanel;
	}
	public static void setPin(boolean pin){
		((InnerWindow) window).setPin(pin);
	}
	public static void refreshInfo() {
		if (Requests.levels.size() == 0) {
			likes.setText("LIKES: NA");
			description.setText("NA");
			length.setText("LENGTH: NA");
			password.setText("PASSWORD: NA");

			downloads.setText("DOWNLOADS: N/A");
		} else {
			likes.setText("LIKES: " + Requests.levels.get(LevelsWindow.getSelectedID()).getLikes());
			description.setText(Requests.levels.get(LevelsWindow.getSelectedID()).getDescription());
			length.setText("LENGTH: " + Requests.levels.get(LevelsWindow.getSelectedID()).getLength());
			String pass;
			if((pass = Requests.levels.get(LevelsWindow.getSelectedID()).getPassword()) != null) {
				if (pass.equalsIgnoreCase("-2")) {
					password.setText("FREE COPY");
				}
				else if (pass.equalsIgnoreCase("-1")) {
					password.setText("NO COPY");
				}else {
					password.setText("PASSWORD: " + pass);
				}
			}
			else {
				password.setText("NO COPY");

			}
			downloads.setText("DOWNLOADS: " + Requests.levels.get(LevelsWindow.getSelectedID()).getDownloads());
		}
	}
	public static void refreshUI() {
		((InnerWindow) window).refreshUI();
		panel.setBackground(Defaults.MAIN);
		descPanel.setBackground(Defaults.SUB_MAIN);
		likes.setForeground(Defaults.FOREGROUND);
		length.setForeground(Defaults.FOREGROUND);
		password.setForeground(Defaults.FOREGROUND);
		downloads.setForeground(Defaults.FOREGROUND);
		description.setForeground(Defaults.FOREGROUND);
	}
	private static JLabel createLabel(String text, int y, int width){
		JLabel label = new JLabel(text);
		label.setFont(Defaults.MAIN_FONT.deriveFont(14f));
		label.setBounds(10, y, width, 20);
		label.setForeground(Defaults.FOREGROUND);
		return label;
	}
	public String getName(){
		return "Information";
	}
	public String getIcon(){
		return "\uE946";
	}
	public static void toggleVisible() {
		((InnerWindow) window).toggle();
	}
	
	public static void setInvisible() {
		((InnerWindow) window).setInvisible();
	}

	//region SetLocation
	public static void setLocation(Point point){
		window.setLocation(point);
	}
	//endregion

	//region SetSettings
	public static void setSettings(){
		((InnerWindow) window).setSettings();
	}
	//endregion

	public static void setVisible() {
		((InnerWindow) window).setVisible();
	}
}