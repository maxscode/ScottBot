package com.gmail.maxarmour2.scottbot.commands.music;

import com.gmail.maxarmour2.scottbot.utils.cmd.Command;
import com.gmail.maxarmour2.scottbot.utils.cmd.CommandContext;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.AudioManager;

/**
 * A command that disconnects the bot from the voice channel.
 * @author Max Armour
 * @since 1.0_02
 */
@SuppressWarnings("ConstantConditions")
public class DisconnectCommand implements Command {

    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Member selfMember = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = selfMember.getVoiceState();

        // Embed Defaults
        String defaultTitle = "Music Command";

        if (!selfVoiceState.inVoiceChannel()) {
            EmbedBuilder notConnected = new EmbedBuilder()
                    .setTitle(defaultTitle)
                    .setDescription("I am not connected to a voice channel.");

            channel.sendMessageEmbeds(notConnected.build()).queue();
            return;
        }

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inVoiceChannel()) {
            EmbedBuilder memberNotConnected = new EmbedBuilder()
                    .setTitle(defaultTitle)
                    .setDescription("You must be connected to a voice channel to invoke this command.");

            channel.sendMessageEmbeds(memberNotConnected.build()).queue();
            return;
        }

        final AudioManager audioManager = ctx.getGuild().getAudioManager();

        audioManager.closeAudioConnection();

        EmbedBuilder connected = new EmbedBuilder()
                .setTitle(defaultTitle)
                .setDescription("Disconnected.");

        channel.sendMessageEmbeds(connected.build()).queue();
    }

    @Override
    public String getName() {
        return "disconnect";
    }

    @Override
    public String getHelp() {
        return "Disconnects the bot from the voice channel.";
    }

    @Override
    public String getUsage() {
        return getName();
    }
}
