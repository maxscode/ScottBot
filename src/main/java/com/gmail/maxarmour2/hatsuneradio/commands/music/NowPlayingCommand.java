package com.gmail.maxarmour2.hatsuneradio.commands.music;

import com.gmail.maxarmour2.hatsuneradio.utils.cmd.Command;
import com.gmail.maxarmour2.hatsuneradio.utils.cmd.CommandContext;
import com.gmail.maxarmour2.hatsuneradio.utils.lavaplayer.GuildMusicManager;
import com.gmail.maxarmour2.hatsuneradio.utils.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

/**
 * A command that outputs the currently playing track to the text channel.
 * @author Max Armour
 * @since 1.0_01
 */
@SuppressWarnings("ConstantConditions")
public class NowPlayingCommand implements Command {

    @Override
    public void handle(CommandContext ctx) {

        // Embed Defaults
        String defaultTitle = "Music Command";

        final TextChannel channel = ctx.getChannel();
        final Member selfMember = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = selfMember.getVoiceState();

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inVoiceChannel() || !memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
            EmbedBuilder noConnectionToBot = new EmbedBuilder()
                    .setTitle(defaultTitle)
                    .setDescription("You must be connected to my current voice channel to invoke this command");

            channel.sendMessageEmbeds(noConnectionToBot.build()).queue();
            return;
        }

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        final AudioPlayer audioPlayer = musicManager.audioPlayer;
        final AudioTrack playingTrack = audioPlayer.getPlayingTrack();

        if (playingTrack == null) {
            EmbedBuilder nothingPlaying = new EmbedBuilder()
                    .setTitle(defaultTitle)
                    .setDescription("There is nothing playing at the moment");

            channel.sendMessageEmbeds(nothingPlaying.build()).queue();
            return;
        }

        final AudioTrackInfo info = playingTrack.getInfo();

        EmbedBuilder nowPlaying = new EmbedBuilder()
                .setTitle(defaultTitle)
                .setDescription("Now Playing:\n `" + info.title + " ` by `" + info.author + "`" +
                        "\n Link: " + info.uri + "" +
                        "\nIn Voice Channel: `" + selfVoiceState.getChannel().getName() + "`");

        channel.sendMessageEmbeds(nowPlaying.build()).queue();
    }


    @Override
    @SuppressWarnings("SpellCheckingInspection")
    public String getName() {
        return "nowplaying";
    }

    @Override
    public String getHelp() {
        return "Shows the track currently being played";
    }

    @Override
    public String getUsage() {
        return getName();
    }
}
