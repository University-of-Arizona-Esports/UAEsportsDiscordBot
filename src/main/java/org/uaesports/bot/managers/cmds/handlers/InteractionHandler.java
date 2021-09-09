package org.uaesports.bot.managers.cmds.handlers;

import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandInteractionOptionsProvider;

/**
 * An interface that handles a small portion of a command.
 */
public interface InteractionHandler {
    
    void handle(SlashCommandInteraction sci, SlashCommandInteractionOptionsProvider provider, Object instance);
    
}
