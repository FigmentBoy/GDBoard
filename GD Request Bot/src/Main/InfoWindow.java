package Main;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

class InfoWindow {

	private static int height = 110;
	private static int width = 400;
	private static JPanel panel = new JPanel();
	private static JPanel descPanel = new JPanel();
	private static JLabel likes = new JLabel();
	private static JLabel downloads = new JLabel();
	private static JLabel length = new JLabel();
	private static JTextPane description = new JTextPane();
	private static JPanel window = new InnerWindow("Information", Settings.getInfoWLoc().x, Settings.getInfoWLoc().y, width, height, "\uE946", false).createPanel();

	static void createPanel() {

		panel.setPreferredSize(new Dimension(width, height));
		panel.setBounds(1, 31, 160, height);
		panel.setBackground(Defaults.MAIN);

		descPanel.setPreferredSize(new Dimension(240, height));
		descPanel.setBounds(161, 31, 240, height);
		descPanel.setBackground(Defaults.SUB_MAIN);
		descPanel.setLayout(null);
		descPanel.setOpaque(true);

		panel.setLayout(null);
		likes = createLabel("LIKES: N/A", 10, width/2);
		downloads = createLabel("DOWNLOADS: N/A", 32, width/2);
		length = createLabel("LENGTH: NA", 54, width/2);
		description.setText("N/A");
		StyledDocument doc = description.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		description.setFont(new Font("bahnschrift", Font.PLAIN, 14));
		description.setOpaque(false);
		description.setEditable(false);
		description.setForeground(Defaults.FOREGROUND);
		description.setBackground(new Color(0,0,0,0));
		description.setBounds(5,5,230, height-10);

		panel.add(likes);
		panel.add(downloads);
		panel.add(length);
		descPanel.add(description);

		window.add(panel);
		window.add(descPanel);
		((InnerWindow)window).refreshListener();
		Overlay.addToFrame(window);
	}
	static void setPin(boolean pin){
		((InnerWindow) window).setPin(pin);
	}
	static void refreshInfo() {
		if (Requests.levels.size() == 0) {
			likes.setText("LIKES: N/A");
			description.setText("N/A");
			length.setText("LENGTH: NA");
			downloads.setText("DOWNLOADS: N/A");
		} else {
			likes.setText("LIKES: " + Requests.levels.get(LevelsWindow.getSelectedID()).getLikes());
			description.setText(Requests.levels.get(LevelsWindow.getSelectedID()).getDescription());
			length.setText("LENGTH: " + Requests.levels.get(LevelsWindow.getSelectedID()).getLength());
			downloads.setText("DOWNLOADS: " + Requests.levels.get(LevelsWindow.getSelectedID()).getDownloads());
		}
	}
	static void refreshUI() {
		((InnerWindow) window).refreshUI();
		panel.setBackground(Defaults.MAIN);
		descPanel.setBackground(Defaults.SUB_MAIN);
		likes.setForeground(Defaults.FOREGROUND);
		length.setForeground(Defaults.FOREGROUND);
		downloads.setForeground(Defaults.FOREGROUND);
		description.setForeground(Defaults.FOREGROUND);
	}
	private static JLabel createLabel(String text, int y, int width){
		JLabel label = new JLabel(text);
		label.setFont(new Font("bahnschrift", Font.PLAIN, 14));
		label.setBounds(10, y, width, 20);
		label.setForeground(Defaults.FOREGROUND);
		return label;
	}
	
	static void toggleVisible() {
		((InnerWindow) window).toggle();
	}
	
	static void setInvisible() {
		((InnerWindow) window).setInvisible();
	}

	//region SetLocation
	static void setLocation(Point point){
		window.setLocation(point);
	}
	//endregion

	//region SetSettings
	static void setSettings(){
		((InnerWindow) window).setSettings();
	}
	//endregion

	static void setVisible() {
		((InnerWindow) window).setVisible();
	}
}
