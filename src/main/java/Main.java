import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.core.EventManager;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import nl.vv32.rcon.Rcon;

import javax.sound.sampled.*;
import java.io.*;
import java.lang.Thread;
import java.util.Formatter;
import java.util.Scanner;
import java.util.Properties;

public class Main {

    public static Rcon RCON;

    public static boolean to = true;

    public static int delay = 30;
    public static String channel;
    public static String accesscode;

    public static String rconip = "localhost";
    public static int rconport = 25575;
    public static String rconpassword = "";

    public static String[] bannedwords = new String[0];

    public static long play() {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(new File("WarpSoundEffect.wav")));
            long length = Math.round((double) clip.getMicrosecondLength() / 1000);
            clip.start();
            return length;
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) { }
        return 0L;

    }

    public static boolean write(String fileName, String Content) {
        try {
            Formatter f = new Formatter(fileName);
            f.format(Content);
            f.close();
        } catch (FileNotFoundException e) {
            try {
                if (new File(fileName).createNewFile()) {
                    try {
                        Formatter f = new Formatter(fileName);
                        f.format(Content);
                        f.close();
                    } catch (FileNotFoundException g) {
                        System.out.println("Failed to write \"" + fileName + "\"");
                        return false;
                    }
                } else {
                    System.out.println("Failed to create \"" + fileName + "\"");
                    return false;
                }
            } catch (IOException f) {
                System.out.println("Failed to open \"" + fileName + "\"");
                return false;
            }
        }
        return true;
    }

    public static boolean match(String seed) {
        char[] letters = seed.toCharArray();
        for (char letter : letters) {
            if (!Character.isLetterOrDigit(letter)) {
                return false;
            }
        }
        return true;
    }

    public static boolean check(String seed) {
        String check = seed.toLowerCase();
        for (String word : bannedwords) {
            if (check.contains(word) || seed.contains(word)) {
                return false;
            }
        }
        return true;
    }

    public static void updateBannedWords() {
        File bannedWordsFile = new File("bannedWords.list");
        try {
            Scanner scanner = new Scanner(bannedWordsFile);
            String lines = "";
            while (scanner.hasNext()) {
                lines += scanner.nextLine() + "\n";
            }
            String[] example = lines.split("\n");
            if (example.length == 0) {
                bannedwords = new String[0];
            } else {
                bannedwords = example.clone();
            }
        } catch (FileNotFoundException e) {
            write(bannedWordsFile.getName(), "simp\nincel\nnigger\nniger\nowner\ngAy\n+\nnigga.\nchat\n12312412345\nWatchinginteresstingAnime");
        }
    }

    public static void input(String seed, String name) {

        updateBannedWords();

        if (!match(seed) || !check(seed)) {
            return;
        }

        timeout TO = new timeout();
        long length = play();
        try { Thread.sleep(length); } catch (InterruptedException ignored) { }
        System.out.println(seed + " -by- " + name);
        try {
            RCON.sendCommand("/execute as @a run warp " + seed);
            RCON.sendCommand("/tellraw @a {\"color\":\"yellow\",\"text\":\"" + name + " > " + seed + "\"}");
        } catch (IOException e) {
            e.printStackTrace();
        }
        TO.start();
        write("currentseed.txt", "Seed: " + seed);
    }

    public static class timeout extends Thread{
        public void run() {
            to = false;
            for(int x = 0; x<delay; x++) {
                write("timeout.txt", (delay - x) + " Seconds");
                try { Thread.sleep(1000); } catch (InterruptedException e) {}
            }
            write("timeout.txt", "Ready");
            to = true;
        }
    }

    public static class mainThread extends Thread {
        public void run() {
            OAuth2Credential credential = new OAuth2Credential(channel, accesscode);
            TwitchClient client = TwitchClientBuilder.builder().withChatAccount(credential).withEnableChat(true).build();
            EventManager eml = client.getEventManager();
            eml.onEvent(ChannelMessageEvent.class, event -> {
                if (event.getMessage().startsWith("!warp ") && to) {
                    input(event.getMessage().substring(6), event.getUser().getName());
                }
            });
        }
    }

    public static void main(String[ ] args) {

        String[] keys = new String[] {"ChannelName", "AccessCode", "Delay", "RconIP", "RconPort", "RconPassword"};

        File settings = new File("settings.properties");
        Properties properties = new Properties();
        if (settings.exists()) {
            try {
                InputStream input = new FileInputStream(settings);
                properties.load(input);
            } catch (FileNotFoundException e) {
                System.out.println("Couldn't find Settings File!");
            } catch (IOException f) {
                System.out.println("Failed to parse Settings File! -> " + f.getMessage());
            }
        } else {
            try {
                if (settings.createNewFile()) {
                    properties.put(keys[0], "<String>");
                    properties.put(keys[1], "<String>");
                    properties.put(keys[2], "30");
                    properties.put(keys[3], "localhost");
                    properties.put(keys[4], "25575");
                    properties.put(keys[5], "");
                    OutputStream output = new FileOutputStream(settings);
                    properties.store(output, "! You can use https://twitchtokengenerator.com/ to get your Access Token !");
                    System.out.println("Settings File created. Put in the required data.");
                    System.exit(0);
                }
            } catch (IOException e) { }
            System.out.println("Couldn't read or write to Settings File!");
            System.exit(0);
        }

        boolean missing = false;
        for (String key : keys) {
            if (!properties.containsKey(key)) {
                missing = true;
                System.out.println("Key \"" + key + "\" missing!");
            }
        }
        if (missing) {
            System.exit(0);
        }

        channel = properties.getProperty(keys[0]);
        accesscode = properties.getProperty(keys[1]);
        rconip = properties.getProperty(keys[3]);
        rconpassword = properties.getProperty(keys[5]);
        try {
            delay = Integer.parseInt(properties.getProperty(keys[2]));
            rconport = Integer.parseInt(properties.getProperty(keys[4]));
        } catch (NumberFormatException e) {
            System.out.println("Invalid Number " + e.getMessage());
            System.exit(0);
        }

        File bannedWordsFile = new File("bannedWords.list");
        if (!bannedWordsFile.exists()) {
            try {
                bannedWordsFile.createNewFile();
                write(bannedWordsFile.getName(), "simp\nincel\nnigger\nniger\nowner\ngAy\nchat\n12312412345\nWatchinginteresstingAnime");
            } catch (IOException e) {
                System.out.println("Couldn't create \"bannedWords.list\"!");
            }
        }
        updateBannedWords();

        try {
            RCON = Rcon.open(rconip, rconport);
            if (!RCON.authenticate(rconpassword)) {
                System.out.println("RCON Connection failed!");
                System.exit(0);
            }
        } catch (IOException e) {
            if (e.getMessage().startsWith("Connection refused")) {
                System.out.println("Couldn't connect to Minecraft Server with RCON!");
            } else {
                System.out.println(e.getMessage());
            }
            System.exit(0);
        }

        if (!write("timeout.txt", "Ready")) {
            System.exit(0);
        }
        if (!write("currentseed.txt", "Seed: N/A")) {
            System.exit(0);
        }

        mainThread runMain = new mainThread();
        runMain.start();
    }
}
