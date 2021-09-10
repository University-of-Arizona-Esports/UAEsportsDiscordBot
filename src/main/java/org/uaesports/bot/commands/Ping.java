package org.uaesports.bot.commands;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.uaesports.bot.managers.cmds.Command;
import org.uaesports.bot.managers.cmds.annotations.Description;
import org.uaesports.bot.managers.cmds.annotations.Execute;
import org.uaesports.bot.managers.cmds.annotations.Name;

import java.awt.*;

/**
 * Handles a simple ping and pong response to the user.
 */
@Name("ping")
@Description("A simple ping and pong interaction.")
public class Ping extends Command {
    private final DiscordApi _api;

    public Ping(DiscordApi api) {
        _api = api;
    }

    /**
     * The command callback for the '/ping' command, used for checking the status and connectivity of the bot.
     *
     * @param sci An interaction interface for the message context that was used to trigger the slash command.
     */
    @Execute
    public void ping(SlashCommandInteraction sci) {
        var latency = _api.getLatestGatewayLatency();
        var restLatency = _api.measureRestLatency().join();

        var res = sci.createImmediateResponder()
                .addEmbed(new EmbedBuilder()
                        .setTitle(":ping_pong: Pong! :ping_pong:")
                        .setDescription(String.format(":heart: Heartbeat: %.2f ms\n:bed: REST Latency: %.2f ms", (float) latency.getNano() / 1000000, (float) restLatency.getNano() / 1000000))
                        .setColor(new Color(0,51,102, 1))
                        .setFooter("UA Esports Bot")
                )
                .respond();
    }
}

