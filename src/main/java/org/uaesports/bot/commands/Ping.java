package org.uaesports.bot.commands;

import org.javacord.api.DiscordApi;
import org.javacord.api.interaction.SlashCommandInteraction;

public class Ping {

    public Ping() {
        System.out.println("Loaded \"ping\" callback class.");
    }

    public void callback(SlashCommandInteraction interaction) {
        // A simple response that anyone can see.
        interaction.createImmediateResponder()
                .setContent("Pong!")
                .respond();
    }
}
