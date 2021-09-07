package org.uaesports.bot;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.message.MessageFlag;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;

public class Test {
    public Test() {
        System.out.println("Loaded \"test\" callback class.");
    }

    public void callback(SlashCommandInteraction interaction) {
        interaction.createImmediateResponder()
                .setContent("Testing response, only the command user should see this.")
                .setFlags(MessageFlag.EPHEMERAL)
                .respond();
    }
}
