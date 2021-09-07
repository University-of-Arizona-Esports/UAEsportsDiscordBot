package org.uaesports.bot;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.interaction.SlashCommandBuilder;

import java.util.Arrays;

// Creates and updates all registered global slash commands for the given bot.
// Only needs to be run once to register the commands, and then again to update the registered commands.
// NOTE: New and Updated commands may take up to an hour for Discord to fully register them and make them available.
public class UpdateCommands {
    public static void main(String[] args) {
        // Get token from environment variables and log in with it.
        var token = System.getenv("UADiscordBotToken");
        if (token == null) {
            System.err.println("TOKEN ENVIRONMENT VARIABLE NOT FOUND");
            return;
        }
        DiscordApi api = new DiscordApiBuilder()
                .setToken(token)
                .login().join();

        // This is used for both creating and updating slash commands, making changes here will
        // create new slash commands or modify existing ones if the descriptions are changed.
        // NOTE: This may take some time to update on Discord's end, up to an hour or more.
        api.bulkOverwriteGlobalSlashCommands(Arrays.asList(
                new SlashCommandBuilder().setName("ping").setDescription("A very basic ping and pong interaction."),
                new SlashCommandBuilder().setName("test").setDescription("Just testing the thing.")
        )).join();
    }
}
