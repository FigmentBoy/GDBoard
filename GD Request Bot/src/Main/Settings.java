package Main;

import Main.InnerWindows.*;

import java.awt.*;
import java.io.*;
import java.util.Properties;

public class Settings {

	private static Properties gfg = new Properties();
	private static FileInputStream in;
	private static BufferedWriter writer;
	private static File file = new File(Defaults.saveDirectory + "\\GDBoard\\config.properties");


	static {
		while (true) {
			try {
				writer = new BufferedWriter(new FileWriter(file, true));
				in = new FileInputStream(file);
				break;
			} catch (IOException e) {
				file.mkdir();
				file.getParentFile().mkdir();
			}
		}
	}

	static void setWindowSettings(String key, String setting) {

		if (key.equalsIgnoreCase("Requests")) {
			int x = Integer.parseInt(setting.split(",")[0]);
			int y = Integer.parseInt(setting.split(",")[1]);
			reqPin = Boolean.parseBoolean(setting.split(",")[2]);
			reqX = Boolean.parseBoolean(setting.split(",")[3]);
			requestsWLoc = new Point(x, y);
		}
		if (key.equalsIgnoreCase("Comments")) {
			int x = Integer.parseInt(setting.split(",")[0]);
			int y = Integer.parseInt(setting.split(",")[1]);
			comPin = Boolean.parseBoolean(setting.split(",")[2]);
			comX = Boolean.parseBoolean(setting.split(",")[3]);
			commentWLoc = new Point(x, y);
		}
		if (key.equalsIgnoreCase("Information")) {
			int x = Integer.parseInt(setting.split(",")[0]);
			int y = Integer.parseInt(setting.split(",")[1]);
			infoPin = Boolean.parseBoolean(setting.split(",")[2]);
			infoX = Boolean.parseBoolean(setting.split(",")[3]);
			infoWLoc = new Point(x, y);
		}
		if (key.equalsIgnoreCase("Actions")) {
			int x = Integer.parseInt(setting.split(",")[0]);
			int y = Integer.parseInt(setting.split(",")[1]);
			actPin = Boolean.parseBoolean(setting.split(",")[2]);
			actX = Boolean.parseBoolean(setting.split(",")[3]);
			actionsWLoc = new Point(x, y);
		}
		if (key.equalsIgnoreCase("Music - Newgrounds Audio")) {
			int x = Integer.parseInt(setting.split(",")[0]);
			int y = Integer.parseInt(setting.split(",")[1]);
			songPin = Boolean.parseBoolean(setting.split(",")[2]);
			songX = Boolean.parseBoolean(setting.split(",")[3]);
			songWLoc = new Point(x, y);
		}
		if (key.equalsIgnoreCase("Settings")) {
			int x = Integer.parseInt(setting.split(",")[0]);
			int y = Integer.parseInt(setting.split(",")[1]);
			settingsX = Boolean.parseBoolean(setting.split(",")[3]);
			settingsWLoc = new Point(x, y);
		}
		if (key.equalsIgnoreCase("Window")) {
			int x = Integer.parseInt(setting.split(",")[0]);
			int y = Integer.parseInt(setting.split(",")[1]);
			windowWLoc = new Point(x, y);
		}
	}

	static void writeLocation() throws IOException {
		writeSettings("song", songWLoc.x + "," + songWLoc.y + "," + songPin + "," + songX);
		writeSettings("actions", actionsWLoc.x + "," + actionsWLoc.y + "," + actPin + "," + actX);
		writeSettings("info", infoWLoc.x + "," + infoWLoc.y + "," + infoPin + "," + infoX);
		writeSettings("requests", requestsWLoc.x + "," + requestsWLoc.y + "," + reqPin + "," + reqX);
		writeSettings("comments", commentWLoc.x + "," + commentWLoc.y + "," + comPin + "," + comX);
		writeSettings("settings", settingsWLoc.x + "," + settingsWLoc.y + "," + settingsX);
		writeSettings("window", windowWLoc.x + "," + windowWLoc.y);
	}

