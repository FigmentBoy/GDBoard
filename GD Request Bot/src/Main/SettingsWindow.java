package Main;

import com.jidesoft.swing.ResizablePanel;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

class SettingsWindow {
	private static int width = 622;
	private static int height = 622;
	private static ResizablePanel window = new InnerWindow("Settings", 1920 / 2 - 250, 1080 / 2 - 300, width, height,
			"\uE713").createPanel();
	private static JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
	private static JPanel content = new JPanel();
	private static JPanel blankSpace = new JPanel();

	private static JButtonUI defaultUI = new JButtonUI();
	private static JButtonUI selectUI = new JButtonUI();

	private static JPanel general = SettingsPanels.GeneralSettings.createPanel();
	private static JPanel overlay = SettingsPanels.OverlaySettings.createPanel();
	private static JPanel accounts = SettingsPanels.AccountSettings.createPanel();
	private static JPanel shortcuts = SettingsPanels.ShortcutSettings.createPanel();
	private static JPanel personalization = SettingsPanels.PersonalizationSettings.createPanel();
	private static JPanel blocked = SettingsPanels.BlockedSettings.createPanel();

	static void createPanel() {

		blankSpace.setBounds(1, 31, 208, 20);
		blankSpace.setBackground(Defaults.MAIN);

		buttons.setBounds(1, 51, 208, height - 20);
		buttons.setBackground(Defaults.MAIN);

		content.setBounds(208, 31, 415, height);
		content.setBackground(Defaults.SUB_MAIN);
		content.setLayout(null);

		content.add(general);
		content.add(overlay);
		content.add(accounts);
		content.add(shortcuts);
		content.add(personalization);
		content.add(blocked);

		general.setVisible(true);
		overlay.setVisible(false);
		accounts.setVisible(false);
		shortcuts.setVisible(false);
		personalization.setVisible(false);
		blocked.setVisible(false);

		JButton general = createButton("General");
		general.setBackground(Defaults.SELECT);
		general.setUI(selectUI);
		JButton overlays = createButton("Overlays");
		JButton accounts = createButton("Accounts");
		JButton shortcuts = createButton("Shortcuts");
		JButton personalization = createButton("Personalization");
		JButton blocked = createButton("Blocked");

		buttons.add(general);
		//buttons.add(overlays);
		buttons.add(accounts);
		//buttons.add(shortcuts);
		//buttons.add(personalization);
		buttons.add(blocked);

		window.add(blankSpace);
		window.add(buttons);
		window.add(content);
		((InnerWindow) window).setPinVisible();
		((InnerWindow) window).refreshListener();
		Overlay.addToFrame(window);
	}

	static void refreshUI() {
		((InnerWindow) window).refreshUI();
		blankSpace.setBackground(Defaults.MAIN);
		defaultUI.setBackground(Defaults.MAIN);
		defaultUI.setHover(Defaults.HOVER);
		defaultUI.setSelect(Defaults.SELECT);
		selectUI.setBackground(Defaults.SELECT);
		selectUI.setHover(Defaults.BUTTON_HOVER);
		buttons.setBackground(Defaults.MAIN);
		content.setBackground(Defaults.SUB_MAIN);
		for (Component component : buttons.getComponents()) {
			if (component instanceof JButton) {
				for (Component component2 : ((JButton) component).getComponents()) {
					if (component2 instanceof JLabel) {
						component2.setForeground(Defaults.FOREGROUND);
					}
				}
				if(!((JButton) component).getUI().equals(selectUI)) {
					component.setBackground(Defaults.MAIN);
				}

			}
		}
	}

	/*static void toggleVisible() {
		((InnerWindow) window).toggle();
	}*/

	static void setInvisible() {
		((InnerWindow) window).setInvisible();
	}

	static void setVisible() {
		((InnerWindow) window).setVisible();
	}

	private static JButton createButton(String text) {

		defaultUI.setBackground(Defaults.MAIN);
		selectUI.setBackground(Defaults.SELECT);
		selectUI.setBackground(Defaults.SELECT);
		selectUI.setHover(Defaults.BUTTON_HOVER);

		JButton button = new JButton();
		JLabel label = new JLabel(text);

		label.setBounds(20, 9, 208, 20);
		label.setForeground(Defaults.FOREGROUND);
		label.setFont(new Font("bahnschrift", Font.PLAIN, 14));

		button.setLayout(null);
		button.add(label);
		button.setBackground(Defaults.MAIN);
		button.setUI(defaultUI);
		button.setForeground(Defaults.FOREGROUND);
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setPreferredSize(new Dimension(208, 38));

		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				((InnerWindow) window).moveToFront();
				super.mousePressed(e);
				for (Component component2 : button.getComponents()) {
					if (component2 instanceof JLabel) {
						System.out.println(((JLabel) component2).getText());
						for (Component componentA : content.getComponents()) {
							if (componentA instanceof JPanel) {
								componentA.setVisible(false);
							}
						}
						switch (((JLabel) component2).getText()) {
							case "General":
								general.setVisible(true);
								break;
							case "Overlays":
								overlay.setVisible(true);
								break;
							case "Accounts":
								accounts.setVisible(true);
								break;
							case "Shortcuts":
								shortcuts.setVisible(true);
								break;
							case "Personalization":
								personalization.setVisible(true);
								break;
							case "Blocked":
								blocked.setVisible(true);
								break;
						}
						break;
					}
				}
				for (Component component : buttons.getComponents()) {
					if (component instanceof JButton) {
						((JButton) component).setUI(defaultUI);
						component.setBackground(Defaults.MAIN);
						buttons.updateUI();
						
					}
				}
				button.setUI(selectUI);
				button.setBackground(Defaults.SELECT);
			}
		});
		return button;
	}
	//region SetLocation
	static void setLocation(Point point){
		window.setLocation(point);
	}
	//endregion
	//region Toggle Visible
	static void toggleVisible() {
		((InnerWindow) window).toggle();
	}
	//endregion
	//region SetSettings
	static void setSettings(){
		((InnerWindow) window).setSettings();
	}
	//endregion
}
