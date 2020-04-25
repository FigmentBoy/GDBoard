package SettingsPanels;

import Main.*;
import Main.ActionsWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.KeyListener;
import java.io.*;

public class GeneralSettings {
	private static JButtonUI defaultUI = new JButtonUI();
	public static boolean followersOption = false;
	public static boolean subsOption = false;
	public static boolean repeatedOption = false;
	public static boolean nowPlayingOption = false;
	public static boolean autoDownloadOption = false;
	public static boolean queueLimitBoolean = false;
	public static boolean userLimitOption = false;
	public static boolean userLimitStreamOption = false;
	private static JLabel versionLabel = new JLabel();
	private static CheckboxButton followers = createButton("Followers Only", 50);
	private static CheckboxButton nowPlaying = createButton("Disable Now Playing Message", 110);
	private static CheckboxButton repeated = createButton("Disable Repeated Requests", 140);
	//private static CheckboxButton autoDownload = createButton("Automatically download Music (Experimental)", 110);
	private static CheckboxButton subOnly = createButton("Subscribers Only", 80);

	private static CheckboxButton queueLimitText = createButton("Maximum Queue Size: ", 170);
	private static CheckboxButton userLimitText = createButton("In Queue Request Limit: ", 245);
	private static CheckboxButton userLimitStreamText = createButton("All Stream Request Limit: ", 320);
	private static JLabel donation = new JLabel();
	private static CurvedButton donationButton = new CurvedButton("Donate");


	public static int queueLimit = 0;
	public static int userLimit = 0;
	public static int userLimitStream = 0;

	private static FancyTextArea queueSizeInput = new FancyTextArea(true);
	private static FancyTextArea userLimitInput = new FancyTextArea(true);
	private static FancyTextArea userLimitStreamInput = new FancyTextArea(true);
	private static JPanel panel = new JPanel();

