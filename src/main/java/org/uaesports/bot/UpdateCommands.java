package org.uaesports.bot;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.interaction.SlashCommandBuilder;
import org.javacord.api.interaction.SlashCommandUpdater;

import java.util.Arrays;

/**
 *
 */
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

        // This is used for deleting commands.
        // Generally I just leave any command names that once existed but don't any more in here.
        var toDelete = new String[] {"test"};
        var commands = api.getGlobalSlashCommands().join();
        for (var command : commands) {
            if (Arrays.asList(toDelete).contains(command.getName())) {
                command.deleteGlobal();
            }
        }

        // This is used for both creating and updating slash commands, making changes here will
        // create new slash commands or modify existing ones if the descriptions are changed.
        // NOTE: This may take some time to update on Discord's end, up to an hour or more.
        api.bulkOverwriteGlobalSlashCommands(Arrays.asList(
                new SlashCommandBuilder().setName("ping").setDescription("A very basic ping and pong interaction."),
                new SlashCommandBuilder().setName("roles").setDescription("See available roles to assign to yourself.")
        )).join();


        // This listing of what Discord has registered as the commands *will* update instantly.
        // However, Discord itself (the user side), may take up to an hour to update.
        commands = api.getGlobalSlashCommands().join();
        for (var command : commands) {
            System.out.println(command.getName());
        }
    }
}
