package Main;

import SettingsPanels.*;
import com.cavariux.twitchirc.Chat.User;
import org.jnativehook.GlobalScreen;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    static List<User> mods = new ArrayList<>();
    static boolean starting = true;

    public static void main(String[] args) {
        //TODO Use nio everywhere
        try {
            Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
            logger.setLevel(Level.OFF);
            logger.setUseParentHandlers(false);

            UIManager.setLookAndFeel(new NimbusLookAndFeel() {
                @Override
                public void provideErrorFeedback(Component component) {
                }
            });
            Settings.loadSettings(true);
            Defaults.startMainThread();        //Starts thread that always checks for changes such as time, resolution, and color scheme

            if(Settings.onboarding){
                Onboarding.createPanel();
                Onboarding.loadSettings();
                Onboarding.frame.setVisible(true);

            }
            else{
                starting = false;
            }
            while(true) {
                if (!starting) {
                    GDBoardBot.start();
                    if (!Settings.hasWindowed) {
                        Settings.writeSettings("windowed", "false");
                    }
                    if (!Settings.hasMonitor) {
                        Settings.writeSettings("monitor", "0");
                    }

                    Thread.sleep(1000);
                    if(!Settings.onboarding){
                        JSONObject authObj = new JSONObject();
                        authObj.put("request_type", "connect");
                        authObj.put("oauth", Settings.oauth);
                        GDBoardBot.sendMessage(authObj.toString());
                    }
                    while (!GDBoardBot.connected && !GDBoardBot.failed) {
                        Thread.sleep(10);
                    }
                    if (GDBoardBot.failed) {
                        TwitchAPI.setOauth();
                    }
                    Main.sendMessage("Thank you for using GDBoard by Alphalaneous and TreeHouseFalcon! Type !help for list of commands!");

                    Overlay.setFrame();                //Creates the JFrame that contains everything
                    ControllerListener.hook();         //Starts Controller Listener

                    GlobalScreen.registerNativeHook();
                    GlobalScreen.addNativeKeyListener(new KeyListener());

                    if (!Settings.windowedMode) {
                        MainBar.createBar();            //Creates the main "Game Bar" in the top center
                    }
                    LevelsWindow.createPanel();         //Creates the Levels Window containing all the requests in the level queue
                    ActionsWindow.createPanel();        //Creates the Action Window containing buttons that do specific actions
                    InfoWindow.createPanel();           //Creates the Info Window containing the information of the selected level
                    CommentsWindow.createPanel();       //Creates the Comment Window containing the comments of the selected level
                    SongWindow.createPanel();           //Creates the Song Window allowing you to play the song of the selected level
                    SettingsWindow.createPanel();
                    if (Settings.windowedMode) {
                        Windowed.createPanel();
                    }
                    InfoWindow.refreshInfo();            //Refreshes the information shown on the Info Window for the first time
                    SongWindow.refreshInfo();            //Refreshes the information shown on the Song Window for the first time

                    if (Settings.windowedMode) {
                        Overlay.refreshUI(true);
                    }
                    Settings.loadSettings(false);
                    GeneralSettings.loadSettings();
                    OutputSettings.loadSettings();
                    RequestSettings.loadSettings();
                    AccountSettings.loadSettings();
                    ShortcutSettings.loadSettings();
                    if (Settings.windowedMode) {
                        Windowed.frame.setVisible(true);
                    }
                    Overlay.setVisible();
                    SettingsWindow.toFront();
                    SettingsWindow.toggleVisible();
                    OutputSettings.setOutputStringFile(Requests.parseInfoString(OutputSettings.outputString, 0));
                    break;
                }
                Thread.sleep(100);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(Overlay.frame, e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    static void sendMessage(String message) {
        JSONObject messageObj = new JSONObject();
        messageObj.put("request_type", "send_message");
        messageObj.put("message", message);
        GDBoardBot.sendMessage(messageObj.toString());
        System.out.println("test");
    }
}
