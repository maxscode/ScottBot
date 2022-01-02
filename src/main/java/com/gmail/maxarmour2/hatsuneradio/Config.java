package com.gmail.maxarmour2.hatsuneradio;

import io.github.cdimascio.dotenv.Dotenv;

public class Config {

    // Dotenv logic
    private static final Dotenv config = Dotenv.load();
    public static String get(String key) {
        return config.get(key);
    }

    // Bot Defaults
    public static String botName = "Hatsune Radio";
    public static String botVer = "1.0_03";

    public static String defaultPresence = botName + " " + botVer;
}
