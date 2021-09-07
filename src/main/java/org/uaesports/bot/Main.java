package org.uaesports.bot;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
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

        // The classes containing callbacks for each command category
        var pingHandler = new Ping(api);
        var roleHandler = new Roles();

        // The listener for when a slash command that belongs to this bot is used.
        api.addSlashCommandCreateListener(event -> {
            // Gets the context of the command usage
            SlashCommandInteraction slashCommandInteraction = event.getSlashCommandInteraction();

            // Use the command-specific callbacks to handle interactions
            var signature = getCommandSignature(slashCommandInteraction);
            System.out.println(signature);
            switch (signature) {
                case "ping":
                    pingHandler.callback(slashCommandInteraction);
                case "roles":
                    roleHandler.callbackRoles(slashCommandInteraction);
            }
        });

        // The listener for when a message component (A menu selector or a button) is used.
        api.addMessageComponentCreateListener(event -> {
            var messageComponentInteraction = event.getMessageComponentInteraction();
            var id = messageComponentInteraction.getCustomId();

            switch (id) {
                case "roleMenu":
                    roleHandler.callbackRoleMenu(messageComponentInteraction);
            }
        });
    }

    public static String getCommandSignature(SlashCommandInteraction interaction) {
        StringBuilder signature = new StringBuilder(interaction.getCommandName());

        var options = interaction.getOptions();
        while (options.size() == 1) {
            var option = options.get(0);
            if (options.get(0).isSubcommandOrGroup()) {
                signature.append(" ").append(option.getName());
                options = option.getOptions();
            } else {
                return signature.toString();
            }
        }


        return signature.toString();
    }
}
