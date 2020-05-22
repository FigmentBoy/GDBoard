package Main.SettingsPanels;

import Main.*;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.nio.file.*;
import java.util.Scanner;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;

public class BlockedUserSettings {
	private static JButtonUI defaultUI = new JButtonUI();
	private static JPanel blockedSettingsPanel = new JPanel();
	private static JPanel blockedListPanel = new JPanel();
	private static JScrollPane scrollPane = new JScrollPane(blockedListPanel);
	private static FancyTextArea blockedInput = new FancyTextArea(false);
	private static RoundedJButton addID = new RoundedJButton("\uECC8");
	private static int i = 0;
	private static double height = 0;

	public static JPanel createPanel() {

		defaultUI.setBackground(Defaults.BUTTON);
		defaultUI.setHover(Defaults.BUTTON_HOVER);
		defaultUI.setSelect(Defaults.SELECT);

		blockedSettingsPanel.setBackground(Defaults.TOP);
		blockedSettingsPanel.setLayout(null);

		JLabel label = new JLabel("Blocked Users:");
		label.setForeground(Defaults.FOREGROUND);
		label.setFont(Defaults.MAIN_FONT.deriveFont(14f));
		label.setBounds(25, 20, label.getPreferredSize().width + 5, label.getPreferredSize().height + 5);

		blockedInput.setBounds(160, 15, 200, 32);
		blockedInput.getDocument().putProperty("filterNewlines", Boolean.TRUE);
		addID.setBackground(Defaults.BUTTON);
		addID.setBounds(370, 16, 30, 30);
		addID.setFont(Defaults.SYMBOLS.deriveFont(22f));
		addID.setForeground(Defaults.FOREGROUND);
		addID.setUI(defaultUI);

		blockedSettingsPanel.add(addID);
		blockedSettingsPanel.add(blockedInput);
		blockedSettingsPanel.add(label);
		blockedListPanel.setDoubleBuffered(true);
		blockedListPanel.setBounds(0, 0, 415, 0);
		blockedListPanel.setPreferredSize(new Dimension(415, 0));
		blockedListPanel.setBackground(Defaults.SUB_MAIN);
		addID.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				try {
					Path file = Paths.get(System.getenv("APPDATA") + "\\GDBoard\\blockedUsers.txt");
					if (!Files.exists(file)) {
						Files.createFile(file);
					}
					boolean goThrough = true;
					Scanner sc = new Scanner(file.toFile());
					while (sc.hasNextLine()) {
						if (String.valueOf(blockedInput.getText()).equals(sc.nextLine())) {
							goThrough = false;
							break;
						}
					}
					sc.close();
					if (goThrough) {
						if (!blockedInput.getText().equalsIgnoreCase("")) {

							Files.write(file, (blockedInput.getText() + "\n").getBytes(), StandardOpenOption.APPEND);
							addButton(blockedInput.getText());
							blockedInput.setText("");
							blockedListPanel.updateUI();
						}
					}
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(null, "There was an error writing to the file!", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		scrollPane.getViewport().setBackground(Defaults.MAIN);
		scrollPane.setBounds(0, 60, 415 + 1, 582);
		scrollPane.setPreferredSize(new Dimension(415, 622));
		scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setUnitIncrement(30);
		scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(1, 622));
		scrollPane.getVerticalScrollBar().setOpaque(false);
		scrollPane.setOpaque(false);
		scrollPane.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {

			private final Dimension d = new Dimension();

			@Override
			protected JButton createDecreaseButton(int orientation) {
				return new JButton() {
					@Override
					public Dimension getPreferredSize() {
						return d;
					}
				};
			}

			@Override
			protected JButton createIncreaseButton(int orientation) {
				return new JButton() {
					@Override
					public Dimension getPreferredSize() {
						return d;
					}
				};
			}

			@Override
			protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
				Graphics2D g2 = (Graphics2D) g.create();
				Color color = new Color(0, 0, 0, 0);

				g2.setPaint(color);
				g2.fillRect(r.x, r.y, r.width, r.height);
				g2.dispose();
			}

			@Override
			protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
				Graphics2D g2 = (Graphics2D) g.create();
				Color color = new Color(0, 0, 0, 0);


				g2.setPaint(color);
				g2.fillRect(r.x, r.y, r.width, r.height);
				g2.dispose();
			}

			@Override
			protected void setThumbBounds(int x, int y, int width, int height) {
				super.setThumbBounds(x, y, width, height);
				scrollbar.repaint();
			}
		});

		File file = new File(System.getenv("APPDATA") + "\\GDBoard\\blockedUsers.txt");
		if (file.exists()) {
			Scanner sc = null;
			try {
				sc = new Scanner(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			assert sc != null;
			while (sc.hasNextLine()) {
				addButton(sc.nextLine());
			}
			sc.close();
		}
		blockedSettingsPanel.setBounds(0, 0, 415, 622);
		blockedSettingsPanel.add(scrollPane);
		return blockedSettingsPanel;

	}

	public static void removeUser(String user) {
		i--;
		if (i % 2 == 0) {
			height = height - 36.7;
			blockedListPanel.setBounds(0, 0, 415, (int) (height + 4));
			blockedListPanel.setPreferredSize(new Dimension(415, (int) (height + 4)));
			scrollPane.updateUI();
		}
		for (Component component : blockedListPanel.getComponents()) {
			if (component instanceof CurvedButton) {
				System.out.println(((CurvedButton) component).getLText());
				if (((CurvedButton) component).getLText().equalsIgnoreCase(user)) {
					blockedListPanel.remove(component);
					blockedListPanel.updateUI();
				}
			}
		}
	}

	public static void addButton(String user) {
		i++;
		System.out.println(height);
		if (i % 2 == 0) {
			height = height + 36.7;

			blockedListPanel.setBounds(0, 0, 415, (int) (height + 4));
			blockedListPanel.setPreferredSize(new Dimension(415, (int) (height + 4)));
			scrollPane.updateUI();
		}
		Path file = Paths.get(System.getenv("APPDATA") + "\\GDBoard\\blockedUsers.txt");
		CurvedButton button = new CurvedButton(user);

		button.setBackground(Defaults.BUTTON);
		button.setUI(defaultUI);
		button.setForeground(Defaults.FOREGROUND);
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setFont(Defaults.MAIN_FONT.deriveFont(14f));
		button.setPreferredSize(new Dimension(187, 30));
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				Object[] options = {"Yes", "No"};
				SettingsWindow.run = false;
				int n = JOptionPane.showOptionDialog(SettingsWindow.frame,
						"Unblock " + button.getLText() + "?",
						"Unblock User? (Temporary Menu)", JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, options, options[1]);

				if (n == 0) {
					if (Files.exists(file)) {
						try {
							Path temp = Paths.get(System.getenv("APPDATA") + "\\GDBoard\\_temp_");
							PrintWriter out = new PrintWriter(new FileWriter(temp.toFile()));
							Files.lines(file)
									.filter(line -> !line.contains(button.getLText()))
									.forEach(out::println);
							out.flush();
							out.close();
							Files.delete(file);
							Files.move(temp, temp.resolveSibling(System.getenv("APPDATA") + "\\GDBoard\\blockedUsers.txt"), StandardCopyOption.REPLACE_EXISTING);

						} catch (IOException ex) {
							ex.printStackTrace();
							JOptionPane.showMessageDialog(Overlay.frame, "There was an error writing to the file!", "Error", JOptionPane.ERROR_MESSAGE);
						}
					}
					removeUser(button.getLText());
				}
				SettingsWindow.run = true;
			}
		});
		button.refresh();
		blockedListPanel.add(button);
		blockedListPanel.updateUI();

	}
	public static void refreshUI() {
		defaultUI.setBackground(Defaults.BUTTON);
		defaultUI.setHover(Defaults.BUTTON_HOVER);
		defaultUI.setSelect(Defaults.SELECT);

		blockedSettingsPanel.setBackground(Defaults.TOP);
		for (Component component : blockedSettingsPanel.getComponents()) {
			if (component instanceof JButton) {
				for (Component component2 : ((JButton) component).getComponents()) {
					if (component2 instanceof JLabel) {
						component2.setForeground(Defaults.FOREGROUND);
					}
				}
				component.setBackground(Defaults.BUTTON);
				component.setForeground(Defaults.FOREGROUND);
			}
			if (component instanceof JLabel) {
				component.setForeground(Defaults.FOREGROUND);

			}
			if(component instanceof JTextArea){
				((FancyTextArea) component).refreshAll();
			}
		}
		blockedListPanel.setBackground(Defaults.SUB_MAIN);
		for (Component component : blockedListPanel.getComponents()) {
			if (component instanceof JButton) {
				for (Component component2 : ((JButton) component).getComponents()) {
					if (component2 instanceof JLabel) {
						component2.setForeground(Defaults.FOREGROUND);
					}
				}
				component.setBackground(Defaults.BUTTON);
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