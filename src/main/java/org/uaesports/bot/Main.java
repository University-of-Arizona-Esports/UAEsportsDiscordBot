package org.uaesports.bot;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandInteraction;

public class Main {
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

        // Currently registered slash commands
        // The command info is stored on Discord's end, meaning the usage and description,
        // and then the bot created will act as a callback. This allows us to see what discord has registered
        // for commands currently on a global level. There are also server level slash commands.
        var commands = api.getGlobalSlashCommands().join();
        for (SlashCommand command : commands) {
            System.out.println(command.getName());
        }

        var pingHandler = new Ping();
        var testHandler = new Test();

        api.addSlashCommandCreateListener(event -> {
            SlashCommandInteraction slashCommandInteraction = event.getSlashCommandInteraction();
            var name = slashCommandInteraction.getCommandName();
            if (name.equals("ping")) {
                pingHandler.callback(slashCommandInteraction);
            } else if (name.equals("test")) {
                testHandler.callback(slashCommandInteraction);
            }
        });
    }
}
