package Main;

import com.cavariux.twitchirc.Chat.Channel;
import com.cavariux.twitchirc.Chat.User;
import com.cavariux.twitchirc.Core.TwitchBot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatReader extends TwitchBot {

    ChatReader(){
        this.setUsername("chatBot");
        this.setOauth_Key("oauth:" + Settings.oauth);
    }
    @Override
    public void onMessage(User user, Channel channel, String message) {
        Matcher m = Pattern.compile("\\s*(\\d{6,})\\s*").matcher(message);
        if(!message.startsWith("!") && !m.find() && !String.valueOf(user).equalsIgnoreCase("gdboard")) {
            Thread thread1 = new Thread(() -> {
                try {
                    while (ServerChatBot.processing) {
                        Thread.sleep(50);
                    }
                    //ServerChatBot.onMessage(String.valueOf(user), message, user.isMod(channel), false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            thread1.start();
        }
    }
}