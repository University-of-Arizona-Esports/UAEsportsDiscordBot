package org.uaesports.bot;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.uaesports.bot.commands.Debug;
import org.uaesports.bot.managers.cmds.CommandData;

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
        
        CommandData.read(Debug.class).overwriteAndUpdatePermissions(api).join();
        
        api.disconnect();
    }
}