	public static JPanel createPanel() {
		defaultUI.setBackground(Defaults.BUTTON);
		defaultUI.setHover(Defaults.BUTTON_HOVER);
		defaultUI.setSelect(Defaults.SELECT);


		panel.setLayout(null);
		panel.setDoubleBuffered(true);
		panel.setBounds(0, 0, 415, 622);
		panel.setBackground(Defaults.SUB_MAIN);

		InputStream is;
		try {
			is = new FileInputStream(System.getenv("APPDATA") + "\\GDBoard\\version.txt");
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			versionLabel.setText("GDBoard version: " + br.readLine().replaceAll("version=", ""));

		} catch (IOException e) {
			versionLabel.setText("GDBoard version: unknown");
		}

		versionLabel.setForeground(Defaults.FOREGROUND2);
		versionLabel.setFont(new Font("bahnschrift", Font.PLAIN, 14));
		versionLabel.setBounds(25,20,versionLabel.getPreferredSize().width+5,versionLabel.getPreferredSize().height+5);

		donation.setForeground(Defaults.FOREGROUND);
		donation.setFont(new Font("bahnschrift", Font.PLAIN, 14));
		donation.setText("It would be highly appreciated if you donate!");
		donation.setBounds(25 + 365/2 - donation.getPreferredSize().width/2,510,donation.getPreferredSize().width+5,donation.getPreferredSize().height+5);

		donationButton.setBounds(25,540, 365,60);
		donationButton.setPreferredSize(new Dimension(365,60));
		donationButton.setFont(new Font("bahnschrift", Font.PLAIN, 14));
		donationButton.setUI(defaultUI);
		donationButton.setForeground(Defaults.FOREGROUND);
		donationButton.setBackground(Defaults.BUTTON);
		donationButton.refresh();

		followers.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				followersOption = followers.getSelectedState();
			}
		});

		subOnly.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				subsOption = subOnly.getSelectedState();
			}
		});

		nowPlaying.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				nowPlayingOption = nowPlaying.getSelectedState();
			}
		});
		repeated.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				repeatedOption = repeated.getSelectedState();
			}
		});

		/*autoDownload.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				autoDownloadOption = autoDownload.getSelectedState();
			}
		});*/

		queueLimitText.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				queueLimitBoolean = queueLimitText.getSelectedState();
				if(!queueLimitBoolean){
					queueSizeInput.setEditable(false);
				}
				else{
					queueSizeInput.setEditable(true);
				}
			}
		});
		queueSizeInput.setEditable(false);
		queueSizeInput.setBounds(25,203,365, 32);
		queueSizeInput.getDocument().putProperty("filterNewlines", Boolean.TRUE);
		queueSizeInput.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) { }
			@Override
			public void keyPressed(KeyEvent e) { }
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					queueLimit = Integer.parseInt(queueSizeInput.getText());
				}
				catch (NumberFormatException f){
					queueLimit = 0;
				}
				System.out.println(queueLimit);
			}
		});

		userLimitText.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				userLimitOption = userLimitText.getSelectedState();
				if(!userLimitOption){
					userLimitInput.setEditable(false);
				}
				else{
					userLimitInput.setEditable(true);
				}
			}
		});

		userLimitInput.setEditable(false);
		userLimitInput.setBounds(25,275,365, 32);
		userLimitInput.getDocument().putProperty("filterNewlines", Boolean.TRUE);
		userLimitInput.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) { }
			@Override
			public void keyPressed(KeyEvent e) { }
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					userLimit = Integer.parseInt(userLimitInput.getText());
				}
				catch (NumberFormatException f){
					userLimit = 0;
				}
				System.out.println(userLimit);
			}
		});

		userLimitStreamText.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				userLimitStreamOption = userLimitStreamText.getSelectedState();
				if(!userLimitStreamOption){
					userLimitStreamInput.setEditable(false);
				}
				else{
					userLimitStreamInput.setEditable(true);
				}
			}
		});

		userLimitStreamInput.setEditable(false);
		userLimitStreamInput.setBounds(25,350,365, 32);
		userLimitStreamInput.getDocument().putProperty("filterNewlines", Boolean.TRUE);
		userLimitStreamInput.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) { }
			@Override
			public void keyPressed(KeyEvent e) { }
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					userLimitStream = Integer.parseInt(userLimitStreamInput.getText());
				}
				catch (NumberFormatException f){
					userLimitStream = 0;
				}
			}
		});


		panel.add(followers);
		panel.add(subOnly);
		panel.add(nowPlaying);
		panel.add(repeated);
		//panel.add(autoDownload);
		panel.add(versionLabel);
		panel.add(queueLimitText);
		panel.add(queueSizeInput);
		panel.add(userLimitText);
		panel.add(userLimitInput);
		panel.add(userLimitStreamText);
		panel.add(userLimitStreamInput);
		//panel.add(donation);
		//panel.add(donationButton);
		return panel;
	}

	public static void loadSettings(){
		try {
			followersOption = Boolean.parseBoolean(Settings.getSettings("followers"));
			subsOption = Boolean.parseBoolean(Settings.getSettings("subscribers"));
			nowPlayingOption = Boolean.parseBoolean(Settings.getSettings("disableNP"));
			repeatedOption = Boolean.parseBoolean(Settings.getSettings("repeatedRequests"));
			//autoDownloadOption = Boolean.parseBoolean(Settings.getSettings("autoDL"));
			autoDownloadOption = false;
			queueLimitBoolean = Boolean.parseBoolean(Settings.getSettings("queueLimitEnabled"));
			if(!Settings.getSettings("queueLimit").equalsIgnoreCase("")) {
				queueLimit = Integer.parseInt(Settings.getSettings("queueLimit"));
				queueSizeInput.setText(String.valueOf(queueLimit));
			}
			userLimitOption = Boolean.parseBoolean(Settings.getSettings("userLimitEnabled"));
			if(!Settings.getSettings("userLimit").equalsIgnoreCase("")) {
				userLimit = Integer.parseInt(Settings.getSettings("userLimit"));
				userLimitInput.setText(String.valueOf(userLimit));
			}
			userLimitStreamOption = Boolean.parseBoolean(Settings.getSettings("userLimitStreamEnabled"));
			if(!Settings.getSettings("userLimitStream").equalsIgnoreCase("")) {
				userLimitStream = Integer.parseInt(Settings.getSettings("userLimitStream"));
				userLimitStreamInput.setText(String.valueOf(userLimitStream));
			}
			followers.setChecked(followersOption);
			nowPlaying.setChecked(nowPlayingOption);
			subOnly.setChecked(subsOption);
			repeated.setChecked(repeatedOption);
			//autoDownload.setChecked(autoDownloadOption);
			queueLimitText.setChecked(queueLimitBoolean);
			userLimitText.setChecked(userLimitOption);
			userLimitStreamText.setChecked(userLimitStreamOption);
			if(!queueLimitBoolean){
				queueSizeInput.setEditable(false);
			}
			else{
				queueSizeInput.setEditable(true);
			}
			if(!userLimitOption){
				userLimitInput.setEditable(false);
			}
			else{
				userLimitInput.setEditable(true);
			}
			if(!userLimitStreamOption){
				userLimitStreamInput.setEditable(false);
			}
			else{
				userLimitStreamInput.setEditable(true);
			}
		}
		catch (IOException e){
			JOptionPane.showMessageDialog(null, "There was an error reading the config file!", "Error",  JOptionPane.ERROR_MESSAGE);
		}
	}
	public static void setSettings(){
		try {
			Settings.writeSettings("followers", String.valueOf(followersOption));
			Settings.writeSettings("subscribers", String.valueOf(subsOption));
			Settings.writeSettings("disableNP", String.valueOf(nowPlayingOption));
			Settings.writeSettings("autoDL", String.valueOf(autoDownloadOption));
			Settings.writeSettings("queueLimitEnabled", String.valueOf(queueLimitBoolean));
			Settings.writeSettings("queueLimit", String.valueOf(queueLimit));
			Settings.writeSettings("userLimitEnabled", String.valueOf(userLimitOption));
			Settings.writeSettings("userLimit", String.valueOf(userLimit));
			Settings.writeSettings("userLimitStreamEnabled", String.valueOf(userLimitStreamOption));
			Settings.writeSettings("userLimitStream", String.valueOf(userLimitStream));
			Settings.writeSettings("repeatedRequests", String.valueOf(repeatedOption));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "There was an error writing to the file!", "Error",  JOptionPane.ERROR_MESSAGE);
		}
	}
	private static JLabel createLabel(String text, int y){
		JLabel label = new JLabel(text);
		label.setFont(new Font("bahnschrift", Font.PLAIN, 14));
		label.setBounds(25,y,label.getPreferredSize().width + 5,30);
		label.setForeground(Defaults.FOREGROUND);

		return label;
	}
	private static CheckboxButton createButton(String text, int y){

		CheckboxButton button = new CheckboxButton(text);
		button.setBounds(25,y,365,30);
		button.setUI(defaultUI);
		button.setForeground(Defaults.FOREGROUND);
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setFont(new Font("bahnschrift", Font.PLAIN, 14));
		button.refresh();
		return button;
	}
	public static void refreshUI() {
		defaultUI.setBackground(Defaults.BUTTON);
		defaultUI.setHover(Defaults.BUTTON_HOVER);
		defaultUI.setSelect(Defaults.SELECT);

		panel.setBackground(Defaults.SUB_MAIN);
		for (Component component : panel.getComponents()) {
			if (component instanceof JButton) {
				for (Component component2 : ((JButton) component).getComponents()) {
					if (component2 instanceof JLabel) {
						component2.setForeground(Defaults.FOREGROUND);
					}
				}
				component.setBackground(Defaults.BUTTON);
			}
			if (component instanceof JLabel && !(((JLabel) component).getText().contains("GDBoard"))) {
				component.setForeground(Defaults.FOREGROUND);

			}
			if(component instanceof JTextArea){
				((FancyTextArea) component).refreshAll();
			}
			if(component instanceof CheckboxButton){
				((CheckboxButton) component).refresh();
			}

		}
	}
}
