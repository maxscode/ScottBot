package com.gmail.maxarmour2.scottbot;

import io.github.cdimascio.dotenv.Dotenv;

public class Config {

    // Dotenv logic
    private static final Dotenv config = Dotenv.load();
    public static String get(String key) {
        return config.get(key);
    }

    // Bot Defaults
    public static String botName = "ScottBot";
    public static String botVer = "v1.1_01";

    public static String defaultPresence = botName + " " + botVer;
}
