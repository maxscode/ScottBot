package com.gmail.maxarmour2.hatsuneradio;

import com.gmail.maxarmour2.hatsuneradio.utils.cmd.Listener;
import com.gmail.maxarmour2.hatsuneradio.utils.database.SQLiteDataSource;
import me.duncte123.botcommons.BotCommons;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.sql.SQLException;
import java.util.Scanner;

public class Hatsune {

    private static final Logger LOGGER = LoggerFactory.getLogger(Hatsune.class);
    private static JDA jda;

    public Hatsune() throws LoginException, SQLException, InterruptedException {
        jda = JDABuilder
                .create(Config.get("DEV_TOKEN"),
                        GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_VOICE_STATES)
                .setChunkingFilter(ChunkingFilter.ALL)
                .disableCache(CacheFlag.ACTIVITY, CacheFlag.CLIENT_STATUS, CacheFlag.EMOTE, CacheFlag.ONLINE_STATUS)
                .addEventListeners(new Listener())
                .build()
                .awaitReady();

        jda.getPresence().setPresence(OnlineStatus.ONLINE, Activity.playing(Config.defaultPresence));

        SQLiteDataSource.getConnection();
    }

    @SuppressWarnings("InstantiationOfUtilityClass")
    public static void main(String[] args) throws SQLException, LoginException, InterruptedException {
        new Hatsune();


        // Commands invoked in the console window
        Scanner consoleCommands = new Scanner(System.in);
        while (consoleCommands.hasNext()) {

            String scanned = consoleCommands.next();

            if (scanned.equals("shutdown")) {

                LOGGER.info("Shutting Down...");
                jda.shutdownNow();
                BotCommons.shutdown(jda);
                System.exit(0);

            } else {
                LOGGER.warn("Invalid Command.");
            }
        }
    }
}