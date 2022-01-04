package com.gmail.maxarmour2.scottbot.commands.music;

import com.gmail.maxarmour2.scottbot.utils.cmd.Command;
import com.gmail.maxarmour2.scottbot.utils.cmd.CommandContext;
import com.gmail.maxarmour2.scottbot.utils.lavaplayer.GuildMusicManager;
import com.gmail.maxarmour2.scottbot.utils.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

/**
 * A command that resumes the currently playing track if it is paused.
 * @author Max Armour
 * @since 1.0_01
 */
@SuppressWarnings("ConstantConditions")
public class ResumeCommand implements Command {

    @Override
    public void handle(CommandContext ctx) {

        final TextChannel channel = ctx.getChannel();
        final Member selfMember = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = selfMember.getVoiceState();

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        // Embed Defaults
        String defaultTitle = "Music Command";

        if (!memberVoiceState.inVoiceChannel() || !memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
            EmbedBuilder noConnectionToVC = new EmbedBuilder()
                    .setTitle(defaultTitle)
                    .setDescription("You must be connected to my current voice channel to invoke this command");

            channel.sendMessageEmbeds(noConnectionToVC.build()).queue();
            return;
        }

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        final AudioPlayer audioPlayer = musicManager.audioPlayer;

        if (audioPlayer.getPlayingTrack() == null) {
            EmbedBuilder nothingPlaying = new EmbedBuilder()
                    .setTitle(defaultTitle)
                    .setDescription("Nothing is playing right now");

            channel.sendMessageEmbeds(nothingPlaying.build()).queue();
            return;
        }
        if (!musicManager.scheduler.player.isPaused()) {
            EmbedBuilder alreadyPaused = new EmbedBuilder()
                    .setTitle(defaultTitle)
                    .setDescription("The Music Player is already paused");

            channel.sendMessageEmbeds(alreadyPaused.build()).queue();
            return;
        }
        musicManager.scheduler.player.setPaused(false);
        EmbedBuilder playerResumed = new EmbedBuilder()
                .setTitle(defaultTitle)
                .setDescription("The Music Player was resumed.");

        channel.sendMessageEmbeds(playerResumed.build()).queue();
    }

    @Override
    public String getName() {
        return "resume";
    }

    @Override
    public String getHelp() {
        return "Resumes the current track";
    }

    @Override
    public String getUsage() {
        return getName();
    }
}
