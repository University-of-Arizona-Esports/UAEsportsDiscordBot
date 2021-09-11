package org.uaesports.bot.managers.cmds.handlers;

import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandInteractionOptionsProvider;
import org.uaesports.bot.managers.cmds.Command;

/**
 * An interface that handles a small portion of a command.
 */
public interface InteractionHandler {
    
    void handle(SlashCommandInteraction sci, SlashCommandInteractionOptionsProvider provider, Command instance);
    
}
