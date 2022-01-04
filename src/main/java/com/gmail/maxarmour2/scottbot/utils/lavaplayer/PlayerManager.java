package com.gmail.maxarmour2.scottbot.utils.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerManager {

    private static PlayerManager INSTANCE;

    private final Map<Long, GuildMusicManager> musicManager;
    private final AudioPlayerManager playerManager;

    public PlayerManager() {
        this.musicManager = new HashMap<>();
        this.playerManager = new DefaultAudioPlayerManager();

        AudioSourceManagers.registerRemoteSources(this.playerManager);
        AudioSourceManagers.registerLocalSource(this.playerManager);
    }

    public GuildMusicManager getMusicManager(Guild guild) {
        return this.musicManager.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            final GuildMusicManager guildMusicManager = new GuildMusicManager(this.playerManager);

            guild.getAudioManager().setSendingHandler(guildMusicManager.getHandler());

            return guildMusicManager;
        });
    }

    public void loadAndPlay(TextChannel channel, String trackUrl) {
        final GuildMusicManager musicManager = this.getMusicManager(channel.getGuild());

        // Embed Defaults
        String defaultTitle = "Music Command";


        this.playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                musicManager.scheduler.queue(track);

                EmbedBuilder trackLoaded = new EmbedBuilder()
                        .setTitle(defaultTitle)
                        .setDescription("Adding to queue: `" + track.getInfo().title + "` by `" + track.getInfo().author + "`");


                channel.sendMessageEmbeds(trackLoaded.build()).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                final List<AudioTrack> tracks = playlist.getTracks();

                if (isUrl(trackUrl)) {
                    for (final AudioTrack track : tracks) {
                        musicManager.scheduler.queue(track);
                    }
                    EmbedBuilder playlistLoaded = new EmbedBuilder()
                            .setTitle(defaultTitle)
                            .setDescription("Adding to queue: `" + tracks.size() + "` tracks from playlist `" + playlist.getName() +"`");

                    channel.sendMessageEmbeds(playlistLoaded.build()).queue();
                    return;
                }

                if (!isUrl(trackUrl)) {
                    final AudioTrack audioTrack = playlist.getTracks().get(0);
                    musicManager.scheduler.queue(audioTrack);

                    EmbedBuilder trackLoaded = new EmbedBuilder()
                            .setTitle(defaultTitle)
                            .setDescription("Adding to queue: `" + audioTrack.getInfo().title + "` by `" + audioTrack.getInfo().author + "`");

                    channel.sendMessageEmbeds(trackLoaded.build()).queue();
                }
            }

            @Override
            public void noMatches() {
            //
            }

            @Override
            public void loadFailed(FriendlyException exception) {
            //
            }
        });
    }

    public static PlayerManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }

        return INSTANCE;
    }

    private boolean isUrl(String url) {
        try {
            new URI(url);
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }
}