	public static void writeSettings(String key, String setting) throws IOException {
		in.close();
		in = new FileInputStream(file);
		try {
			gfg.load(in);
		}
		catch (IOException e){
			in = new FileInputStream(file);
			gfg.load(in);
		}

		if (gfg.containsKey(key)) {
			BufferedReader file = new BufferedReader(new FileReader(Defaults.saveDirectory + "\\GDBoard\\config.properties"));
			StringBuilder inputBuffer = new StringBuilder();
			String line;
			while ((line = file.readLine()) != null) {
				inputBuffer.append(line);
				inputBuffer.append('\n');
			}
			file.close();

			FileOutputStream fileOut = new FileOutputStream(Defaults.saveDirectory + "\\GDBoard\\config.properties");
			fileOut.write(inputBuffer.toString().replace(key + "=" + gfg.get(key).toString(), key + "=" + setting).getBytes());
			fileOut.close();
			System.out.println(key + ": " + setting);
		} else {
			writer = new BufferedWriter(new FileWriter(file, true));
			writer.newLine();
			writer.write((key + "=" + setting));
			writer.close();
		}

	}

	public static String getSettings(String key) {
		while(true) {
			try {
				in.close();
				in = new FileInputStream(file);
				gfg.load(in);
				if (gfg.containsKey(key)) {
					return gfg.get(key).toString();
				}
				else{
					return "";
				}
			} catch (IOException e) {
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	static void loadSettings(boolean start) throws IOException {

		GraphicsDevice[] screens = GraphicsEnvironment
				.getLocalGraphicsEnvironment()
				.getScreenDevices();
		in = new FileInputStream(file);
		gfg.load(in);
		if(gfg.containsKey("oauth") ) {
			oauth = gfg.get("oauth").toString();
		}
		if(gfg.containsKey("channel")) {
			channel = gfg.get("channel").toString().toLowerCase();
		}
		if(gfg.containsKey("windowed")) {
			windowedMode = Boolean.parseBoolean(gfg.get("windowed").toString());
			hasWindowed = true;
		}
		if(gfg.containsKey("onboarding")) {
			onboarding = Boolean.parseBoolean(gfg.get("onboarding").toString());
		}
		if(gfg.containsKey("monitor")) {
			monitor = Integer.parseInt(gfg.get("monitor").toString());
			if (monitor >= screens.length) {
				monitor = 0;
			}
			hasMonitor = true;
		}


		if (!start) {

			if (gfg.containsKey("requests")) {
				String req = gfg.get("requests").toString();
				int x = Integer.parseInt(req.split(",")[0]);
				int y = Integer.parseInt(req.split(",")[1]);
				reqPin = Boolean.parseBoolean(req.split(",")[2]);
				reqX = Boolean.parseBoolean(req.split(",")[3]);
				requestsWLoc = new Point(x, y);
				LevelsWindow.setPin(reqPin);
				if (!Settings.getSettings("windowed").equalsIgnoreCase("true")) {
					LevelsWindow.setLocation(requestsWLoc);

					if (!reqX) {
						LevelsWindow.toggleVisible();
					}
				}
			}

			if (gfg.containsKey("actions")) {
				String act = gfg.get("actions").toString();
				int x = Integer.parseInt(act.split(",")[0]);
				int y = Integer.parseInt(act.split(",")[1]);
				actPin = Boolean.parseBoolean(act.split(",")[2]);
				actX = Boolean.parseBoolean(act.split(",")[3]);
				actionsWLoc = new Point(x, y);
				ActionsWindow.setPin(actPin);
				if (!Settings.getSettings("windowed").equalsIgnoreCase("true")) {
					ActionsWindow.setLocation(actionsWLoc);

					if (!actX) {
						ActionsWindow.toggleVisible();
					}
				}
			}

			if (gfg.containsKey("comments")) {
				String com = gfg.get("comments").toString();
				int x = Integer.parseInt(com.split(",")[0]);

				int y = Integer.parseInt(com.split(",")[1]);
				comPin = Boolean.parseBoolean(com.split(",")[2]);
				comX = Boolean.parseBoolean(com.split(",")[3]);
				commentWLoc = new Point(x, y);
				CommentsWindow.setPin(comPin);
				if (!Settings.getSettings("windowed").equalsIgnoreCase("true")) {
					CommentsWindow.setLocation(commentWLoc);

					if (!comX) {
						CommentsWindow.toggleVisible();
					}
				}
			}

			if (gfg.containsKey("info")){
				String info = gfg.get("info").toString();
				int x = Integer.parseInt(info.split(",")[0]);
				int y = Integer.parseInt(info.split(",")[1]);
				infoPin = Boolean.parseBoolean(info.split(",")[2]);
				infoX = Boolean.parseBoolean(info.split(",")[3]);
				infoWLoc = new Point(x, y);
				InfoWindow.setPin(infoPin);
				if (!Settings.getSettings("windowed").equalsIgnoreCase("true")) {
					InfoWindow.setLocation(infoWLoc);

					if (!infoX) {
						InfoWindow.toggleVisible();
					}
				}
			}

			if(gfg.containsKey("song")){
				String song = gfg.get("song").toString();
				int x = Integer.parseInt(song.split(",")[0]);
				int y = Integer.parseInt(song.split(",")[1]);
				songPin = Boolean.parseBoolean(song.split(",")[2]);
				songX = Boolean.parseBoolean(song.split(",")[3]);
				songWLoc = new Point(x, y);
				SongWindow.setPin(songPin);
				if (!Settings.getSettings("windowed").equalsIgnoreCase("true")) {
					SongWindow.setLocation(songWLoc);
					if (!songX) {
						SongWindow.toggleVisible();
					}
				}
			}
			if(gfg.containsKey("window")){
				String window = gfg.get("window").toString();
				int x = Integer.parseInt(window.split(",")[0]);
				int y = Integer.parseInt(window.split(",")[1]);
				windowWLoc = new Point(x, y);
				Windowed.setLocation(windowWLoc);
			}

			if(gfg.containsKey("settings")){
				String set = gfg.get("settings").toString();
				int x = Integer.parseInt(set.split(",")[0]);
				int y = Integer.parseInt(set.split(",")[1]);
				settingsX = Boolean.parseBoolean(set.split(",")[2]);
				settingsWLoc = new Point(x, y);
				if(Settings.getSettings("windowed").equalsIgnoreCase("false")) {
					SettingsWindow.setLocation(settingsWLoc);
				}
			}
		}
	}

	public static boolean windowedMode = false;
	static String oauth;
	//static String clientID;
	private static Point requestsWLoc = new Point(10, 10);
	private static Point infoWLoc = new Point(1920 - 400 - 10, 10);
	private static Point commentWLoc = new Point(10, 500);
	private static Point songWLoc = new Point(1920 - 300 - 10, 600);
	private static Point actionsWLoc = new Point(1920 - 300 - 10, 200);
	private static Point settingsWLoc = new Point(1920 / 2 - 250, 1080 / 2 - 300);
	private static Point windowWLoc = new Point(0, 0);
	static int monitor;
	public static String channel;
	static boolean hasMonitor = false;
	public static boolean onboarding = true;
	static boolean hasWindowed = false;
	private static boolean songPin = false;
	private static boolean infoPin = false;
	private static boolean comPin = false;
	private static boolean actPin = false;
	private static boolean reqPin = false;
	private static boolean songX = true;
	private static boolean infoX = true;
	private static boolean comX = true;
	private static boolean actX = true;
	private static boolean reqX = true;
	private static boolean settingsX = false;
	//static boolean hasClientID = false;


    /*static void setWindowedMode(boolean mode) {
        Settings.getSettings("windowed").equalsIgnoreCase("true") = mode;
    }*/

	public static Point getRequestsWLoc() {
		if (Settings.getSettings("windowed").equalsIgnoreCase("true")) {
			requestsWLoc = new Point(0, 0);
		}
		return requestsWLoc;
	}
	static Point getSettingsWLoc() {
		if (Settings.getSettings("windowed").equalsIgnoreCase("true")) {
			settingsWLoc = new Point(0, 0);
		}
		return settingsWLoc;
	}

	public static Point getInfoWLoc() {
		if (Settings.getSettings("windowed").equalsIgnoreCase("true")) {
			infoWLoc = new Point(0, 402);
		}
		return infoWLoc;
	}

	public static Point getCommentWLoc() {
		if (Settings.getSettings("windowed").equalsIgnoreCase("true")) {
			commentWLoc = new Point(402, 98);
		}
		return commentWLoc;
	}

	public static Point getSongWLoc() {
		if (Settings.getSettings("windowed").equalsIgnoreCase("true")) {
			songWLoc = new Point(402, 422);
		}
		return songWLoc;
	}

	public static Point getActionsWLoc() {
		if (Settings.getSettings("windowed").equalsIgnoreCase("true")) {
			actionsWLoc = new Point(402, 20);
		}
		return actionsWLoc;
	}
	static Point getWindowWLoc() {
		return windowWLoc;
	}
}
