package Main;

import SettingsPanels.GeneralSettings;
import SettingsPanels.OutputSettings;
import SettingsPanels.RequestSettings;
import com.github.alex1304.jdash.client.AnonymousGDClient;
import com.github.alex1304.jdash.client.GDClientBuilder;
import com.github.alex1304.jdash.entity.GDLevel;
import com.github.alex1304.jdash.exception.MissingAccessException;
import org.apache.commons.io.FileUtils;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.GZIPInputStream;

class Requests {

    static ArrayList<LevelData> levels = new ArrayList<>();
    private static HashMap<String, Integer> userStreamLimitMap = new HashMap<>();

    static void addRequest(String ID, String requester) {
        OutputSettings.setOutputStringFile(Requests.parseInfoString(OutputSettings.outputString, 0));
        if (MainBar.requests) {
            Path blocked = Paths.get(System.getenv("APPDATA") + "\\GDBoard\\blocked.txt");
            try {
                if (Files.exists(blocked)) {
                    Scanner sc = new Scanner(blocked.toFile());
                    while (sc.hasNextLine()) {
                        if (ID.equals(sc.nextLine())) {
                            sc.close();
                            Main.sendMessage("@" + requester + " That Level is Blocked!");
                            System.out.println("Blocked ID");
                            return;
                        }
                    }
                    sc.close();
                }
                Path blockedUser = Paths.get(System.getenv("APPDATA") + "\\GDBoard\\blockedUsers.txt");
                if (Files.exists(blockedUser)) {
                    Scanner sc = new Scanner(blockedUser.toFile());
                    while (sc.hasNextLine()) {
                        if (requester.equalsIgnoreCase(sc.nextLine())) {
                            sc.close();
                            return;
                        }
                    }
                    sc.close();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            for (int k = 0; k < levels.size(); k++) {

                if (ID.equals(levels.get(k).getLevelID())) {
                    int j = k + 1;
                    Main.sendMessage(
                            "@" + requester + " Level is already in the queue at position " + j + "!");
                    System.out.println("Level Already Exists");
                    return;
                }
            }
            if (GeneralSettings.queueLimitBoolean && (levels.size() >= GeneralSettings.queueLimit)) {
                System.out.println(GeneralSettings.queueLimit + ", " + (levels.size()));
                Main.sendMessage("@" + requester + " The queue is full!");
                return;
            }
            if (GeneralSettings.userLimitOption) {
                int size = 0;
                for (LevelData level : levels) {
                    if (level.getRequester().equalsIgnoreCase(requester)) {
                        size++;
                    }
                }
                if (size >= GeneralSettings.userLimit) {
                    Main.sendMessage("@" + requester + " You have the maximum amount of levels in the queue!");
                    return;
                }
            }

            if (GeneralSettings.userLimitStreamOption) {
                if (userStreamLimitMap.containsKey(requester)) {
                    if (userStreamLimitMap.get(requester) >= GeneralSettings.userLimitStream) {
                        Main.sendMessage("@" + requester + " You've reached the maximum amount of levels for the stream!");
                        return;
                    }
                }
            }
            if (userStreamLimitMap.containsKey(requester)) {
                userStreamLimitMap.put(requester, userStreamLimitMap.get(requester) + 1);
            } else {
                userStreamLimitMap.put(requester, 1);
            }
            System.out.println(userStreamLimitMap.get(requester));

            AnonymousGDClient client = GDClientBuilder.create().buildAnonymous();
            GDLevel level;
            try {
                level = client.getLevelById(Integer.parseInt(ID)).block();
            } catch (MissingAccessException | NumberFormatException e) {
                Main.sendMessage("@" + requester + " That level ID doesn't exist!");
                return;
            } catch (Exception e) {
                Main.sendMessage("@" + requester + " Level search failed... (Servers down?)");
                return;
            }
            LevelData levelData = new LevelData();
            // --------------------
            Thread parse;
            if (level != null && RequestSettings.ratedOption && !(level.getStars() > 0)) {
                Main.sendMessage("@" + requester + " Please send star rated levels only!");
                return;
            }
            levelData.setRequester(requester);
            levelData.setAuthor(Objects.requireNonNull(level).getCreatorName());
            levelData.setName(level.getName());
            levelData.setDifficulty(level.getDifficulty().toString());
            levelData.setDescription(level.getDescription());
            levelData.setLikes(String.valueOf(level.getLikes()));
            levelData.setDownloads(String.valueOf(level.getDownloads()));
            levelData.setSongURL(Objects.requireNonNull(level.getSong().block()).getDownloadURL());
            levelData.setLength(level.getLength().toString());
            levelData.setLevelID(ID);
            levelData.setVersion(level.getGameVersion());
            levelData.setEpic(level.isEpic());
            levelData.setSongID(String.valueOf(Objects.requireNonNull(level.getSong().block()).getId()));
            levelData.setStars(level.getStars());

            if (levelData.getDescription().toLowerCase().contains("nong")) {
                String[] words = levelData.getDescription().split(" ");
                for (String word : words) {
                    if (isValisURL(word)){
                        levelData.setSongURL(word);
                    }
                }
            }

            levelData.setSongName(Objects.requireNonNull(level.getSong().block()).getSongTitle());
            levelData.setSongAuthor(Objects.requireNonNull(level.getSong().block()).getSongAuthorName());

            if (level.getFeaturedScore() > 0) {
                levelData.setFeatured();
            }

            if (level.isDemon()) {
                if (level.getDifficulty().toString().equalsIgnoreCase("EASY")) {
                    levelData.setDifficulty("easy demon");
                } else if (level.getDifficulty().toString().equalsIgnoreCase("NORMAL")) {
                    levelData.setDifficulty("medium demon");
                } else if (level.getDifficulty().toString().equalsIgnoreCase("HARD")) {
                    levelData.setDifficulty("hard demon");
                } else if (level.getDifficulty().toString().equalsIgnoreCase("HARDER")) {
                    levelData.setDifficulty("insane demon");
                } else if (level.getDifficulty().toString().equalsIgnoreCase("INSANE")) {
                    levelData.setDifficulty("extreme demon");
                }
            }

            parse = new Thread(() -> {
                if (!(level.getStars() > 0) && level.getGameVersion() / 10 >= 2) {
                    parse(Objects.requireNonNull(Objects
                            .requireNonNull(client.getLevelById(Long.parseLong(ID)).block()).download().block())
                            .getData(), ID
                    );
                    LevelsWindow.updateUI(levelData.getLevelID(), levelData.getContainsVulgar(), levelData.getContainsImage(), levelData.getAnalyzed());
                }
            });
            parse.start();

            levels.add(levelData);
            if (GeneralSettings.autoDownloadOption) {

            }
            Main.sendMessage("@" + levelData.getRequester() + " " + levelData.getName() + " ("
                    + levelData.getLevelID() + ") has been added to the queue at position " + levels.size() + "!");
            if (levels.size() == 1) {
                StringSelection selection = new StringSelection(Requests.levels.get(0).getLevelID());
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(selection, selection);
                if (!GeneralSettings.nowPlayingOption) {
                    Main.sendMessage("Now Playing " + Requests.levels.get(0).getName() + " ("
                            + Requests.levels.get(0).getLevelID() + "). Requested by "
                            + Requests.levels.get(0).getRequester());
                }
            }
            OutputSettings.setOutputStringFile(Requests.parseInfoString(OutputSettings.outputString, 0));
            LevelsWindow.createButton(levelData.getName(), levelData.getAuthor(), levelData.getLevelID(), levelData.getDifficulty(), levelData.getEpic(), levelData.getFeatured(), levelData.getStars(), levelData.getRequester(), levelData.getVersion());

        } else {
            Main.sendMessage("@" + requester + " Requests are off!");
        }
    }


    private static ArrayList<LevelData> getLevelData() {
        return levels;
    }

    private static void parse(byte[] level, String levelID) {
        all:
        for (int k = 0; k < Requests.getLevelData().size(); k++) {
            if (Requests.getLevelData().get(k).getLevelID().equalsIgnoreCase(levelID)) {
                String decompressed = null;
                try {
                    decompressed = decompress(level);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                assert decompressed != null;
                int imageIDCount = 0;
                String color = "";
                String[] values = decompressed.split(";");
                for (String value1 : values) {
                    if (value1.startsWith("1,1110") || value1.startsWith("1,211") || value1.startsWith("1,914")) {
                        String value = value1.replaceAll("(,[^,]*),", "$1;");
                        String[] attributes = value.split(";");
                        double scale = 0;
                        boolean hsv = false;
                        boolean zOrder = false;
                        String tempColor = "";
                        String text = "";
                        for (String attribute : attributes) {

                            if (attribute.startsWith("32")) {
                                if (Double.parseDouble(attribute.split(",")[1]) < 1) {
                                    scale = Double.parseDouble(attribute.split(",")[1]);
                                }
                            }
                            if (attribute.startsWith("41")) {
                                hsv = true;
                            }
                            if (attribute.startsWith("21")) {
                                tempColor = attribute.split(",")[1];
                            }
                            if (attribute.startsWith("25")) {
                                zOrder = true;
                            }
                            if (attribute.startsWith("31")) {
                                String formatted = attribute.split(",")[1].replace("_", "/").replace("-", "+");
                                text = new String(Base64.getDecoder().decode(formatted));
                            }
                        }
                        InputStream is = Main.class.getClassLoader()
                                .getResourceAsStream("Resources/blockedWords.txt");
                        assert is != null;
                        InputStreamReader isr = new InputStreamReader(is);
                        BufferedReader br = new BufferedReader(isr);
                        String line;

                        try {
                            out:
                            while ((line = br.readLine()) != null) {
                                String[] text1 = text.toUpperCase().split(" ");

                                for (String s : text1) {
                                    if (s.equalsIgnoreCase(line)) {
                                        System.out.println("Contains Vulgar");
                                        Requests.getLevelData().get(k).setContainsVulgar();
                                        break out;
                                    }
                                }
                            }
                            if (scale != 0.0 && hsv) {
                                if (tempColor.equalsIgnoreCase(color) && !zOrder) {
                                    imageIDCount++;
                                }
                            }
                            if (imageIDCount >= 1000) {
                                Requests.getLevelData().get(k).setContainsImage();
                            }
                            color = tempColor;
                        } catch (IOException e) {
                            e.printStackTrace();
                            LevelsWindow.updateUI(Requests.getLevelData().get(k).getLevelID(), Requests.getLevelData().get(k).getContainsVulgar(), Requests.getLevelData().get(k).getContainsImage(), false);
                            break all;
                        }
                    }
                }
                Requests.getLevelData().get(k).setAnalyzed();
                LevelsWindow.updateUI(Requests.getLevelData().get(k).getLevelID(), Requests.getLevelData().get(k).getContainsVulgar(), Requests.getLevelData().get(k).getContainsImage(), true);
                System.out.println("Analyzed " + k);
            }
        }
    }

    private static String decompress(byte[] compressed) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(compressed);
        GZIPInputStream gis = new GZIPInputStream(bis);
        BufferedReader br = new BufferedReader(new InputStreamReader(gis, StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        gis.close();
        bis.close();
        return sb.toString();
    }

    static String parseInfoString(String text, int level) {
        if (Requests.levels.size() != 0) {
            text = text.replaceAll("(?i)%levelName%", levels.get(level).getName())
                    .replaceAll("(?i)%levelID%", levels.get(level).getLevelID())
                    .replaceAll("(?i)%levelAuthor%", levels.get(level).getAuthor())
                    .replaceAll("(?i)%requester%", levels.get(level).getRequester())
                    .replaceAll("(?i)%songName%", levels.get(level).getSongName())
                    .replaceAll("(?i)%songID%", levels.get(level).getSongID())
                    .replaceAll("(?i)%songArtist%", levels.get(level).getSongAuthor())
                    .replaceAll("(?i)%likes%", levels.get(level).getLikes())
                    .replaceAll("(?i)%downloads%", levels.get(level).getDownloads())
                    .replaceAll("(?i)%description%", levels.get(level).getDescription())
                    .replaceAll("(?i)%queueSize%", String.valueOf(levels.size()))
                    .replaceAll("(?i)%s%", "");
            return text;
        } else {
            return OutputSettings.noLevelString.replaceAll("(?i)%s%", "");
        }
    }

    public static boolean isValisURL(String url) {

        try {
            URL url1 = new URL(url);
            return true;
        } catch (MalformedURLException e) {
            return false;
        }

    }
}
