package org.uaesports.bot;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.SlashCommandInteraction;

import java.awt.*;

/**
 * Handles a simple ping and pong response to the user.
 */
public class Ping {
    private final DiscordApi _api;

    /**
     *  The initializer for the Ping command handler class. This uses the commands attached to the core DiscordApi
     *  class to determine the relative latency of bot interactions and responses with Discord.
     *
     * @param api The DiscordApi core object that has been logged in to the bot account.
     */
    public Ping(DiscordApi api) {
        _api = api;

        System.out.println("Loaded \"ping\" callback class.");
    }

    /**
     * The command callback for the '/ping' command, used for checking the status and connectivity of the bot.
     *
     * @param interaction An interaction interface for the message context that was used to trigger the slash command.
     */
    public void callback(SlashCommandInteraction interaction) {
        var latency = _api.getLatestGatewayLatency();
        var restLatency = _api.measureRestLatency().join();

        // Respond with latency information from the bot.
        interaction.createImmediateResponder()
                .addEmbed(new EmbedBuilder()
                        .setTitle(":ping_pong: Pong! :ping_pong:")
                        .setDescription(String.format(":heart: Heartbeat: %.2f ms\n:bed: REST Latency: %.2f ms", (float) latency.getNano() / 1000000, (float) restLatency.getNano() / 1000000))
                        .setColor(new Color(0,51,102, 1))
                        .setFooter("UA Esports Bot")
                )
                .respond();
    }
}
